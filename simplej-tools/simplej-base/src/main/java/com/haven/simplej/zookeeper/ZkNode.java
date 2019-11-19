package com.haven.simplej.zookeeper;

import com.alibaba.fastjson.annotation.JSONField;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.Setter;
import lombok.Getter;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * zk节点信息
 * @author haven.zhang
 * @date 2019/1/30.
 */
@Setter
@Getter
public class ZkNode {
	/**
	 * 数据
	 */
	private byte[] data;

	/**
	 * 孩子节点
	 */
	private List<ZkNode> childrens;

	/**
	 * 节点路径
	 */
	private String path;

	/**
	 * 节点类型，临时节点或者持久化节点
	 */
	private CreateMode mode;

	/**
	 * 节点状态信息
	 */
	@JSONField(serialize = false)
	private Stat stat;

	/**
	 * 是否为叶子节点
	 * @return
	 */
	public boolean isLeaf(){

		return CollectionUtil.isEmpty(childrens);
	}


}
