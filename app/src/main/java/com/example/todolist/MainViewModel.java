package com.example.todolist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private NoteDb noteDb;
    public MainViewModel(@NonNull Application application) {
        super(application);
        noteDb = NoteDb.getInstance(application);
    }
    public LiveData<List<Note>> getNotes() {
        return noteDb.notesDao().getNotes();
    }
    public void remove(Note note) {
        // во всех местах, где работаем с БД. Выносим работу в отдельный поток.
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                noteDb.notesDao().remove(note.getId());
            }
        });
        thread.start();
    }
}
