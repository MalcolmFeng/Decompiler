package com.xuanjie.bean.constant;

public class Constant_MethodHandle_info extends Constant_X_info {
	public static int tag_length = 1;
	public static int reference_kind_length = 1;
	public static int reference_index_length = 2;
	
	private String tag;
	private int reference_kind;
	private int reference_index;
	public static int getTagLength() {
		return tag_length;
	}
	public static void setTagLength(int tagLength) {
		tag_length = tagLength;
	}
	public static int getReferenceKindLength() {
		return reference_kind_length;
	}
	public static void setReferenceKindLength(int referenceKindLength) {
		reference_kind_length = referenceKindLength;
	}
	public static int getReferenceIndexLength() {
		return reference_index_length;
	}
	public static void setReferenceIndexLength(int referenceIndexLength) {
		reference_index_length = referenceIndexLength;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getReference_kind() {
		return reference_kind;
	}
	public void setReference_kind(int reference_kind) {
		this.reference_kind = reference_kind;
	}
	public int getReference_index() {
		return reference_index;
	}
	public void setReference_index(int reference_index) {
		this.reference_index = reference_index;
	}
}
