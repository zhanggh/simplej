package com.haven.simplej.rpc.mock.model;

import com.haven.simplej.rpc.mock.domain.MockInstance;
import lombok.*;
import java.util.Collection;

/**
 * mock服务对应的实例
 */
@Getter
@Setter
@ToString(callSuper = true)
public class MockInstanceModel extends MockInstance {
	private static final long serialVersionUID = 1L;

	private Long start;

	private Integer length;

    private Collection<Long> ids;

    public void setRequestUserId(Long requestUserId){
        //this.setCreateBy(requestUserId);
    }

    public void setRequestUserName(String requestUserName){
        //this.setCreateBy(requestUserName);
    }

}