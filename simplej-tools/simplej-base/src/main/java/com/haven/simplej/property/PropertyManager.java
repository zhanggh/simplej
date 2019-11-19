package com.haven.simplej.property;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.base.type.Pair;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.io.FileUtil;
import com.vip.vjtools.vjkit.io.IOUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by haven.zhang on 2019/1/3.
 */
@Slf4j
public class PropertyManager implements InitializingBean {

	@Autowired
	private Environment environment;
	private static Properties prop = new Properties();
	/**
	 * 配置中心的属性
	 */
	private static Properties remoteProps = new Properties();
	private static PropertyManager manager;

	private static Map<String, List<Pair<String, InputStream>>> propMap = Maps.newHashMap();

	/**
	 * 默认的属性配置文件
	 */
	private static String[] propFiles = new String[]{"/application.properties","/rpc-client.properties","/rpc-server"
			+ ".properties"};

	static {
		for (String propFile : propFiles) {
			try (InputStream is = PropertyManager.class.getResourceAsStream(propFile)) {
				if (is == null || is.available() <= 0) {
					log.warn("file " + propFile + " not found");
				} else {
					prop.load(is);
				}
			} catch (Exception e) {
				log.error("init properites error", e);
			}
		}

	}



	public PropertyManager() {
		log.info("init ConfigManager");
	}

	public static long getLong(String key, long defaultVal) {
		String value = get(key, String.valueOf(defaultVal));
		return Long.parseLong(value);
	}

	public static int getInt(String key, int defaultVal) {
		String value = get(key, String.valueOf(defaultVal));
		return Integer.parseInt(value);
	}

	public static int getInt(String key) {
		String value = get(key);
		return Integer.parseInt(value);
	}

	public static long getLong(String key) {
		String value = get(key);
		return Long.parseLong(value);
	}

	public static String get(String key, String defaultVal) {

		return StringUtils.isEmpty(get(key)) ? defaultVal : StringUtils.trim(get(key));
	}

	/**
	 * 获取属性值，
	 * 首先从项目的properties文件中查询，查不到则从System.getProperties(key)方法中查询，再则从环境变量中查询
	 * @param key
	 * @return
	 */
	public static String get(String key) {

		String value = remoteProps.getProperty(key);
		if (StringUtils.isEmpty(value)) {
			value = prop.getProperty(key);
		}
		if (StringUtils.isEmpty(value) && manager != null) {
			value = manager.environment.getProperty(key);
		}
		if (StringUtils.isEmpty(value)) {
			value = System.getProperty(key);
		}
		if (StringUtils.isEmpty(value)) {
			value = System.getenv(key);
		}
		return value;
	}

	public static Properties getProp() {
		return prop;
	}

	public static boolean getBoolean(String key, boolean defaultBool) {
		if (StringUtil.isEmpty(get(key))) {
			return defaultBool;
		}
		Boolean b = Boolean.parseBoolean(get(key));
		return b;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		manager = this;
	}

	public static void updateRemoteProps(String key,String value){
		remoteProps.setProperty(key,value);
	}

	/**
	 * 读取property文件内容
	 * @param propertyFile
	 * @return
	 */
	public static Properties read(String propertyFile) {
		Properties prop = null;
		try {
			InputStream is = PropertyManager.class.getResourceAsStream(propertyFile);
			if (is == null) {
				is = PropertyManager.class.getClassLoader().getResourceAsStream(propertyFile);
			}
			if (is == null) {
				is = new FileInputStream(new File(propertyFile));
			}
			if (is == null || is.available() <= 0) {
				throw new IOException("file " + propertyFile + " not found");
			}
			prop = new Properties();
			prop.load(is);
		} catch (Exception e) {
			log.error("init properites error", e);
		}
		return prop;
	}


	/**
	 * 从resource下指定的目录读取文件流，多个文件返回多个流
	 * 比如resource/db目录下包含：db1.properties 和 db2.properties文件
	 * getResourceFiles("/db")
	 * 则返回db1.properties 和 db2.properties文件流
	 * @param path
	 * @return
	 */
	public static List<InputStream> getResourceFiles(String path) {
		List<InputStream> inputStreamList = Lists.newArrayList();
		List<Pair<String, InputStream>> pairs = getResource(path);
		pairs.forEach(e -> inputStreamList.add(e.getRight()));
		return inputStreamList;
	}


	/**
	 * 从resource下指定的目录读取文件流，多个文件返回多个流
	 * 比如resource/db目录下包含：db1.properties 和 db2.properties文件
	 * getResourceFiles("/db")
	 * 则返回db1.properties 和 db2.properties文件流
	 * @param path
	 * @return
	 */
	public static List<Pair<String, InputStream>> getResource(String path) {
		List<Pair<String, InputStream>> inputStreamList = null;
		try {
			inputStreamList = propMap.get(path);
			if (CollectionUtil.isNotEmpty(inputStreamList)) {
				return inputStreamList;
			}
			inputStreamList = Lists.newArrayList();
			File dbPath = new File(path.substring(1));
			if (dbPath.exists()) {
				inputStreamList.addAll(readFiles(dbPath));
				propMap.put(path, inputStreamList);
				return inputStreamList;
			}
			URL configUrl = PropertyManager.class.getResource(path);
			if (configUrl == null) {
				configUrl = PropertyManager.class.getClassLoader().getResource(path);
			}
			if (configUrl == null) {
				path = "/BOOT-INF/classes" + path;
				configUrl = PropertyManager.class.getClassLoader().getResource(path);
			}
			if (configUrl == null) {
				configUrl = PropertyManager.class.getResource(path);
			}
			log.info("getResourceFiles protocl:{},path:{}", configUrl.getProtocol(), configUrl.getPath());
			if (StringUtils.equalsIgnoreCase("jar", configUrl.getProtocol())) {
				String jarPath = configUrl.toString().substring(0, configUrl.toString().indexOf("!/") + 2);
				log.info("getResourceFiles protocl:{},jarPath:{}", configUrl.getProtocol(), jarPath);
				URL jarURL = new URL(jarPath);
				JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
				JarFile jarFile = jarCon.getJarFile();
				Enumeration jarEntries = jarFile.entries();
				while (jarEntries.hasMoreElements()) {
					JarEntry entry = (JarEntry) jarEntries.nextElement();

					String entryName = entry.getName();

					if (entryName.contains(path.substring(1)) && entryName.endsWith("properties")) {
						log.info("read properties file :{}", entryName);
						InputStream in = PropertyManager.class.getClassLoader().getResourceAsStream(entryName);
						if (in == null) {
							in = PropertyManager.class.getResourceAsStream(path);
						}
						Pair<String, InputStream> pair = new Pair<>(entryName, in);
						inputStreamList.add(pair);
					}
				}
			} else {
				String[] files = new File(configUrl.getPath()).list();
				if (!path.endsWith("\\/")) {
					path += "/";
				}
				if (files != null) {
					for (String file : files) {
						InputStream in = PropertyManager.class.getResourceAsStream(path + file);
						Pair<String, InputStream> pair = new Pair<>(file, in);
						inputStreamList.add(pair);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		propMap.put(path, inputStreamList);
		return inputStreamList;
	}

	public static List<Pair<String, InputStream>> readFiles(File dir) throws IOException {
		List<Pair<String, InputStream>> inputStreamList = Lists.newArrayList();
		String[] files = dir.list();

		if (files != null) {
			for (String file : files) {
				InputStream in = FileUtil.asInputStream(file);
				Pair<String, InputStream> pair = new Pair<>(file, in);
				inputStreamList.add(pair);
			}
		}
		return inputStreamList;
	}
}
