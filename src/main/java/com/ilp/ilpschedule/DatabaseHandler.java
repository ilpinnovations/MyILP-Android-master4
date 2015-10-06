package com.ilp.ilpschedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ilp.ilpschedule.emergency.Contact;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "ilpschedule";

	// Contacts table name
	private static final String TABLE_INFO = "info";

	// Emergency contact table
	private static final String TABLE_EMERGENCY = "EmergencyContact";
	
	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_LOC = "loc";
	private static final String KEY_BATCH = "batch";
	
	// Emergency Contacts Table Columns names
	private static final String KEY_TITLE = "title";
	private static final String KEY_NUMBER = "number";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		SQLiteDatabase db =  getWritableDatabase();
		this.onCreate(db);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_INFO + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_LOC + " TEXT,"  
		+ KEY_BATCH + " TEXT" + ")";
		String CREATE_EMERGENCY_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_EMERGENCY
				+ "("+ KEY_TITLE +" TEXT,"+ KEY_NUMBER +" TEXT)";
		db.execSQL(CREATE_CONTACTS_TABLE);
		db.execSQL(CREATE_EMERGENCY_TABLE);
		Log.d("Message---->", "Tables created....");
		
		
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INFO);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMERGENCY);
		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	void addContact(Info info) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, info.getID()); // Info Id
		values.put(KEY_NAME, info.getName()); // Info Name
		values.put(KEY_LOC, info.getLoc()); // Info Loc
		values.put(KEY_BATCH, info.getBatch()); // Info Batch

		// Inserting Row
		db.insert(TABLE_INFO, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	Info getContact(int id) {
		
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_INFO, new String[] { KEY_ID,
				KEY_NAME, KEY_LOC,KEY_BATCH }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Info info = new Info(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2) ,cursor.getString(3));
		// return contact
		return info;
	}
	
	// Getting All Contacts
	public List<Info> getAllContacts() {
		List<Info> contactList = new ArrayList<Info>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_INFO;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Info info = new Info();
				info.setID(Integer.parseInt(cursor.getString(0)));
				info.setName(cursor.getString(1));
				info.setLoc(cursor.getString(2));
				info.setBatch(cursor.getString(3));
				// Adding contact to list
				contactList.add(info);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}

	// Updating single contact
	public int updateContact(Info info) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, info.getName());
		values.put(KEY_LOC, info.getLoc());
		values.put(KEY_BATCH, info.getBatch());
		// updating row
		return db.update(TABLE_INFO, values, KEY_ID + " = ?",
				new String[] { String.valueOf(info.getID()) });
	}

	// Deleting single contact
	public void deleteContact(Info info) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_INFO, KEY_ID + " = ?",
				new String[] { String.valueOf(info.getID()) });
		db.close();
	}


	// Getting contacts Count
	public int getContactsCount() {
		String countQuery = "SELECT * FROM " + TABLE_INFO;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
//		cursor.close();
         
		// return count
		return cursor.getCount();
	}
	
	public String getLocation() {
		String loc = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT loc FROM info", null);
		if(cursor!=null)
		{
			cursor.moveToFirst();

			loc = cursor.getString(0);
		}
		System.out.print("Printing location................"+loc);

		return loc;
	}
	
	public List<Contact> getEmergencyContacts() {
		SQLiteDatabase db = this.getReadableDatabase();
		List<Contact> emergencyContacts = new ArrayList<Contact>();
		Cursor cursor = db.query(TABLE_EMERGENCY, new String[]{KEY_TITLE,KEY_NUMBER},
				null, null, null, null, null);
		if(cursor!=null) {
			cursor.moveToFirst();
			try{
				do
				{
					Contact contact = new Contact(cursor.getString(0), cursor.getString(1));
					emergencyContacts.add(contact);
				} while(cursor.moveToNext());
			} catch (CursorIndexOutOfBoundsException e	){
				e.printStackTrace();
				
			}
		}
		return emergencyContacts;
	}
	
	public void addEmergencyContacts(List<Contact> newContacts) {
		SQLiteDatabase db = this.getWritableDatabase();
		for(Contact each:newContacts)
		{
			ContentValues values = new ContentValues();
			values.put(KEY_TITLE, each.getTitle());
			values.put(KEY_NUMBER, each.getPhoneNum());
			db.insert(TABLE_EMERGENCY, null, values);
		}
		db.close();
	}

}
