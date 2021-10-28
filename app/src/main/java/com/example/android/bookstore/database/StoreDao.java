package com.example.android.bookstore.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.android.bookstore.data.Book;
import com.example.android.bookstore.data.Cd;

import java.util.List;

@Dao
public interface StoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBook(Book book);

    @Query("SELECT * FROM book_table ORDER BY title_column ASC")
    LiveData<List<Book>> getAllBooks();

    @Query("SELECT * FROM book_table WHERE id_column =:id")
    LiveData<Book> getBook(int id);

    @Query("SELECT * FROM cd_table WHERE id_column =:id")
    LiveData<Cd> getCd(int id);

    // Not used
    @Delete()
    void deleteAllBooks(List<Book> books);

    @Query("DELETE FROM book_table WHERE id_column IN (:idList)")
    void deleteBooks(List<Integer> idList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCd(Cd cd);

    @Query("SELECT * FROM cd_table ORDER BY title_column ASC")
    LiveData<List<Cd>> getAllCds();

    // Not used
    @Delete()
    void deleteAllCds(List<Cd> cds);

    @Query("DELETE FROM cd_table WHERE id_column IN (:idList)")
    void deleteCds(List<Integer> idList);
}
