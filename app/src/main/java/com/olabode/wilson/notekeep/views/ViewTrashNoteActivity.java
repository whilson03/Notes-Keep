package com.olabode.wilson.notekeep.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.olabode.wilson.notekeep.R;
import com.olabode.wilson.notekeep.models.Note;
import com.olabode.wilson.notekeep.viewmodels.NoteViewModel;

public class ViewTrashNoteActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    private int id;
    private String title;
    private String body;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trash_note);


        EditText editTextTitle = findViewById(R.id.edit_text_title);
        EditText editTextDescription = findViewById(R.id.edit_text_description);

        editTextDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewTrashNoteActivity.this, "NOTE CANNOT BE EDITED IN TRASH", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        editTextTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewTrashNoteActivity.this, "NOTE CANNOT BE EDITED IN TRASH", Toast.LENGTH_SHORT)
                        .show();
            }
        });


        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");
        body = intent.getStringExtra("description");
        date = intent.getStringExtra("date");


        editTextTitle.setText(title);
        editTextDescription.setText(body);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_view_trash_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_permanently:
                Note note = new Note(title, body, date);
                note.setId(id);
                noteViewModel.delete(note);
                finish();
                break;

            case R.id.action_restore:
                Note noteToRestore = new Note(title, body, date);
                noteToRestore.setId(id);
                noteViewModel.insert(noteToRestore);
                finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
