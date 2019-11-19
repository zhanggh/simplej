package com.haven.simplej.limiter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.limiter.enums.LimiterType;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * 流量控制器，支持：
 * 1.guava_limiter（RateLimiter，qps控制）
 * 2.redis_limiter （qps/区间流量控制）
 * 3.semaphore（信号量的方式实现并发限制，非qps限制）
 * 3.local_limiter（基于本地缓存实现的qps流控器）
 * 默认值：guava_limiter
 * qps流控器和并发流控器的区别在于：
 * qps流控器限制了每秒允许的访问量，比如每秒允许1000个请求，当第500毫秒到时候就达到了1000个请求，那么后面的500毫秒将不允许再进入新的请求，
 * qps流控器只需要申请许可，不需要归还许可
 *
 * 并发流控限制的是同一个时刻（可以理解为某个时间点，而非时间段，qps是时间段的概要）最高同时访问的请求数，
 * 并发流控器需要每一个请求拿去令牌之后，用完后归还，总的令牌数是固定有限的，不归还则影响后面的请求。
 *
 * 对于网络io型的请求（比如一个请求耗时500ms），则建议不要使用并发流控器，因为过多长时间不归还令牌的请求，会导致资源不断减少，影响新的请求，此时可考虑用qps流控器，或者不要使用流控
 * 对于计算型请求（比如几毫秒就能完成的），可以考虑使用并发流控器。
 *
 * Created by haven.zhang on 2018/11/13.
 *
 */
@Slf4j
public class ApRequestLimiter {

	/**
	 * 信号量流控器
	 */
	private Semaphore semaphoreLimiter;

	/**
	 * guava提供的流控器
	 */
	private RateLimiter guavaRateLimiter;

	/**
	 * 基于本地缓存实现的流控器
	 */
	private LocalLimiter localLimiter;

	/**
	 * 基于redis缓存实现的流控器
	 */
	private RedisRateLimiter redisLimiter;


	/**
	 * 流控器配置
	 */
	private LimiterConfig cfg =new LimiterConfig();

	/**
	 * 心跳作用，用于检测流控配置是否发生变化
	 */
	private ScheduledExecutorService scheduledExecutorService;



	/**
	 * 每一个key（可以认为是接口别名）对应一个流控器
	 */
	private static Map<String,ApRequestLimiter> limiterMap = Maps.newConcurrentMap();


	/**
	 * 默认 最大总流量qps，所有节点总和
	 */
	private static int maxTotalQps = 5000;

	/**
	 * 默认 单节点最大qps
	 */
	private static int maxSigleQps = 500;

	/**
	 * 流控器类型 默认 类型
	 */
	private static LimiterType type = LimiterType.GUAVA_LIMITER;

	/**
	 * 流量统计器key
	 */
	private static final String COUNTER_KEY="c:counter:rate";

	/**
	 * 本地流量统计，时效为1秒钟
	 */
	private LoadingCache<String, LongAdder> rateCounter = CacheBuilder.newBuilder().maximumSize(1)
			.expireAfterWrite(1, TimeUnit.SECONDS).build(new CacheLoader<String, LongAdder>() {
				@Override
				public LongAdder load(String key) throws Exception {
					return new LongAdder();
				}
			});

	/**
	 * 获取流控器
	 * @param key 可以为接口名，或者其他别名
	 * @return
	 */
	public static ApRequestLimiter getLimiter(String key){

		if(!limiterMap.containsKey(key)){
			synchronized (new String(key).intern()){
				if(!limiterMap.containsKey(key)){
					limiterMap.put(key,ApRequestLimiter.createLimiter(key));
				}
			}
		}

		return limiterMap.get(key);
	}


	/**
	 * 创建流控器
	 * @param type
	 * @return
	 */
	public static ApRequestLimiter createLimiter(LimiterType type,int maxTotalQps,int maxSigleQps,String key) {
		ApRequestLimiter limiter;
		limiter = new ApRequestLimiter();
		limiter.cfg.setType(type);
		limiter.cfg.setMaxSigleQps(maxSigleQps);
		limiter.cfg.setMaxTotalQps(maxTotalQps);
		limiter.cfg.setAlias(key);
		limiter.type = type;
		limiter.initLimiter();

//		limiterMap.put(limiterKey,limiter);
		//配置心跳,每30秒一次
		limiter.scheduledExecutorService = Executors.newScheduledThreadPool(1);
		limiter.scheduledExecutorService.scheduleAtFixedRate(new LimiterConfigHandler(limiter.cfg),
				30L, 10L, TimeUnit.MINUTES);
		return limiter;
	}

	/**
	 * 创建流控器
	 * @return
	 */
	public static ApRequestLimiter createLimiter() {
		return createLimiter("");
	}
	/**
	 * 创建流控器
	 * @return
	 */
	public static ApRequestLimiter createLimiter(String key) {
		return createLimiter(getLimiterType(key),maxTotalQps,maxSigleQps,key);
	}

	/**
	 * 初始化流控器
	 */
	private void initLimiter() {
		if (LimiterType.GUAVA_LIMITER.equals(this.type)) {
			guavaRateLimiter = RateLimiter.create(Double.valueOf(cfg.getMaxSigleQps()));
		} else if (LimiterType.LOCAL_LIMITER.equals(type)) {
			localLimiter = LocalLimiter.createLimiter(cfg.getMaxSigleQps());
		} else if (LimiterType.REDIS_LIMITER.equals(type)) {
			redisLimiter = RedisRateLimiter.createLimiter(cfg.getAlias(), cfg.getMaxTotalQps());
		} else if (LimiterType.SEMAPHORE.equals(type)) {
			semaphoreLimiter = new Semaphore(cfg.getMaxSigleQps());
		} else {
			throw new UncheckedException("不支持的流控器类型" + type.name());
		}
	}

	/**
	 * 尝试申请令牌
	 * @param permits 申请令牌数
	 * @param timeout 等待超时时间
	 * @return boolean true申请通过，false 不通过
	 */
	public boolean tryAquire(int permits, long timeout) {
		try {
			if (LimiterType.GUAVA_LIMITER.equals(cfg.getType())) {
				boolean isPass = guavaRateLimiter.tryAcquire(permits, timeout, TimeUnit.MILLISECONDS);
				rateCounter.get(COUNTER_KEY).add(1);
				return isPass;
			} else if (LimiterType.LOCAL_LIMITER.equals(cfg.getType())) {
				return localLimiter.tryAquire(permits,timeout);
			} else if (LimiterType.REDIS_LIMITER.equals(cfg.getType())) {
				return redisLimiter.tryAquire(permits,timeout);
			} else if (LimiterType.SEMAPHORE.equals(cfg.getType())) {
				return semaphoreLimiter.tryAcquire(permits, timeout, TimeUnit.MILLISECONDS);
			} else {
				throw new UncheckedException("不支持的流控器类型" + cfg.getType().name());
			}
		} catch (Exception e) {
			log.error("tryAquire error", e);
		}

		return true;
	}

	/**
	 * 获取当前流速
	 * @return int 流速
	 */
	public double getRate() {
		try {
			if (LimiterType.GUAVA_LIMITER.equals(cfg.getType())) {
				return rateCounter.get(COUNTER_KEY).sum();
			} else if (LimiterType.LOCAL_LIMITER.equals(cfg.getType())) {
				return localLimiter.getRate();
			} else if (LimiterType.REDIS_LIMITER.equals(cfg.getType())) {
				return redisLimiter.getRate();
			} else if (LimiterType.SEMAPHORE.equals(cfg.getType())) {
				return this.cfg.getMaxSigleQps() - semaphoreLimiter.availablePermits();
			} else {
				throw new UncheckedException("不支持的流控器类型" + cfg.getType().name());
			}
		} catch (Exception e) {
			log.error("getRate error", e);
		}

		return 0;
	}

	/**
	 * 申请访问令牌（许可）
	 * @return
	 */
	public boolean tryAquire() {
		return tryAquire(1, 0);
	}

	/**
	 * 返回许可
	 * 只有使用并发流控器的时候，才需要归还令牌，其他qps流控器则不需要
	 * @param permits
	 */
	public void release(int permits) {
		if (LimiterType.SEMAPHORE.equals(cfg.getType())) {
			semaphoreLimiter.release(permits);
		}
	}


	/**
	 * 返回许可
	 * 只有使用并发流控器的时候，才需要归还令牌，其他qps流控器则不需要
	 */
	public void release() {

		release(1);
	}


	/**
	 * 获取流控器类型
	 * @return
	 */
	public static LimiterType getLimiterType(String alias) {
		String limiterType = null;
		if (LimiterType.GUAVA_LIMITER.name().equalsIgnoreCase(limiterType)) {
			return LimiterType.GUAVA_LIMITER;
		} else if (LimiterType.LOCAL_LIMITER.name().equalsIgnoreCase(limiterType)) {
			return LimiterType.LOCAL_LIMITER;
		} else if (LimiterType.REDIS_LIMITER.name().equalsIgnoreCase(limiterType)) {
			return LimiterType.REDIS_LIMITER;
		} else if (LimiterType.SEMAPHORE.name().equalsIgnoreCase(limiterType)) {
			return LimiterType.SEMAPHORE;
		}
		//默认返回
		return LimiterType.GUAVA_LIMITER;
	}

	public void close(){
		this.scheduledExecutorService.shutdown();
	}

}

/**
 * 流控器配置处理器
 */
@Slf4j
class LimiterConfigHandler implements Runnable{

	LimiterConfig cfg;
	public LimiterConfigHandler(LimiterConfig limiterConfig) {
		this.cfg = limiterConfig;
	}

	@Override
	public void run() {
		LimiterConfig cfg = refresh();
		handle(cfg);
	}

	/**
	 * 查询流控器参数配置
	 */
	private LimiterConfig refresh(){
		LimiterConfig cfg = new LimiterConfig();

		Integer tempMaxSigleQps = null;
		Integer tempMaxTotalQps = null;
		cfg.setType(ApRequestLimiter.getLimiterType(this.cfg.getAlias()));
		cfg.setMaxSigleQps(tempMaxSigleQps);
		cfg.setMaxTotalQps(tempMaxTotalQps);
		cfg.setAlias(this.cfg.getAlias());
		return cfg;
	}

	/**
	 * 如果流控器的快码参数值发生变化，则重置流控器
	 */
	private void handle(LimiterConfig cfg){

		if (!cfg.toString().equals(this.cfg.toString())) {
			log.info("----------------ApRequestLimiter refresh----------------");
			log.info("new config:{}",cfg.toString());
			//重置流控器
			ApRequestLimiter.getLimiter(this.cfg.getAlias());
		}
	}


}