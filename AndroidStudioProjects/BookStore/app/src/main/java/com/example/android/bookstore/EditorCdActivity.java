package com.example.android.bookstore;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.bookstore.data.StoreContract;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditorCdActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.genre_spinner)
    Spinner genreSpinner;

    @BindView(R.id.product_name_edit_text)
    EditText nameEditText;

    @BindView(R.id.price_edit_text)
    EditText priceEditText;

    @BindView(R.id.quantity_edit_text)
    EditText quantityEditText;

    @BindView(R.id.artist_edit_text)
    EditText artistEditText;

    @BindView(R.id.increase_quantity)
    ImageButton increaseButton;

    @BindView(R.id.decrease_quantity)
    ImageButton decreaseButton;

    public int genreInt = 0;

    int currentQuantity;

    private Uri currentCdUri;

    private static final int LOADER_ID = 1;

    private boolean cdHasChanged = false;

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            cdHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_cd);

        ButterKnife.bind(this);

        Intent cdIntent = getIntent();
        currentCdUri = cdIntent.getData();

        if (currentCdUri == null) {

            setTitle(getResources().getString(R.string.add_a_cd));
            invalidateOptionsMenu();
            increaseButton.setVisibility(View.GONE);
            decreaseButton.setVisibility(View.GONE);
        } else {

            setTitle(getResources().getString(R.string.edit_a_cd));
            getLoaderManager().initLoader(LOADER_ID, null, this);
        }

        nameEditText.setOnTouchListener(touchListener);
        priceEditText.setOnTouchListener(touchListener);
        quantityEditText.setOnTouchListener(touchListener);
        genreSpinner.setOnTouchListener(touchListener);
        artistEditText.setOnTouchListener(touchListener);

        setupSpinner();
    }

    // Setups the spinner.
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
                    } else if (genreString.equals(getString(R.string.audiobook))) {

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

    // Saves-Updates CD.
    public void saveCd() {

        String artistNameBlank = getResources().getString(R.string.unknown_artist);
        double cd_price_double;
        double priceDoubleRounded;
        int quantityInt;

        String cdName = nameEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String quantity = quantityEditText.getText().toString().trim();
        String artist = artistEditText.getText().toString().trim();

        if (currentCdUri == null && TextUtils.isEmpty(cdName) && TextUtils.isEmpty(price) && TextUtils.isEmpty(quantity)
                && TextUtils.isEmpty(artist) && genreInt == StoreContract.CDEntry.GENRE_JAZZ) {

            finish();
        }

        if (cdName.isEmpty() || price.isEmpty() || quantity.isEmpty()) {

            if (!cdName.isEmpty()) {

                nameEditText.setTextKeepState(cdName);

                if (!price.isEmpty()) {

                    priceEditText.setTextKeepState(price);

                    if (!quantity.isEmpty()) {

                        quantityEditText.setTextKeepState(quantity);

                        if (genreInt != StoreContract.CDEntry.GENRE_JAZZ) {

                            genreSpinner.setSelection(genreInt);
                        }

                    }
                }
            }

            Toast.makeText(this, getResources().getString(R.string.toast_for_missing_info), Toast.LENGTH_SHORT).show();
        } else {

            cd_price_double = Double.parseDouble(price);
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            price = decimalFormat.format(cd_price_double).toString();
            priceDoubleRounded = Double.parseDouble(price);
            quantityInt = Integer.parseInt(quantity);

            ContentValues contentValues = new ContentValues();
            contentValues.put(StoreContract.CDEntry.COLUMN_CD_NAME, cdName);
            contentValues.put(StoreContract.CDEntry.COLUMN_CD_PRICE, priceDoubleRounded);
            contentValues.put(StoreContract.CDEntry.COLUMN_CD_QUANTITY, quantityInt);
            contentValues.put(StoreContract.CDEntry.COLUMN_CD_GENRE, genreInt);

            finish();

            if (!TextUtils.isEmpty(artist)) {

                contentValues.put(StoreContract.CDEntry.COLUMN_CD_ARTIST, artist);
            } else {

                contentValues.put(StoreContract.CDEntry.COLUMN_CD_ARTIST, artistNameBlank);
            }

            if (currentCdUri == null) {

                Uri newCdUri = getContentResolver().insert(StoreContract.CDEntry.CONTENT_CDS_URI, contentValues);

                if (newCdUri == null) {

                    Toast.makeText(this, getResources().getString(R.string.error_saving_cd), Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(this, getResources().getString(R.string.cd_saved_successfully), Toast.LENGTH_SHORT).show();
                }

            } else {

                int rowsAffected = getContentResolver().update(StoreContract.CDEntry.CONTENT_CDS_URI, contentValues,
                        null, null);

                if (rowsAffected == 0) {

                    Toast.makeText(this, getResources().getString(R.string.error_updating_cd), Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(this, getResources().getString(R.string.cd_updated), Toast.LENGTH_SHORT).show();
                }
            }
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

                saveCd();
                return true;

            case R.id.delete:

                showDeletionDialog();
                return true;

            case android.R.id.home:

                if (!cdHasChanged) {

                    NavUtils.navigateUpFromSameTask(this);
                } else {

                    DialogInterface.OnClickListener discardChangesDialogListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            NavUtils.navigateUpFromSameTask(EditorCdActivity.this);
                        }
                    };

                    discardChangesDialog(discardChangesDialogListener);
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (!cdHasChanged) {

            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardChangesDialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                finish();
            }
        };

        discardChangesDialog(discardChangesDialogListener);
    }

    // This method is called when CD info are missing.
    private void missingFields() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.alert_cd));
        builder.setPositiveButton(getResources().getString(android.R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                dialog.cancel();
            }
        });

        builder.setNegativeButton(getResources().getString(android.R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                dialog.cancel();
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {StoreContract.CDEntry._ID,
                StoreContract.CDEntry.COLUMN_CD_NAME,
                StoreContract.CDEntry.COLUMN_CD_PRICE,
                StoreContract.CDEntry.COLUMN_CD_QUANTITY,
                StoreContract.CDEntry.COLUMN_CD_ARTIST,
                StoreContract.CDEntry.COLUMN_CD_GENRE};

        return new CursorLoader(this,
                currentCdUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {

            return;
        }

        if (cursor.moveToFirst()) {

            int nameColumnIndex = cursor.getColumnIndex(StoreContract.CDEntry.COLUMN_CD_NAME);
            int priceColumnIndex = cursor.getColumnIndex(StoreContract.CDEntry.COLUMN_CD_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(StoreContract.CDEntry.COLUMN_CD_QUANTITY);
            int artistColumnIndex = cursor.getColumnIndex(StoreContract.CDEntry.COLUMN_CD_ARTIST);
            int genreColumnIndex = cursor.getColumnIndex(StoreContract.CDEntry.COLUMN_CD_GENRE);

            String currentName = cursor.getString(nameColumnIndex);
            double currentPrice = cursor.getDouble(priceColumnIndex);
            currentQuantity = cursor.getInt(quantityColumnIndex);
            String currentArtist = cursor.getString(artistColumnIndex);
            int currentGenre = cursor.getInt(genreColumnIndex);

            nameEditText.setText(currentName);
            priceEditText.setText(String.valueOf(currentPrice));
            quantityEditText.setText(String.valueOf(currentQuantity));
            artistEditText.setText(currentArtist);

            switch (currentGenre) {

                case StoreContract.CDEntry.GENRE_JAZZ:
                    genreSpinner.setSelection(0);

                case StoreContract.CDEntry.GENRE_POP:
                    genreSpinner.setSelection(1);

                case StoreContract.CDEntry.GENRE_DANCE:
                    genreSpinner.setSelection(2);

                case StoreContract.CDEntry.GENRE_ROCK:
                    genreSpinner.setSelection(3);

                case StoreContract.CDEntry.GENRE_CLASSICAL:
                    genreSpinner.setSelection(4);

                case StoreContract.CDEntry.GENRE_OPERA:
                    genreSpinner.setSelection(5);

                case StoreContract.CDEntry.GENRE_AUDIOBOOK:
                    genreSpinner.setSelection(6);

            }

            increaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    currentQuantity += 1;
                    quantityEditText.setText(String.valueOf(currentQuantity));
                }
            });

            decreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (currentQuantity < 1) {

                        Toast.makeText(EditorCdActivity.this, getResources().getString(R.string.quantity_cannot_decrease), Toast.LENGTH_SHORT).show();
                    } else {

                        currentQuantity -= 1;
                        quantityEditText.setText(String.valueOf(currentQuantity));
                    }
                }
            });

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        nameEditText.setText("");
        priceEditText.setText("");
        quantityEditText.setText("");
        artistEditText.setText("");
        genreSpinner.setSelection(0);

    }

    // This method is called when back is pressed before saving changes.
    private void discardChangesDialog(DialogInterface.OnClickListener discardButtonListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.check_dialog));
        builder.setPositiveButton(getResources().getString(android.R.string.yes), discardButtonListener);
        builder.setNegativeButton(getResources().getString(android.R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (dialogInterface != null) {

                    dialogInterface.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Deletes CD.
    private void deleteCd() {

        if (currentCdUri != null) {

            int rowsDeleted = getContentResolver().delete(currentCdUri, null, null);
            if (rowsDeleted == 0) {

                Toast.makeText(this, getResources().getString(R.string.deletion_failed), Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, getResources().getString(R.string.deletion_successful), Toast.LENGTH_SHORT).show();
            }

        }

        finish();
    }

    // This method is called when delete is clicked.
    private void showDeletionDialog() {

        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setMessage(getResources().getString(R.string.check_delete));
        deleteDialog.setPositiveButton(getResources().getString(android.R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                deleteCd();
            }
        });
        deleteDialog.setNegativeButton(getResources().getString(android.R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (dialogInterface != null) {

                    dialogInterface.dismiss();
                }
            }
        });

        deleteDialog.create().show();

    }

    public boolean onPrepareOptionsMenu(Menu menu) {

        if (currentCdUri == null) {

            MenuItem deleteItem = menu.findItem(R.id.delete);
            deleteItem.setVisible(false);
        }

        return true;
    }
}



