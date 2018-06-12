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
 * Created by XXX on 05-Jun-18.
 */

public class CdCursorAdapter extends CursorAdapter {

    private View.OnClickListener mOnClickListener;
    private int cdQuantity = 0;
    private TextView quantityTextView;
    private int quantityColumnIndex;
    private Uri currentUri;

    public CdCursorAdapter(Context context, Cursor cursor) {

        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(R.layout.cd_list_item, viewGroup, false);

    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView titleTextView = view.findViewById(R.id.title);
        int titleColumnIndex = cursor.getColumnIndex(StoreContract.CDEntry.COLUMN_CD_NAME);
        String cdName = cursor.getString(titleColumnIndex);
        titleTextView.setText(cdName);

        TextView priceTextView = view.findViewById(R.id.price);
        int priceColumnIndex = cursor.getColumnIndex(StoreContract.CDEntry.COLUMN_CD_PRICE);
        double cdPrice = cursor.getDouble(priceColumnIndex);
        priceTextView.setText(String.valueOf(cdPrice));

        quantityTextView = view.findViewById(R.id.quantity);
        quantityColumnIndex = cursor.getColumnIndex(StoreContract.CDEntry.COLUMN_CD_QUANTITY);
        cdQuantity = cursor.getInt(quantityColumnIndex);
        quantityTextView.setText(String.valueOf(cdQuantity));

        TextView artistTextView = view.findViewById(R.id.artist);
        int artistColumnIndex = cursor.getColumnIndex(StoreContract.CDEntry.COLUMN_CD_ARTIST);
        final String cdArtist = cursor.getString(artistColumnIndex);
        artistTextView.setText(cdArtist);

        TextView genreTextView = view.findViewById(R.id.genre);
        int genreColumnIndex = cursor.getColumnIndex(StoreContract.CDEntry.COLUMN_CD_GENRE);
        int cdGenre = cursor.getInt(genreColumnIndex);

        switch (cdGenre) {

            case StoreContract.CDEntry.GENRE_JAZZ:
                genreTextView.setText(context.getResources().getString(R.string.jazz));
                break;

            case StoreContract.CDEntry.GENRE_POP:
                genreTextView.setText(context.getResources().getString(R.string.pop));
                break;

            case StoreContract.CDEntry.GENRE_DANCE:
                genreTextView.setText(context.getResources().getString(R.string.dance));
                break;

            case StoreContract.CDEntry.GENRE_ROCK:
                genreTextView.setText(context.getResources().getString(R.string.rock));
                break;

            case StoreContract.CDEntry.GENRE_CLASSICAL:
                genreTextView.setText(context.getResources().getString(R.string.classical));
                break;

            case StoreContract.CDEntry.GENRE_OPERA:
                genreTextView.setText(context.getResources().getString(R.string.opera));
                break;

            case StoreContract.CDEntry.GENRE_AUDIOBOOK:
                genreTextView.setText(context.getResources().getString(R.string.audiobook));
                break;

        }

        Button saleButton = view.findViewById(R.id.sale_button);

        final int position = cursor.getPosition();

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cursor.moveToPosition(position);

                int id = cursor.getColumnIndex(StoreContract.CDEntry._ID);
                long itemId = cursor.getLong(id);
                currentUri = ContentUris.withAppendedId(StoreContract.CDEntry.CONTENT_CDS_URI, itemId);
                cdQuantity = cursor.getInt(quantityColumnIndex);

                if (cdQuantity < 1) {
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

                    cdQuantity--;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(StoreContract.CDEntry.COLUMN_CD_QUANTITY, cdQuantity);
                    context.getContentResolver().update(currentUri, contentValues, null, null);

                    quantityTextView.setText(String.valueOf(cdQuantity));

                    Snackbar snackbar = Snackbar.make(view, context.getResources().getString(R.string.item_sold), Snackbar.LENGTH_LONG).
                            setAction(context.getResources().getString(R.string.undo), mOnClickListener).setActionTextColor(Color.GREEN);
                    snackbar.show();

                }
            }

        });

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cdQuantity++;

                ContentValues contentValues = new ContentValues();
                contentValues.put(StoreContract.CDEntry.COLUMN_CD_QUANTITY, cdQuantity);
                context.getContentResolver().update(currentUri, contentValues, null, null);

                quantityTextView.setText(String.valueOf(cdQuantity));
            }
        };

    }

}