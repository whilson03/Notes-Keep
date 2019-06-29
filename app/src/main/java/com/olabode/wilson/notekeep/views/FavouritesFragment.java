package com.olabode.wilson.notekeep.views;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.olabode.wilson.notekeep.R;
import com.olabode.wilson.notekeep.adapters.NoteAdapter;
import com.olabode.wilson.notekeep.models.Note;
import com.olabode.wilson.notekeep.viewmodels.NoteViewModel;

import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment {

    public static final int EDIT_NOTE_REQUEST = 4;
    private static final int ADD_NOTE_REQUEST = 5;
    private static final String TAG = FavouritesFragment.class.getSimpleName();
    private NoteAdapter mNoteAdapter;
    private NoteViewModel noteViewModel;


    public FavouritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        mNoteAdapter = new NoteAdapter();
        recyclerView.setAdapter(mNoteAdapter);


        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllFavouriteNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                mNoteAdapter.submitList(notes);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT |
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        noteViewModel.delete(mNoteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                        break;
                    case ItemTouchHelper.RIGHT:
                        Note note = mNoteAdapter.getNoteAt(viewHolder.getAdapterPosition());
                        noteViewModel.delete(note);
                        break;
                }
            }
        }).attachToRecyclerView(recyclerView);

        mNoteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(getActivity(), NoteActivity.class);
                intent.putExtra(NoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(NoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(NoteActivity.EXTRA_DESCRIPTION, note.getBody());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });


        mNoteAdapter.setTlistener(new NoteAdapter.ToggleListener() {
            @Override
            public void onItemToggle(Note note, boolean isChecked) {
                if (!isChecked) {
                    Toast.makeText(getContext(), "Removed from Favourite", Toast.LENGTH_SHORT).show();
                    note.setIsFavourite(0);
                    noteViewModel.removeFromFavourite(note);
                }
            }
        });


        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(NoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(NoteActivity.EXTRA_DESCRIPTION);
            String timeStamp = data.getStringExtra(NoteActivity.EXTRA_DATE);

            Note note = new Note(title, description, timeStamp);
            noteViewModel.insert(note);
            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();


        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(NoteActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(getContext(), "Note Can't be Updated", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
            String title = data.getStringExtra(NoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(NoteActivity.EXTRA_DESCRIPTION);
            String timeStamp = data.getStringExtra(NoteActivity.EXTRA_DATE);

            Note note = new Note(title, description, timeStamp);
            note.setId(id);
            note.setIsFavourite(1);
            noteViewModel.update(note);

        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_CANCELED) {
            Log.i(TAG, "i was cancelled");

            Toast.makeText(getContext(), "Saved To Draft", Toast.LENGTH_SHORT).show();
            int id = data.getIntExtra(NoteActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(getContext(), "Note Can't be Updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(NoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(NoteActivity.EXTRA_DESCRIPTION);
            String timeStamp = data.getStringExtra(NoteActivity.EXTRA_DATE);

            Note note = new Note(title, description, timeStamp);
            note.setId(id);
            note.setIsFavourite(1);
            noteViewModel.update(note);


        } else {
            Toast.makeText(getActivity(), "Note not saved", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Favourites");
    }

}
