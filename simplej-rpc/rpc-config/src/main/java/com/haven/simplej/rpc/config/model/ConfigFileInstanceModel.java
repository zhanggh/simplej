package com.haven.simplej.rpc.config.model;

import lombok.*;
import com.haven.simplej.rpc.config.domain.ConfigFileInstance;
import java.util.Collection;

/**
 * 配置项生效的服务对应的实例
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ConfigFileInstanceModel extends ConfigFileInstance {
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