package com.example.philipgo.servodoorlock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "lockcodes_table";
    private static final String TABLE_NAME1 = "register_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "Name";
    private static final String COL3 = "Lock_codes";
    private static final String COL4 = "ID";
    private static final String COL5 = "Name";
    private static final String COL6 = "email";
    private static final String COL7 = "password";
    private static final String COL8 = "secret_code";


    public DatabaseHelper(Data context) {
        super(context, TABLE_NAME, null, 1);
    }

    public DatabaseHelper(EditDataActivity editDataActivity) {
        super(editDataActivity, TABLE_NAME, null, 1);
    }

    public DatabaseHelper(LockCodesActivity lockCodesActivity) {
        super(lockCodesActivity, TABLE_NAME, null, 1);
    }

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME1, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable1 = "CREATE TABLE " + TABLE_NAME1 + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL5 + " TEXT," +
                COL6 + " VARCHAR(320), " + COL7 + " VARCHAR(320), " + COL8 + " VARCHAR(320) " + ")";
        db.execSQL(createTable1);      //Register table

        String createTable2 = "CREATE TABLE " + TABLE_NAME + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT," +
                COL3 + " INTEGER " + ")";
        db.execSQL(createTable2);     //Lock Codes table

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        onCreate(db);
    }

    public boolean addData(String name, int Lockcode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, name);
        contentValues.put(COL3, Lockcode);

        Log.d(TAG, "addData: Adding " + name + " to " + TABLE_NAME);
        Log.d(TAG, "addData: Adding " + Lockcode + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);


        System.out.println("Add lockcode result: " + result);

        //if date is inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getLoginData(){
        //      System.out.println("How are you????");
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME1;
        Cursor data = db.rawQuery(query, null);
        System.out.println("No.of.data.stored in Lock codes table: " + data.getCount());
        return data;
    }

    public Cursor getLockCodesData(){
        //      System.out.println("How are you????");
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        System.out.println("No.of.data.stored in Lock codes table: " + data.getCount());
        return data;
    }

    public boolean addRegisterDetails(String name, String email, String password, String secret_code ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL5, name);
        contentValues.put(COL6, email);
        contentValues.put(COL7, password);
        contentValues.put(COL8, secret_code);

        Log.d(TAG, "addData: Adding " + name + " to " + TABLE_NAME1);
        Log.d(TAG, "addData: Adding " + email + " to " + TABLE_NAME1);
        Log.d(TAG, "addData: Adding " + password + " to " + TABLE_NAME1);
        Log.d(TAG, "addData: Adding " + secret_code + " to " + TABLE_NAME1);

        long result = db.insert(TABLE_NAME1, null, contentValues);

        System.out.println("No of data added: " + result);

        //if date is inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the data from database
     * @return
     */


    public int getLoginDetails(String email_id, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME1 + " WHERE " + COL6 + " = '" + email_id + "'" + " AND " + COL7 + " = '" + password + "'";;
        Cursor data = db.rawQuery(query, null);
        System.out.println("Running query is" + query);
        System.out.println("Result data:" + data);
        System.out.println("Result data: count" + data.getCount());
        while(data.moveToNext()){
            System.out.println("Result data 1:" + data);
            System.out.println("Result data 2:" + data.getCount());
        }
        return data.getCount();
    }

    public int getEmailDetails(String email_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME1 + " WHERE " + COL6 + " = '" + email_id + "'";
        Cursor data = db.rawQuery(query, null);
        System.out.println("Running query is" + query);
        System.out.println("Result data:" + data);
        System.out.println("Result data: count" + data.getCount());
        while(data.moveToNext()){
            System.out.println("Result data 1:" + data);
            System.out.println("Result data 2:" + data.getCount());
        }
        return data.getCount();
    }

    public int getSecretCodeDetails(String secret_code){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME1 + " WHERE " + COL8 + " = '" + secret_code + "'";;
        Cursor data = db.rawQuery(query, null);
        System.out.println("Running query is" + query);
        System.out.println("Result data:" + data);
        System.out.println("Result data: count" + data.getCount());
        while(data.moveToNext()){
            System.out.println("Result data 1:" + data);
            System.out.println("Result data 2:" + data.getCount());
        }
        return data.getCount();
    }
    /**
     * Returns only the id that matches the Lock code passed in
     * @param lockcode
     * @return
     */
//    public Cursor getItemID(int lockcode){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
//                " WHERE " + COL3 + " = '" + lockcode + "'";
//        Cursor data = db.rawQuery(query, null);
//        return data;
//    }

    /**
     * Returns only the id that matches the Name passed in
     * @param Name
     * @return
     */
    public Cursor getItemID(String Name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT *  FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + Name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the name field
     * @param newName
     * @param oldName
     * @param id
     */
    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    /**
     * Updates the Lockcode field
     * @param id
     * @param newLockcode
     * @param oldLockcode
     */
    public void updateLockcode(int newLockcode, int id, int oldLockcode){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL3 +
                " = '" + newLockcode + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL3 + " = '" + oldLockcode + "'";
        Log.d(TAG, "updateLockcode: query: " + query);
        Log.d(TAG, "updateLockcode: Setting Lockcode to " + newLockcode);
        db.execSQL(query);
    }


    /**
     * Delete from database
     * @param name
     * @param Lockcode
     * @param id
     */
    public void deleteName(int id, String name, int Lockcode){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        Log.d(TAG, "deleteName: Deleting " + Lockcode + " from database.");
        db.execSQL(query);
    }

}

























