package com.xuanjie.bean.attribute;

public class Local_variable_info {
	private int start_pc;
	private int length;
	private int name_index;
	private int descriptor_index;
	private int index;
	
	
	public int getStart_pc() {
		return start_pc;
	}
	public void setStart_pc(int start_pc) {
		this.start_pc = start_pc;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
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
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
}
