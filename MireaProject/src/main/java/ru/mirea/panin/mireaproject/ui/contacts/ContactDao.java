package ru.mirea.panin.mireaproject.ui.contacts;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contact")
    LiveData<List<Contact>> getAll();
    @Query("SELECT * FROM contact WHERE id = :id")
    Contact getById(long id);
    @Insert
    void insert(Contact employee);
    @Update
    void update(Contact employee);
    @Delete
    void delete(Contact employee);
}
