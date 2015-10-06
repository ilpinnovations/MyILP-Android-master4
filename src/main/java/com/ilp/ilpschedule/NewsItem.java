package com.ilp.ilpschedule;

public class NewsItem {
	private int _id;
	private String _date;
	private String _title;
	private String _image;
	private String _bigimage;
	private String _desc;
	private String _content;
	private Boolean _selected;

	public NewsItem() {
	};

	public NewsItem(int id, String title ) {
		this._id = id;
		
		this._title = title;
		
	}
	
	
	public NewsItem(int id, String date, String title, String image,String bigimage, String desc,String content,Boolean selected) {
		this._id = id;
		this._date = date;
		this._title = title;
		this._image = image;
		this._bigimage = bigimage;
		this._desc = desc;
		this._content = content;
		this._selected = selected;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String get_date() {
		return _date;
	}

	public void set_date(String _date) {
		this._date = _date;
	}

	public String get_title() {
		return _title;
	}

	public void set_title(String _title) {
		this._title = _title;
	}

	public String get_image() {
		return _image;
	}

	public void set_image(String _image) {
		this._image = _image;
	}

	public String get_bigimage() {
		return _bigimage;
	}

	public void set_bigimage(String _bigimage) {
		this._bigimage = _bigimage;
	}

	public String get_desc() {
		return _desc;
	}

	public void set_desc(String _desc) {
		this._desc = _desc;
	}

	public String get_content() {
		return _content;
	}

	public void set_content(String _content) {
		this._content = _content;
	}

	public Boolean get_selected() {
		return _selected;
	}

	public void set_selected(Boolean _selected) {
		this._selected = _selected;
	};

	
}
