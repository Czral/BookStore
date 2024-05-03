package com.example.android.bookstore.view;

import static com.example.android.bookstore.data.Constants.BOOK_INTENT;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.bookstore.R;
import com.example.android.bookstore.data.Book;
import com.example.android.bookstore.database.StoreViewModel;
import com.example.android.bookstore.databinding.ActivityEditorBookBinding;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Collections;

public class EditorBookActivity extends AppCompatActivity {

    private StoreViewModel storeViewModel;
    private Book existingBook;

    private int cover;
    private int genre;
    private int go;

    private ActivityEditorBookBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditorBookBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Picasso.get().load(R.drawable.books_stack).into(binding.imageView);

        setupSpinners();

        Intent bookIntent = getIntent();
        int id = bookIntent.getIntExtra(BOOK_INTENT, 0);

        storeViewModel = new ViewModelProvider(this).get(StoreViewModel.class);

        if (id > 0) {

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {

                actionBar.setTitle("Edit book");
            }

            go = id;

            storeViewModel.getBook(id).observe(this, book -> {

                if (book != null) {

                    existingBook = book;

                    binding.nameEditText.setText(book.getTitle());
                    binding.authorArtistEditText.setText(book.getAuthor());
                    binding.priceEditText.setText(String.valueOf(book.getPrice()));
                    binding.quantityEditText.setText(String.valueOf(book.getQuantity()));
                    if (book.getIsbn() != 0) {

                        binding.isbnEditText.setText(String.valueOf(book.getIsbn()));
                    }

                    binding.coverSpinner.setSelection(book.getCover());
                    binding.genreSpinner.setSelection(book.getGenre());

                    setTitle(getResources().getString(R.string.edit_a_book));

                } else {

                    setTitle(getResources().getString(R.string.add_a_book));
                }
            });

        }


    }

    // Setup spinners.
    private void setupSpinners() {

        ArrayAdapter<CharSequence> coverSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.book_cover_spinner, android.R.layout.simple_spinner_item);
        coverSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        binding.coverSpinner.setAdapter(coverSpinnerAdapter);

        binding.coverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                cover = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                cover = 0;
            }
        });

        ArrayAdapter<CharSequence> genreSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.book_genre_spinner, android.R.layout.simple_spinner_item);
        genreSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        binding.genreSpinner.setAdapter(genreSpinnerAdapter);

        binding.genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                genre = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                genre = 0;
            }
        });

    }

    // Saves-Updates book.
    private void saveBook() {

        if (existingBook == null) {

            long isbn;

            String title = binding.nameEditText.getText().toString().trim();
            String prices = binding.priceEditText.getText().toString().trim();
            String isbnText = binding.isbnEditText.getText().toString().trim();
            String quantityText = binding.quantityEditText.getText().toString().trim();
            String author = binding.authorArtistEditText.getText().toString().trim();

            if (!checkIfEditTextsAreEmpty()) {

                binding.nameEditText.setTextKeepState(title);
                binding.priceEditText.setTextKeepState(prices);
                binding.quantityEditText.setTextKeepState(quantityText);

                missingFields();
            } else {

                int quantity = Integer.parseInt(quantityText);

                if (isbnText.isEmpty()) {

                    isbn = 0;
                } else {

                    isbn = Long.parseLong(isbnText);
                }

                double price = Double.parseDouble(prices);
                DecimalFormat decimalFormat = new DecimalFormat("####0.00");
                prices = decimalFormat.format(price);
                price = Double.parseDouble(prices);


                String covers = getResources().getStringArray(R.array.book_cover_spinner)[cover];
                String genres = getResources().getStringArray(R.array.book_genre_spinner)[genre];

                Book book = new Book(title, author, isbn, price, prices, quantity, cover, covers, genre, genres);
                storeViewModel.insertBook(book);

                finish();
            }
        } else {

            long isbn;

            String title = binding.nameEditText.getText().toString().trim();
            String prices = binding.priceEditText.getText().toString().trim();
            String isbnText = binding.isbnEditText.getText().toString().trim();
            String quantityText = binding.quantityEditText.getText().toString().trim();
            String author = binding.authorArtistEditText.getText().toString().trim();

            if (!checkIfEditTextsAreEmpty()) {

                Log.d("RomiRain1", "EMPTY");

                binding.nameEditText.setTextKeepState(title);
                binding.priceEditText.setTextKeepState(prices);
                binding.quantityEditText.setTextKeepState(quantityText);

                missingFields();

            } else {


                Log.d("RomiRain1", "NU:LL EMPTY");

                int quantity = Integer.parseInt(quantityText);

                if (isbnText.isEmpty()) {

                    isbn = 0;
                } else {

                    isbn = Long.parseLong(isbnText);
                }

                double price = Double.parseDouble(prices);
                DecimalFormat decimalFormat = new DecimalFormat("####0.00");
                prices = decimalFormat.format(price);
                price = Double.parseDouble(prices);

                String covers = getResources().getStringArray(R.array.book_cover_spinner)[cover];
                String genres = getResources().getStringArray(R.array.book_genre_spinner)[genre];

                existingBook = new Book(title, author, isbn, price, prices, quantity, cover, covers, genre, genres);

                storeViewModel.deleteBooks(Collections.singletonList(go));
                storeViewModel.insertBook(existingBook);
                finish();

            }
        }
    }

    private boolean checkIfEditTextsAreEmpty() {

        return binding.nameEditText.getText().toString().trim().isEmpty()
                && binding.authorArtistEditText.getText().toString().trim().isEmpty()
                && binding.priceEditText.getText().toString().trim().isEmpty()
                && binding.quantityEditText.getText().toString().trim().isEmpty();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor_book, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.save) {

            saveBook();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (checkIfEditTextsAreEmpty()) {

            DialogInterface.OnClickListener discardChangesDialogListener = (dialogInterface, i) -> finish();
            discardChangesDialog(discardChangesDialogListener);
        } else {

            super.onBackPressed();
        }
    }

    // This method is called when back is pressed before saving changes.
    private void discardChangesDialog(DialogInterface.OnClickListener discardButtonListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.check_dialog));
        builder.setPositiveButton(getResources().getString(android.R.string.yes), discardButtonListener);
        builder.setNegativeButton(getResources().getString(android.R.string.no), (dialogInterface, i) -> {

            if (dialogInterface != null) {

                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // This method is called when book info are missing.
    private void missingFields() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.alert_book));
        builder.setPositiveButton(getResources().getString(android.R.string.yes), (dialog, which) -> dialog.cancel());
        builder.setNegativeButton(getResources().getString(android.R.string.no), (dialog, which) -> {

            dialog.cancel();
            finish();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
