package com.haven.simplej.proxytest;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author: havenzhang
 * @date: 2019/5/18 22:59
 * @version 1.0
 */
@Data
public class Person {
	private String name;
	private int age;
	private float height;

	private Date time;
	private Timestamp birthday;
}
