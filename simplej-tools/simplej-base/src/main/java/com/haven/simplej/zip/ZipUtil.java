package com.haven.simplej.zip;

import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.ScatterZipOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.parallel.InputStreamSupplier;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;

/**
 * @author: havenzhang
 * @date: 2019/4/23 18:59
 * @version 1.0
 */
public class ZipUtil {

	private static ParallelScatterZipCreator scatterZipCreator = new ParallelScatterZipCreator();
	private static ScatterZipOutputStream dirs;

	private static final String DEFAULT_ENCODE = "utf-8";

	static {
		try {
			dirs = ScatterZipOutputStream.fileBased(File.createTempFile("scatter-dirs", "tmp"));
		} catch (IOException e) {

		}
	}


	/**
	 * 压缩目录/文件
	 * @param file，文件/目录
	 * @param outputFile 输出压缩文件
	 * @return
	 */
	public boolean zip(File file, String outputFile) {

		return true;
	}

	/**
	 * 解压文件
	 * @param zipFile 压缩文件
	 * @param outputPath 输出目录
	 * @return
	 */
	public boolean unzip(String zipFile, String outputPath) {

		return true;
	}

	private static void addEntry(String entryName, File currentFile, Scatter scatter) throws IOException {
		ZipArchiveEntry archiveEntry = new ZipArchiveEntry(entryName);
		archiveEntry.setMethod(ZipEntry.DEFLATED);
		final InputStreamSupplier supp = new CustomInputStreamSupplier(currentFile);
		scatter.addEntry(archiveEntry, supp);
	}


	private static void compressCurrentDirectory(File dir, Scatter scatter) throws IOException {
		if (dir == null) {
			throw new IOException("源路径不能为空！");
		}
		String relativePath = "";
		if (dir.isFile()) {
			relativePath = dir.getName();
			addEntry(relativePath, dir, scatter);
			return;
		}


		// 空文件夹
		if (dir.listFiles().length == 0) {
			relativePath = dir.getAbsolutePath().replace(scatter.getRootPath(), "");
			addEntry(relativePath + File.separator, dir, scatter);
			return;
		}
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				compressCurrentDirectory(f, scatter);
			} else {
				relativePath = f.getParent().replace(scatter.getRootPath(), "");
				addEntry(relativePath + File.separator + f.getName(), f, scatter);
			}
		}
	}


	public static void createZipFile(String rootPath, File result) throws Exception {
		File rootFile = new File(rootPath);
		File[] files = rootFile.listFiles();
		if (files == null || files.length == 0) {
			return;
		}
		rootPath = rootFile.getCanonicalPath();
		File dstFolder = new File(result.getParent());
		if (!dstFolder.isDirectory()) {
			dstFolder.mkdirs();
		}
		File rootDir = new File(rootPath);
		Scatter scatter = new Scatter(rootPath);
		compressCurrentDirectory(rootDir, scatter);
		ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(result);
		scatter.writeTo(zipArchiveOutputStream);
		zipArchiveOutputStream.close();
	}

}
