package com.haven.epay.payment.web.controller;

import com.haven.simplej.authen.controller.AbstractBaseAuthenController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @Author: havenzhang
 * @Date: 2019/3/31 23:03
 * @Version 1.0
 */
@Slf4j
@Controller
public class ComController  extends AbstractBaseAuthenController {



	@RequestMapping("/index")
	public String hello(Locale locale, Model model) {
		model.addAttribute("greeting", "Hello!");

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		model.addAttribute("currentTime", formattedDate);
		log.info("----------------hello----------------------");
		return "index";
	}
}
