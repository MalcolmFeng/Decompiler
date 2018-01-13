package com.xuanjie.bean.attribute;

import java.util.List;

/**
 * 方法属性。
 * 
 * 存放每个方法 throws 的所有异常。
 * @author Malcolm
 *
 */
public class Attribute_Exceptions_info extends Attribute_info{
	
	private int attribute_name_index;
	private int attrbute_length;
	
	private int number_of_exceptions;
	private List<Integer> exception_index_tableList; // 指向常量池 Constant_class_info 的索引。
	
	public int getAttribute_name_index() {
		return attribute_name_index;
	}
	public void setAttribute_name_index(int attribute_name_index) {
		this.attribute_name_index = attribute_name_index;
	}
	public int getAttrbute_length() {
		return attrbute_length;
	}
	public void setAttrbute_length(int attrbute_length) {
		this.attrbute_length = attrbute_length;
	}
	public int getNumber_of_exceptions() {
		return number_of_exceptions;
	}
	public void setNumber_of_exceptions(int number_of_exceptions) {
		this.number_of_exceptions = number_of_exceptions;
	}
	public List<Integer> getException_index_tableList() {
		return exception_index_tableList;
	}
	public void setException_index_tableList(List<Integer> exception_index_tableList) {
		this.exception_index_tableList = exception_index_tableList;
	}
	
	
	
	
}
