package com.missedops.library;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class CorrespondenceDatabase extends SQLiteOpenHelper{
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 4;
 
    // Database Name
    private static final String DATABASE_NAME = "android_api";
 
    // Login table name
    private static final String TABLE_CORRESPONDENCE = "correspondence";
 /*
  * Correspondence table
  * id auto increment int(11) 
  * qid varchar(23) not null
  * contact_soc_ntwk_name varchar(50) not null
  * contact_unique_id varchar(23) null
  * contact_soc_ntwk_id varchar(100) not null
  * contact_displayname varchar(100) not null
  * question_sent datetime not null
  * question_read  datetime null
  */
    // Login Table Columns names
    private static final String KEY_ID= "id";
    private static final String KEY_QID = "qid";
    private static final String KEY_CON_SOC_NTWK_NAME = "contact_soc_ntwk_name";
    private static final String KEY_CON_UID = "contact_unique_id ";
    private static final String KEY_CON_SOC_NTWK_ID = "contact_soc_ntwk_id";
    private static final String KEY_CON_NAME = "contact_displayname";
    private static final String KEY_Q_SENT = "question_sent";
    private static final String KEY_Q_READ = "question_read";
    public CorrespondenceDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CORRESPONDENCE_TABLE = "CREATE TABLE " + TABLE_CORRESPONDENCE + "("
        		+ KEY_ID + " INTEGER PRIMARY KEY, "
        		+ KEY_QID + " TEXT, "
                + KEY_CON_SOC_NTWK_NAME + " TEXT,"
                + KEY_CON_UID + " TEXT,"
                + KEY_CON_SOC_NTWK_ID + " TEXT,"
                + KEY_CON_NAME + " TEXT,"
                + KEY_Q_SENT + " DATETIME,"
                + KEY_Q_READ + " DATETIME"+")";                                
        db.execSQL(CREATE_CORRESPONDENCE_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CORRESPONDENCE);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * Storing user details in database
     * */
    public void addCorrespondence(String qid, String contact_soc_ntwk_name, String contact_unique_id, String contact_soc_ntwk_id, String contact_displayname, String question_sent, String question_read) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_QID, qid); 
        values.put(KEY_CON_SOC_NTWK_NAME, contact_soc_ntwk_name); 
        values.put(KEY_CON_UID, contact_unique_id); 
        values.put(KEY_CON_SOC_NTWK_ID, contact_soc_ntwk_id); 
        values.put(KEY_CON_NAME, contact_displayname);
        values.put(KEY_Q_SENT, question_sent);
        values.put(KEY_Q_READ, question_read);        
        // Inserting Row
        db.insert(TABLE_CORRESPONDENCE, null, values);
        db.close(); // Closing database connection
    }
     
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_CORRESPONDENCE;
          
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("qid", cursor.getString(1));
            user.put("contact_soc_ntwk_name", cursor.getString(2));
            user.put("contact_unique_id", cursor.getString(3));
            user.put("contact_soc_ntwk_id", cursor.getString(4));
            user.put("contact_displayname", cursor.getString(5));
            user.put("question_sent", cursor.getString(6));
            user.put("question_read", cursor.getString(7));            
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
        String countQuery = "SELECT  * FROM " + TABLE_CORRESPONDENCE;
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
        db.delete(TABLE_CORRESPONDENCE, null, null);
        db.close();
    }
}
