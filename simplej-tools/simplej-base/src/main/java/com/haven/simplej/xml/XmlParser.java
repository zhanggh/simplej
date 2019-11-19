package com.haven.simplej.xml;


import com.google.common.collect.Maps;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.text.StringUtil;
import com.haven.simplej.xml.annotation.XmlAlias;
import com.vip.vjtools.vjkit.collection.MapUtil;
import com.vip.vjtools.vjkit.reflect.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultText;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

/**
 * xml 配置文件解析及
 **/
@Slf4j
public class XmlParser {
	/**
	 * class 字段字符串与Field的映射
	 */
	private static Map<String, Field> fieldMap = Maps.newHashMap();
	/**
	 * class 与字段映射关系
	 */
	private static Map<Class, Map<String, Field>> classFieldMap = Maps.newHashMap();

	/**
	 * class xml别名
	 */
	private static Map<Class, String> classAlias = Maps.newHashMap();

	/**
	 * xml 映射成obj
	 * @param xml xml
	 * @param parserNode xml字符串中待解析的节点
	 * @param tClass 目标对象的class
	 * @param <T> 返回实例
	 * @return
	 */
	public static <T> T xml2Obj(String xml, String parserNode, Class<T> tClass) {
		Document doc = null;
		try {

			// 读取并解析XML文档
			// SAXReader就是一个管道，用一个流的方式，把xml文件读出来
			//
			// SAXReader reader = new SAXReader(); //User.hbm.xml表示你要解析的xml文档
			// Document document = reader.read(new File("User.hbm.xml"));
			// 下面的是通过解析xml字符串的
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML

			Element rootElt = doc.getRootElement(); // 获取根节点
			Element recordEle = rootElt.element(parserNode); // 获取根节点下的子节点head
			if (recordEle == null) {
				return null;
			}
			T obj = tClass.newInstance();
			Iterator childs = recordEle.elementIterator();
			while (childs.hasNext()) {
				Element recordEle2 = (Element) childs.next();
				Field field = fieldMap.get(tClass.getName() + "." + recordEle2.getName());
				if (field == null) {
					addClassField(tClass);
					field = fieldMap.get(tClass.getName() + "." + recordEle2.getName());
				}
				if (field == null) {
					log.info("can not find field:{}", recordEle2.getName());
					continue;
				}
				ReflectionUtil.setField(obj, field, recordEle2.getTextTrim());
			}
			return obj;
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
	}

	/**
	 * 初始化类的字段映射
	 * @param clz
	 * @return
	 */
	public static Map<String, Field> addClassField(Class clz) {
		Map<String, Field> tmpFieldMap = Maps.newHashMap();
		PropertyDescriptor pdas[] = BeanUtils.getPropertyDescriptors(clz);
		for (PropertyDescriptor pd : pdas) {
			Field field = ReflectionUtil.getField(clz, pd.getName());
			if (field == null) {
				log.warn("can not find field:{}", pd.getName());
				continue;
			}

			XmlAlias alias = field.getAnnotation(XmlAlias.class);
			if (alias != null) {
				tmpFieldMap.put(clz.getName() + "." + alias.value(), field);
			} else {
				tmpFieldMap.put(clz.getName() + "." + pd.getName(), ReflectionUtil.getField(clz, pd.getName()));
			}
		}
		fieldMap.putAll(tmpFieldMap);
		return tmpFieldMap;
	}

	/**
	 * 将对象实例化成xml字符串
	 * @param obj 实例
	 * @return string
	 */
	public static String obj2xml(Object obj) {

		return obj2xmlDoc(obj).asXML();
	}

	/**
	 * 将实例实例化成xml document
	 * @param obj 实例
	 * @return Document
	 */
	public static Document obj2xmlDoc(Object obj) {

		Map<String, Field> fieldMap = classFieldMap.get(obj.getClass());
		if (MapUtil.isEmpty(fieldMap)) {
			fieldMap = Maps.newHashMap();
			fieldMap.putAll(addClassField(obj.getClass()));
			classFieldMap.put(obj.getClass(), fieldMap);
		}

		String rootNodeName = "root";
		if (!classAlias.containsKey(obj.getClass())) {
			XmlAlias alias = obj.getClass().getAnnotation(XmlAlias.class);
			if (alias != null) {
				rootNodeName = alias.value();
			}
			classAlias.put(obj.getClass(), rootNodeName);
		} else {
			rootNodeName = classAlias.get(obj.getClass());
		}

		Element rootElement = DocumentHelper.createElement(rootNodeName);
		Document doc = DocumentHelper.createDocument(rootElement);
		fieldMap.forEach((k, v) -> {
			String elementName = StringUtil.replaceAll(k, obj.getClass().getName() + ".", "");
			Element element = DocumentHelper.createElement(elementName);
			element.add(new DefaultText(ReflectionUtil.getFieldValue(obj, v)));
			doc.getRootElement().add(element);
		});
		return doc;
	}

	/**
	 * xml格式化
	 * @param xml Document
	 * @return
	 * @throws IOException
	 */
	public static String prettyXml(Document xml) throws IOException {
		// 设置XML文档格式
		OutputFormat outputFormat = OutputFormat.createPrettyPrint();
		// 设置XML编码方式,即是用指定的编码方式保存XML文档到字符串(String),这里也可以指定为GBK或是ISO8859-1
		outputFormat.setEncoding("UTF-8");
//		outputFormat.setSuppressDeclaration(true); //是否生产xml头
		outputFormat.setIndent(true); //设置是否缩进
		outputFormat.setIndent("    "); //以四个空格方式实现缩进
//		outputFormat.setNewlines(true); //设置是否换行
		// stringWriter字符串是用来保存XML文档的
		StringWriter stringWriter = new StringWriter();
		// xmlWriter是用来把XML文档写入字符串的(工具)
		XMLWriter xmlWriter = new XMLWriter(stringWriter, outputFormat);
		// 把创建好的XML文档写入字符串
		xmlWriter.write(xml);
		xmlWriter.close();
		return stringWriter.toString();
	}
}