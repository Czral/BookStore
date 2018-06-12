package com.example.android.bookstore;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookstore.data.StoreContract;
import com.example.android.bookstore.data.StoreContract.BookEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.fab_book)
    FloatingActionButton fabBook;

    @BindView(R.id.fab_cd)
    FloatingActionButton fabCd;

    @BindView(R.id.book_list)
    ListView bookList;

    @BindView(R.id.cd_list)
    ListView cdList;

    @BindView(R.id.view)
    View view;

    @BindView(R.id.linear_layout)
    View linearLayout;

    @BindView(R.id.empty)
    View emptyView;

    @BindView(R.id.empty_view_book)
    TextView emptyViewBook;

    @BindView(R.id.empty_view_cd)
    View emptyViewCd;

    BookCursorAdapter bookCursorAdapter;
    CdCursorAdapter cdCursorAdapter;

    private static final int BOOK_LOADER = 0;
    private static final int CD_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        ButterKnife.bind(this);

        fabBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent bookIntent = new Intent(StoreActivity.this, EditorBookActivity.class);
                startActivity(bookIntent);

            }
        });

        fabCd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cdIntent = new Intent(StoreActivity.this, EditorCdActivity.class);
                startActivity(cdIntent);
            }
        });
        bookCursorAdapter = new BookCursorAdapter(this, null);
        cdCursorAdapter = new CdCursorAdapter(this, null);

        bookList.setAdapter(bookCursorAdapter);

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Uri bookUri = ContentUris.withAppendedId(BookEntry.CONTENT_BOOK_URI, l);
                Intent bookIntent = new Intent(StoreActivity.this, EditorBookActivity.class);
                bookIntent.setData(bookUri);
                startActivity(bookIntent);
            }
        });

        cdList.setAdapter(cdCursorAdapter);

        TextView emptyText = findViewById(android.R.id.empty);
        cdList.setEmptyView(emptyViewCd);
        bookList.setActivated(true);
        bookList.setEmptyView(emptyViewBook);

        cdList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Uri cdUri = ContentUris.withAppendedId(StoreContract.CDEntry.CONTENT_CDS_URI, l);
                Intent cdIntent = new Intent(StoreActivity.this, EditorCdActivity.class);
                cdIntent.setData(cdUri);
                startActivity(cdIntent);
            }
        });

        getLoaderManager().initLoader(BOOK_LOADER, null, this);
        getLoaderManager().initLoader(CD_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_store, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.delete_books:

                deleteAllBooks();
                return true;

            case R.id.delete_cds:

                deleteAllCds();
                return true;

            case R.id.delete_everything:

                deleteEverything();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        CursorLoader cursorLoader = new CursorLoader(this, null, null, null
                , null, null);

        if (i == 0) {

            String[] projection = {BookEntry._ID,
                    BookEntry.COLUMN_BOOK_NAME,
                    BookEntry.COLUMN_BOOK_PRICE,
                    BookEntry.COLUMN_BOOK_QUANTITY,
                    BookEntry.COLUMN_BOOK_COVER,
                    BookEntry.COLUMN_BOOK_SUPPLIER,
                    BookEntry.COLUMN_BOOK_PHONE};

            cursorLoader = new CursorLoader(this,
                    BookEntry.CONTENT_BOOK_URI,
                    projection,
                    null,
                    null,
                    null);

        }

        if (i == 1) {

            String[] projection = {StoreContract.CDEntry._ID,
                    StoreContract.CDEntry.COLUMN_CD_NAME,
                    StoreContract.CDEntry.COLUMN_CD_PRICE,
                    StoreContract.CDEntry.COLUMN_CD_QUANTITY,
                    StoreContract.CDEntry.COLUMN_CD_ARTIST,
                    StoreContract.CDEntry.COLUMN_CD_GENRE};

            cursorLoader = new CursorLoader(this,
                    StoreContract.CDEntry.CONTENT_CDS_URI,
                    projection,
                    null,
                    null,
                    null);

        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {

        if (loader.getId() == 1) {
            cdCursorAdapter.swapCursor(cursor);
        }

        if (loader.getId() == 0) {

            bookCursorAdapter.swapCursor(cursor);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        cdCursorAdapter.swapCursor(null);
        bookCursorAdapter.swapCursor(null);

    }

    // Deletes the Book list.
    private void deleteAllBooks() {

        int bookRowsDeleted = getContentResolver().delete(BookEntry.CONTENT_BOOK_URI, null, null);

        if (bookCursorAdapter.getCount() == 0) {

            Toast.makeText(this, getResources().getString(R.string.nothing_to_delete), Toast.LENGTH_SHORT).show();
        } else {

            if (bookRowsDeleted != 0) {

                Toast.makeText(this, getResources().getString(R.string.book_list_deleted), Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, getResources().getString(R.string.deletion_failed), Toast.LENGTH_SHORT).show();
            }
        }

    }

    // Deletes the CD list.
    private void deleteAllCds() {

        int rowsDeleted = getContentResolver().delete(StoreContract.CDEntry.CONTENT_CDS_URI, null, null);

        if (cdCursorAdapter.getCount() == 0) {

            Toast.makeText(this, getResources().getString(R.string.nothing_to_delete), Toast.LENGTH_SHORT).show();
        } else {
            if (rowsDeleted != 0) {

                Toast.makeText(this, getResources().getString(R.string.cd_list_deleted), Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, getResources().getString(R.string.deletion_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Deletes everything (CD and Book lists).
    private void deleteEverything() {

        int bookRowsDeleted = getContentResolver().delete(BookEntry.CONTENT_BOOK_URI, null, null);
        int cdRowsDeleted = getContentResolver().delete(StoreContract.CDEntry.CONTENT_CDS_URI, null, null);

        if (cdCursorAdapter.getCount() == 0 && bookCursorAdapter.getCount() == 0) {

            Toast.makeText(this, getResources().getString(R.string.nothing_to_delete), Toast.LENGTH_SHORT).show();
        } else if (bookCursorAdapter.getCount() != 0) {

            if (cdCursorAdapter.getCount() == 0 && bookRowsDeleted != 0) {
                Toast.makeText(this, getResources().getString(R.string.book_list_deleted), Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, getResources().getString(R.string.lists_deleted), Toast.LENGTH_SHORT).show();
        } else if (cdCursorAdapter.getCount() != 0) {

            if (bookCursorAdapter.getCount() == 0 && cdRowsDeleted != 0) {

                Toast.makeText(this, getResources().getString(R.string.cd_list_deleted), Toast.LENGTH_SHORT).show();
            }
        }

    }
}




