package com.haven.simplej.rule.engine.model;

import lombok.*;
import com.haven.simplej.rule.engine.domain.AppInfo;
import java.util.Collection;

/**
 * 接入方信息
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AppInfoModel extends AppInfo {
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