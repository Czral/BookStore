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
import com.example.android.bookstore.adapters.CdRecyclerAdapter;
import com.example.android.bookstore.database.StoreViewModel;
import com.example.android.bookstore.databinding.FragmentBookCdBinding;
import com.squareup.picasso.Picasso;

public class CdFragment extends Fragment {

    FragmentBookCdBinding binding;

    public CdFragment() {
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

            Intent intent = new Intent(getContext(), EditorCdActivity.class);
            getContext().startActivity(intent);
        });

        StoreViewModel storeViewModel = new ViewModelProvider(getActivity()).get(StoreViewModel.class);

        storeViewModel.getAllCds().observe((LifecycleOwner) getContext(), cds -> {

            if (cds != null && cds.size() > 0) {

                CdRecyclerAdapter cdAdapter = new CdRecyclerAdapter(cds);
                binding.bookCdRecyclerView.setAdapter(cdAdapter);
            } else {

                binding.bookCdRecyclerView.setAdapter(new BookRecyclerAdapter(null));
            }
        });

        registerForContextMenu(view);

        return view;

    }
}