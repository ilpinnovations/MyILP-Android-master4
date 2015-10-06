package com.ilp.ilpschedule.emergency;

public class Contact {
	
	private String title;
	private String phoneNum;
	public Contact(String title, String phoneNum) {
		super();
		this.title = title;
		this.phoneNum = phoneNum;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
}
