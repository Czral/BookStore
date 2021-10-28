package com.example.android.bookstore.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.android.bookstore.data.Book;
import com.example.android.bookstore.data.Cd;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Book.class, Cd.class}, version = 1, exportSchema = false)
public abstract class StoreDatabase extends RoomDatabase {

    public abstract StoreDao storeDao();

    private static volatile StoreDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static StoreDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (StoreDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            StoreDatabase.class, "store_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
