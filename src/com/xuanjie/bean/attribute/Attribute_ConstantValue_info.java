package com.xuanjie.bean.attribute;

public class Attribute_ConstantValue_info extends Attribute_info{
	private int attribute_name_index;
	private int attribute_length; 	
	private int constantvalue_index;   // 指向 Constant_X(基本数据类型)_info 型常量的引用。 
	
	public int getAttribute_name_index() {
		return attribute_name_index;
	}
	public void setAttribute_name_index(int attribute_name_index) {
		this.attribute_name_index = attribute_name_index;
	}
	public int getAttribute_length() {
		return attribute_length;
	}
	public void setAttribute_length(int attribute_length) {
		this.attribute_length = attribute_length;
	}
	public int getConstantvalue_index() {
		return constantvalue_index;
	}
	public void setConstantvalue_index(int constantvalue_index) {
		this.constantvalue_index = constantvalue_index;
	}
	
	
}
