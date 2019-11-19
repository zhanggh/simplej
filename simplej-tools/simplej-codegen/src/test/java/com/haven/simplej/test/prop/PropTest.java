package com.haven.simplej.test.prop;

import com.haven.simplej.property.PropertyManager;
import com.vip.vjtools.vjkit.io.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by haven.zhang on 2018/12/28.
 */
public class PropTest {
	public static void main(String[] args) throws IOException {
//		InputStream in = PropertyManager.class.getResourceAsStream("/db/datasource01.properties");
//		System.out.println(IOUtil.toString(in));
		List<InputStream> list = PropertyManager.getResourceFiles("/db2");
		list.forEach(in -> {
			try {
				System.out.println("------------------------------------------");
				System.out.println(IOUtil.toString(in));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

}
