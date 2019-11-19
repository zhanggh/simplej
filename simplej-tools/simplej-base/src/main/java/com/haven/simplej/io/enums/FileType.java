package com.haven.simplej.io.enums;

/**
 * 文件类型枚举类
 * @author  haven.zhang
 * @date 2018/11/7.
 */
public enum FileType {
	TXT,XLS,XLSX,CSV;

	public static final FileType get(String name){
		if(TXT.name().equalsIgnoreCase(name)){
			return TXT;
		}
		if(XLS.name().equalsIgnoreCase(name)){
			return XLS;
		}
		if(XLSX.name().equalsIgnoreCase(name)){
			return XLSX;
		}
		if(CSV.name().equalsIgnoreCase(name)){
			return CSV;
		}
		return null;
	}
}
