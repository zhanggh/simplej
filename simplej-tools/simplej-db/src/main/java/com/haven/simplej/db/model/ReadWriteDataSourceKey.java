package com.haven.simplej.db.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import com.haven.simplej.db.datasource.balance.strategy.LoadBalanceStrategy;
import com.haven.simplej.db.datasource.balance.strategy.RandomLoadBalanceStrategy;
import com.haven.simplej.exception.UncheckedException;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author haven.zhang
 * @date 2019/1/22.
 */
@Setter
@Getter
@ToString
public class ReadWriteDataSourceKey{

	/**
	 * 写库(master)的数据源lookupkey
	 */
	private DataSourceLookupKey writeKey;

	/**
	 * 读库数据源lookupkey
	 */
	private List<DataSourceLookupKey> readKeys = Lists.newArrayList();


	/**
	 * 读库负载策略器
	 */
	private LoadBalanceStrategy<DataSourceLookupKey> lbStrategy;

	public ReadWriteDataSourceKey(DataSourceLookupKey writeKey, List<DataSourceLookupKey> readKeys) {
		this.writeKey = writeKey;
		if(CollectionUtil.isNotEmpty(readKeys)){
			this.readKeys.addAll(readKeys);
			LoadBalanceStrategy strategy = new RandomLoadBalanceStrategy(this.readKeys);
			setLbStrategy(strategy);
		}

	}

	@JSONField(serialize = false)
	public DataSourceLookupKey getReadKey(){
		List<DataSourceLookupKey> readKeys = getReadKeys();
		if(CollectionUtil.isEmpty(readKeys)){
			return null;
		}
		if(lbStrategy == null){
			if(CollectionUtil.isEmpty(readKeys)){
				throw new UncheckedException("readKeys must not be empty");
			}
			return readKeys.get(0);
		}else {
			return lbStrategy.elect();
		}
	}
}
