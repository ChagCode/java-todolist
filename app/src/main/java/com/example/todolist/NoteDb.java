package com.example.todolist;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDb extends RoomDatabase {
    private static NoteDb instance = null;
    private static final String DB_NAME = "notes.db";
    public static NoteDb getInstance(Application application) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    application,
                    NoteDb.class,
                    DB_NAME
            ).build();
        }
        return instance;
    }
    public abstract NotesDao notesDao(); // вернет экземпляр интерфейсного типа
}
