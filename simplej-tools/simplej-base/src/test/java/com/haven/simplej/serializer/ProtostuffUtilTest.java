package com.haven.simplej.serializer;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.codegen.ProjectInfo;
import com.vip.vjtools.vjkit.time.DateUtil;
import lombok.Setter;
import lombok.Getter;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author haven.zhang
 * @date 2019/1/29.
 */
public class ProtostuffUtilTest {


	@Test
	public void serializer() {

		Map<String, ProjectInfo> map = Maps.newHashMap();
		ProjectInfo projectInfo = new ProjectInfo();
		projectInfo.setAppName("epay");
		projectInfo.setParentVersion("1.0.0");
		map.put("pro001", projectInfo);

		byte[] bytes = ProtostuffUtil.serializer(map);

		Map map2 = ProtostuffUtil.deserializer(bytes, HashMap.class);

		System.out.println(map2.get("pro001"));

		Request request = new Request();
		Header header = new Header();
		header.setOrgins("xxxxxx");
		header.setRequestId(System.currentTimeMillis() + "");
		header.setMap(map);
		header.setRequestTime(new Timestamp(new Date().getTime()));
		header.setDate(new Date());

		Body body = new Body();
		List<Detail> list = Lists.newArrayList();
		Detail detail = new Detail();
		detail.setMsg("xxxxx msg");
		list.add(detail);
		body.setDetailList(list);

		request.setHeader(header);
		request.setBody(body);

		bytes = ProtostuffUtil.serializer(request);

		Request request1 = ProtostuffUtil.deserializer(bytes, Request.class);

		System.out.println(JSON.toJSONString(request1, true));

		bytes = ProtostuffUtil.serializeList(list);

		List ls = ProtostuffUtil.deserializeList(bytes,Detail.class);


		System.out.println(JSON.toJSONString(ls, true));
	}

}

@Setter
@Getter
class Header {
	String orgins;
	String requestId;
	Timestamp requestTime;
	Date date;
	Map<String, ProjectInfo> map;
}

@Setter
@Getter
class Body {
	List<Detail> detailList;
}

@Setter
@Getter
class Detail {
	String msg;
}

@Setter
@Getter
class Request {
	Header header;
	Body body;
}