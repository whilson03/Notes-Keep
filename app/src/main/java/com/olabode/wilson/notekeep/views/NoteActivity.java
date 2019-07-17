
package com.olabode.wilson.notekeep.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.olabode.wilson.notekeep.R;
import com.olabode.wilson.notekeep.utils.Constants;
import com.olabode.wilson.notekeep.utils.HelperMethods;

import java.util.Objects;

public class NoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_DESCRIPTION = "description";
    public static final String EXTRA_DATE = "time_stamp";
    private static final String TAG = NoteActivity.class.getSimpleName();

    private EditText editTextTitle;
    private EditText editTextDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);


        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);


        // add the close icon from the activity.
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle(R.string.title_edit);
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
        } else {
            setTitle("Add Note");
        }
        setFont();

    }


    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String timeStamp = HelperMethods.getDate();


        if (title.trim().isEmpty() && description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        if (title.trim().isEmpty() && !description.trim().isEmpty()) {
            title = "No title";
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_DATE, timeStamp);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_note) {
            saveNote();
            return true;
        } else if (item.getItemId() == R.id.change_color) {
            Toast.makeText(this, "Change note colour", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveDraft();
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
    }


    private void saveDraft() {
        Log.i(TAG, "entering into save draft method");

        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String timeStamp = HelperMethods.getDate();


        if (description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!description.isEmpty() && title.isEmpty()) {
            Log.i(TAG, "save draft empty title");

            Intent data = new Intent();
            data.putExtra(EXTRA_TITLE, "No Title");
            data.putExtra(EXTRA_DESCRIPTION, description);
            data.putExtra(EXTRA_DATE, timeStamp);

            setResult(RESULT_CANCELED, data);
            finish();

        } else {
            Intent data = new Intent();
            data.putExtra(EXTRA_TITLE, title);
            data.putExtra(EXTRA_DESCRIPTION, description);
            data.putExtra(EXTRA_DATE, timeStamp);

            setResult(RESULT_CANCELED, data);
            finish();

        }
    }


    private void setFont() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(NoteActivity.this);
        int fontSize = (int) Float.parseFloat(preferences.getString(Constants.SharedPreferenceKeys.FONT_PREFERENCE_KEY, "20"));
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

