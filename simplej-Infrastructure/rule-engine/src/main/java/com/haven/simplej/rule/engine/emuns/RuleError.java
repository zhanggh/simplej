package com.haven.simplej.rule.engine.emuns;

/**
 * 错误码
 * @author: havenzhang
 * @date: 2019/4/18 22:15
 * @version 1.0
 */
public enum RuleError {
	generate_error("systemError"),
	param_parser_error("param_parser_error"),
	java_plugin_parser_error("java_plugin_parser_error"),
	el_plugin_parser_error("el_plugin_parser_error"),
	groovy_plugin_parser_error("groovy_plugin_parser_error"),
	data_type_parser_error("data_type_parser_error"),
	plugin_parser_error("plugin_parser_error");


	private String msg;

	RuleError(String errorMsg){
		this.msg = errorMsg;
	}
}
