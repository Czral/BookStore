package com.example.android.bookstore.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.bookstore.R;
import com.example.android.bookstore.adapters.FragmentsAdapter;
import com.example.android.bookstore.data.Book;
import com.example.android.bookstore.data.Cd;
import com.example.android.bookstore.database.StoreViewModel;
import com.example.android.bookstore.databinding.MainActivityBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private StoreViewModel storeViewModel;
    List<Book> bookList;
    List<Cd> cdList;

    public List<Integer> bookIds = new ArrayList<>();
    public List<Integer> cdIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivityBinding binding = MainActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        FragmentsAdapter fragmentsAdapter = new FragmentsAdapter(this);

        binding.viewPager.setAdapter(fragmentsAdapter);

        storeViewModel = new ViewModelProvider(this).get(StoreViewModel.class);
        storeViewModel.getAllBooks().observe(this, books -> bookList = books);
        storeViewModel.getAllCds().observe(this, cds -> cdList = cds);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {

            switch (position) {

                case 0:

                    tab.setText("Book");
                    break;

                case 1:

                    tab.setText("CD");
                    break;
            }
        }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_store, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        if (item.getItemId() == R.id.delete_books) {

            storeViewModel.getAllBooks().observe(this, books -> {

                for (int i = 0; i < books.size(); i++) {

                    Book book = books.get(i);
                    if (book.isSelection()) {
                        bookIds.add(book.getId());
                    }
                }

                if (!bookIds.isEmpty()) {

                    builder.setPositiveButton("Yes", (dialog, which) -> {

                        storeViewModel.deleteBooks(bookIds);
                        dialog.dismiss();
                        bookIds.clear();
                    });

                    builder.setMessage("Are you sure to delete the selected books?");
                    builder.create().show();

                } else {

                    Toast.makeText(MainActivity.this, "No items selected.", Toast.LENGTH_SHORT).show();
                }

            });

        } else if (item.getItemId() == R.id.delete_cds) {

            storeViewModel.getAllCds().observe(this, cds -> {

                for (int i = 0; i < cds.size(); i++) {

                    Cd cd = cds.get(i);
                    if (cd.isSelection()) {
                        cdIds.add(cd.getId());
                    }
                }

                if (!cdIds.isEmpty()) {

                    builder.setPositiveButton("Yes", (dialog, which) -> {

                        storeViewModel.deleteCds(cdIds);
                        dialog.dismiss();
                        cdIds.clear();
                    });

                    builder.setMessage("Are you sure to delete the selected CDs?");
                    builder.create().show();
                } else {

                    Toast.makeText(MainActivity.this, "No items selected.", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (item.getItemId() == R.id.clear_all) {

            builder.setPositiveButton("Yes", (dialog, which) -> {

                storeViewModel.getAllBooks().observe(MainActivity.this, books -> {

                    if (books != null) {

                        storeViewModel.deleteAllBooks(books);
                    }
                });

                storeViewModel.getAllCds().observe(MainActivity.this, cds -> {

                    if (cds != null) {

                        storeViewModel.deleteAllCds(cds);
                    }
                });

                dialog.dismiss();
            });

            builder.setMessage("Are you sure to delete everything?");
            builder.create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        storeViewModel.getAllCds().removeObservers(this);
        storeViewModel.getAllBooks().removeObservers(this);
    }
}
