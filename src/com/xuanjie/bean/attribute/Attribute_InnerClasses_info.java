package com.xuanjie.bean.attribute;

import java.util.List;

public class Attribute_InnerClasses_info extends Attribute_info{
	private int attribute_name_index;
	private int attribute_length; 	
	private int number_of_classes;  
	private List<Inner_classes_info> inner_classes;
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
	public int getNumber_of_classes() {
		return number_of_classes;
	}
	public void setNumber_of_classes(int number_of_classes) {
		this.number_of_classes = number_of_classes;
	}
	public List<Inner_classes_info> getInner_classes() {
		return inner_classes;
	}
	public void setInner_classes(List<Inner_classes_info> inner_classes) {
		this.inner_classes = inner_classes;
	}
	
	
	
}
