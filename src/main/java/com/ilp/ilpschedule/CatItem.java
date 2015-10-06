package com.ilp.ilpschedule;

public class CatItem {
	private String _date;

	private String _batch;
	private String _slot;
	private String _course;
	private String _faculty;
	private String _room;
	private boolean _selected;

	public CatItem(String date, String batch, String slot, String course,
			String faculty, String room, Boolean selected) {
		this._date = date;
		this._batch = batch;
		this._slot = slot;
		this._course = course;
		this._faculty = faculty;
		this._room = room;

		this._selected = selected;
	}

	public String get_date() {
		return _date;
	}

	public void set_date(String _date) {
		this._date = _date;
	}

	public String get_batch() {
		return _batch;
	}

	public void set_batch(String _batch) {
		this._batch = _batch;
	}

	public String get_slot() {
		return _slot;
	}

	public void set_slot(String _slot) {
		this._slot = _slot;
	}

	public String get_course() {
		return _course;
	}

	public void set_course(String _course) {
		this._course = _course;
	}

	public String get_faculty() {
		return _faculty;
	}

	public void set_faculty(String _faculty) {
		this._faculty = _faculty;
	}

	public String get_room() {
		return _room;
	}

	public void set_room(String _room) {
		this._room = _room;
	}

	public boolean is_selected() {
		return _selected;
	}

	public void set_selected(boolean _selected) {
		this._selected = _selected;
	}

}
