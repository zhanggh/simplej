package com.haven.simplej.spring;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vip.vjtools.vjkit.collection.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * spring 工具类
 * Created by haven.zhang on 2019/1/3.
 */
@Slf4j
public class SpringContext implements ApplicationContextAware {

	private static ApplicationContext context;


	/**
	 * 默认的环境是production
	 */
	private static final String DEFAULT_ENV = "production";

	/**
	 * bean缓存
	 */
	private static final Map<String,Object> beanCache = Maps.newConcurrentMap();

	/**
	 * 取ApplicationContext
	 * @return
	 */
	public static ApplicationContext getContext() {
		return context;
	}

	public static void setContext(ApplicationContext ctx) {
		context = ctx;
	}

	@Override
	public void setApplicationContext(ApplicationContext ac) {
		setContext(ac);
	}

	public static <T> T getBean(String beanName) {
		if(beanCache.containsKey(beanName)){
			return (T) beanCache.get(beanName);
		}
		ConfigurableApplicationContext context = (ConfigurableApplicationContext) getContext();
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
		if (!beanFactory.containsBeanDefinition(beanName)) {
			log.warn("getBean :{} fail ,it is not exits", beanName);
			return null;
		}
		return (T) context.getBean(beanName);
	}

	public static <T> T getBean(Class clz) {
		try {
			return (T) context.getBean(clz);
		} catch (BeansException e) {
			log.error("getBean :{} fail ,it is not exits", clz.getName());
		}
		return null;
	}

	public static Environment getEnv() {
		return context.getEnvironment();
	}

	/**
	 *动态注册bean
	 * @param beanName
	 * @param beanClz
	 * @param <T>
	 */
	public static <T> void registerBean(String beanName, Class<T> beanClz, Map<String, Object> properties) {
		ConfigurableApplicationContext context = (ConfigurableApplicationContext) getContext();
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(beanClz);
		if (MapUtil.isNotEmpty(properties)) {
			properties.forEach((k, v) -> beanDefinitionBuilder.addPropertyValue(k, v));
		}

		beanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
	}

	/**
	 * 手工注册实例到缓存中
	 * @param beanName 实例名称
	 * @param bean 实例对象
	 */
	public static void registerBean(String beanName,Object bean){
		beanCache.put(beanName,bean);
	}

	/**
	 * 自动注入相关bean，比如非spring初始化的实例，依赖spring容器里面的相关bean
	 * @param bean
	 */
	public static void autowireBean(Object bean) {
		context.getAutowireCapableBeanFactory().autowireBean(bean);
	}

	/**
	 * 获取所有注册在spring 容器里面的bean
	 * @return
	 */
	public static List<String> queryBeansNames() {
		return Arrays.asList(context.getBeanDefinitionNames());
	}

	/**
	 * 获取某个类型的bean
	 * @param interfaceClz
	 * @return
	 */
	public static <T> List<T> getBeansOfType(Class interfaceClz) {
		List beans = Lists.newArrayList();

		try {
			Map<String, Object> beansMap = getContext().getBeansOfType(interfaceClz);
			beansMap.forEach((k, v) -> beans.add(v));
		} catch (Exception e) {
			log.warn("getBeansOfType error");
		}
		return beans;
	}

	/**
	 * 	获取当前环境
	 */
	public static String getActiveProfile() {
		if (context.getEnvironment().getActiveProfiles() == null || context.getEnvironment().getActiveProfiles().length == 0) {
			log.info("default env :{}", JSON.toJSONString(context.getEnvironment().getDefaultProfiles()));
			return DEFAULT_ENV;
		}
		String env = context.getEnvironment().getActiveProfiles()[0];
		if (StringUtils.isEmpty(env)) {
			return DEFAULT_ENV;
		} else if (StringUtils.equalsIgnoreCase("prod", env)) {
			return DEFAULT_ENV;
		} else {
			return env;
		}
	}
}
