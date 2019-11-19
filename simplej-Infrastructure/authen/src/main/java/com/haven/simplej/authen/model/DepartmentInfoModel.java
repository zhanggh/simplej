package com.haven.simplej.authen.model;

import com.haven.simplej.authen.domain.DepartmentInfo;
import lombok.*;
import java.util.Collection;

/**
 * 部门信息
 */
@Getter
@Setter
@ToString(callSuper = true)
public class DepartmentInfoModel extends DepartmentInfo {
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