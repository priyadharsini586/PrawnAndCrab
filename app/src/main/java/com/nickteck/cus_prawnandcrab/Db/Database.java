package com.nickteck.cus_prawnandcrab.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 4/9/2018.
 */

public class Database  extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ID = "ID";
    private static final String NAME= "NAME";

    private static final String DATABASE_NAME = "TABLE";


    private static final String CUSTOMER_ID = "customer_id";
    private static final String ID = "id";
    private static final  String CUSTOMER_NAME ="CUSTOMER_NAME";
    private static final  String CUSTOMER_PHONE ="CUSTOMER_PHONE";
    private static final  String CUSTOMER_E_MAIL ="CUSTOMER_E_MAIL";
    private static final String USER_PROFILE_PIC= "profile_pic";

    // Contacts table name
    private static final String CUSTOMER_TABLE = "customer_table";
    private static final String CREATE_TABLE_LIST ="table_list";
    public static String customer_id;
    public static String customer_phoneNo;
    public static String profile_img;


    public Database(Context context) {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String TABLE="CREATE TABLE "+CREATE_TABLE_LIST+"("
                +KEY_ID +" TEXT,"
                +NAME +" TEXT"+")";

        String CUSTOMER="CREATE TABLE "+CUSTOMER_TABLE+"("
                +CUSTOMER_ID +" TEXT,"
                +CUSTOMER_NAME+" TEXT,"
                + CUSTOMER_PHONE+" TEXT,"
                + USER_PROFILE_PIC + " TEXT ,"
                +CUSTOMER_E_MAIL+" TEXT"+")";
        sqLiteDatabase.execSQL(TABLE);
        sqLiteDatabase.execSQL(CUSTOMER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +CREATE_TABLE_LIST);
        onCreate(sqLiteDatabase);
    }
    public long insertTable(String id,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID,id);
        values.put(NAME,name);
        long list=db.insert(CREATE_TABLE_LIST, null, values);
        db.close();
        return list;
    }

    public long insertCustomerTable(String id,String customer_name,String customer_phone,String e_mail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_ID,id);
        values.put(CUSTOMER_NAME,customer_name);
        values.put(CUSTOMER_PHONE,customer_phone);
        values.put(CUSTOMER_E_MAIL,e_mail);
        long list=db.insert(CUSTOMER_TABLE, null, values);
        db.close();
        return list;
    }
    public  String getCustomerName() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + CUSTOMER_TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();
       // customer_phone = cursor.getString()
        customer_id = cursor.getString(0);
        String data = cursor.getString(1);
        customer_phoneNo = cursor.getString(2);
        profile_img = cursor.getString(3);

        db.close();


        return data;
    }



    public  String getData() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + CREATE_TABLE_LIST;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();


        String data = cursor.getString(1);
        db.close();

        return data;
    }

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ CREATE_TABLE_LIST);
        db.close();

        SQLiteDatabase db1 = this.getWritableDatabase();
        db1.execSQL("delete from "+ CUSTOMER_TABLE);
        db1.close();
    }
    public boolean checkTables() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + CUSTOMER_TABLE, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        if (count > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }

    }

    /*public String[] getUserName(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER_DETAILS, new String[] { USER_NAME,
                        USER_MBL,USER_PROFILE_PIC,USER_TYPE}, USER_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();


        String[] userDetails = {cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)};

        // return contact
        return userDetails;
    }*/




}
