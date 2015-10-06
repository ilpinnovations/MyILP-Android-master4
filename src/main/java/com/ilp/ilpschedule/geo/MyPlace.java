package com.ilp.ilpschedule.geo;

public class MyPlace {
	
	
	private int resId;
	private String headLine;
	private String description;
	
	public MyPlace(int resId, String headLine, String description) {
		super();
		this.resId = resId;
		this.headLine = headLine;
		this.description = description;
	}
	
	public int getResId() {
		return resId;
	}
	public void setResId(int resId) {
		this.resId = resId;
	}
	public String getHeadLine() {
		return headLine;
	}
	public void setHeadLine(String headLine) {
		this.headLine = headLine;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
