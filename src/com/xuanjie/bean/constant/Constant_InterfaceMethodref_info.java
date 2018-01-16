package com.xuanjie.bean.constant;

public class Constant_InterfaceMethodref_info extends Constant_X_info {
	public static final int tag_length = 1;
	public static final int index_length = 2;
	public static final int index2_length = 2;
	
	private String tag;
	private int index;
	private int index2;
	
	public int getIndex2() {
		return index2;
	}
	public void setIndex2(int index2) {
		this.index2 = index2;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
}
