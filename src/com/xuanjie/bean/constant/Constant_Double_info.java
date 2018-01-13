package com.xuanjie.bean.constant;

public class Constant_Double_info extends Constant_X_info {
	public static final int tag_length = 1;
	public static final int bytes_length = 8;
	
	private String tag;
	private String bytes;
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getBytes() {
		return bytes;
	}
	public void setBytes(String bytes) {
		this.bytes = bytes;
	}
	
}
