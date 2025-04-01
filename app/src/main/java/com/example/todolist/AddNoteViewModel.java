package com.example.todolist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class AddNoteViewModel extends AndroidViewModel {
    private NoteDb noteDb;
    private MutableLiveData<Boolean> shouldCloseScreen = new MutableLiveData<>();

    public AddNoteViewModel(@NonNull Application application) {
        super(application);
        noteDb = NoteDb.getInstance(application);
    }
    public LiveData<Boolean> getShouldCloseScreen() {
        return shouldCloseScreen;
    }
    public void saveNode(Note note) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                noteDb.notesDao().add(note);
                shouldCloseScreen.postValue(true);
            }
        });
        thread.start();
    }
    public LiveData<List<Note>> getNotes() {
        return noteDb.notesDao().getNotes();
    }
}