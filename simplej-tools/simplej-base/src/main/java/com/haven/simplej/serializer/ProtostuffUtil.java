package com.haven.simplej.serializer;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.Delegate;
import io.protostuff.runtime.RuntimeEnv;
import io.protostuff.runtime.RuntimeSchema;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * protostuff 序列化工具
 * @author haven.zhang
 * @date 2019/1/29.
 */
public class ProtostuffUtil {

	private final static Delegate<Timestamp> TIMESTAMP_DELEGATE = new TimestampDelegate();

	private final static DefaultIdStrategy idStrategy = ((DefaultIdStrategy) RuntimeEnv.ID_STRATEGY);

	static {
		idStrategy.registerDelegate(TIMESTAMP_DELEGATE);
	}

	private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

	private static <T> Schema<T> getSchema(Class<T> clazz) {
		Schema<T> schema = (Schema<T>) cachedSchema.get(clazz);
		if (schema == null) {
			schema = RuntimeSchema.getSchema(clazz);
			if (schema != null) {
				cachedSchema.put(clazz, schema);
			}
		}
		return schema;
	}

	/**
	 * 序列化
	 *
	 * @param obj 序列化对象
	 * @return 序列化后的byte[]值
	 */
	public static <T> byte[] serializer(T obj) {
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) obj.getClass();
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		try {
			Schema<T> schema = getSchema(clazz);
			return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		} finally {
			buffer.clear();
		}
	}

	/**
	 * 反序列化
	 *
	 * @param data 序列化后的byte[]值
	 * @param clazz 反序列化后的对象
	 * @return 返回的对象
	 */
	public static <T> T deserializer(byte[] data, Class<T> clazz) {
		try {
			T obj = clazz.newInstance();
			Schema<T> schema = getSchema(clazz);
			ProtostuffIOUtil.mergeFrom(data, obj, schema);
			return obj;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	/**
	 * 序列化list集合
	 * @param objs
	 * @param <T>
	 * @return
	 */
	public static <T> byte[] serializeList(List<T> objs) {
		if (objs == null || objs.isEmpty()) {
			throw new RuntimeException("序列化对象列表(" + objs + ")参数异常!");
		}
		@SuppressWarnings("unchecked")
		Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(objs.get(0).getClass());
		LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);
		byte[] protostuff = null;
		ByteArrayOutputStream bos = null;
		try {
			bos = new ByteArrayOutputStream();
			ProtostuffIOUtil.writeListTo(bos, objs, schema, buffer);
			protostuff = bos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("序列化对象列表(" + objs + ")发生异常!", e);
		} finally {
			buffer.clear();
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (IOException ignore) {

			}
		}
		return protostuff;
	}


	public static <T> List<T> deserializeList(byte[] bytes, Class<T> targetClass) {
		if (bytes == null || bytes.length == 0) {
			throw new RuntimeException("反序列化对象发生异常,byte序列为空!");
		}

		Schema<T> schema = RuntimeSchema.getSchema(targetClass);
		List<T> result;
		try {
			result = ProtostuffIOUtil.parseListFrom(new ByteArrayInputStream(bytes), schema);
		} catch (IOException e) {
			throw new RuntimeException("反序列化对象列表发生异常!", e);
		}
		return result;
	}
}
