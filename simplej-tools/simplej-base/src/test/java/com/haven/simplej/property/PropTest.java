package com.haven.simplej.property;

import com.haven.simplej.codegen.PropertyKey;
import com.vip.vjtools.vjkit.io.IOUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by haven.zhang on 2018/12/28.
 */
public class PropTest {
	public static void main(String[] args) throws IOException {
		System.out.println(PropertyManager.get(PropertyKey.BASE_PACKAGE));

		List<InputStream> list = PropertyManager.getResourceFiles("/db");
		list.forEach(in->{
			try {
				System.out.println("------------------------------------------");
				System.out.println(IOUtil.toString(in));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

	private void copyJarEntryOutside(URL entryUrl, String entryDir, String destDir) {
		try {
			String jarPath = entryUrl.toString().substring(0, entryUrl.toString().indexOf("!/") + 2);
			URL jarURL = new URL(jarPath);
			JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
			JarFile jarFile = jarCon.getJarFile();
			Enumeration jarEntries = jarFile.entries();
			while (jarEntries.hasMoreElements()) {
				JarEntry entry = (JarEntry) jarEntries.nextElement();

				String entryName = entry.getName();
				if ((entryName.startsWith(entryDir)) && (!entryName.endsWith("mock.conf")) && (!entry.isDirectory())) {
					String destFilename = entryName.replace(entryDir, "");
					File destFile = new File(destDir + destFilename);
					try {
						InputStream in = PropertyManager.class.getClassLoader().getResourceAsStream(entryName);
						Throwable localThrowable2 = null;
						try {

							FileUtils.copyInputStreamToFile(in, destFile);
						} catch (Throwable localThrowable1) {
							localThrowable2 = localThrowable1;
							throw localThrowable1;
						} finally {
							if (in != null)
								if (localThrowable2 != null)
									try { in.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); }
								else
									in.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
