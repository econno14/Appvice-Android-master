package com.missedops.library;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class SocialDatabase extends SQLiteOpenHelper{
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;
 
    // Database Name
    private static final String DATABASE_NAME = "android_api";
 
    // Login table name
    private static final String TABLE_LOGIN = "Soc_Ntwk_Login ";
 
    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_UID = "uid";
    private static final String KEY_SOC_NW_NAME = "soc_ntwk_name";
    private static final String KEY_NID = "soc_ntwk_id";
    private static final String KEY_NAME = "displayname";
    
    public SocialDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
        		+ KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_UID + " TEXT UNIQUE,"
                + KEY_SOC_NW_NAME + " TEXT,"
                + KEY_NID + " TEXT UNIQUE,"
                + KEY_NAME + " TEXT "+")";
        db.execSQL(CREATE_LOGIN_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * Storing user details in database
     * */
    public void addUser(String uid, String soc_ntwk_name, String soc_ntwk_id, String displayname) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_UID, uid); 
        values.put(KEY_SOC_NW_NAME, soc_ntwk_name); 
        values.put(KEY_NID, soc_ntwk_id); 
        values.put(KEY_NAME, displayname); 
        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }
     
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
          
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("uid", cursor.getString(1));
            user.put("soc_ntwk_name", cursor.getString(2));
            user.put("soc_ntwk_id", cursor.getString(3));
            user.put("displayname", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }
 
    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
         
        // return row count
        return rowCount;
    }
     
    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }
}