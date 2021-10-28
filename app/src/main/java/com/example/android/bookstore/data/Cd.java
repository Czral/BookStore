package com.example.android.bookstore.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cd_table")
public class Cd implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_column")
    public int id;

    @ColumnInfo(name = "title_column")
    private String title;
    private String artist;
    private double price;
    private String prices;
    private int quantity;
    private int genre;
    private String genres;

    private boolean selection = false;

    public Cd(String title, String artist, double price, String prices, int quantity, int genre, String genres) {
        this.title = title;
        this.price = price;
        this.prices = prices;
        this.quantity = quantity;
        this.artist = artist;
        this.genre = genre;
        this.genres = genres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
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

    protected Cd(Parcel in) {
        title = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
        artist = in.readString();
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
        dest.writeDouble(price);
        dest.writeInt(quantity);
        dest.writeString(artist);
        dest.writeInt(genre);
        dest.writeString(genres);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Cd> CREATOR = new Parcelable.Creator<Cd>() {
        @Override
        public Cd createFromParcel(Parcel in) {
            return new Cd(in);
        }

        @Override
        public Cd[] newArray(int size) {
            return new Cd[size];
        }
    };
}