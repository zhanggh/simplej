package com.haven.simplej.io;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.bean.BeanUtil;
import com.haven.simplej.io.enums.FileType;
import com.opencsv.CSVReader;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * 文件解析工具，可以对上传的文件流解析成相应的model，
 * Created by haven.zhang on 2018/11/7.
 */
public class FileParser {

	/**
	 * 默认的日期格式
	 */
	private static final String DEFAULT_DATEFORMAT="yyyy-MM-dd";
	/**
	 * 默认编码
	 */
	private static final String DEFAULT_ENCODE="utf-8";


	/**
	 * 解析Excel文件流
	 * @param in
	 * @param fileType
	 * @param clss
	 * @param <T>
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static <T> List<T> parserExcel(InputStream in,FileType fileType,Class<T> clss)
			throws Exception {
		List<T> list = Lists.newArrayList();
		try (Workbook workbook = WorkbookFactory.create(in);) {
			Sheet sheet = workbook.getSheetAt(0);
			List<String> cellValue =  Lists.newArrayList();

			// 创建一个DataFormat对象
			DataFormat format = workbook.createDataFormat();
			// 创建一个样式 ，整个文件所有字段都以文本格式保存
			CellStyle cellStyle = workbook.createCellStyle();
			//这样才能真正的控制单元格格式，@就是指文本型
			cellStyle.setDataFormat(format.getFormat("@"));
			//具体如何创建cell就省略了，最后设置单元格的格式这样写
			//cell.setCellStyle(cellStyle);
			String[] header = null;
			Map<String, Object> properties = Maps.newHashMap();
			for (Row row : sheet) {
				int len = row.getLastCellNum();
				for(int i=0;i<len;i++){
					Cell cell = row.getCell(i);
					if (cell == null || CellType.BLANK == cell.getCellType()) {
						cellValue.add("");
					} else if (CellType.STRING == cell.getCellType()) {
						cellValue.add(StringUtils.trimToEmpty(cell.getStringCellValue()));
					} else if (CellType.NUMERIC == cell.getCellType()) {
						cellValue.add(Double.toString(cell.getNumericCellValue()));
					}
					if (header != null) {
						properties.put(header[i], cellValue.get(i));
					}
				}
				if (header == null) {
					header = cellValue.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
					cellValue.clear();
					continue;
				}
				cellValue.clear();
				T obj = clss.newInstance();
				BeanUtil.copyProperties(properties, obj, DEFAULT_DATEFORMAT);
				list.add(obj);
			}
		}
		return list;
	}

	/**
	 * 文件的第一行内容必须是model对应的属性名称，如：vendorCode,vendorName,billType,expireDays,warrantyRate,startDateActive
	 * 解析数据流，并把相关字段映射到model对象中，返回list集合
	 * @param in 文件流
	 * @param fileType 文件类型，目前仅支持CSV,XLSX,TXT等同于CSV
	 * @param clss model的class对象
	 * @return List<T> 解析后的集合
	 */
	public static <T> List<T> parser(InputStream in,FileType fileType,Class<T> clss){
		List<T> list = Lists.newArrayList();
		try {
			if(FileType.CSV.equals(fileType)){ //解析CVS文件
				InputStreamReader reader = new InputStreamReader(in, DEFAULT_ENCODE);
				try (CSVReader csvReader = new CSVReader(reader)) {
					String[] header = null;
					Map<String,Object> properties = Maps.newHashMap();
					for (String[] strs : csvReader) {
						if(header == null){
							header = strs;
							continue;
						}
						for(int i=0;i<strs.length;i++){
							properties.put(header[i],strs[i]);
						}
						T obj =clss.newInstance();
						BeanUtil.copyProperties(properties,obj,DEFAULT_DATEFORMAT);
						list.add(obj);
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}else if(FileType.XLSX.equals(fileType)) { //解析XLSX文件
				return parserExcel(in,fileType,clss);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Long value= 99L;
		System.out.println(value.getClass().getTypeName());
		Class clz = Class.forName("com.vip.fcs.app.web.util.FileParser");
		System.out.println(clz.getName());
//		List list = parser(FileUtils.openInputStream(new File("E:\\fcs_code\\fcs2.0\\fcs2-web\\src\\main\\resources\\test.csv")), FileType.CVS, ApWarrantyConfig.class);
//		System.out.println(JSON.toJSONString(list));

		System.out.println("--------------------------------------------");

//		List list2 = parser(FileUtils.openInputStream(new File("F:\\ap_warranty_config.xlsx")), FileType.XLSX, ApWarrantyConfig.class);
//		System.out.println(JSON.toJSONString(list2));
	}
}
