package com.haven.simplej.rpc.config.service.rpc.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.config.constant.RpcConfigConstant;
import com.haven.simplej.rpc.config.model.*;
import com.haven.simplej.rpc.config.service.*;
import com.haven.simplej.security.DigestUtils;
import com.haven.simplej.text.StringUtil;
import com.haven.simplej.time.DateUtils;
import com.vip.vjtools.vjkit.mapper.BeanMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author: havenzhang
 * @date: 2018/06/11 23:45
 * @version 1.0
 */
@Service
@Slf4j
public class RpcConfigServiceImpl implements IConfigService {

	/**
	 * 属性文件监视器
	 */
	private Object configFileLock = new Object();
	/**
	 * 属性项监视器
	 */
	private Object configItemLock = new Object();

	/**
	 * 属性项的md5，用于判断属性是否发生变化
	 */
	private String configItemMd5;

	/**
	 * 属性文件的md5，用于判断文件内容是否发生变化
	 */
	private String configFileMd5;

	@Autowired
	private ConfigItemService confSvc;

	@Autowired
	private ConfigFileService configFileService;

	@Autowired
	private ConfigItemHistoryService confHisSvc;

	@Autowired
	private ConfigFileHistoryService configFileHistoryService;

	@Override
	public ConfigFileResponse queryNewConfigFile(String namespace, boolean waitForChange) {
		if (waitForChange) {
			synchronized (configFileLock) {
				long timeout = PropertyManager.getLong(RpcConfigConstant.RPC_SERVICE_CONFIG_WAITING_GAP, 10000);
				try {
					configFileLock.wait(timeout);
				} catch (Exception e) {
					log.error("configFileLock interrupt", e);
				}
			}
		}

		ConfigFileModel req = new ConfigFileModel();
		req.setNamespace(namespace);
		List<ConfigFileModel> list = configFileService.query(req);
		List<ConfigFileRpcModel> files = Lists.newArrayList();
		StringBuilder sb = new StringBuilder();
		list.forEach(e -> {
			ConfigFileRpcModel model = new ConfigFileRpcModel();
			model.setFileContext(e.getFileContext());
			model.setId(e.getId());
			model.setStatus(e.getStatus());
			files.add(model);
			sb.append(model.toString()).append("|");
		});
		ConfigFileResponse response = new ConfigFileResponse();
		String md5 = DigestUtils.md5Hex(sb.toString());
		if (!StringUtil.equalsIgnoreCase(md5, configFileMd5)) {
			response.setFiles(files);
			response.setChange(true);
		}
		//最新的md5
		configFileMd5 = md5;
		return response;
	}

	@Override
	public ConfigItemResponse queryNewConfigItem(String namespace, boolean waitForChange) {
		if (waitForChange) {
			synchronized (configItemLock) {
				long timeout = PropertyManager.getLong(RpcConfigConstant.RPC_SERVICE_CONFIG_WAITING_GAP, 10000);
				try {
					configItemLock.wait(timeout);
				} catch (Exception e) {
					log.error("configFileLock interrupt", e);
				}
			}
		}
		ConfigItemModel req = new ConfigItemModel();
		req.setNamespace(namespace);
		List<ConfigItemModel> list = confSvc.query(req);

		List<ConfigItemRpcModel> items = Lists.newArrayList();
		StringBuilder sb = new StringBuilder();
		list.forEach(e -> {
			ConfigItemRpcModel model = new ConfigItemRpcModel();
			model.setId(e.getId());
			model.setKey(e.getItemName());
			model.setValue(e.getItemValue());
			model.setStatus(e.getStatus());
			model.setDescription(e.getDescription());
			model.setVersion(e.getItemVersion());
			items.add(model);
			sb.append(model.toString()).append("|");
		});

		ConfigItemResponse response = new ConfigItemResponse();
		String md5 = DigestUtils.md5Hex(sb.toString());
		if (!StringUtil.equalsIgnoreCase(md5, configItemMd5)) {
			response.setNewProps(items);
			response.setChange(true);
		}
		//最新的md5
		configItemMd5 = md5;
		return response;
	}

	@Override
	public int addConfigFile(ConfigFileRpcModel configFile) {

		return 0;
	}

	@Override
	public int updateConfigFile(ConfigFileRpcModel configFile) {

		return 0;
	}

	@Override
	public int addConfigItem(ConfigItemRpcModel configItem) {
		log.debug("addConfigItem:{}", JSON.toJSONString(configItem));
		ConfigItemModel model = new ConfigItemModel();
		model.setDescription(configItem.getDescription());
		model.setItemName(configItem.getKey());
		model.setItemValue(configItem.getValue());
		model.setStatus(configItem.getStatus());
		model.setCreateTime(DateUtils.getTimestamp(new Date()));
		model.setUpdateTime(DateUtils.getTimestamp(new Date()));
		model.setCreatedBy(configItem.getOperator());
		model.setUpdatedBy(configItem.getOperator());
		model.setNamespace(configItem.getNamespace());
		model.setItemVersion(1);//初始化版本号
		return confSvc.save(model);
	}

	@Override
	@Transactional
	public int updateConfigItem(ConfigItemRpcModel configItem) {
		log.debug("updateConfigItem:{}", JSON.toJSONString(configItem));
		ConfigItemModel model = new ConfigItemModel();
		model.setNamespace(configItem.getNamespace());
		model.setItemName(configItem.getKey());
		model = confSvc.get(model);
		if (model == null) {
			log.error("can not find config item:{}", configItem.getKey());
			return 0;
		}
		//登记操作流水
		ConfigItemHistoryModel historyModel = BeanMapper.map(model, ConfigItemHistoryModel.class);
		historyModel.setOrgId(model.getId());
		historyModel.setId(null);
		confHisSvc.save(historyModel);
		model.setDescription(configItem.getDescription());
		model.setItemValue(configItem.getValue());
		model.setStatus(configItem.getStatus());
		model.setCreateTime(DateUtils.getTimestamp(new Date()));
		model.setUpdateTime(DateUtils.getTimestamp(new Date()));
		model.setCreatedBy(configItem.getOperator());
		model.setUpdatedBy(configItem.getOperator());
		model.setNamespace(configItem.getNamespace());
		model.setItemVersion(model.getItemVersion() + 1);//更新操作，版本号加1
		return confSvc.update(model);
	}

	@Override
	public ConfigItemRpcModel getConfigItem(String namespace, String key) {
		log.debug("getConfigItem，namespace:{}，key:{}", namespace, key);
		ConfigItemModel model = new ConfigItemModel();
		model.setNamespace(namespace);
		model.setItemName(key);
		model = confSvc.get(model);

		ConfigItemRpcModel item = new ConfigItemRpcModel();
		item.setVersion(model.getItemVersion());
		item.setDescription(model.getDescription());
		item.setStatus(model.getStatus());
		item.setKey(model.getItemName());
		item.setValue(model.getItemValue());
		item.setId(model.getId());
		item.setOperator(model.getUpdatedBy());
		return item;
	}

	@Override
	public ConfigItemRpcModel getConfigFile(String namespace, String fileName) {
		return null;
	}
}
