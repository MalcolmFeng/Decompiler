package com.xuanjie.bean.constant;

public class Constant_InvokeDynamic_info extends Constant_X_info {
	public static int tag_length = 1;
	public static int bootstrap_method_attr_index_length = 2;
	public static int name_and_type_index_length = 2;
	
	private String tag;
	private int bootstrap_method_attr_index;
	private int name_and_type_index;
	public static int getTagLength() {
		return tag_length;
	}
	public static void setTagLength(int tagLength) {
		tag_length = tagLength;
	}
	public static int getBootstrapMethodAttrIndexLength() {
		return bootstrap_method_attr_index_length;
	}
	public static void setBootstrapMethodAttrIndexLength(int bootstrapMethodAttrIndexLength) {
		bootstrap_method_attr_index_length = bootstrapMethodAttrIndexLength;
	}
	public static int getNameAndTypeIndexLength() {
		return name_and_type_index_length;
	}
	public static void setNameAndTypeIndexLength(int nameAndTypeIndexLength) {
		name_and_type_index_length = nameAndTypeIndexLength;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getBootstrap_method_attr_index() {
		return bootstrap_method_attr_index;
	}
	public void setBootstrap_method_attr_index(int bootstrap_method_attr_index) {
		this.bootstrap_method_attr_index = bootstrap_method_attr_index;
	}
	public int getName_and_type_index() {
		return name_and_type_index;
	}
	public void setName_and_type_index(int name_and_type_index) {
		this.name_and_type_index = name_and_type_index;
	}
	
}
