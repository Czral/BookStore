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

import com.example.android.bookstore.data.BookDbHelper;
import com.example.android.bookstore.data.StoreContract.BookEntry;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditorBookActivity extends AppCompatActivity {

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

    public int bookCoverInt = 0;

    long newRowId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_book);

        ButterKnife.bind(this);

        setupSpinner();

    }

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

    private void insertBook() {

        double priceDouble;
        double priceDoubleRounded;
        int quantityInt;
        int supplierPhoneInt;

        BookDbHelper bookDbHelper = new BookDbHelper(this);
        SQLiteDatabase database = bookDbHelper.getWritableDatabase();

        String productName = productEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String quantity = quantityEditText.getText().toString().trim();
        String supplierName = supplierNameEditText.getText().toString().trim();
        String supplierPhone = supplierPhoneEditText.getText().toString().trim();

        ContentValues contentValues = new ContentValues();

        if (productName.isEmpty() || price.isEmpty() || quantity.isEmpty() || supplierName.isEmpty()
                || supplierPhone.isEmpty()) {

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

            onBackPressed();
        } else {

            priceDouble = Double.parseDouble(price);
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            price = decimalFormat.format(priceDouble).toString();
            priceDoubleRounded = Double.parseDouble(price);
            quantityInt = Integer.parseInt(quantity);
            supplierPhoneInt = Integer.parseInt(supplierPhone);

            contentValues.put(BookEntry.COLUMN_BOOK_NAME, productName);
            contentValues.put(BookEntry.COLUMN_BOOK_PRICE, priceDoubleRounded);
            contentValues.put(BookEntry.COLUMN_BOOK_QUANTITY, quantityInt);
            contentValues.put(BookEntry.COLUMN_BOOK_COVER, bookCoverInt);
            contentValues.put(BookEntry.COLUMN_BOOK_SUPPLIER, supplierName);
            contentValues.put(BookEntry.COLUMN_BOOK_PHONE, supplierPhoneInt);
            finish();
        }

        newRowId = database.insert(BookEntry.TABLE_NAME, null, contentValues);
        if (newRowId == -1) {

            Toast.makeText(this, getResources().getString(R.string.error_saving_book), Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, getResources().getString(R.string.book_saved_in_row) + " " + newRowId, Toast.LENGTH_SHORT).show();
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

                insertBook();
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
        builder.setMessage(getResources().getString(R.string.alert_book));
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
