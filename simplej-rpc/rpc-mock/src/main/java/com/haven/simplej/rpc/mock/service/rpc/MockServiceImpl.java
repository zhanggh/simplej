package com.haven.simplej.rpc.mock.service.rpc;

import com.haven.simplej.rpc.mock.model.MockInfo;
import com.haven.simplej.rpc.mock.model.MockMethod;
import com.haven.simplej.rpc.mock.service.MockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: havenzhang
 * @date: 2018/10/6 20:55
 * @version 1.0
 */
@Slf4j
@Service
public class MockServiceImpl implements MockService {

	@Override
	public MockInfo getMockInfo() {
		log.debug("----------------------------getMockInfo----------------------------------");
		return null;
	}

	@Override
	public int addMockMethod(MockMethod mockMethod) {
		return 0;
	}

	@Override
	public int updateMockMethod(MockMethod mockMethod) {
		return 0;
	}

	@Override
	public int getMockMethod(MockMethod mockMethod) {
		return 0;
	}

	@Override
	public int deleteMockMethod(MockMethod mockMethod) {
		return 0;
	}
}
