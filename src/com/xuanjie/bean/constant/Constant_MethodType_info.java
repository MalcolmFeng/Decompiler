package com.xuanjie.bean.constant;

public class Constant_MethodType_info extends Constant_X_info {
	public static int tag_length = 1;
	public static int descriptor_index_length = 2;
	
	private String tag;
	private int descriptor_index;
	public static int getTagLength() {
		return tag_length;
	}
	public static void setTagLength(int tagLength) {
		tag_length = tagLength;
	}
	public static int getDescriptorIndexLength() {
		return descriptor_index_length;
	}
	public static void setDescriptorIndexLength(int descriptorIndexLength) {
		descriptor_index_length = descriptorIndexLength;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getDescriptor_index() {
		return descriptor_index;
	}
	public void setDescriptor_index(int descriptor_index) {
		this.descriptor_index = descriptor_index;
	}
}
