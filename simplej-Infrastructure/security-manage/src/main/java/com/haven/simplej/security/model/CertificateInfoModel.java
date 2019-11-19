package com.haven.simplej.security.model;

import com.haven.simplej.security.domain.CertificateInfo;
import lombok.*;
import java.util.Collection;

/**
 * 接入方对应的key关联管理表
 */
@Getter
@Setter
@ToString(callSuper = true)
public class CertificateInfoModel extends CertificateInfo {
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