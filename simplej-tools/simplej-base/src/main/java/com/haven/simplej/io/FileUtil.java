package com.haven.simplej.io;

import com.google.common.collect.Lists;
import com.haven.simplej.property.PropertyManager;
import com.vip.vjtools.vjkit.base.type.Pair;
import com.vip.vjtools.vjkit.io.IOUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by haven.zhang on 2019/1/3.
 */
public class FileUtil extends com.vip.vjtools.vjkit.io.FileUtil {

	/**
	 * 根据文件路径创建文件，如果目录不存在，则创建目录
	 * 如：/apps/data/logs/20181019/app.log,则会判断/apps/data/logs/20181019/是否存在，不存在则创建
	 *
	 * @param filePath
	 */
	public static File forceTouch(String filePath) throws IOException {
		File file = new File(filePath);
		String path = file.getPath().substring(0, file.getPath().lastIndexOf(File.separatorChar));
		if (!isDirExists(path)) {
			makesureDirExists(path);
		}
		FileUtil.touch(filePath);
		return file;
	}

	/**
	 * 写入字节数组到文件
	 * @param file
	 * @param data
	 * @throws IOException
	 */
	public static void writeByteArrayToFile(File file, byte[] data) throws IOException {
		OutputStream out = null;
		try {
			out = asOututStream(file);
			out.write(data);
		} finally {
			IOUtil.closeQuietly(out);
		}

	}

	public static String getResourceStr(String file) throws IOException {
		InputStream in = FileUtil.class.getClassLoader().getResourceAsStream(file);
		if (in == null) {
			in = FileUtil.class.getResourceAsStream(file);
		}
		if (in == null) {
			return StringUtils.EMPTY;
		}
		return IOUtils.toString(in, "utf-8");
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
		List<Pair<String, InputStream>> inputStreamList = Lists.newArrayList();
		try {
			URL configUrl = PropertyManager.class.getResource(path);
			if (configUrl == null) {
				configUrl = PropertyManager.class.getClassLoader().getResource(path);
			}
			if (StringUtils.equalsIgnoreCase("jar", configUrl.getProtocol())) {
				String jarPath = configUrl.toString().substring(0, configUrl.toString().indexOf("!/") + 2);
				URL jarURL = new URL(jarPath);
				JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
				JarFile jarFile = jarCon.getJarFile();
				Enumeration jarEntries = jarFile.entries();
				while (jarEntries.hasMoreElements()) {
					JarEntry entry = (JarEntry) jarEntries.nextElement();

					String entryName = entry.getName();
					if (entryName.startsWith(path.substring(1))) {
						InputStream in = PropertyManager.class.getClassLoader().getResourceAsStream(entryName);
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
		return inputStreamList;
	}
}
