package com.xuanjie.bean;

import java.util.List;

import com.xuanjie.bean.attribute.Attribute_info;

public class Fields_info {
	private String access_flag;
	private int name_index;
	private int descriptor_index;
	private int attributes_count;
	private List<Attribute_info> attributes_list;
	
	
	public String getAccess_flag() {
		return access_flag;
	}
	public void setAccess_flag(String access_flag) {
		this.access_flag = access_flag;
	}
	public int getName_index() {
		return name_index;
	}
	public void setName_index(int name_index) {
		this.name_index = name_index;
	}
	public int getDescriptor_index() {
		return descriptor_index;
	}
	public void setDescriptor_index(int descriptor_index) {
		this.descriptor_index = descriptor_index;
	}
	public int getAttributes_count() {
		return attributes_count;
	}
	public void setAttributes_count(int attributes_count) {
		this.attributes_count = attributes_count;
	}
	public List<Attribute_info> getAttributes_list() {
		return attributes_list;
	}
	public void setAttributes_list(List<Attribute_info> attributes_list) {
		this.attributes_list = attributes_list;
	}
	
	
}
