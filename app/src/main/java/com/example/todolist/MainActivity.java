package com.example.todolist;

import android.content.Intent;
import android.database.DatabaseErrorHandler;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linearLayoutNotes;
    private FloatingActionButton buttonAddNote;
    private Database database = Database.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        // клик - добавление новой заметки (переход на AddNoteActivity)
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddNoteActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }
    // короче. при добалении новой заметки, начальный экран не обновляется.
    // поэтому нужно делать так!
    @Override
    protected void onResume() {
        super.onResume();
        showNotes();
    }
    private void showNotes() {
        // для обнуления экрана - чтоб не было дубляжа
        linearLayoutNotes.removeAllViews();
        // для каждой заметки создадим view и вставим в макет
        for (Note note : database.getNotes()) {
            // add "note_item.xml" in "linearLayoutNotes".
            // Но для начала необходимо "note_item.xml" преобразовать в view элемент.
            View view = getLayoutInflater().inflate(
                    R.layout.note_item,
                    // linearLayoutNotes - контейнер в который будет вложен элемент
                    linearLayoutNotes,
                    false
            );
            // add TEXT in notes, т.е. чтобы работать с view элементом необходимо, его найти
            // view.findViewById - !обрати внимание!, что id ищем в view, а не в основном макете
            TextView textViewNote = view.findViewById(R.id.textViewNote);
            textViewNote.setText(note.getText());

            // add background in note
            int colorResId;
            switch (note.getPriority()) {
                case 0:
                    colorResId = android.R.color.holo_green_light;
                    break;
                case 1:
                    colorResId = android.R.color.holo_orange_light;
                    break;
                default:
                    colorResId = android.R.color.holo_red_light;
            }
            int color = ContextCompat.getColor(this, colorResId);
            textViewNote.setBackgroundColor(color);

            linearLayoutNotes.addView(view);
        }
    }

    private void initView() {
        linearLayoutNotes = findViewById(R.id.linearLayoutNotes);
        buttonAddNote = findViewById(R.id.buttonAddNote);
    }
}