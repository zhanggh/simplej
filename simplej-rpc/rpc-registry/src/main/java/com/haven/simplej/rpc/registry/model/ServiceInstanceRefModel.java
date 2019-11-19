package com.haven.simplej.rpc.registry.model;

import com.haven.simplej.rpc.registry.domain.ServiceInstanceRef;
import lombok.*;
import java.util.Collection;

/**
 * 服务与实例的关联关系
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ServiceInstanceRefModel extends ServiceInstanceRef {
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