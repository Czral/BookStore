package com.example.android.bookstore.data;

import android.provider.BaseColumns;

/**
 * Created by XXX on 18-May-18.
 */

public class StoreContract {

    public static class BookEntry implements BaseColumns {

        public static final String TABLE_NAME = "books";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_BOOK_NAME = "Name";
        public static final String COLUMN_BOOK_PRICE = "Price";
        public static final String COLUMN_BOOK_QUANTITY = "Quantity";
        public static final String COLUMN_BOOK_SUPPLIER = "Supplier";
        public static final String COLUMN_BOOK_PHONE = "Phone";
        public static final String COLUMN_BOOK_COVER = "Cover";

        public static final int COVER_HARDCOVER = 0;
        public static final int COVER_PAPERBACK = 1;

    }

    public static class CDEntry implements BaseColumns {

        public static final String TABLE_NAME = "cds";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_CD_NAME = "Name";
        public static final String COLUMN_CD_PRICE = "Price";
        public static final String COLUMN_CD_QUANTITY = "Quantity";
        public static final String COLUMN_CD_ARTIST = "Artist";
        public static final String COLUMN_CD_GENRE = "Genre";

        public static final int GENRE_JAZZ = 0;
        public static final int GENRE_POP = 1;
        public static final int GENRE_DANCE = 2;
        public static final int GENRE_ROCK = 3;
        public static final int GENRE_CLASSICAL = 4;
        public static final int GENRE_OPERA = 5;
        public static final int GENRE_AUDIOBOOK = 6;

    }

}
