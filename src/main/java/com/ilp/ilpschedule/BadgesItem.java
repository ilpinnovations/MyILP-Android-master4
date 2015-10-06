package com.ilp.ilpschedule;

public class BadgesItem {
	
	//private variables
	int _id;
	String _name;
	int _count;
//	String _batch;
	
	public int get_count() {
		return _count;
	}
	public void set_count(int _count) {
		this._count = _count;
	}
	// Empty constructor
	public BadgesItem(){
		
	}
	// constructor
	public BadgesItem(int id, String name, int count){
		this._id = id;
		this._name = name;
		this._count = count;
//		this._batch = batch;
	}
	
	// constructor
//	public BadgesItem(String name, String loc){
//		this._name = name;
//		this._loc = loc;
//	}
	// getting ID
	public int getID(){
		return this._id;
	}
	
	// setting id
	public void setID(int id){
		this._id = id;
	}
	
	// getting name
	public String getName(){
		return this._name;
	}
	
	// setting name
	public void setName(String name){
		this._name = name;
	}
	
	
}
