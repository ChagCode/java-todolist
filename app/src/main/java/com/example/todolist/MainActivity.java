package com.example.todolist;

import android.content.Intent;
import android.database.DatabaseErrorHandler;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewNotes;
    private FloatingActionButton buttonAddNote;
    private NotesAdapter notesAdapter;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // чтобы наша модель могла пережить переворот экрана используем след. реализацию
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        initView();
        notesAdapter = new NotesAdapter();

        // для того, чтобы RecyclerView понимал, какой необходимо использовать адаптер выполним
        recyclerViewNotes.setAdapter(notesAdapter);

        // подпишемся на изменения в БД
        viewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                // в метод onChanged прилетают данные и мы их устанавливаем в Адаптер
                notesAdapter.setNotes(notes);
            }
        });

        // для удаления свайпом необходимо
        // ItemTouchHelper.RIGHT | "<-это 'или'" ItemTouchHelper.LEFT - для определения свайпа
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target
            ) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                 int direction) {
                int position = viewHolder.getAdapterPosition();
                // add getter getNotes() и получем объект по которому будет произведен свайп
                Note note = notesAdapter.getNotes().get(position);

                // remove note
                viewModel.remove(note);
            }
        }
        );

        // теперь приклепляем к recyclerView свайп
        itemTouchHelper.attachToRecyclerView(recyclerViewNotes);

        // клик - добавление новой заметки (переход на AddNoteActivity)
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddNoteActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }
    // когда activity получает фокус


    private void initView() {
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        buttonAddNote = findViewById(R.id.buttonAddNote);
    }
}