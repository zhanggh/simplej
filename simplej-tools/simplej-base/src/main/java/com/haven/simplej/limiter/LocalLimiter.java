package com.haven.simplej.limiter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * 基于本地缓存实现的流控器,实现每秒只允许多少个请求的流控
 * 等待请求重新竞争资源时，属于非随机抢占式
 * Created by haven.zhang on 2018/11/13.
 */
public class LocalLimiter {

	/**
	 * 监听器，用于监听缓存删除事件
	 */
	private Object listiner = new Object();

	/**
	 * 本地缓存，时效为1秒钟
	 */
	private LoadingCache<String, CacheCounter> cahceBuilder = CacheBuilder.newBuilder().maximumSize(1)
			.expireAfterWrite(1, TimeUnit.SECONDS).removalListener(removalNotification -> {
				synchronized (listiner){
					listiner.notifyAll();
				}
			}).build(new CacheLoader<String, CacheCounter>() {
				@Override
				public CacheCounter load(String key) throws Exception {
					return new CacheCounter();
				}
			});

	/**
	 * 等待超时累计器
	 */
	private ThreadLocal<Long> waitTimeCounter = new ThreadLocal<Long>() {
		protected Long initialValue() {
			return 0L;
		}
	};

	/**
	 * 本地缓存计数器key
	 */
	private static final String LIMITER_KEY = "local_limiter";


	/**
	 * 单节点最大qps
	 */
	private int maxSigleQps;

	/**
	 * 创建流控器
	 * @return
	 */
	public static LocalLimiter createLimiter(int maxSigleQps) {

		LocalLimiter limiter = new LocalLimiter();
		limiter.maxSigleQps = maxSigleQps;
		return limiter;
	}

	/**
	 * 尝试申请令牌
	 * @param permits 申请令牌数
	 * @param timeout 等待超时时间
	 * @return boolean true申请通过，false 不通过
	 */
	public boolean tryAquire(int permits, long timeout) {
		LongAdder counter = getCache().getCounter();
		long activeTime = getCache().getActiveTime();
		if (counter.sum() >= maxSigleQps) {
			//如果当前令牌已用完，则等下一个区间
			synchronized (listiner) {
				try {
					long waitTime = System.nanoTime() - activeTime;
					long millis = waitTime / 1000000;
					int nanos = (int) (waitTime % 1000000);
					if (millis> timeout || waitTimeCounter.get() >= timeout) {
						return false;
					}
					waitTimeCounter.set(waitTimeCounter.get() + millis);
					listiner.wait(millis, nanos);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}

			return tryAquire(permits, timeout);
		} else if (counter.sum() + permits > maxSigleQps) {
			try {
				long waitTime = System.nanoTime() - activeTime;
				long millis = waitTime / 1000000;
				int nanos = (int) (waitTime % 1000000);
				if (millis> timeout || waitTimeCounter.get() >= timeout) {
					return false;
				}
				waitTimeCounter.set(waitTimeCounter.get() + millis);
				listiner.wait(millis, nanos);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}

			return tryAquire(permits, timeout);
		}

		waitTimeCounter.remove();
		counter.add(permits);
		return true;
	}


	/**
	 * 获取累加器
	 * @return
	 */
	private CacheCounter getCache() {
		CacheCounter cache = null;
		try {
			cache = cahceBuilder.get(LIMITER_KEY);
		} catch (Exception e) {

		}
		return cache;
	}

	/**
	 * 重置累计器
	 */
	private void reset() {
		getCache().getCounter().reset();
	}


	/**
	 * 获取当前节点流量
	 * @return
	 */
	public long getRate(){
		return getCache().getCounter().sum();
	}
}

/**
 * 令牌计数器
 */
class CacheCounter {
	/**
	 * 令牌激活时间，单位：纳秒
	 */
	private long activeTime;
	/**
	 * 计数器
	 */
	private LongAdder counter = new LongAdder();
	CacheCounter(){
		activeTime = System.nanoTime();
	}

	public long getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(long activeTime) {
		this.activeTime = activeTime;
	}

	public LongAdder getCounter() {
		return counter;
	}

	public void setCounter(LongAdder counter) {
		this.counter = counter;
	}
}