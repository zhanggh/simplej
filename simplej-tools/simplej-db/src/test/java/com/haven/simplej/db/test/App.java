package com.haven.simplej.db.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: havenzhang
 * @date: 2019/9/3 23:49
 * @version 1.0
 */
@SpringBootApplication
public class App {
	public static void main(String[] args) {
		System.out.println("------------------App-----------------------");
		SpringApplication application = new SpringApplication(App.class);
		application.run(args);

	}
}
