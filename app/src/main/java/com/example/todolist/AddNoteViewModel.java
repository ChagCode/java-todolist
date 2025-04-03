package com.example.todolist;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddNoteViewModel extends AndroidViewModel {
    private NoteDb noteDb;
    private MutableLiveData<Boolean> shouldCloseScreen = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable(); // создаем коллекцию всех подписок

    public AddNoteViewModel(@NonNull Application application) {
        super(application);
        noteDb = NoteDb.getInstance(application);
    }

    public LiveData<Boolean> getShouldCloseScreen() {
        return shouldCloseScreen;
    }

    public void saveNode(Note note) {
        Disposable disposable = noteDb.notesDao().add(note)
                // subscribeOn - перевод сохранение в фоновый поток
                .subscribeOn(Schedulers.io())
                // после сохранения переключаемся на главный поток - .observeOn()
                // и все остальные действия происходят на главном потоке
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        //  используется для закрытия текущего экрана после успешного сохранения
                        shouldCloseScreen.setValue(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d("MainViewModel", "Error saveNode");
                    }
                });
        compositeDisposable.add(disposable);
    }

    // после использования метода saveNode() необходимо завершить подписку (от утечек памяти)
    // напрмер в случае уничтожения viewModel
    // вызывается метод onCleared()
    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}