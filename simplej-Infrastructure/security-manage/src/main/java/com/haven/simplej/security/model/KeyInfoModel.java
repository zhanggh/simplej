package com.haven.simplej.security.model;

import com.haven.simplej.security.domain.KeyInfo;
import lombok.*;
import java.util.Collection;

/**
 * 秘钥信息表
 */
@Getter
@Setter
@ToString(callSuper = true)
public class KeyInfoModel extends KeyInfo {
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