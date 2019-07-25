package com.olabode.wilson.notekeep.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.olabode.wilson.notekeep.R;
import com.olabode.wilson.notekeep.models.Note;
import com.olabode.wilson.notekeep.utils.Constants;
import com.olabode.wilson.notekeep.viewmodels.NoteViewModel;

public class ViewTrashNoteActivity extends AppCompatActivity {

    private LinearLayout mNoteLayout;
    private NoteViewModel noteViewModel;
    private int id;
    private String title;
    private String body;
    private String date;
    private EditText editTextTitle;
    private EditText editTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trash_note);

        mNoteLayout = findViewById(R.id.trash_view_note_layout);
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);


        setFont();

        editTextDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("clicked ", " clicked");
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


    private void setFont() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ViewTrashNoteActivity.this);
        int fontSize = (int) Float.parseFloat(preferences.
                getString(Constants.SharedPreferenceKeys.FONT_PREFERENCE_KEY, "20"));
        switch (fontSize) {
            case 14:
                setEditTextSize(fontSize);
                break;
            case 20:
                setEditTextSize(fontSize);
                break;
            case 24:
                setEditTextSize(fontSize);
                break;
            case 28:
                setEditTextSize(fontSize);
                break;
        }
    }

    /**
     * increase edit text size dynamically based on the specified size.
     *
     * @param size
     */
    private void setEditTextSize(float size) {
        editTextDescription.setTextSize(size);
        editTextTitle.setTextSize(size);

    }
}
