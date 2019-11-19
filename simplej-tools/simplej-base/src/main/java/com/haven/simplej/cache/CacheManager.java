package com.haven.simplej.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.haven.simplej.cache.enums.DataSource;
import com.haven.simplej.cache.enums.ExpireTime;
import com.haven.simplej.security.DigestUtils;
import com.haven.simplej.text.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 缓存工具类
 * @author  haven.zhang
 * @date 2019/1/3.
 */
@Slf4j
public class CacheManager {

	private static RedisTemplate template;

	public void setTemplate(RedisTemplate template){
		CacheManager.template = template;
	}

	/**
	 * 缓存key的最大长�?
	 */
	private static final int KEY_LEN = 50;

	/**
	 * 谷歌LoadingCache缓存使用,公共缓存
	 */
	private static LoadingCache<String, Object> commontCahceBuilder = CacheBuilder.newBuilder().maximumSize(5000)
			.expireAfterAccess(5, TimeUnit.MINUTES).build(new CacheLoader<String, Object>() {
				@Override
				public Object load(String key) throws Exception {
					return null;
				}
			});

	public static LoadingCache<String, Object> createCache(long size,long expireTime){
		LoadingCache<String, Object> cache = CacheBuilder.newBuilder().maximumSize(size)
				.expireAfterAccess(expireTime, TimeUnit.MILLISECONDS).build(new CacheLoader<String, Object>() {
					@Override
					public Object load(String key) throws Exception {
						return null;
					}
				});

		return cache;
	}

	/**
	 * 更新缓存，更新目标包含本地缓存及远程缓存（如redis）
	 * @param key
	 * @param value
	 * @param expireTime
	 * @param <T>
	 */
	public static <T> void put(String key, T value, long expireTime) {
		put(key, value, DataSource.LOCAL_AND_REMOTE, expireTime);
	}

	/**
	 * 仅更新本地缓存
	 * @param key
	 * @param value
	 * @param <T>
	 */
	public static <T> void putLocal(String key, T value) {
		put(key, value, DataSource.LOCAL, ThreadLocalRandom.current().nextLong(
				ExpireTime.ONE_DAY.get(), ExpireTime.THREE_DAY.get()));
	}

	/**
	 * 仅更新远程缓存
	 * @param key
	 * @param value
	 * @param <T>
	 */
	public static <T> void putRemote(String key, T value) {
		put(key, value, DataSource.REMOTE, ThreadLocalRandom.current().nextLong(ExpireTime.ONE_DAY.get(), ExpireTime.THREE_DAY.get()));
	}

	/**
	 * 更新缓存，更新目标包含本地缓存及远程缓存（如redis）
	 * @param key
	 * @param value
	 * @param <T>
	 */
	public static <T> void put(String key, T value) {
		put(key, value, DataSource.LOCAL_AND_REMOTE, ThreadLocalRandom.current().nextLong(ExpireTime.TWO_DAY.get(), ExpireTime.THREE_DAY.get()));
	}

	/**
	 * 更新缓存，支持put pojo，list、map对象
	 *
	 * @param key
	 * @param value
	 * @param dataSource
	 * @param <T>
	 */
	public static <T> void put(String key, T value, DataSource dataSource, long timeout) {
		try {
			if(value == null){
				return;
			}

			if (StringUtil.length(key) > KEY_LEN) {
				key = DigestUtils.md2Hex(key);
			}
			if (dataSource.equals(DataSource.LOCAL)) {
				commontCahceBuilder.put(key, value);
			} else if (dataSource.equals(DataSource.LOCAL_AND_REMOTE)) {
				commontCahceBuilder.put(key, value);
				if(template != null){
					template.opsForValue().set(key, value,timeout);
				}
			} else if (dataSource.equals(DataSource.REMOTE)) {
				if(template != null){
					template.opsForValue().set(key, value,timeout);
				}
			}
		} catch (Exception e) {
			log.error("put cache error ,key:{}", key, e);
		}
	}

	/**
	 * 从本地缓存中查询数据，如果查不到在从远程查询
	 * @param key
	 * @param clz
	 * @param <T>
	 * @return
	 */
	public static <T> T getObject(String key, Class clz) {

		return getObject(key, DataSource.LOCAL_AND_REMOTE, clz);
	}

	/**
	 * 从本地缓存中查询数据
	 * @param key
	 * @param <T>
	 * @return
	 */
	public static <T> T getObject(String key) {

		return getObject(key, DataSource.LOCAL, null);
	}

	/**
	 * 获取缓存
	 *
	 * @param key
	 * @param dataSource
	 * @param clz
	 * @param <T>
	 * @return
	 */
	public static <T> T getObject(String key, DataSource dataSource, Class clz) {
		T value = null;
		try {

			if (StringUtil.length(key) > KEY_LEN) {
				key = DigestUtils.md2Hex(key);
			}
			if (dataSource.equals(DataSource.LOCAL)) {
				value = (T) commontCahceBuilder.get(key);
			} else if (dataSource.equals(DataSource.LOCAL_AND_REMOTE)) {
				value = (T) commontCahceBuilder.get(key);
				if (value != null) {
					return value;
				}
				//@TODO 从远程redis查询缓存
				if(template != null){
					String json = (String) template.opsForValue().get(key);
					value = (T) JSON.parseObject(json,clz);
				}
			} else if (dataSource.equals(DataSource.REMOTE)) {
				//@TODO 从远程redis查询缓存
				if(template != null){
					String json = (String) template.opsForValue().get(key);
					value = (T) JSON.parseObject(json,clz);
				}
			}
		} catch (Exception e) {
			log.info("获取不到key为：" + key + "的缓存");
		}
		return value;
	}

	public static <T> T getMap(String key, TypeReference<T> typeReference) {

		return getMap(key, DataSource.LOCAL_AND_REMOTE, typeReference);
	}

	public static <T> T getMap(String key) {

		return getMap(key, DataSource.LOCAL, null);
	}

	/**
	 * 获取缓存
	 *
	 * @param key
	 * @param dataSource
	 * @param <T>
	 * @return
	 */
	public static <T> T getMap(String key, DataSource dataSource, TypeReference<T> typeReference) {
		T value = null;
		try {

			if (StringUtil.length(key) > KEY_LEN) {
				key = DigestUtils.md2Hex(key);
			}
			if (dataSource.equals(DataSource.LOCAL)) {
				value = (T) commontCahceBuilder.get(key);
			} else if (dataSource.equals(DataSource.LOCAL_AND_REMOTE)) {
				value = (T) commontCahceBuilder.get(key);
				if (value != null) {
					return value;
				}
				//@TODO 从远程redis查询缓存
				if(template != null){
					String jsonValue = (String) template.opsForValue().get(key);
					value = JSON.parseObject(jsonValue, typeReference);
				}

			} else if (dataSource.equals(DataSource.REMOTE)) {
				//@TODO 从远程redis查询缓存
				if(template != null){
					String jsonValue = (String) template.opsForValue().get(key);
					value =  JSON.parseObject(jsonValue, typeReference);
				}
			}
		} catch (Exception e) {
			log.info("获取不到key为：" + key + "的缓存");
		}
		return value;
	}

	public static <T> List<T> getList(String key, Class clz) {

		return getList(key, DataSource.LOCAL_AND_REMOTE, clz);
	}

	public static <T> List<T> getList(String key) {

		return getList(key, DataSource.LOCAL, null);
	}

	/**
	 * 获取缓存
	 *
	 * @param key
	 * @param dataSource
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> getList(String key, DataSource dataSource, Class clz) {
		List<T> value = null;
		try {

			if (StringUtil.length(key) > KEY_LEN) {
				key = DigestUtils.md2Hex(key);
			}
			if (dataSource.equals(DataSource.LOCAL)) {
				value = (List<T>) commontCahceBuilder.get(key);
			} else if (dataSource.equals(DataSource.LOCAL_AND_REMOTE)) {
				value = (List<T>) commontCahceBuilder.get(key);
				if (value != null) {
					return value;
				}
				//@TODO 从远程redis查询缓存
				if(template != null){
					String jsonValue = (String) template.opsForValue().get(key);
					value = JSON.parseArray(jsonValue, clz);
				}
			} else if (dataSource.equals(DataSource.REMOTE)) {
				//@TODO 从远程redis查询缓存
				if(template != null){
					String jsonValue = (String) template.opsForValue().get(key);
					value = JSON.parseArray(jsonValue, clz);
				}
			}
		} catch (Exception e) {
			log.info("获取不到key为：" + key + "的缓存");
		}
		return value;
	}
}
