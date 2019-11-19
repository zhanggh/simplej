package com.haven.simplej.authen.model;

import com.haven.simplej.authen.domain.UserInfo;
import lombok.*;
import java.util.Collection;

/**
 * 用户信息表
 */
@Getter
@Setter
@ToString(callSuper = true)
public class UserInfoModel extends UserInfo {
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