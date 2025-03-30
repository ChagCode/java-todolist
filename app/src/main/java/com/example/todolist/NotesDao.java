package com.example.todolist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotesDao {
    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getNotes(); // метод возвращает список всех заметок из БД

    @Insert
    void add(Note note); // метод вставляет в БД

    @Query("DELETE FROM notes WHERE id=:id") // ставим : при обращении к передоваемому параметру
    void remove(int id); // метод удаляет из БД по id
}
