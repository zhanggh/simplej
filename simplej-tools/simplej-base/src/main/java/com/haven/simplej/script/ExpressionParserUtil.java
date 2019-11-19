package com.haven.simplej.script;

import com.vip.vjtools.vjkit.collection.MapUtil;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * el 表达式工具类
 * @author: havenzhang
 * @date: 2019/4/18 20:40
 * @version 1.0
 */
public class ExpressionParserUtil {

	/**
	 * el表达式解析
	 * @param express 表达式
	 * @param params 参数
	 * @param clz 返回结果数据类型
	 * @return T
	 */
	public static <T> T parser(String express, Map<String,Object> params,Class<T> clz){
		EvaluationContext context = new StandardEvaluationContext();  // 表达式的上下文,
		if(MapUtil.isNotEmpty(params)){
			params.forEach(context::setVariable);
		}
		ExpressionParser parser = new SpelExpressionParser();
		// 属性
		T value = parser.parseExpression(express).getValue(context,clz);
		return value;
	}
}
