package com.haven.simplej.authen.builder;

import com.haven.simplej.authen.domain.MenuInfo;
import com.haven.simplej.authen.model.MenuInfoModel;
import org.springframework.beans.BeanUtils;

/**
 * @Author: havenzhang
 * @Date: 2019/4/11 14:35
 * @Version 1.0
 */
public class MenuBuilder {

	public static MenuInfoModel build(MenuInfo menuInfo){
		MenuInfoModel model = new MenuInfoModel();
		BeanUtils.copyProperties(menuInfo,model);
		return model;
	}
}
