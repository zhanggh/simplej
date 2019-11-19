package com.haven.simplej.rpc.docgen.model;


import lombok.Data;

/**
 * maven 配置参数
 * @author: havenzhang
 * @date: 2018/9/22 21:24
 * @version 1.0
 */
@Data
public class MavenCfgModel {

	private String artifactId;

	private String groupId;

	private String version;

	public MavenCfgModel(String artifactId, String groupId,String version){
		this.artifactId = artifactId;
		this.groupId = groupId;
		this.version = version;
	}
}
