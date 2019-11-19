package com.haven.simplej.rule.engine.model;

import lombok.*;
import com.haven.simplej.rule.engine.domain.RuleOperator;
import java.util.Collection;

/**
 * 规则操作符配置表
 */
@Getter
@Setter
@ToString(callSuper = true)
public class RuleOperatorModel extends RuleOperator {
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