package com.example.android.bookstore.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "book_table")
public class Book implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_column")
    private int id;

    @ColumnInfo(name = "title_column")
    private String title;
    private String author;
    private long isbn;
    private double price;
    private String prices;
    private int quantity;
    private int cover;
    private String covers;
    private int genre;
    private String genres;

    private boolean selection = false;

    public Book (String title,
                 String author,
                 long isbn,
                 double price,
                 String prices,
                 int quantity,
                 int cover,
                 String covers,
                 int genre,
                 String genres) {

        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.prices = prices;
        this.quantity = quantity;
        this.cover = cover;
        this.genre = genre;
        this.covers = covers;
        this.genres = genres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int isCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public int getCover() {
        return cover;
    }

    public String getCovers() {
        return covers;
    }

    public void setCovers(String covers) {
        this.covers = covers;
    }

    public int getGenre() {
        return genre;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public boolean isSelection() {
        return selection;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }

    protected Book(Parcel in) {
        title = in.readString();
        author = in.readString();
        isbn = in.readLong();
        prices = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
        cover = in.readInt();
        covers = in.readString();
        genre = in.readInt();
        genres = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeLong(isbn);
        dest.writeString(prices);
        dest.writeDouble(price);
        dest.writeInt(quantity);
        dest.writeInt(cover);
        dest.writeString(covers);
        dest.writeInt(genre);
        dest.writeString(genres);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}