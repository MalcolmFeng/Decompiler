package com.xuanjie.bean.attribute;

public class Attribute_SourceFile_info extends Attribute_info{
	private int attribute_name_index;
	private int attribute_length; 	
	private int sourcefile_index;   // 指向 Constant_Utf8_info 型常量的索引 值为源码文件的文件名
	
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
	public int getSourcefile_index() {
		return sourcefile_index;
	}
	public void setSourcefile_index(int sourcefile_index) {
		this.sourcefile_index = sourcefile_index;
	}
	
	
}
