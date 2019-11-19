package com.haven.simplej.authen.model;

import com.google.common.collect.Lists;
import com.haven.simplej.authen.domain.MenuInfo;
import lombok.*;
import java.util.Collection;
import java.util.List;

/**
 * 菜单信息表
 */
@Getter
@Setter
@ToString(callSuper = true)
public class MenuInfoModel extends MenuInfo {
	private static final long serialVersionUID = 1L;

	private Long start;

	private Integer length;

    private Collection<Long> ids;

    public MenuInfoModel(){
    	super();
	}

    public MenuInfoModel(String menuCode){
    	super();
    	super.setMenuCode(menuCode);
	}

	/**
	 * 子菜单
	 */
	private List<MenuInfoModel> childs= Lists.newArrayList();

    public void setRequestUserId(Long requestUserId){
        //this.setCreateBy(requestUserId);
    }

    public void setRequestUserName(String requestUserName){
        //this.setCreateBy(requestUserName);
    }

}