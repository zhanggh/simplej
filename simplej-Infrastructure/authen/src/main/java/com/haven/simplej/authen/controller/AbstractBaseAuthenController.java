package com.haven.simplej.authen.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.authen.builder.MenuBuilder;
import com.haven.simplej.authen.domain.MenuInfo;
import com.haven.simplej.authen.manager.AuthorityManager;
import com.haven.simplej.authen.model.MenuInfoModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: havenzhang
 * @Date: 2019/4/10 22:01
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractBaseAuthenController {

	@Autowired
	private AuthorityManager manager;

	@ModelAttribute
	public void loadMenus(ModelMap map, HttpServletRequest request) {
		log.info("----------loadMenus-----------");
		List<MenuInfo> menus = manager.getUserMenus((String) request.getSession().getAttribute("userCode"));
		log.info("menus:{}", JSON.toJSONString(menus, true));
		// 一级菜单映射
		Map<String, MenuInfoModel> level1Map = Maps.newHashMap();

		menus.forEach(e -> {
			if (e.getMenuLevel().intValue() == 1) {
				//一级菜单
				if (level1Map.containsKey(e.getMenuCode())) {
					BeanUtils.copyProperties(e, level1Map.get(e.getMenuCode()));
				} else {
					level1Map.put(e.getMenuCode(), MenuBuilder.build(e));
				}
			} else if (e.getMenuLevel().intValue() == 2) {
				//二级菜单
				if (!level1Map.containsKey(e.getParentMenuCode())) {
					level1Map.put(e.getParentMenuCode(), new MenuInfoModel(e.getParentMenuCode()));
				}
				level1Map.get(e.getParentMenuCode()).getChilds().add(MenuBuilder.build(e));
			}
			//不设三级菜单
		});
		map.addAttribute("menus", level1Map);
		log.info("menus:{}", JSON.toJSONString(menus, true));
	}
}
