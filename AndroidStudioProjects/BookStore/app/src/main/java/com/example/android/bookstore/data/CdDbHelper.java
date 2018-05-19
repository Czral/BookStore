package com.example.android.bookstore.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by XXX on 18-May-18.
 */

public class CdDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bookstore2.db";

    public CdDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQL_CREATE_CDS_TABLE = "CREATE TABLE " + StoreContract.CDEntry.TABLE_NAME +
                "(" + StoreContract.CDEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StoreContract.CDEntry.COLUMN_CD_NAME + " TEXT NOT NULL, " +
                StoreContract.CDEntry.COLUMN_CD_PRICE + " REAL NOT NULL, " +
                StoreContract.CDEntry.COLUMN_CD_QUANTITY + " INTEGER NOT NULL, " +
                StoreContract.CDEntry.COLUMN_CD_ARTIST + " TEXT, " +
                StoreContract.CDEntry.COLUMN_CD_GENRE + " INTEGER" + ")";

        sqLiteDatabase.execSQL(SQL_CREATE_CDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
