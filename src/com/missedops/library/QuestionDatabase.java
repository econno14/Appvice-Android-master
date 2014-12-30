package com.missedops.library;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class QuestionDatabase extends SQLiteOpenHelper{
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;
 
    // Database Name
    private static final String DATABASE_NAME = "android_api";
 
    // Login table name
    private static final String TABLE_LOGIN = "questions";
 
    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_UID = "uid";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_QID = "qid";
    private static final String KEY_TIME = "created_at";
    private static final String KEY_STATUS = "stat";
    private static final String KEY_CAT = "category";
    private static final String KEY_DLINE = "deadline";
    private static final String KEY_PRIVACY = "privacylevel";
    public QuestionDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
        		+ KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_UID + " TEXT,"
                + KEY_QUESTION + " TEXT,"
                + KEY_QID + " TEXT UNIQUE,"
                + KEY_TIME + " TEXT,"
                + KEY_STATUS + " TEXT," 
                + KEY_CAT + " TEXT,"
                + KEY_DLINE + " TEXT,"
                + KEY_PRIVACY + " TEXT"+")";
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
    
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
    	db.execSQL("DROP TABLE IF EXISTS "+ TABLE_LOGIN);
    	onCreate(db);
    }
 
    /**
     * Storing user details in database
     * */
    public void addUser(String uid, String question, String qid, String created_at, String stat, String category, String deadline, String privacylevel) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_UID, uid); 
        values.put(KEY_QUESTION, question); 
        values.put(KEY_QID, qid); 
        values.put(KEY_TIME, created_at); 
        values.put(KEY_STATUS, stat);
        values.put(KEY_CAT, category);
        values.put(KEY_DLINE, deadline);
        values.put(KEY_PRIVACY, privacylevel);
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
            user.put("question", cursor.getString(2));
            user.put("qid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
            user.put("stat", cursor.getString(5));
            user.put("category", cursor.getString(6));
            user.put("deadline", cursor.getString(7));
            user.put("privacylevel", cursor.getString(8));
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
        if ( db != null )
        {
        	db.delete(TABLE_LOGIN, null, null);
        	db.close();
        }
    }
}
