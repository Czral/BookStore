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

import com.example.android.bookstore.data.StoreContract.BookEntry;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditorBookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.cover_spinner)
    Spinner coverSpinner;

    @BindView(R.id.product_name_edit_text)
    EditText productEditText;

    @BindView(R.id.price_edit_text)
    EditText priceEditText;

    @BindView(R.id.quantity_edit_text)
    EditText quantityEditText;

    @BindView(R.id.supplier_name_edit_text)
    EditText supplierNameEditText;

    @BindView(R.id.supplier_phone_edit_text)
    EditText supplierPhoneEditText;

    @BindView(R.id.increase_quantity)
    ImageButton increaseButton;

    @BindView(R.id.decrease_quantity)
    ImageButton decreaseButton;

    @BindView(R.id.order_button)
    ImageButton orderButton;

    public int bookCoverInt = 0;

    int currentQuantity;
    long currentSupplierPhone;

    private Uri currentBookUri;

    private static final int LOADER_ID = 0;

    private boolean bookHasChanged = false;

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            bookHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_book);

        ButterKnife.bind(this);

        Intent bookIntent = getIntent();
        currentBookUri = bookIntent.getData();

        if (currentBookUri == null) {

            setTitle(getResources().getString(R.string.add_a_book));
            invalidateOptionsMenu();
            increaseButton.setVisibility(View.GONE);
            decreaseButton.setVisibility(View.GONE);
        } else {

            setTitle(getResources().getString(R.string.edit_a_book));
            getLoaderManager().initLoader(LOADER_ID, null, this);
        }
        productEditText.setOnTouchListener(touchListener);
        priceEditText.setOnTouchListener(touchListener);
        supplierNameEditText.setOnTouchListener(touchListener);
        coverSpinner.setOnTouchListener(touchListener);
        supplierNameEditText.setOnTouchListener(touchListener);
        supplierPhoneEditText.setOnTouchListener(touchListener);

        setupSpinner();
    }

    // Setups spinner.
    private void setupSpinner() {

        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.cover_spinner, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        coverSpinner.setAdapter(spinnerAdapter);

        coverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String bookCoverString = (String) adapterView.getItemAtPosition(i);

                if (!TextUtils.isEmpty(bookCoverString)) {
                    if (bookCoverString.equals(getString(R.string.hard_cover))) {

                        bookCoverInt = BookEntry.COVER_HARDCOVER;
                    } else {

                        bookCoverInt = BookEntry.COVER_PAPERBACK;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                bookCoverInt = BookEntry.COVER_HARDCOVER;
            }
        });

    }

    // Saves-Updates book.
    private void saveBook() {

        String supplierNameBlank = getResources().getString(R.string.no_supplier_name);
        double priceDouble;
        double priceDoubleRounded;
        int quantityInt;
        int supplierPhoneBlank = 0;
        long supplierPhoneLong;

        String productName = productEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String quantity = quantityEditText.getText().toString().trim();
        String supplierName = supplierNameEditText.getText().toString().trim();
        String supplierPhone = supplierPhoneEditText.getText().toString().trim();

        if (currentBookUri == null && TextUtils.isEmpty(productName) && TextUtils.isEmpty(price) && TextUtils.isEmpty(quantity)
                && TextUtils.isEmpty(supplierName) && TextUtils.isEmpty(supplierPhone) && bookCoverInt == BookEntry.COVER_HARDCOVER) {

            finish();
        }
        if (productName.isEmpty() || price.isEmpty() || quantity.isEmpty()) {

            if (!productName.isEmpty()) {

                productEditText.setTextKeepState(productName);

                if (!price.isEmpty()) {

                    priceEditText.setTextKeepState(price);

                    if (!quantity.isEmpty()) {

                        quantityEditText.setTextKeepState(quantity);

                        if (!supplierName.isEmpty()) {

                            supplierNameEditText.setTextKeepState(supplierName);

                            if (!supplierPhone.isEmpty()) {

                                supplierPhoneEditText.setTextKeepState(supplierPhone);
                            }
                        }

                    }

                }

            }

            Toast.makeText(this, getResources().getString(R.string.toast_for_missing_info), Toast.LENGTH_SHORT).show();

        } else {

            priceDouble = Double.parseDouble(price);
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            price = decimalFormat.format(priceDouble).toString();
            priceDoubleRounded = Double.parseDouble(price);
            quantityInt = Integer.parseInt(quantity);

            ContentValues contentValues = new ContentValues();
            contentValues.put(BookEntry.COLUMN_BOOK_NAME, productName);
            contentValues.put(BookEntry.COLUMN_BOOK_PRICE, priceDoubleRounded);
            contentValues.put(BookEntry.COLUMN_BOOK_QUANTITY, quantityInt);
            contentValues.put(BookEntry.COLUMN_BOOK_COVER, bookCoverInt);

            finish();

            if (!TextUtils.isEmpty(supplierName)) {

                contentValues.put(BookEntry.COLUMN_BOOK_SUPPLIER, supplierName);
            } else {

                contentValues.put(BookEntry.COLUMN_BOOK_SUPPLIER, supplierNameBlank);
            }

            if (!TextUtils.isEmpty(supplierPhone)) {

                supplierPhoneLong = Long.parseLong(supplierPhone);
                contentValues.put(BookEntry.COLUMN_BOOK_PHONE, supplierPhoneLong);
            } else {

                contentValues.put(BookEntry.COLUMN_BOOK_PHONE, supplierPhoneBlank);
            }

            if (currentBookUri == null) {

                Uri newBookUri = getContentResolver().insert(BookEntry.CONTENT_BOOK_URI, contentValues);

                if (newBookUri == null) {

                    Toast.makeText(this, getResources().getString(R.string.error_saving_book), Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(this, getResources().getString(R.string.book_saved_successfully), Toast.LENGTH_SHORT).show();
                }

            } else {

                int rowsAffected = getContentResolver().update(currentBookUri, contentValues,
                        null, null);

                if (rowsAffected == 0) {

                    Toast.makeText(this, getResources().getString(R.string.error_updating_book), Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(this, getResources().getString(R.string.book_updated), Toast.LENGTH_SHORT).show();
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

                saveBook();
                return true;

            case R.id.delete:

                showDeletionDialog();
                return true;

            case android.R.id.home:

                if (!bookHasChanged) {

                    NavUtils.navigateUpFromSameTask(this);
                } else {

                    DialogInterface.OnClickListener discardChangesDialogListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            NavUtils.navigateUpFromSameTask(EditorBookActivity.this);
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

        if (!bookHasChanged) {

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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_COVER,
                BookEntry.COLUMN_BOOK_SUPPLIER,
                BookEntry.COLUMN_BOOK_PHONE};

        return new CursorLoader(this,
                currentBookUri,
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

            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int coverColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_COVER);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PHONE);

            String currentName = cursor.getString(nameColumnIndex);
            double currentPrice = cursor.getDouble(priceColumnIndex);
            currentQuantity = cursor.getInt(quantityColumnIndex);
            int currentCover = cursor.getInt(coverColumnIndex);
            String currentSupplierName = cursor.getString(supplierNameColumnIndex);
            currentSupplierPhone = cursor.getLong(supplierPhoneColumnIndex);

            productEditText.setText(currentName);
            priceEditText.setText(String.valueOf(currentPrice));
            quantityEditText.setText(String.valueOf(currentQuantity));

            switch (currentCover) {

                case BookEntry.COVER_HARDCOVER:
                    coverSpinner.setSelection(0);
                    break;

                case BookEntry.COVER_PAPERBACK:
                    coverSpinner.setSelection(1);
                    break;
            }

            supplierNameEditText.setText(currentSupplierName);
            supplierPhoneEditText.setText(String.valueOf(currentSupplierPhone));

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

                        Toast.makeText(EditorBookActivity.this, getResources().getString(R.string.quantity_cannot_decrease), Toast.LENGTH_SHORT).show();
                    } else {

                        currentQuantity -= 1;
                        quantityEditText.setText(String.valueOf(currentQuantity));
                    }
                }
            });

            orderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (currentSupplierPhone != 0) {
                        Intent orderIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", String.valueOf(currentSupplierPhone), null));
                        if (orderIntent.resolveActivity(getPackageManager()) != null) {

                            startActivity(orderIntent);
                        }
                    } else {

                        missingPhoneNumber();
                    }

                }
            });

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        productEditText.setText("");
        priceEditText.setText("");
        quantityEditText.setText("");
        coverSpinner.setSelection(0);
        supplierNameEditText.setText("");
        supplierPhoneEditText.setText("");
    }

    public boolean onPrepareOptionsMenu(Menu menu) {

        if (currentBookUri == null) {

            MenuItem deleteItem = menu.findItem(R.id.delete);
            deleteItem.setVisible(false);
        }

        return true;
    }

    // Deletes book.
    private void deleteBook() {

        if (currentBookUri != null) {

            int rowsDeleted = getContentResolver().delete(currentBookUri, null, null);
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

                deleteBook();
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

    private void missingPhoneNumber() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.toast_for_missing_phone_number));
        builder.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}
