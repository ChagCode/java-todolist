package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTextEnterNote;
    private RadioButton radioButtonGreen, radioButtonOrange;
    private Button buttonSaveNote;
    private NoteDb noteDb;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        noteDb = NoteDb.getInstance(getApplication());
        initView();
        buttonSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
    }

    private int getPriority() {
        int priority;
        if (radioButtonGreen.isChecked()) {
            priority = 0;
        } else if (radioButtonOrange.isChecked()) {
            priority = 1;
        } else {
            priority = 2;
        }
        return priority;
    }

    private void saveNote() {
        String text = editTextEnterNote.getText().toString().trim();
        int priority = getPriority();
        Note note = new Note(text, priority);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                noteDb.notesDao().add(note);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // чтобы закончить работу AddNoteActivity необходимо вызвать
                        // в главном потоке
                        finish();
                    }
                });
            }
        });
        thread.start();
    }

    private void initView() {
        editTextEnterNote = findViewById(R.id.editTextEnterNote);
        radioButtonGreen = findViewById(R.id.radioButtonGreen);
        radioButtonOrange = findViewById(R.id.radioButtonOrange);
        buttonSaveNote = findViewById(R.id.buttonSaveNote);
    }
    public static Intent newIntent(Context context) {
        return new Intent(context, AddNoteActivity.class);
    }
}