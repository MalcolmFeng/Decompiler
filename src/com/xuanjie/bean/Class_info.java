package com.xuanjie.bean;

import java.util.List;
import java.util.Map;

import com.xuanjie.bean.attribute.Attribute_info;
import com.xuanjie.bean.constant.Constant_X_info;

public class Class_info {
	private String magic;
	private String minor_version;
	private String major_version;
	private int cp_count;
	private Map<Integer, Constant_X_info> constant_pool_Map;
	private String access_flag;
	private int this_class_index;
	private int super_class_index;
	private int interfaces_count;
	private List<Integer> interfacesList;
	private int fields_count;
	private List<Fields_info> fields_info_List;
	private int Methods_count;
	private List<Methods_info> methods_info_List;
	private int attributes_count;
	private List<Attribute_info> attributes;
	
	public String getMagic() {
		return magic;
	}
	public void setMagic(String magic) {
		this.magic = magic;
	}
	public String getMinor_version() {
		return minor_version;
	}
	public void setMinor_version(String minor_version) {
		this.minor_version = minor_version;
	}
	public String getMajor_version() {
		return major_version;
	}
	public void setMajor_version(String major_version) {
		this.major_version = major_version;
	}
	public int getCp_count() {
		return cp_count;
	}
	public void setCp_count(int cp_count) {
		this.cp_count = cp_count;
	}
	public Map<Integer, Constant_X_info> getConstant_pool_Map() {
		return constant_pool_Map;
	}
	public void setConstant_pool_Map(Map<Integer, Constant_X_info> constant_pool_Map) {
		this.constant_pool_Map = constant_pool_Map;
	}
	public String getAccess_flag() {
		return access_flag;
	}
	public void setAccess_flag(String access_flag) {
		this.access_flag = access_flag;
	}
	public int getThis_class_index() {
		return this_class_index;
	}
	public void setThis_class_index(int this_class_index) {
		this.this_class_index = this_class_index;
	}
	public int getSuper_class_index() {
		return super_class_index;
	}
	public void setSuper_class_index(int super_class_index) {
		this.super_class_index = super_class_index;
	}
	public int getInterfaces_count() {
		return interfaces_count;
	}
	public void setInterfaces_count(int interfaces_count) {
		this.interfaces_count = interfaces_count;
	}
	public List<Integer> getInterfacesList() {
		return interfacesList;
	}
	public void setInterfacesList(List<Integer> interfacesList) {
		this.interfacesList = interfacesList;
	}
	public int getFields_count() {
		return fields_count;
	}
	public void setFields_count(int fields_count) {
		this.fields_count = fields_count;
	}
	public List<Fields_info> getFields_info_List() {
		return fields_info_List;
	}
	public void setFields_info_List(List<Fields_info> fields_info_List) {
		this.fields_info_List = fields_info_List;
	}
	public int getMethods_count() {
		return Methods_count;
	}
	public void setMethods_count(int methods_count) {
		Methods_count = methods_count;
	}
	public List<Methods_info> getMethods_info_List() {
		return methods_info_List;
	}
	public void setMethods_info_List(List<Methods_info> methods_info_List) {
		this.methods_info_List = methods_info_List;
	}
	public int getAttributes_count() {
		return attributes_count;
	}
	public void setAttributes_count(int attributes_count) {
		this.attributes_count = attributes_count;
	}
	public List<Attribute_info> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attribute_info> attributes) {
		this.attributes = attributes;
	}
	
}
