package com.xuanjie.bean.constant;

public class Constant_Utf8_info extends Constant_X_info {
	
	public static final int tag_length = 1;
	public static final int length_length = 2;
	public static final int bytes_length = 1;
	
	
	private String tag;
	private int length;
	private String bytes;
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getBytes() {
		return bytes;
	}
	public void setBytes(String bytes) {
		this.bytes = bytes;
	}
	
	
}
