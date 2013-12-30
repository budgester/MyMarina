package com.budgester.mymarina;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by budgester on 17/09/13.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "marinaDB";
    private static final String TABLE_MARINAS = "marinas";
    private static final String MARINA_NAME = "marina_name";
    private static final String MAIN_CODE = "main_code";
    private static final String PONTOON_CODE = "pontoon_code";
    private static final String MALE_TOILET = "male_toilet";
    private static final String MALE_SHOWER = "male_shower";
    private static final String FEMALE_TOILET = "female_toilet";
    private static final String FEMALE_SHOWER = "female_shower";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MARINA_TABLE = "CREATE TABLE " + TABLE_MARINAS
                + "("
                + MARINA_NAME + " TEXT,"
                + MAIN_CODE + " TEXT,"
                + PONTOON_CODE + " TEXT,"
                + MALE_TOILET + " TEXT,"
                + MALE_SHOWER + " TEXT,"
                + FEMALE_TOILET + " TEXT,"
                + FEMALE_SHOWER + " TEXT" + ")";
        db.execSQL(CREATE_MARINA_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARINAS);

        // Create tables again
        onCreate(db);
    }

    public void addMarina(Marina marina) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MARINA_NAME, marina.get_marina_name());
        values.put(MAIN_CODE, "----");
        values.put(PONTOON_CODE, "----");
        values.put(MALE_TOILET, "----");
        values.put(MALE_SHOWER, "----");
        values.put(FEMALE_TOILET, "----");
        values.put(FEMALE_SHOWER, "----");

        // Inserting Row
        db.insert(TABLE_MARINAS, null, values);
        db.close(); // Closing database connection
    }

    public Marina getMarina(String marina_name){
    //public void getMarina(Marina marina, String marina_name){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MARINAS, new String[] {
                MARINA_NAME, MAIN_CODE, PONTOON_CODE, MALE_TOILET, MALE_SHOWER, FEMALE_TOILET, FEMALE_SHOWER
                }, MARINA_NAME + "=?",
                new String [] {marina_name},null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Marina marina = new Marina(marina_name);

        marina.set_main_code(cursor.getString(1));
        marina.set_pontoon_code(cursor.getString(2));
        marina.set_male_toilet(cursor.getString(3));
        marina.set_male_shower(cursor.getString(4));
        marina.set_female_toilet(cursor.getString(5));
        marina.set_female_shower(cursor.getString(6));
        db.close();
        return marina;
    }

    // Updating single Marina
    // A marina has to have been created in the database for this to work.
    public void updateMarina(Marina marina) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MARINA_NAME, marina.get_marina_name());
        values.put(MAIN_CODE, marina.get_main_code());
        values.put(PONTOON_CODE, marina.get_pontoon_code());
        values.put(MALE_TOILET, marina.get_male_toilet());
        values.put(MALE_SHOWER, marina.get_male_shower());
        values.put(FEMALE_TOILET, marina.get_female_toilet());
        values.put(FEMALE_SHOWER, marina.get_female_shower());

        // updating row
        db.update(TABLE_MARINAS, values, MARINA_NAME + " = ?",
                new String [] {marina.get_marina_name()}
        );
    }

    // Delete single marina
    public void deleteMarina(Marina marina) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MARINAS, MARINA_NAME + " =?", new String [] {marina.get_marina_name()});
        db.close();
    }

    //Get all Marinas
    public ArrayList<String> getAllMarinas() {
        ArrayList<String> marinaList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MARINAS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                marinaList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // return contact list
        return marinaList;
    }
}
