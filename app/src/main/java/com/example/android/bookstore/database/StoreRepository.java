package com.example.android.bookstore.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.android.bookstore.data.Book;
import com.example.android.bookstore.data.Cd;

import java.util.List;

public class StoreRepository {

    private StoreDao storeDao;
    private LiveData<List<Book>> books;
    private LiveData<List<Cd>> cds;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    StoreRepository(Application application) {
        StoreDatabase db = StoreDatabase.getDatabase(application);
        storeDao = db.storeDao();
        books = storeDao.getAllBooks();
        cds = storeDao.getAllCds();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Book>> getAllBooks() {
        return books;
    }

    public LiveData<List<Cd>> getAllCds() {
        return cds;
    }

    public void insertCd(Cd cd) {

        StoreDatabase.databaseWriteExecutor.execute(() -> storeDao.insertCd(cd));
    }

    public LiveData<Book> getBook(int id) {

        return storeDao.getBook(id);
    }

    public LiveData<Cd> getCd(int id) {

        return storeDao.getCd(id);
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insertBook(Book book) {
        StoreDatabase.databaseWriteExecutor.execute(() -> storeDao.insertBook(book));
    }

    // Not used
    public void deleteAllBooks(List<Book> books) {

        StoreDatabase.databaseWriteExecutor.execute(() -> storeDao.deleteAllBooks(books));
    }

    // Not used
    public void deleteAllCds(List<Cd> cds) {

        StoreDatabase.databaseWriteExecutor.execute(() -> storeDao.deleteAllCds(cds));
    }

    void deleteBooks(List<Integer> ids) {

        StoreDatabase.databaseWriteExecutor.execute(() -> storeDao.deleteBooks(ids));
    }

    void deleteCds(List<Integer> ids) {

        StoreDatabase.databaseWriteExecutor.execute(() -> storeDao.deleteCds(ids));
    }
}
