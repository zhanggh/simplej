package com.haven.simplej.authen.model;

import com.haven.simplej.authen.domain.GroupInfo;
import lombok.*;
import java.util.Collection;

/**
 * 小组信息，一个部门下面对应多个小组
 */
@Getter
@Setter
@ToString(callSuper = true)
public class GroupInfoModel extends GroupInfo {
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