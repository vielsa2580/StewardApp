//package com.example.stewardimperial.handler;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//
//import com.example.steward.Models.MODELOrder;
//import com.example.steward.Models.MODELOrderToDel;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class DatabaseHandler extends SQLiteOpenHelper {
//
//	// All Static variables
//	// Database Version
//	private static final int DATABASE_VERSION = 1;
//
//	// Database Name
//	private static final String DATABASE_NAME = "orderManager";
//
//	// Contacts table name
//	private static final String TABLE_NAME = "orders";
//
//	// Contacts Table Columns names
//	private static final String KEY_ID = "id";
//	private static final String KEY_CATEGORY = "category";
//	private static final String KEY_TABLE_NUMBER = "tablenumber";
//	private static final String KEY_ITEM = "item";
//
//	public DatabaseHandler(Context context) {
//		super(context, DATABASE_NAME, null, DATABASE_VERSION);
//	}
//
//	// Creating Tables
//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
//				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_CATEGORY + " TEXT,"
//				+ KEY_TABLE_NUMBER + " TEXT," + KEY_ITEM + " TEXT)";
//		db.execSQL(CREATE_CONTACTS_TABLE);
//	}
//
//	// Upgrading database
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		// Drop older table if existed
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//
//		// Create tables again
//		onCreate(db);
//	}
//
//	/**
//	 * All CRUD(Create, Read, Update, Delete) Operations
//	 */
//
//	// Adding new contact
//	public void addOrder(MODELOrder order) {
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		ContentValues values = new ContentValues();
//		values.put(KEY_CATEGORY, order.getCategory()); 
//		values.put(KEY_TABLE_NUMBER, order.getTableNumber());
//		values.put(KEY_ITEM, order.getItem());
//
//		// Inserting Row
//		db.insert(TABLE_NAME, null, values);
//		db.close(); // Closing database connection
//	}
//
//	// Getting single contact
//	MODELOrder getOrder(String table_number) {
//
//		SQLiteDatabase db = this.getReadableDatabase();
//
//		Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,KEY_CATEGORY, KEY_TABLE_NUMBER, KEY_ITEM }, 
//				KEY_TABLE_NUMBER + "=?",new String[] { table_number }, null, null, null, null);
//
//		if (cursor != null)
//			cursor.moveToFirst();
//
//		MODELOrder order = new MODELOrder(cursor.getString(0),
//				cursor.getString(1), cursor.getString(2),cursor.getString(3));
//		return order;
//	}
//
//	// Getting All Orders
//	public List<MODELOrder> getAllOrder() {
//		List<MODELOrder> orderList = new ArrayList<MODELOrder>();
//		// Select All Query
//		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
//
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cursor = db.rawQuery(selectQuery, null);
//
//		// looping through all rows and adding to list
//		if (cursor.moveToFirst()) {
//			do {
//				MODELOrder contact = new MODELOrder(cursor.getString(1),cursor.getString(2),cursor.getString(3));
//				// Adding contact to list
//				orderList.add(contact);
//			} while (cursor.moveToNext());
//		}
//
//		// return contact list
//		return orderList;
//	}
//
//
//	//	Getting All Tables
//	public HashSet<String> getAllTables() {
////		List<String> tableList = new ArrayList<String>();
//		HashSet<String> tableList = new HashSet<String>();
//		// Select All Query
//		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
//
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cursor = db.rawQuery(selectQuery, null);
//
//		// looping through all rows and adding to list
//		if (cursor.moveToFirst()) {
//			do {
//				String table = new String(cursor.getString(2));  //new MODELOrder(cursor.getString(1),cursor.getString(2),cursor.getString(3));
//				// Adding contact to list
//				tableList.add(table);
//			} while (cursor.moveToNext());
//		}
//
//		// return contact list
//		return tableList;
//	}
//
//
//
//	// Updating single contact
//	public int updateOrder(MODELOrder contact) {
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		ContentValues values = new ContentValues();
//
//		values.put(KEY_CATEGORY, contact.getCategory());
//		values.put(KEY_TABLE_NUMBER, contact.getTableNumber());
//		values.put(KEY_ITEM, contact.getItem());
//
//		// updating row
//		return db.update(TABLE_NAME, values, KEY_TABLE_NUMBER + " = ?",
//				new String[] { String.valueOf(contact.getCategory()) });
//	}
//
//	// Deleting single contact
//	public void deleteOrder(MODELOrderToDel contact) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		db.delete(TABLE_NAME, KEY_TABLE_NUMBER + " = ? AND "+KEY_ITEM+ " =?",
//				new String[] { String.valueOf(contact.getCat()), String.valueOf(contact.getItem())});
//		db.close();
//	}
//
//	// Getting contacts Count
//	public int getContactsCount() {
//		String countQuery = "SELECT  * FROM " + TABLE_NAME;
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cursor = db.rawQuery(countQuery, null);
//		cursor.close();
//
//		// return count
//		return cursor.getCount();
//	}
//
//}