package com.xuanjie.bean.attribute;

public class Attribute_Signature_info extends Attribute_info{
	private int attribute_name_index;
	private int attribute_length; 	
	private int signature_index;   // 指向 Constant_Utf8_info 型常量的索引 值为源码文件的文件名
	
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
	public int getSignature_index() {
		return signature_index;
	}
	public void setSignature_index(int signature_index) {
		this.signature_index = signature_index;
	}
	
	
	
}
