package com.haven.simplej.rpc.mock.model;

import lombok.*;
import com.haven.simplej.rpc.mock.domain.MockServiceData;
import java.util.Collection;

/**
 * 远程服务方法模拟结果信息
 */
@Getter
@Setter
@ToString(callSuper = true)
public class MockServiceDataModel extends MockServiceData {
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