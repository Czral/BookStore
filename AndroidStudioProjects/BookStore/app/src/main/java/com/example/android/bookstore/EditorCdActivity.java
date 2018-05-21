package com.example.android.bookstore;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.bookstore.data.CdDbHelper;
import com.example.android.bookstore.data.StoreContract;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditorCdActivity extends AppCompatActivity {

    @BindView(R.id.genre_spinner)
    Spinner genreSpinner;

    @BindView(R.id.product_name_edit_text)
    EditText cdName;

    @BindView(R.id.price_edit_text)
    EditText cdPrice;

    @BindView(R.id.quantity_edit_text)
    EditText cdQuantity;

    @BindView(R.id.artist_edit_text)
    EditText cdArtist;

    public int genreInt = 0;

    public CdDbHelper cdDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_cd);

        ButterKnife.bind(this);

        setupSpinner();
    }

    private void setupSpinner() {

        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.genre_spinner, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        genreSpinner.setAdapter(spinnerAdapter);

        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String genreString = (String) adapterView.getItemAtPosition(i);

                if (!TextUtils.isEmpty(genreString)) {
                    if (genreString.equals(getString(R.string.jazz))) {

                        genreInt = StoreContract.CDEntry.GENRE_JAZZ;
                    } else if (genreString.equals(getString(R.string.pop))) {

                        genreInt = StoreContract.CDEntry.GENRE_POP;
                    } else if (genreString.equals(getString(R.string.dance))) {

                        genreInt = StoreContract.CDEntry.GENRE_DANCE;
                    } else if (genreString.equals(getString(R.string.rock))) {

                        genreInt = StoreContract.CDEntry.GENRE_ROCK;
                    } else if (genreString.equals(getString(R.string.classical))) {

                        genreInt = StoreContract.CDEntry.GENRE_CLASSICAL;
                    } else if (genreString.equals(getString(R.string.opera))) {

                        genreInt = StoreContract.CDEntry.GENRE_OPERA;
                    } else {

                        genreInt = StoreContract.CDEntry.GENRE_AUDIOBOOK;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                genreInt = StoreContract.CDEntry.GENRE_JAZZ;
            }
        });

    }

    public void insertCd() {

        cdDbHelper = new CdDbHelper(this);
        SQLiteDatabase sqLiteDatabase = cdDbHelper.getWritableDatabase();

        String cd_name = cdName.getText().toString().trim();
        String cd_price = cdPrice.getText().toString().trim();
        String cd_quantity = cdQuantity.getText().toString().trim();
        String cd_artist = cdArtist.getText().toString().trim();

        ContentValues contentValues = new ContentValues();

        if (cd_name.isEmpty() || cd_price.isEmpty() || cd_quantity.isEmpty() || cd_artist.isEmpty()) {

            if (!cd_name.isEmpty()) {

                cdName.setTextKeepState(cd_name);

                if (!cd_price.isEmpty()) {

                    cdPrice.setTextKeepState(cd_price);

                    if (!cd_quantity.isEmpty()) {

                        cdQuantity.setTextKeepState(cd_quantity);

                        if (!cd_artist.isEmpty()) {

                            cdArtist.setTextKeepState(cd_artist);
                        }
                    }
                }
            }

            onBackPressed();
        } else {

            double cd_price_double = Double.parseDouble(cd_price);
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            cd_price = decimalFormat.format(cd_price_double).toString();
            double priceDoubleRounded = Double.parseDouble(cd_price);
            int cd_quantity_int = Integer.parseInt(cd_quantity);

            contentValues.put(StoreContract.CDEntry.COLUMN_CD_NAME, cd_name);
            contentValues.put(StoreContract.CDEntry.COLUMN_CD_PRICE, priceDoubleRounded);
            contentValues.put(StoreContract.CDEntry.COLUMN_CD_QUANTITY, cd_quantity_int);
            contentValues.put(StoreContract.CDEntry.COLUMN_CD_ARTIST, cd_artist);
            contentValues.put(StoreContract.CDEntry.COLUMN_CD_GENRE, genreInt);
            finish();
        }

        long newRowId = sqLiteDatabase.insert(StoreContract.CDEntry.TABLE_NAME, null, contentValues);
        if (newRowId == -1) {

            Toast.makeText(this, getResources().getString(R.string.error_saving_cd), Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, getResources().getString(R.string.cd_saved_in_row) + newRowId, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor_book, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.save:

                insertCd();
                return true;

            case R.id.delete:

                //Delete item
                return true;

            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.alert_cd));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}



