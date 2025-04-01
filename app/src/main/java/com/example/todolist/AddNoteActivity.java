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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTextEnterNote;
    private RadioButton radioButtonGreen, radioButtonOrange;
    private Button buttonSaveNote;
    private AddNoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        viewModel = new ViewModelProvider(this).get(AddNoteViewModel.class);
        viewModel.getShouldCloseScreen().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean shouldClose) {
                if (shouldClose) {
                    finish();
                }
            }
        });
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
        viewModel.saveNode(note);

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