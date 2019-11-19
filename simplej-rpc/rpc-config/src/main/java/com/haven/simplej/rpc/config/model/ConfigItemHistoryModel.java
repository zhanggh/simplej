package com.haven.simplej.rpc.config.model;

import lombok.*;
import com.haven.simplej.rpc.config.domain.ConfigItemHistory;
import java.util.Collection;

/**
 * 属性配置信息日志
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ConfigItemHistoryModel extends ConfigItemHistory {
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