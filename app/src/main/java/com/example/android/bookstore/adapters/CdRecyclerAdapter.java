package com.example.android.bookstore.adapters;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.bookstore.R;
import com.example.android.bookstore.data.Cd;
import com.example.android.bookstore.data.Constants;
import com.example.android.bookstore.view.EditorCdActivity;

import java.util.List;

import static com.example.android.bookstore.data.Constants.CD_INTENT;

/**
 * Created by XXX on 05-Jun-18.
 */

public class CdRecyclerAdapter extends RecyclerView.Adapter<CdRecyclerAdapter.CdViewHolder> {

    private static List<Cd> bookList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class CdViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleTextView;
        public TextView priceTextView;

        public CdViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.title);
            priceTextView = v.findViewById(R.id.price);

            itemView.setOnClickListener(v1 -> {

                int position = getAdapterPosition();
                Cd cd = bookList.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext())
                        .setMessage("Title:  " + cd.getTitle() + "\n"
                                + "Artist:  " + cd.getArtist() + "\n"
                                + "Price:  " + cd.getPrices() + Constants.EURO_SIGN + "\n"
                                + "Quantity:  " + cd.getQuantity() + "\n"
                                + "Genre:  " + cd.getGenres());

                builder.setPositiveButton("Edit", (dialog, which) -> {

                    Intent intent = new Intent(itemView.getContext(), EditorCdActivity.class);
                    intent.putExtra(CD_INTENT, bookList.get(position).getId());
                    itemView.getContext().startActivity(intent);
                    dialog.dismiss();
                });

                builder.create().show();

            });

            itemView.setOnLongClickListener(v12 -> {

                int position = getAdapterPosition();
                Cd cd = bookList.get(position);
                cd.setSelection(!cd.isSelection());

                itemView.setBackgroundColor(cd.isSelection() ? Color.YELLOW : Color.WHITE);
                return true;
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CdRecyclerAdapter(List<Cd> books) {
        super();
        bookList = books;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public CdRecyclerAdapter.CdViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_item, parent, false);
        return new CdRecyclerAdapter.CdViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CdRecyclerAdapter.CdViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.titleTextView.setText(bookList.get(position).getTitle());
        holder.priceTextView.setText(holder.itemView.getContext().getResources().getString(R.string.price_euro, bookList.get(position).getPrices()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        if (bookList != null) {

            return bookList.size();
        } else {

            return 0;
        }
    }

}
