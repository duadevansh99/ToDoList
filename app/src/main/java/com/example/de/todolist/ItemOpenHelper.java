package com.example.de.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="TODO LIST DATABASE";
    public static final int Version=1;
    public ItemOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQLitems= "CREATE TABLE " + Contract.TABLE_NAME +
                " ( " + Contract.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.COLUMN_ITEM + " TEXT, " +
                Contract.COLUMN_DESC + " TEXT, " +
                Contract.COLUMN_DATE + " TEXT, " +
                Contract.COLUMN_TIME + " TEXT, " +
                Contract.YEAR + "  INTEGER, " +
                Contract.MONTH + " INTEGER, " +
                Contract.DAY + " INTEGER, " +
                Contract.HOUR + " INTEGER, " +
                Contract.MINUTE + " INTEGER " + " ) ";
        db.execSQL(SQLitems);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
