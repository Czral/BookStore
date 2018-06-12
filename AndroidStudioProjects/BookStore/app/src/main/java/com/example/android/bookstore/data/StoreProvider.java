package com.example.android.bookstore.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by XXX on 31-May-18.
 */

public class StoreProvider extends ContentProvider {

    private static final String LOG = StoreProvider.class.getSimpleName();

    private static final int BOOKS = 100;

    private static final int BOOK_ID = 101;

    private static final int CDS = 200;

    private static final int CD_ID = 201;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private StoreDbHelper storeDbHelper;

    static {

        uriMatcher.addURI(StoreContract.CONTENT_AUTHORITY, StoreContract.PATH_BOOKS, BOOKS);
        uriMatcher.addURI(StoreContract.CONTENT_AUTHORITY, StoreContract.PATH_BOOKS + "/#", BOOK_ID);
        uriMatcher.addURI(StoreContract.CONTENT_AUTHORITY, StoreContract.PATH_CDS, CDS);
        uriMatcher.addURI(StoreContract.CONTENT_AUTHORITY, StoreContract.PATH_CDS + "/#", CD_ID);
    }

    @Override
    public boolean onCreate() {

        storeDbHelper = new StoreDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase sqLiteDatabase = storeDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = uriMatcher.match(uri);

        switch (match) {

            case BOOKS:

                cursor = sqLiteDatabase.query(StoreContract.BookEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            case BOOK_ID:

                selection = StoreContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = sqLiteDatabase.query(StoreContract.BookEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);

                break;

            case CDS:

                cursor = sqLiteDatabase.query(StoreContract.CDEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            case CD_ID:

                selection = StoreContract.CDEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = sqLiteDatabase.query(StoreContract.CDEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            default:

                throw new IllegalArgumentException("Cannot query unknown URI" + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = uriMatcher.match(uri);

        switch (match) {

            case BOOKS:

                return StoreContract.BookEntry.CONTENT_BOOK_LIST_TYPE;

            case BOOK_ID:

                return StoreContract.BookEntry.CONTENT_BOOK_ITEM_TYPE;

            case CDS:

                return StoreContract.CDEntry.CONTENT_CDS_LIST_TYPE;

            case CD_ID:

                return StoreContract.CDEntry.CONTENT_CDS_ITEM_TYPE;

            default:

                throw new IllegalArgumentException("Unknown URI" + uri + "with match" + match);

        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = uriMatcher.match(uri);

        switch (match) {

            case BOOKS:

                return insertBook(uri, contentValues);

            case CDS:

                return insertCd(uri, contentValues);

            default:

                throw new IllegalArgumentException("Insertion is not supported for " + uri);

        }

    }

    // Inserts new book in Book list.
    private Uri insertBook(Uri uri, ContentValues contentValues) {

        String title = contentValues.getAsString(StoreContract.BookEntry.COLUMN_BOOK_NAME);
        if (title == null || title.isEmpty()) {

            throw new IllegalArgumentException("Book title required.");
        }

        Double price = contentValues.getAsDouble(StoreContract.BookEntry.COLUMN_BOOK_PRICE);
        if (price == null || StoreContract.BookEntry.COLUMN_BOOK_PRICE.isEmpty()) {

            throw new IllegalArgumentException("Book price required");
        }

        Integer quantity = contentValues.getAsInteger(StoreContract.BookEntry.COLUMN_BOOK_QUANTITY);
        if (quantity == null || StoreContract.BookEntry.COLUMN_BOOK_QUANTITY.isEmpty()) {

            throw new IllegalArgumentException("Book quantity required.");
        }

        Integer cover = contentValues.getAsInteger(StoreContract.BookEntry.COLUMN_BOOK_COVER);
        if (cover == null || !StoreContract.BookEntry.isValidCover(cover)) {

            throw new IllegalArgumentException("Book cover type required.");
        }

        // No need to check for supplier info, any value is valid (including null).

        SQLiteDatabase sqLiteDatabase = storeDbHelper.getWritableDatabase();

        long id = sqLiteDatabase.insert(StoreContract.BookEntry.TABLE_NAME, null, contentValues);

        if (id == -1) {

            Log.e(LOG, "Failed to insert new book for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase sqLiteDatabase = storeDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = uriMatcher.match(uri);

        switch (match) {

            case BOOKS:

                rowsDeleted = sqLiteDatabase.delete(StoreContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case BOOK_ID:

                selection = StoreContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = sqLiteDatabase.delete(StoreContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case CDS:

                rowsDeleted = sqLiteDatabase.delete(StoreContract.CDEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case CD_ID:

                selection = StoreContract.CDEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = sqLiteDatabase.delete(StoreContract.CDEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:

                throw new IllegalArgumentException("Deletion is not supported for " + uri);

        }

        if (rowsDeleted != 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        final int match = uriMatcher.match(uri);

        switch (match) {

            case BOOKS:

                return updateBook(uri, contentValues, selection, selectionArgs);

            case BOOK_ID:

                selection = StoreContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, contentValues, selection, selectionArgs);

            case CDS:

                return updateCd(uri, contentValues, selection, selectionArgs);

            case CD_ID:

                selection = StoreContract.CDEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateCd(uri, contentValues, selection, selectionArgs);
            default:

                throw new IllegalArgumentException("Update not supported for " + uri);

        }

    }

    // Updates existing book from Book list.
    private int updateBook(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        if (contentValues.containsKey(StoreContract.BookEntry.COLUMN_BOOK_NAME)) {

            String title = contentValues.getAsString(StoreContract.BookEntry.COLUMN_BOOK_NAME);
            if (title == null) {

                throw new IllegalArgumentException("Book title required.");
            }
        }

        if (contentValues.containsKey(StoreContract.BookEntry.COLUMN_BOOK_PRICE)) {

            Integer price = contentValues.getAsInteger(StoreContract.BookEntry.COLUMN_BOOK_PRICE);
            if (price == null) {

                throw new IllegalArgumentException("Book price required.");
            }
        }

        if (contentValues.containsKey(StoreContract.BookEntry.COLUMN_BOOK_QUANTITY)) {

            Integer quantity = contentValues.getAsInteger(StoreContract.BookEntry.COLUMN_BOOK_QUANTITY);
            if (quantity == null) {

                throw new IllegalArgumentException("Book quantity required.");
            }
        }

        if (contentValues.containsKey(StoreContract.BookEntry.COLUMN_BOOK_COVER)) {

            Integer cover = contentValues.getAsInteger(StoreContract.BookEntry.COLUMN_BOOK_COVER);
            if (cover == null || !StoreContract.BookEntry.isValidCover(cover)) {

                throw new IllegalArgumentException("Book cover type required.");
            }
        }

        // No need to check for supplier info, any value is valid (including null).

        if (contentValues.size() == 0) {

            return 0;
        }

        SQLiteDatabase sqLiteDatabase = storeDbHelper.getWritableDatabase();

        int rowsUpdated = sqLiteDatabase.update(StoreContract.BookEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        if (rowsUpdated != 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;

    }

    // Updates existing CD from CD list.
    private int updateCd(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        if (contentValues.containsKey(StoreContract.CDEntry.COLUMN_CD_NAME)) {

            String title = contentValues.getAsString(StoreContract.CDEntry.COLUMN_CD_NAME);
            if (title == null) {

                throw new IllegalArgumentException("CD title required.");
            }
        }

        if (contentValues.containsKey(StoreContract.CDEntry.COLUMN_CD_PRICE)) {

            Double price = contentValues.getAsDouble(StoreContract.CDEntry.COLUMN_CD_PRICE);
            if (price == null) {

                throw new IllegalArgumentException("CD price required.");
            }
        }

        if (contentValues.containsKey(StoreContract.CDEntry.COLUMN_CD_QUANTITY)) {

            Integer quantity = contentValues.getAsInteger(StoreContract.CDEntry.COLUMN_CD_QUANTITY);
            if (quantity == null) {

                throw new IllegalArgumentException("CD quantity required.");
            }
        }

        if (contentValues.containsKey(StoreContract.CDEntry.COLUMN_CD_GENRE)) {

            Integer genre = contentValues.getAsInteger(StoreContract.CDEntry.COLUMN_CD_GENRE);
            if (genre == null || !StoreContract.CDEntry.isValidGenre(genre)) {

                throw new IllegalArgumentException("CD genre type required.");
            }
        }

        // No need to check for artist info, any value is valid (including null).

        if (contentValues.size() == 0) {

            return 0;
        }

        SQLiteDatabase sqLiteDatabase = storeDbHelper.getWritableDatabase();

        int rowsUpdated = sqLiteDatabase.update(StoreContract.CDEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        if (rowsUpdated != 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;

    }

    // Inserts new CD in CD list.
    private Uri insertCd(Uri uri, ContentValues contentValues) {

        String title = contentValues.getAsString(StoreContract.CDEntry.COLUMN_CD_NAME);
        if (title == null || title.isEmpty()) {

            throw new IllegalArgumentException("Book title required.");
        }

        Double price = contentValues.getAsDouble(StoreContract.CDEntry.COLUMN_CD_PRICE);
        if (price == null || StoreContract.CDEntry.COLUMN_CD_PRICE.isEmpty()) {

            throw new IllegalArgumentException("Book price required");
        }

        Integer quantity = contentValues.getAsInteger(StoreContract.CDEntry.COLUMN_CD_QUANTITY);
        if (quantity == null || StoreContract.CDEntry.COLUMN_CD_QUANTITY.isEmpty()) {

            throw new IllegalArgumentException("Book quantity required.");
        }

        // No need to check for artist info, any value is valid (including null).

        // No need to check for genre info, any value is valid (including null).

        SQLiteDatabase sqLiteDatabase = storeDbHelper.getWritableDatabase();

        long id = sqLiteDatabase.insert(StoreContract.CDEntry.TABLE_NAME, null, contentValues);

        if (id == -1) {

            Log.e(LOG, "Failed to insert new CD for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);

    }
}
