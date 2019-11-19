package com.haven.epay.payment.model;

import lombok.*;
import com.haven.epay.payment.domain.PaymentList;
import java.util.Collection;

/**
 * 交易流水表
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PaymentListModel extends PaymentList {
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