package com.project.gestureappv2;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	
	private static DatabaseHandler database;
	
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "store";

	// Contacts table name
	private static final String TABLE_IMAGE_GALLERY = "image_gallery";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_IMAGE = "image";
	private static final String KEY_POS = "pos";

	private DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public static synchronized DatabaseHandler getInstance(Context context) {

	    // Use the application context, which will ensure that you 
	    // don't accidentally leak an Activity's context.
	    // See this article for more information: http://bit.ly/6LRzfx
	    if (database == null) {
	      database = new DatabaseHandler(context.getApplicationContext());
	    }
	    return database;
	  }

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		// CREATING IMAGE GALLERY TABLE
		db.execSQL("CREATE TABLE IMAGE_GALLERY(ID INTEGER PRIMARY KEY,IMAGE BLOB,POS INT)");
		Log.d("Database : ", "Database and Table Created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS IMAGE_GALLERY");

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new image
	void addImage(byte[] image, int pos) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_IMAGE, image); // Image byte array
		values.put(KEY_POS, pos); // Image Position

		// Inserting Row
		db.insert(TABLE_IMAGE_GALLERY, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	byte[] getImage(int pos) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_IMAGE_GALLERY,
				new String[] { KEY_IMAGE }, KEY_POS + "=?",
				new String[] { String.valueOf(pos) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			// return contact
			byte[] rV=cursor.getBlob(0);
			db.close();
			
			return rV;
		}
		db.close();
		return null;
	}

	// Getting All Contacts
	public List<byte[]> getAllImages() {
		List<byte[]> imgByteList = new ArrayList<byte[]>();
		// Select All Query
		String selectQuery = "SELECT IMAGE,POS FROM IMAGE_GALLERY ORDER BY POS";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if(cursor.getCount()==0){
			return new ArrayList<byte[]>();
		}
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				imgByteList.add(cursor.getBlob(0));
			} while (cursor.moveToNext());
		}

		db.close();
		// return image list
		return imgByteList;
	}
	
	public int getIDofPos(int pos){
		int id=0;
		String selectQuery = "SELECT ID FROM IMAGE_GALLERY WHERE POS="+ pos +"";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
				id=cursor.getInt(0);
			
		}

		db.close();

		return id;
	}

	// Updating single Image
	public int updateImagePos(int id, int newPos) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();		
		values.put(KEY_POS, newPos);

		// updating row
		int rV=db.update(TABLE_IMAGE_GALLERY, values, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
		return rV;
		
	}
	
	public void updateImagePos(int pos) {
		SQLiteDatabase db = this.getWritableDatabase();

		// updating row
		db.execSQL("Update "+TABLE_IMAGE_GALLERY+" Set Pos=Pos-1 Where Pos>"+pos);
		db.close();				
	}

	// Deleting single image
	public void deleteImage(int pos) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_IMAGE_GALLERY, KEY_POS + " = ?",
				new String[] { String.valueOf(pos) });
		db.close();
	}

	// Getting images Count
	public int getImagesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_IMAGE_GALLERY;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count =cursor.getCount();
		cursor.close();		
		db.close();
		// return count
		return count;
	}
	
	public void deleteAll(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from "+ TABLE_IMAGE_GALLERY);		
		db.close();
	}

}