package com.haven.simplej.authen.manager;

import com.haven.simplej.authen.domain.MenuInfo;
import com.haven.simplej.authen.service.MenuInfoService;
import com.haven.simplej.authen.sql.SqlBuilder;
import com.haven.simplej.cache.CacheManager;
import com.haven.simplej.db.annotation.RepositorySharding;
import com.haven.simplej.db.dao.CommonDao;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: havenzhang
 * @Date: 2019/4/6 16:22
 * @Version 1.0
 */
@Slf4j
@Component
@RepositorySharding(dbName = "authen")
public class AuthorityManager {


	@Autowired
	private MenuInfoService menuInfoService;

	@Autowired
	private CommonDao dao;

	/**
	 * 判断用户是否有权限访问
	 * @return
	 */
	public boolean checkAccess(String userCode, String uri) {
		List<MenuInfo> menus = getUserMenus(userCode);
		for (MenuInfo menu : menus) {
			Pattern p = CacheManager.getObject(menu.getMenuUri());
			if (p == null) {
				p = Pattern.compile(menu.getMenuUri());
				CacheManager.putLocal(menu.getMenuUri(), p);
			}
			Matcher m = p.matcher(uri);
			return m.matches();
		}
		return false;
	}


	/**
	 * 根据用户号获取对应的权限列表
	 * @param userCode
	 * @return
	 */
	public List<MenuInfo> getUserMenus(String userCode) {
		List<MenuInfo> menus;
		String sql = SqlBuilder.getUserAuthoritySql();
		menus = dao.getObjs(sql, new Object[]{userCode}, MenuInfo.class);
		if (CollectionUtil.isEmpty(menus)) {
			menus = Collections.EMPTY_LIST;
		}
		return menus;
	}
}
