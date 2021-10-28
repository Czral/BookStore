package com.example.android.bookstore.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.android.bookstore.data.Cd;
import com.example.android.bookstore.database.StoreViewModel;
import com.example.android.bookstore.databinding.ActivityEditorBookBinding;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Collections;

import static com.example.android.bookstore.data.Constants.CD_INTENT;

public class EditorCdActivity extends AppCompatActivity {

    private int genre;
    private StoreViewModel storeViewModel;
    private ActivityEditorBookBinding binding;
    private boolean isExistingCd = false;
    private int go;
    private Cd existingCd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditorBookBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Picasso.get().load(R.drawable.musical_note).into(binding.imageView);

        setupSpinner();

        storeViewModel = new ViewModelProvider(this).get(StoreViewModel.class);

        Intent cdIntent = getIntent();
        int id = cdIntent.getIntExtra(CD_INTENT, 0);

        if (id > 0) {

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {

                actionBar.setTitle("Edit CD");
            }

            go = id;
            isExistingCd = true;

            storeViewModel.getCd(id).observe(this, cd -> {

                if (cd != null) {

                    existingCd = cd;

                    binding.nameEditText.setText(cd.getTitle());
                    binding.authorArtistEditText.setText(cd.getArtist());
                    binding.quantityEditText.setText(String.valueOf(cd.getQuantity()));
                    binding.priceEditText.setText(String.valueOf(cd.getPrice()));

                    binding.genreSpinner.setSelection(cd.getGenre());
                }
            });
        }
    }

    // Setups the spinner.
    private void setupSpinner() {

        binding.authorArtistEditText.setHint("Artist");

        binding.textView1.setVisibility(View.GONE);
        binding.isbnEditText.setVisibility(View.GONE);
        binding.coverSpinner.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> genreAdapter = ArrayAdapter.createFromResource(this, R.array.cd_genre_spinner, android.R.layout.simple_spinner_item);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        binding.genreSpinner.setAdapter(genreAdapter);

        binding.genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                genre = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                genre = 0;
            }
        });
    }

    // Saves-Updates CD.
    public void saveCd() {

        double price;
        int quantityInt;

        String title = binding.nameEditText.getText().toString().trim();
        String prices = binding.priceEditText.getText().toString().trim();
        String quantity = binding.quantityEditText.getText().toString().trim();
        String artist = binding.authorArtistEditText.getText().toString().trim();

        if (!checkIfEditTextsAreEmpty()) {

            binding.nameEditText.setTextKeepState(title);
            binding.authorArtistEditText.setTextKeepState(artist);
            binding.priceEditText.setTextKeepState(prices);
            binding.quantityEditText.setTextKeepState(quantity);

            missingFields();
        } else {

            price = Double.parseDouble(prices);
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            prices = decimalFormat.format(price);
            price = Double.parseDouble(prices);
            quantityInt = Integer.parseInt(quantity);

            String genres = getResources().getStringArray(R.array.cd_genre_spinner)[genre];

            existingCd = new Cd(title, artist, price, prices, quantityInt, genre, genres);

            if (isExistingCd) {

                storeViewModel.deleteCds(Collections.singletonList(go));
            }

            storeViewModel.insertCd(existingCd);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor_book, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.save) {

            saveCd();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkIfEditTextsAreEmpty() {

        return binding.nameEditText.getText().toString().trim().length() > 0
                && binding.authorArtistEditText.getText().toString().trim().length() > 0
                && binding.priceEditText.getText().toString().trim().length() > 0
                && binding.quantityEditText.getText().toString().trim().length() > 0;
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

    // This method is called when CD info are missing.
    private void missingFields() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.alert_cd));
        builder.setPositiveButton(getResources().getString(android.R.string.yes), (dialog, i) -> dialog.cancel());

        builder.setNegativeButton(getResources().getString(android.R.string.no), (dialog, i) -> {
            dialog.cancel();
            finish();
        });

        AlertDialog alert = builder.create();
        alert.show();

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
}



