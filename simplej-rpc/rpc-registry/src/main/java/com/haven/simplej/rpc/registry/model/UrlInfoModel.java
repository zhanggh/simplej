package com.haven.simplej.rpc.registry.model;

import com.haven.simplej.rpc.registry.domain.UrlServiceInfo;
import lombok.*;
import java.util.Collection;

/**
 * web服务url信息
 */
@Getter
@Setter
@ToString(callSuper = true)
public class UrlInfoModel extends UrlServiceInfo {
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