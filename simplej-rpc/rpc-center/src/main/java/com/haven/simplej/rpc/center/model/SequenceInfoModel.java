package com.haven.simplej.rpc.center.model;

import com.haven.simplej.rpc.center.domain.SequenceInfo;
import lombok.*;
import java.util.Collection;

/**
 * 序列号生成规则信息
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SequenceInfoModel extends SequenceInfo {
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