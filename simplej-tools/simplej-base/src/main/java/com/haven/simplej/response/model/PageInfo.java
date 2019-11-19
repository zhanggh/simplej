package com.haven.simplej.response.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author  haven.zhang
 * @date 2019/1/8.
 */
@Getter
@Setter
public class PageInfo<T> {
	private List<T> data;
	private long count;
	private int pageNum;
	private int pageSize;

	public PageInfo(long count,int pageNum,int pageSize){
		this.count = count;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}
}
