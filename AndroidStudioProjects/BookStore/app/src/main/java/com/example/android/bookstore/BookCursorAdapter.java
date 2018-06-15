package com.example.android.bookstore;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.bookstore.data.StoreContract;

/**
 * Created by XXX on 13-Jun-18.
 */

public class BookCursorAdapter extends CursorAdapter {

    private View.OnClickListener mOnClickListener;
    private TextView quantityTextView;
    private int quantityColumnIndex;
    private int bookQuantity;
    private Uri currentUri;

    public BookCursorAdapter(Context context, Cursor cursor) {

        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(R.layout.book_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView titleTextView = view.findViewById(R.id.title);
        int titleColumnIndex = cursor.getColumnIndex(StoreContract.BookEntry.COLUMN_BOOK_NAME);
        String bookName = cursor.getString(titleColumnIndex);
        titleTextView.setText(bookName);

        TextView priceTextView = view.findViewById(R.id.price);
        int priceColumnIndex = cursor.getColumnIndex(StoreContract.BookEntry.COLUMN_BOOK_PRICE);
        double bookPrice = cursor.getDouble(priceColumnIndex);
        priceTextView.setText(String.valueOf(bookPrice));

        quantityTextView = view.findViewById(R.id.quantity);
        quantityColumnIndex = cursor.getColumnIndex(StoreContract.BookEntry.COLUMN_BOOK_QUANTITY);
        bookQuantity = cursor.getInt(quantityColumnIndex);
        quantityTextView.setText(String.valueOf(bookQuantity));

        TextView supplierNameTextView = view.findViewById(R.id.supplier_name);
        int supplierNameColumnIndex = cursor.getColumnIndex(StoreContract.BookEntry.COLUMN_BOOK_SUPPLIER);
        String supplierName = cursor.getString(supplierNameColumnIndex);
        supplierNameTextView.setText(supplierName);

        TextView supplierPhoneTextView = view.findViewById(R.id.supplier_phone);
        int supplierPhoneColumnIndex = cursor.getColumnIndex(StoreContract.BookEntry.COLUMN_BOOK_PHONE);
        long supplierPhone = cursor.getLong(supplierPhoneColumnIndex);
        supplierPhoneTextView.setText(String.valueOf(supplierPhone));

        TextView coverTextView = view.findViewById(R.id.cover_type);
        int coverColumnIndex = cursor.getColumnIndex(StoreContract.BookEntry.COLUMN_BOOK_COVER);
        int coverType = cursor.getInt(coverColumnIndex);

        switch (coverType) {

            case StoreContract.BookEntry.COVER_HARDCOVER:

                coverTextView.setText(context.getResources().getString(R.string.hard_cover));
                break;

            case StoreContract.BookEntry.COVER_PAPERBACK:

                coverTextView.setText(context.getResources().getString(R.string.paper_back));
                break;
        }

        Button saleButton = view.findViewById(R.id.sale_button);
        final int position = cursor.getPosition();
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cursor.moveToPosition(position);
                int id = cursor.getColumnIndex(StoreContract.BookEntry._ID);
                long longId = cursor.getLong(id);

                currentUri = ContentUris.withAppendedId(StoreContract.BookEntry.CONTENT_BOOK_URI, longId);
                bookQuantity = cursor.getInt(quantityColumnIndex);

                if (bookQuantity < 1) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(false);
                    builder.setMessage(context.getResources().getString(R.string.quantity_cannot_decrease));
                    builder.setNeutralButton(context.getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                        }
                    });

                    builder.create().show();

                } else {

                    bookQuantity--;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(StoreContract.BookEntry.COLUMN_BOOK_QUANTITY, bookQuantity);
                    context.getContentResolver().update(currentUri, contentValues, null, null);
                    quantityTextView.setText(String.valueOf(bookQuantity));

                    Snackbar snackbar = Snackbar.make(view, context.getResources().getString(R.string.item_sold), Snackbar.LENGTH_LONG).
                            setAction(context.getResources().getString(R.string.undo), mOnClickListener).setActionTextColor(Color.GREEN);
                    snackbar.show();
                }

            }
        });

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookQuantity++;

                ContentValues contentValues = new ContentValues();
                contentValues.put(StoreContract.BookEntry.COLUMN_BOOK_QUANTITY, bookQuantity);
                context.getContentResolver().update(currentUri, contentValues, null, null);
                quantityTextView.setText(String.valueOf(bookQuantity));
            }
        };

    }
}
