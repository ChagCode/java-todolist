package com.example.todolist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface NotesDao {
    @Query("SELECT * FROM notes")
    Single<List<Note>> getNotes(); // метод возвращает список всех заметок из БД (и не только)

    @Insert
    Completable add(Note note); // метод вставляет в БД

    @Query("DELETE FROM notes WHERE id=:id") // ставим : при обращении к передоваемому параметру
    Completable remove(int id); // метод удаляет из БД по id
}
 