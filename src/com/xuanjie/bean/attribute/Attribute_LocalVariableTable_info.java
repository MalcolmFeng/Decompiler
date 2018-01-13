package com.xuanjie.bean.attribute;

import java.util.List;

public class Attribute_LocalVariableTable_info extends Attribute_info{
	private int attribute_name_index;
	private int attribute_length; 	
	private int local_variable_table_length;  
	private List<Local_variable_info> local_variable_table_List;
	
	
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
	public int getLocal_variable_table_length() {
		return local_variable_table_length;
	}
	public void setLocal_variable_table_length(int local_variable_table_length) {
		this.local_variable_table_length = local_variable_table_length;
	}
	public List<Local_variable_info> getLocal_variable_table_List() {
		return local_variable_table_List;
	}
	public void setLocal_variable_table_List(List<Local_variable_info> local_variable_table_List) {
		this.local_variable_table_List = local_variable_table_List;
	}
	
	
	
	
}
