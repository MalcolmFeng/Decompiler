package com.xuanjie.bean;

public class ExceptionTable {
	private int start_pc;
	private int end_pc;
	private String handler_pc;
	private String catch_type;
	
	
	public int getStart_pc() {
		return start_pc;
	}
	public void setStart_pc(int start_pc) {
		this.start_pc = start_pc;
	}
	public int getEnd_pc() {
		return end_pc;
	}
	public void setEnd_pc(int end_pc) {
		this.end_pc = end_pc;
	}
	public String getHandler_pc() {
		return handler_pc;
	}
	public void setHandler_pc(String handler_pc) {
		this.handler_pc = handler_pc;
	}
	public String getCatch_type() {
		return catch_type;
	}
	public void setCatch_type(String catch_type) {
		this.catch_type = catch_type;
	}
	
	
}
