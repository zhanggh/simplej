package com.haven.simplej.rpc.config.model;

import lombok.*;
import com.haven.simplej.rpc.config.domain.ConfigFile;
import java.util.Collection;

/**
 * 属性文件信息
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ConfigFileModel extends ConfigFile {
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