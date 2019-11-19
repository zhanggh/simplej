package com.haven.simplej.limiter;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;

/**
 * redis流控器
 * 延时误差为毫秒级别
 * Created by haven.zhang on 2018/11/13.
 */
public class RedisRateLimiter {

	private static final long SUCCESS_CODE = 2000L;

	private static final String LUA ="local key = KEYS[1] --限流KEY（一秒一个）\n"
			+ "local isExists = tonumber(redis.call('EXISTS', key))\n"
			+ "if isExists == 0 then \n"//如果key不存在，则创建key，并设置失效时间
			+ "    redis.call('INCRBY', key ,ARGV[2])\n"
			+ "    redis.call('expire', key,'1')\n"
			+ "    return "+SUCCESS_CODE+"\n"//返回2000，代表成功
			+ "end\n"
			+ "local limit = tonumber(ARGV[1])\n"// --限流大小
			+ "local current = tonumber(redis.call('INCRBY', key,'0'))\n"
			+ "if current + 1 > limit then \n"//--如果超出限流大小
			+ "    return redis.call('PTTL', key)\n"//返回key剩余时间，代表获取许可失败
			+ "else \n" //--请求数+1，并设置1秒过期
			+ "    redis.call('INCRBY', key ,ARGV[2])\n"
			+ "    return "+SUCCESS_CODE+"\n"
			+ "end";

	/**
	 * 最大总流量qps，所有节点总和
	 */
	private int maxTotalQps;


	/**
	 * redis 模板
	 */
	private RedisTemplate redisTemplate;

	/**
	 * 别名
	 */
	private String alias;
	/**
	 * 等待超时累计器
	 */
	private ThreadLocal<Long> waitTimeCounter = new ThreadLocal<Long>() {
		protected Long initialValue() {
			return 0L;
		}
	};


	/**
	 * 流控器计数器key
	 */
	private String redisCounterKey ="c:redis:qps:counter:";
	/**
	 * 创建流控器
	 * @return
	 */
	public static RedisRateLimiter createLimiter(String alias, int maxTotalQps) {
		RedisRateLimiter limiter = new RedisRateLimiter();
		limiter.maxTotalQps = maxTotalQps;
		limiter.redisCounterKey += alias;
		return limiter;
	}


	/**
	 * 尝试申请令牌
	 * @param permits 申请令牌数
	 * @param timeout 等待超时时间，单位：毫秒
	 * @return boolean true申请通过，false 不通过
	 */
	public boolean tryAquire(int permits, long timeout) {

		Long result = excute(redisCounterKey, LUA, new Long[]{Long.valueOf(maxTotalQps),
				Long.valueOf(permits)}, Long.class);
		//result 是该key的pttl 时间
		if (result == SUCCESS_CODE) {
			return true;
		} else {
			if (timeout < result || waitTimeCounter.get() >= timeout) {
				return false;
			}
			waitTimeCounter.set(waitTimeCounter.get() + result);
			//等待至超时，误差单位为毫秒
//			LockSupport.parkNanos(Thread.currentThread(), result * 1000000);
			synchronized (this){
				try {
					this.wait(result);
				} catch (InterruptedException e) {
					//忽略异常
					System.out.println("tryAquire wait InterruptedException");
				}
			}
			return tryAquire(permits, timeout);
		}
	}


	/**
	 * 获取当前总流量
	 * @return
	 */
	public double getRate() {
		return redisTemplate.opsForValue().increment(redisCounterKey,0L);
	}

	/**
	 * 执行lua脚本
	 * @param key
	 * @param lua
	 * @param arg
	 * @param resultTypeClz
	 * @param <T>
	 * @return
	 */
	public <T> T excute(String key,String lua,Object[] arg,Class<T> resultTypeClz){
		DefaultRedisScript<T> redisScript = new DefaultRedisScript<T>();
		redisScript.setResultType(resultTypeClz);
		redisScript.setScriptText(lua);
		T result = (T) redisTemplate.execute(redisScript, Collections.singletonList(key),arg);
		return result;

	}
}
