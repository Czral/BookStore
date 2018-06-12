package com.example.android.bookstore.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by XXX on 18-May-18.
 */

public class StoreDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bookstore.db";

    public StoreDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + StoreContract.BookEntry.TABLE_NAME +
                "(" + StoreContract.BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StoreContract.BookEntry.COLUMN_BOOK_NAME + " TEXT NOT NULL, " +
                StoreContract.BookEntry.COLUMN_BOOK_PRICE + " REAL NOT NULL, " +
                StoreContract.BookEntry.COLUMN_BOOK_QUANTITY + " INTEGER NOT NULL, " +
                StoreContract.BookEntry.COLUMN_BOOK_COVER + " INTEGER DEFAULT 0, " +
                StoreContract.BookEntry.COLUMN_BOOK_SUPPLIER + " TEXT, " +
                StoreContract.BookEntry.COLUMN_BOOK_PHONE + " INTEGER" + ")";

        String SQL_CREATE_CDS_TABLE = "CREATE TABLE " + StoreContract.CDEntry.TABLE_NAME +
                "(" + StoreContract.CDEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StoreContract.CDEntry.COLUMN_CD_NAME + " TEXT NOT NULL, " +
                StoreContract.CDEntry.COLUMN_CD_PRICE + " REAL NOT NULL, " +
                StoreContract.CDEntry.COLUMN_CD_QUANTITY + " INTEGER NOT NULL, " +
                StoreContract.CDEntry.COLUMN_CD_ARTIST + " TEXT, " +
                StoreContract.CDEntry.COLUMN_CD_GENRE + " INTEGER" + ")";

        sqLiteDatabase.execSQL(SQL_CREATE_BOOKS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
