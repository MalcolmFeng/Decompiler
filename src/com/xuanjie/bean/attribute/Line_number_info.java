package com.xuanjie.bean.attribute;

public class Line_number_info {
	private int start_pc; //字节码行号
	private int line_number; //java 源码行号
	
	public int getStart_pc() {
		return start_pc;
	}
	public void setStart_pc(int start_pc) {
		this.start_pc = start_pc;
	}
	public int getLine_number() {
		return line_number;
	}
	public void setLine_number(int line_number) {
		this.line_number = line_number;
	}
	
}
