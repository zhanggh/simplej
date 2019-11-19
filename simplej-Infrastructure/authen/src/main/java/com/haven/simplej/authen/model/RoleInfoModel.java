package com.haven.simplej.authen.model;

import com.haven.simplej.authen.domain.RoleInfo;
import lombok.*;
import java.util.Collection;

/**
 * 角色信息表
 */
@Getter
@Setter
@ToString(callSuper = true)
public class RoleInfoModel extends RoleInfo {
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