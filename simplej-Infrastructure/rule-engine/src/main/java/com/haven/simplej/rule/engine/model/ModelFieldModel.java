package com.haven.simplej.rule.engine.model;

import lombok.*;
import com.haven.simplej.rule.engine.domain.ModelField;
import java.util.Collection;

/**
 * 模型字段信息，与model_info表是多对一关系
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ModelFieldModel extends ModelField {
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