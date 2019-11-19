package com.haven.simplej.http;

import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHost;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @Author: havenzhang
 * @Date: 2019/3/8 17:41
 * @Version 1.0
 */
public class HttpTest {
	private static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";

	@Test
	public void sendTest(){


//		String url="http://localhost/form";
//		Map<String,String> params= Maps.newHashMap();
//		params.put("name","zhangsan");
//		params.put("age","33");
//		String resp =HttpExecuter.post(url,params,DEFAULT_CONTENT_TYPE);
//		System.out.println(resp);
	}

	@Test
	public void proxyRequestTest() throws UnsupportedEncodingException {
//		HttpHost proxy = new HttpHost("web-proxy.tencent.com", 8080, "http"); //添加代理
//		HttpExecuter.setHttpProxy(proxy);
//		byte[] resp = HttpExecuter.get("https://www.baidu.com","");
//		System.out.println(new String(resp));

		String word = "鬣";
		System.out.println(word.getBytes("GBK"));
		System.out.println(new String(word.getBytes("GBK"),"GBK"));
	}

	@Test
	public void  getTest() throws IOException {
		HttpBuilder builder = HttpExecuter.create();
		HttpExecuter executer = builder.build();
		String url = "https://iservice.boccc.com.hk/MSVWEB/ValidCodeGetter";
		//http请求，content是http响应字节数组
		byte[] content = executer.get(url, "");
		FileUtils.writeByteArrayToFile(new File("test.jpg"), content);
	}

	@Test
	public void httpsTest() throws UnsupportedEncodingException {
		HttpExecuter executer = HttpExecuter.create().setMimetype("application/x-www-form-urlencoded").setEncoding(
				"utf-8").build();

		String[] args = new String[]{};
		if (args == null || args.length == 0) {
			args = new String[]{"https://9.175.7.16:10016/wxwallet/WECHAT/wallet/sales.do"};
			args = new String[]{"https://localhost/test"};
		}
		byte[] resp = executer.post(args[0], "");
		System.out.println("response:" + new String(resp,"utf-8"));

	}
}
