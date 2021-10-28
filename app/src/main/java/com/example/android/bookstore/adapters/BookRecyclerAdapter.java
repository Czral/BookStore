package com.example.android.bookstore.adapters;

import static com.example.android.bookstore.data.Constants.BOOK_INTENT;

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
import com.example.android.bookstore.data.Book;
import com.example.android.bookstore.data.Constants;
import com.example.android.bookstore.view.EditorBookActivity;

import java.util.List;

/**
 * Created by XXX on 13-Jun-18.
 */

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.BookViewHolder> {

    private static List<Book> bookList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class BookViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView priceTextView;

        public BookViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.title);
            priceTextView = v.findViewById(R.id.price);


            itemView.setOnClickListener(v1 -> {

                int position = getAdapterPosition();
                Book book = bookList.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext())
                        .setMessage("Title:  " + book.getTitle() + "\n"
                                + "Artist:  " + book.getAuthor() + "\n"
                                + "Price:  " + book.getPrices() + Constants.EURO_SIGN + "\n"
                                + "Quantity:  " + book.getQuantity() + "\n"
                                + "Cover type:  " + book.getCovers() + "\n"
                                + "Genre:   " + book.getGenres());

                builder.setPositiveButton("Edit", (dialog, which) -> {

                    Intent intent = new Intent(itemView.getContext(), EditorBookActivity.class);
                    intent.putExtra(BOOK_INTENT, bookList.get(position).getId());
                    itemView.getContext().startActivity(intent);
                    dialog.dismiss();

                });

                builder.create().show();

            });

            itemView.setOnLongClickListener(v12 -> {

                int position = getAdapterPosition();
                Book book = bookList.get(position);
                book.setSelection(!book.isSelection());

                itemView.setBackgroundColor(book.isSelection() ? Color.GREEN : Color.WHITE);
                return true;
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BookRecyclerAdapter(List<Book> books) {
        super();
        bookList = books;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public BookRecyclerAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // Create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_item, parent, false);
        return new BookViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {

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
