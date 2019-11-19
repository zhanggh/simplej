package com.haven.simplej.script.groovy;

import com.alibaba.fastjson.JSON;
import groovy.lang.GroovyClassLoader;
import lombok.extern.slf4j.Slf4j;

/**
 * @author haven
 * @date 2018/9/22.
 */
@Slf4j
public class GroovyClassLoaderExt extends GroovyClassLoader {
	public GroovyClassLoaderExt(ClassLoader loader) {
		super(loader);
	}

	/**
	 * 删除缓存中的class
	 * @param fileName
	 * @param className
	 */
	public void removeCache(String fileName, String className) {
		log.info("remove groovy cache file:{}", fileName);
		log.info("groovy sourceCache:{}", JSON.toJSONString(this.sourceCache, true));
		log.info("groovy classCache:{}", JSON.toJSONString(this.sourceCache, true));
		String key = "file:/" + fileName;
		key = key.replaceAll("//", "/");
		log.info("groovy remove key:{}", key);
		this.sourceCache.remove(key);
		this.sourceCache.remove(key.replaceAll("\\\\", "/"));
		this.classCache.remove(key);
		this.classCache.remove(className);

		log.info("groovy after remove sourceCache:{}", JSON.toJSONString(this.sourceCache, true));
		log.info("groovy after remove classCache:{}", JSON.toJSONString(this.sourceCache, true));
	}
}
