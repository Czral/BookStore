package com.example.android.bookstore.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.android.bookstore.R;
import com.example.android.bookstore.adapters.BookRecyclerAdapter;
import com.example.android.bookstore.database.StoreViewModel;
import com.example.android.bookstore.databinding.FragmentBookCdBinding;
import com.squareup.picasso.Picasso;

public class BookFragment extends Fragment {

    FragmentBookCdBinding binding;

    public BookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookCdBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.bookCdRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));

        Picasso.get().load(R.drawable.add).into(binding.bookCdFab);
        binding.bookCdFab.setOnClickListener(v -> {

            Intent intent = new Intent(getContext(), EditorBookActivity.class);
            getContext().startActivity(intent);
        });

        StoreViewModel storeViewModel = new ViewModelProvider(getActivity()).get(StoreViewModel.class);

        storeViewModel.getAllBooks().observe((LifecycleOwner) getContext(), books -> {

            if (books != null && books.size() > 0) {

                BookRecyclerAdapter bookCursorAdapter = new BookRecyclerAdapter(books);
                binding.bookCdRecyclerView.setAdapter(bookCursorAdapter);
            } else {

                binding.bookCdRecyclerView.setAdapter(new BookRecyclerAdapter(null));
            }
        });

        registerForContextMenu(view);
        return view;
    }
}