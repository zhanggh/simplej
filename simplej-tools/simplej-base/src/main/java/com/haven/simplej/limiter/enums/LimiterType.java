package com.haven.simplej.limiter.enums;

/**
 * 流控器类型
 * Created by haven.zhang on 2018/11/13.
 * GUAVA_LIMITER:guava提供的RateLimiter来实现流控
 * REDIS_LIMITER：redis incr + expire实现的流控
 * LOCAL_LIMITER：本地缓存+LongAdder的方式实现的qps
 * SEMAPHORE：jdk semaphore 信号量实现的流控（并发控制，非qps控制）;
 */
public enum LimiterType {
	GUAVA_LIMITER,REDIS_LIMITER,LOCAL_LIMITER,SEMAPHORE;
}
