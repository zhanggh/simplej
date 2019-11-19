package com.haven.simplej.rule.engine.service;

import com.google.common.collect.Lists;
import com.haven.simplej.rule.engine.emuns.DataType;
import com.haven.simplej.rule.engine.emuns.ParserType;
import com.haven.simplej.rule.engine.model.RuleOperatorModel;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author: havenzhang
 * @date: 2019/4/18 19:55
 * @version 1.0
 */
public class RuleOperatorDataInit {

	/**
	 * 初始化操作符配置
	 * 操作符类型：1-算术运算符，2-逻辑运算符，3-比较运算符，
	 * 4-移位运算符，5-括号,6-赋值运算符，7-插件运算符
	 *
	 * 适用的数据类型 1:字符串; 2:数字; 3:日期; 4:布尔; 5:字典; 0:其他
	 * @return
	 */
	public static List<RuleOperatorModel> initList(){
		List<RuleOperatorModel> list = Lists.newArrayList();
		RuleOperatorModel model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("1"));
		model.setParamType("STRING,STRING");
		model.setDescription("字符串$1 大于 字符串$2");
		model.setName("字符串比较符-大于");
		model.setCode("#param1!=null and #param1.compareTo(#param2) >0 ");
		model.setLabel("<字符串>大于<字符串>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("7"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("1"));
		model.setParamType("STRING,STRING");
		model.setDescription("字符串$1 小于 字符串$2");
		model.setName("字符串比较符-小于");
		model.setCode("#param1!=null and #param1.compareTo(#param2) < 0 ");
		model.setLabel("<字符串>小于<字符串>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("7"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("1"));
		model.setParamType("STRING,STRING");
		model.setDescription("字符串$1 等于 字符串$2");
		model.setName("字符串比较符-等于");
		model.setCode("#param1!=null and #param1.equals(#param2) ");
		model.setLabel("<字符串>等于<字符串>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("7"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("1"));
		model.setParamType("STRING,STRING");
		model.setDescription("字符串$1 不等于 字符串$2");
		model.setName("字符串比较符-不等于");
		model.setCode("#param1!=null and !#param1.equals(#param2) ");
		model.setLabel("<字符串>不等于<字符串>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("7"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("1"));
		model.setParamType("STRING,STRING");
		model.setDescription("字符串$1 包含 字符串$2");
		model.setName("字符串模糊查询");
		model.setCode("#param1!=null and #param1.contains(#param2) ");
		model.setLabel("<字符串>包含<字符串>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("7"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("3"));
		model.setParamType("DATE,DATE");
		model.setDescription("日期$1 大于 日期$2");
		model.setName("日期比较符-大于");
		model.setCode("#param1.after(#param2)");
		model.setLabel("<日期> 大于 <日期>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("3"));
		model.setParamType("DATE,DATE");
		model.setDescription("日期$1 等于 日期$2");
		model.setName("日期比较符-等于");
		model.setCode("#param1.getTime() ==#param2.getTime()");
		model.setLabel("<日期> 等于 <日期>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("3"));
		model.setParamType("DATE,DATE");
		model.setDescription("日期$1 不等于 日期$2");
		model.setName("日期比较符-不等于");
		model.setCode("#param1.getTime() !=#param2.getTime()");
		model.setLabel("<日期> 不等于 <日期>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("3"));
		model.setParamType("DATE,DATE");
		model.setDescription("日期$1 小于 日期$2");
		model.setName("日期比较符-小于");
		model.setCode("#param1.before(#param2)");
		model.setLabel("<日期> 小于 <日期>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("3"));
		model.setParamType("DATE,DATE");
		model.setDescription("日期$1 小于等于 日期$2");
		model.setName("日期比较符-小于等于");
		model.setCode("!#param1.after(#param2)");
		model.setLabel("<日期> 小于等于 <日期>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("3"));
		model.setParamType("DATE,DATE");
		model.setDescription("日期$1 大于等于 日期$2");
		model.setName("日期比较符-大于等于");
		model.setCode("!#param1.before(#param2)");
		model.setLabel("<日期> 大于等于 <日期>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);
		//日期期间比较
		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("3"));
		model.setParamType("DATE,DATE");
		model.setDescription("日期$1 在 日期$2和日期$3之间");
		model.setName("日期比较符-区间");
		model.setCode("#param1.after(#param2) and #param1.before(#param3) ");
		model.setLabel("<日期> 在 <日期>和<日期>区间");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);
		//日期计算
		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("3"));
		model.setParamType("DATE,INT");
		model.setDescription("日期$1 加上 数字$2天数");
		model.setName("日期运算-天数移动");
		model.setCode("com.vip.vjtools.vjkit.time.DateUtil#addDays");
		model.setLabel("<日期> 加 <整数> 天");
		model.setParserType(ParserType.JAVA.name());
		model.setOpType(Byte.valueOf("7"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.DATE.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("3"));
		model.setParamType("DATE,STRING");
		model.setDescription("日期$1 转换成字符串，格式：$2");
		model.setName("日期运算-转换成格式字符串");
		model.setCode("com.haven.simplej.time.DateUtils#dateToString");
		model.setLabel("<日期> 转换成字符串，格式：<字符串>");
		model.setParserType(ParserType.JAVA.name());
		model.setOpType(Byte.valueOf("7"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.DATE.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 小于 数字$2");
		model.setName("数字比较符-小于");
		model.setCode("#param1 < #param2");
		model.setLabel("<数字> 小于 <数字>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 小于等于 数字$2");
		model.setName("数字比较符-小于等于");
		model.setCode("#param1 <= #param2");
		model.setLabel("<数字> 小于等于 <数字>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 大于等于 数字$2");
		model.setName("数字比较符-大于等于");
		model.setCode("#param1 >= #param2");
		model.setLabel("<数字> 大于等于 <数字>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 大于 数字$2");
		model.setName("数字比较符-大于");
		model.setCode("#param1 > #param2");
		model.setLabel("<数字> 大于 <数字>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 不等于 数字$2");
		model.setName("数字比较符-不等于");
		model.setCode("#param1 != #param2");
		model.setLabel("<数字> 不等于 <数字>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);


		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 等于 数字$2");
		model.setName("数字比较符-等于");
		model.setCode("#param1 = #param2");
		model.setLabel("<数字> 等于 <数字>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		//数字区间比较
		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 在闭区间[数字$2,数字$3]内");
		model.setName("数字区间比较-在闭区间内");
		model.setCode("#param1 >= #param2 and #param1 <= #param3");
		model.setLabel("<数字>  在闭区间[数字 ,数字 ]内");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 在开区间[数字$2,数字$3]内");
		model.setName("数字区间比较-在开区间内");
		model.setCode("#param1 > #param2 and #param1 < #param3");
		model.setLabel("<数字>  在开区间[数字 ,数字 ]内");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("4"));
		model.setParamType("BOOLEAN,BOOLEAN");
		model.setDescription("条件$1 && 条件$2");
		model.setName("逻辑运算符-and");
		model.setCode("#param1 and #param2");
		model.setLabel("<条件> && <条件>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("2"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("4"));
		model.setParamType("BOOLEAN,BOOLEAN");
		model.setDescription("条件$1 || 条件$2");
		model.setName("逻辑运算符-or");
		model.setCode("#param1 or #param2");
		model.setLabel("<条件> || <条件>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("2"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("4"));
		model.setParamType("STRING");
		model.setDescription("字符串为空");
		model.setName("字符串判断");
		model.setCode("#param1==null or #param1.trim().length()==0");
		model.setLabel("字符串为空");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("2"));
		model.setParamCount(Byte.valueOf("1"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);
		//正则匹配
		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("4"));
		model.setParamType("STRING,STRING");
		model.setDescription("字符串是否符合正则");
		model.setName("字符串判断");
		model.setCode("com.haven.simplej.text.StringUtil#match");
		model.setLabel("<字符串>是否符合<正则>");
		model.setParserType(ParserType.JAVA.name());
		model.setOpType(Byte.valueOf("7"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("4"));
		model.setParamType("BOOLEAN");
		model.setDescription("条件$1取反");
		model.setName("逻辑运算符-非");
		model.setCode("!#param1");
		model.setLabel("<条件>取反");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("2"));
		model.setParamCount(Byte.valueOf("1"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		//算术运算
		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 + 数字$2");
		model.setName("算术运算符-整数相加");
		model.setCode("#param1 + #param2");
		model.setLabel("<数字> + <数字>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("1"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.INT.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 - 数字$2");
		model.setName("算术运算符-整数相减");
		model.setCode("#param1 - #param2");
		model.setLabel("<数字> - <数字>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("1"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.INT.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 乘以 数字$2");
		model.setName("算术运算符-整数相乘");
		model.setCode("#param1 * #param2");
		model.setLabel("<数字> 乘以 <数字>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("1"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.INT.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 除以 数字$2");
		model.setName("算术运算符-整数相除");
		model.setCode("#param1 / #param2");
		model.setLabel("<数字> 除以 <数字>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("1"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.INT.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 对 数字$2 求余");
		model.setName("算术运算符-整数求余");
		model.setCode("#param1 % #param2");
		model.setLabel("<数字> 对 <数字> 求余");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("1"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.INT.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 的 数字$2 次方");
		model.setName("算术运算符-幂指数");
		model.setCode("#param1 ^ #param2");
		model.setLabel("<数字> 的 <数字> 次方");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("1"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.INT.name());
		list.add(model);

		//算术浮点型运算
		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 + 数字$2");
		model.setName("算术运算符-浮点型相加");
		model.setCode("#param1 + #param2");
		model.setLabel("<数字> + <数字>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("1"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.DOUBLE.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 - 数字$2");
		model.setName("算术运算符-浮点型相减");
		model.setCode("#param1 - #param2");
		model.setLabel("<数字> - <数字>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("1"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.DOUBLE.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 乘以 数字$2");
		model.setName("算术运算符-浮点型相乘");
		model.setCode("#param1 * #param2");
		model.setLabel("<数字> 乘以 <数字>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("1"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.DOUBLE.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 除以 数字$2");
		model.setName("算术运算符-浮点型相除");
		model.setCode("#param1 / #param2");
		model.setLabel("<数字> 除以 <数字>");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("1"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.DOUBLE.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 对 数字$2 求余");
		model.setName("算术运算符-浮点型求余");
		model.setCode("#param1 % #param2");
		model.setLabel("<数字> 对 <数字> 求余");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("1"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.DOUBLE.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("NUMBER,NUMBER");
		model.setDescription("数字$1 的 数字$2 次方");
		model.setName("算术运算符-浮点型幂指数");
		model.setCode("#param1 ^ #param2");
		model.setLabel("<数字> 的 <数字> 次方");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("1"));
		model.setParamCount(Byte.valueOf("2"));
		model.setReturnType(DataType.DOUBLE.name());
		list.add(model);



		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("OBJECT");
		model.setDescription("对象为空");
		model.setName("对象空判断");
		model.setCode("#param1 == null");
		model.setLabel("对象为空-空");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("1"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("OBJECT");
		model.setDescription("对象不为空");
		model.setName("对象空判断-非空");
		model.setCode("#param1 != null");
		model.setLabel("对象不为空");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("1"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("COLLECTION");
		model.setDescription("集合为空");
		model.setName("集合空判断-空");
		model.setCode("#param1==null or #param1.size()==0");
		model.setLabel("集合为空");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("1"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		model = new RuleOperatorModel();
		model.setDataType(Byte.valueOf("2"));
		model.setParamType("COLLECTION");
		model.setDescription("集合不为空");
		model.setName("集合空判断-非空");
		model.setCode("#param1!=null and #param1.size()>0");
		model.setLabel("集合不为空");
		model.setParserType(ParserType.EL.name());
		model.setOpType(Byte.valueOf("3"));
		model.setParamCount(Byte.valueOf("1"));
		model.setReturnType(DataType.BOOLEAN.name());
		list.add(model);

		list.forEach(e->{
			e.setCreateTime(new Timestamp(new Date().getTime()));
			e.setCreatedBy("test");
		});

		return list;
	}
}
