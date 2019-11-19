package com.haven.simplej.script.groovy;


import com.haven.simplej.exception.UncheckedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * groovy 工厂类
 * @author haven.zhang
 */
@Slf4j
public class GroovyFactory {


	private Map<String, GroovyFile> grvMap = new HashMap<>();
	private static GroovyFactory _INSTANCE = new GroovyFactory();

	private GroovyClassLoaderExt groovyCl = new GroovyClassLoaderExt(GroovyFactory.class.getClassLoader());

	private GroovyFactory() {
		super();
	}

	public static GroovyFactory getFactory() {
		return _INSTANCE;
	}

	/**
	 * 获取groovy对象
	 * @param path
	 * @param <T>
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public <T> T getObject(String path) {
		T obj = null;
		try {
			Class<T> clz = getGroovyClass(path);
			obj = clz.newInstance();
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
		return obj;
	}

	/**
	 * 加载groovy文件
	 * @param path
	 * @return
	 */
	public <T> Class<T> getGroovyClass(String path) {
		try {
			Class<T> groovyClass;
			File file = new File(path);
			//            String md5Hex = DigestUtils.md5Hex(new FileInputStream(file));
			//通过文件的修改时间判断是否需要加载
			long lastModify = file.lastModified();
			path = file.getCanonicalPath();
			if (grvMap.containsKey(path)) {
				log.info("groovy file:{},cache modifyed:{},lastModify{}", path, grvMap.get(path).getLastModify(), lastModify);
				if (lastModify != grvMap.get(path).getLastModify()) {
					log.info("reload groovy file:{}", path);
					groovyCl.removeCache(path, grvMap.get(path).getCls().getName());
					groovyClass = groovyCl.parseClass(file);
					grvMap.get(path).setCls(groovyClass);
					grvMap.get(path).setLastModify(lastModify);
				} else {
					groovyClass = (Class<T>) grvMap.get(path).getCls();
				}
			} else {
				groovyClass = groovyCl.parseClass(file);
				grvMap.put(path, new GroovyFile(groovyClass, lastModify));
			}
			return groovyClass;
		} catch (Exception e) {
			log.error("加载groovy文件失败：{}", path, e);
		}
		return null;
	}

	/**
	 * 根据script文本生成class对象
	 * @param scriptText 脚步内容
	 * @param alias 定义一个别名
	 * @return
	 */
	public Class<?> getGroovyClass(String scriptText, String alias) {
		try {
			Class groovyClass;
			alias = "script_" + alias;
			String md5Hex = DigestUtils.md5Hex(scriptText);
			if (grvMap.containsKey(alias)) {
				if (!md5Hex.equals(grvMap.get(alias).getMd5())) {
					groovyCl.removeCache(alias, grvMap.get(alias).getCls().getName());
					groovyClass = groovyCl.parseClass(scriptText);
					grvMap.get(alias).setCls(groovyClass);
					grvMap.get(alias).setMd5(md5Hex);
				} else {
					groovyClass = grvMap.get(alias).getCls();
				}
			} else {
				groovyClass = groovyCl.parseClass(alias);
				grvMap.put(alias, new GroovyFile(groovyClass, md5Hex));
			}
			return groovyClass;
		} catch (Exception e) {
			log.error("加载groovy脚步失败：{}", alias, e);
		}
		return null;
	}

	public static void main(String[] args) throws IllegalAccessException, InstantiationException {
		//        String path = "C:/epay";
		//        long start = System.currentTimeMillis();
		//        Class c = GroovyFactory.getFactory().getGroovyClass(path);
		//        System.out.println(c.toString());
		//        long end = System.currentTimeMillis();
		//
		//        GroovyObject groovyObject = (GroovyObject) c.newInstance();
		//        groovyObject.invokeMethod("smail", null);
		//
		//        System.out.println("first:" + (end - start));
		//        start = System.currentTimeMillis();
		//        Class c2 = GroovyFactory.getFactory().getGroovyClass(path);
		//        System.out.println(c2.toString());
		//        System.out.println(c2 == c);
		//
		//        groovyObject = (GroovyObject) c2.newInstance();
		//        groovyObject.invokeMethod("smail", null);
		//
		//        end = System.currentTimeMillis();
		//        System.out.println("second:" + (end - start));
	}


}