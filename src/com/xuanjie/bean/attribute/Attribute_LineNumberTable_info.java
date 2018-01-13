package com.xuanjie.bean.attribute;

import java.util.List;


public class Attribute_LineNumberTable_info extends Attribute_info{
	
	private int attribute_name_index;
	private int attrbute_length;
	
	private int line_number_table_length;  //Line_number_info 表的个数
	private List<Line_number_info> line_number_table_List;
	
	
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
	public int getLine_number_table_length() {
		return line_number_table_length;
	}
	public void setLine_number_table_length(int line_number_table_length) {
		this.line_number_table_length = line_number_table_length;
	}
	public List<Line_number_info> getLine_number_table_List() {
		return line_number_table_List;
	}
	public void setLine_number_table_List(List<Line_number_info> line_number_table_List) {
		this.line_number_table_List = line_number_table_List;
	}
	
}
