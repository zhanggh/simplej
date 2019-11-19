package com.haven.simplej.spring.el;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author haven.zhang
 * @date 2019/1/25.
 */
public class SpelExpressionParserTest {
	public static void main(String[] args) {
		//创建SpEL表达式的解析器
		ExpressionParser parser=new SpelExpressionParser();
		//解析表达式'Hello '+' World!'
		Expression exp=parser.parseExpression("'Hello '+' World!'");
		//取出解析结果
		String result=exp.getValue().toString();
		//输出结果
		System.out.println(result);
		EvaluationContext ctx = new StandardEvaluationContext();
		String[] students=new String[]{"tom","jack","rose","mark","lucy"};
		ctx.setVariable("students", students);
		String student = parser.parseExpression("#students[3]").getValue(ctx,
				String.class);
		System.out.println(student);
	}
}
