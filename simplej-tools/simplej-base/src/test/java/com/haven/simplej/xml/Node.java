package com.haven.simplej.xml;

import com.haven.simplej.xml.annotation.XmlAlias;
import lombok.Data;

/**
 * @Author: havenzhang
 * @Date: 2019/4/15 20:03
 * @Version 1.0
 */
@Data
@XmlAlias("person")
public class Node {

	@XmlAlias("englishName")
	private String name;
	@XmlAlias("age2")
	private String age;
}
