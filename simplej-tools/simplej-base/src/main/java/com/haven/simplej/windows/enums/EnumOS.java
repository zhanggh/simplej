package com.haven.simplej.windows.enums;

/**
 * 操作系统
 * @author haven.zhang
 * @date 2019/1/16.
 */
public enum EnumOS {
	/** linux系统 */
	linux,

	/** macos系统 */
	macos,

	/** solaris系统 */
	solaris,

	/** unknown系统 */
	unknown,

	/** windows系统 */
	windows;

	/**
	 * @return 是否linux
	 */
	public boolean isLinux() {
		return this == linux || this == solaris;
	}

	/**
	 * @return 是否mac
	 */
	public boolean isMac() {
		return this == macos;
	}

	/**
	 * @return 是否windows
	 */
	public boolean isWindows() {
		return this == windows;
	}
}