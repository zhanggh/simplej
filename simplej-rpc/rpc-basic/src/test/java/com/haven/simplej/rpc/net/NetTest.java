package com.haven.simplej.rpc.net;

import com.vip.vjtools.vjkit.net.NetUtil;
import org.junit.Test;

/**
 * @author: havenzhang
 * @date: 2019/4/27 21:02
 * @version 1.0
 */
public class NetTest {


	@Test
	public void getIpTest(){
		System.out.println(NetUtil.getLocalHost());
	}
}
