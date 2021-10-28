package com.example.android.bookstore.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.bookstore.data.Book;
import com.example.android.bookstore.data.Cd;

import java.util.List;

public class StoreViewModel extends AndroidViewModel {

    private final StoreRepository repository;
    private LiveData<List<Book>> books;
    private LiveData<List<Cd>> cds;

    public StoreViewModel(Application application) {
        super(application);
        repository = new StoreRepository(application);
        books = repository.getAllBooks();
        cds = repository.getAllCds();
    }

    public LiveData<List<Book>> getAllBooks() {
        return books;
    }

    public void insertBook(Book book) {
        repository.insertBook(book);
    }

    // Not used
    public void deleteAllBooks(List<Book> books) {
        repository.deleteAllBooks(books);
    }

    // Not used
    public void deleteAllCds(List<Cd> cds) {
        repository.deleteAllCds(cds);
    }

    public LiveData<List<Cd>> getAllCds() {
        return cds;
    }

    public void insertCd(Cd cd) {
        repository.insertCd(cd);
    }

    public void deleteCds(List<Integer> cds) {
        repository.deleteCds(cds);
    }

    public void deleteBooks(List<Integer> ids) {
        repository.deleteBooks(ids);
    }

    public LiveData<Book> getBook(int id) {
        return repository.getBook(id);
    }

    public LiveData<Cd> getCd(int id) {return  repository.getCd(id);}
}
