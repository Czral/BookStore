package com.example.android.bookstore.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by XXX on 18-May-18.
 */

public class StoreContract {

    private StoreContract() {

    }

    public static final String CONTENT_AUTHORITY = "com.example.android.bookstore";
    public static final Uri BOOKS_BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri CDS_BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BOOKS = "books";
    public static final String PATH_CDS = "cds";

    public static class BookEntry implements BaseColumns {

        public static final Uri CONTENT_BOOK_URI = Uri.withAppendedPath(BOOKS_BASE_CONTENT_URI, PATH_BOOKS);
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

        public static final String CONTENT_BOOK_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public static final String CONTENT_BOOK_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        // Checks if cover type is valid.
        public static boolean isValidCover(int cover) {

            if (cover == BookEntry.COVER_HARDCOVER || cover == BookEntry.COVER_PAPERBACK) {

                return true;
            }

            return false;
        }

    }

    public static class CDEntry implements BaseColumns {

        public static final Uri CONTENT_CDS_URI = Uri.withAppendedPath(CDS_BASE_CONTENT_URI, PATH_CDS);
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

        public static final String CONTENT_CDS_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CDS;

        public static final String CONTENT_CDS_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CDS;

        // Checks if genre is valid.
        public static boolean isValidGenre(int genre) {

            if (genre == CDEntry.GENRE_JAZZ || genre == CDEntry.GENRE_POP ||
                    genre == CDEntry.GENRE_DANCE || genre == CDEntry.GENRE_ROCK ||
                    genre == CDEntry.GENRE_CLASSICAL || genre == CDEntry.GENRE_OPERA || genre == CDEntry.GENRE_AUDIOBOOK) {

                return true;
            }
            return false;
        }

    }

}
