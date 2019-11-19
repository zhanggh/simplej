package com.haven.simplej.rule.engine.model;

import lombok.*;
import com.haven.simplej.rule.engine.domain.Expression;
import java.util.Collection;

/**
 * 规则表达式，一个规则对应多个表达式
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ExpressionModel extends Expression {
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