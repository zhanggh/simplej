package com.haven.simplej.zip;

import com.haven.simplej.exception.UncheckedException;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.io.input.NullInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author: havenzhang
 * @date: 2019/4/23 19:36
 * @version 1.0
 */
public class CustomInputStreamSupplier implements InputStreamSupplier {
	private File currentFile;


	public CustomInputStreamSupplier(File currentFile) {
		this.currentFile = currentFile;
	}


	@Override
	public InputStream get() {
		try {
			// InputStreamSupplier api says:
			// 返回值：输入流。永远不能为Null,但可以是一个空的流
			return currentFile.isDirectory() ? new NullInputStream(0) : new FileInputStream(currentFile);
		} catch (FileNotFoundException e) {
			throw new UncheckedException(e);
		}
	}
}
