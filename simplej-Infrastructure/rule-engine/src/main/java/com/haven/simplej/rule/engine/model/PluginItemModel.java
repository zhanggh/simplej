package com.haven.simplej.rule.engine.model;

import lombok.*;
import com.haven.simplej.rule.engine.domain.PluginItem;
import java.util.Collection;

/**
 * 插件信息，注意，一个插件仅提供一个方法
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PluginItemModel extends PluginItem {
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