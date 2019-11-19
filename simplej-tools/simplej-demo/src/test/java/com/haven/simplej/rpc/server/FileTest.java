package com.haven.simplej.rpc.server;

import com.haven.simplej.exception.UncheckedException;
import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: havenzhang
 * @date: 2019/4/29 11:26
 * @version 1.0
 */
public class FileTest {


	@Test
	public void test(){
		System.out.println(System.getProperty("user.dir"));
		String path = "../logs/${yyyyMMdd}/";
		String result = formatFilePath(path, new Date());
		System.out.println(result);
		File file = new File(result);
		System.out.println(Arrays.toString(file.list()));
	}


	public static String formatFilePath(String filePath, Date date) {
		Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(filePath);
		while (matcher.find()) {
			filePath = filePath.replace(matcher.group(), new SimpleDateFormat(matcher.group(1)).format(date));
		}
		System.out.println(filePath);
		return filePath;
	}

	public static void main(String[] args) {

		int i=0;
		while (i<10){

			try{
				System.out.println(i);
				throw new UncheckedException("test err");
			}catch (Exception e){
				i++;
			}
		}

		System.out.println("---------------------");
	}
}
