package com.haven.simplej.rule.engine.model;

import lombok.*;
import com.haven.simplej.rule.engine.domain.RuleItem;
import java.util.Collection;

/**
 * 规则配置信息
 */
@Getter
@Setter
@ToString(callSuper = true)
public class RuleItemModel extends RuleItem {
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