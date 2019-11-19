package com.haven.simplej.rpc.config.model;

import lombok.*;
import com.haven.simplej.rpc.config.domain.ConfigFileHistory;
import java.util.Collection;

/**
 * 属性文件信息修改记录日志
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ConfigFileHistoryModel extends ConfigFileHistory {
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