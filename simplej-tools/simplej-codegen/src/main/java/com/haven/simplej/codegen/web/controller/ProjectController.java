package com.haven.simplej.codegen.web.controller;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.codegen.model.ProjectRequestModel;
import com.haven.simplej.codegen.service.ProjectService;
import com.haven.simplej.response.builder.ResponseBuilder;
import com.haven.simplej.response.model.JsonResponse;
import com.haven.simplej.response.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 创建项目模板
 * @author haven.zhang
 * @date 2019/1/14.
 */
@Controller
@RequestMapping("/project")
@Slf4j
public class ProjectController {

	@Autowired
	private ProjectService service;

	@RequestMapping("/create")
	@ResponseBody
	public JsonResponse<String> create(ProjectRequestModel model) throws Exception {

		log.info("---------------------创建新项目---------------------");
		log.info("create new project ,requestModel:{}", JSON.toJSONString(model, true));
		Response resp = service.createProject(model);
		return ResponseBuilder.build(resp.getRespCode().name(), resp.getRespMsg());
	}

	@RequestMapping("/download")
	public void download(HttpServletRequest request, HttpServletResponse response, String projectName)
			throws IOException {

		//打开目录
		String openPath = System.getProperty("user.dir") + "/output/project/";
		File outputFile = new File(openPath + projectName + ".zip");

		/* 第二步：根据已存在的文件，创建文件输入流 */
		InputStream inputStream = new BufferedInputStream(new FileInputStream(outputFile));
		/* 第三步：创建缓冲区，大小为流的最大字符数 */
		byte[] buffer = new byte[inputStream.available()]; // int available() 返回值为流中尚未读取的字节的数量
		/* 第四步：从文件输入流读字节流到缓冲区 */
		inputStream.read(buffer);
		/* 第五步： 关闭输入流 */
		inputStream.close();
		String fileName = outputFile.getName();// 获取文件名
		response.reset();
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		response.addHeader("Content-Length", "" + outputFile.length());
		/* 第六步：创建文件输出流 */
		OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
		response.setContentType("application/octet-stream");
		/* 第七步：把缓冲区的内容写入文件输出流 */
		outputStream.write(buffer);
		/* 第八步：刷空输出流，并输出所有被缓存的字节 */
		outputStream.flush();
	}
}
