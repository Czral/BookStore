package com.example.android.bookstore.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.android.bookstore.view.BookFragment;
import com.example.android.bookstore.view.CdFragment;

public class FragmentsAdapter extends FragmentStateAdapter {

    public FragmentsAdapter(FragmentActivity activity) {

        super(activity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 0) {

            return new BookFragment();
        } else {

            return new CdFragment();
        }
    }

    @Override
    public int getItemCount() {

        return 2;
    }

}
