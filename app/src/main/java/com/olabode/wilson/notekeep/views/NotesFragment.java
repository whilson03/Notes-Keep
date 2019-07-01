package com.olabode.wilson.notekeep.views;


import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
public class NotesFragment extends Fragment {

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    private NoteViewModel noteViewModel;
    private NoteAdapter adapter;

    private Drawable icon;
    private ColorDrawable background;


    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);


        icon = ContextCompat.getDrawable(getActivity(),
                R.drawable.ic_trash);
        background = new ColorDrawable(Color.RED);


        FloatingActionButton buttonAddNote = rootView.findViewById(R.id.button_add_notes);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });


        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);


        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter.submitList(notes);
            }
        });


        /**
         * handles swipe to delete recycler view
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT |
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder
                    viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20; //so background is behind the rounded corners of itemView

                int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                int iconBottom = iconTop + icon.getIntrinsicHeight();

                if (dX > 0) { // Swiping to the right
                    int iconLeft = itemView.getLeft() + iconMargin;
                    int iconRight = iconLeft + icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    background.setBounds(itemView.getLeft(), itemView.getTop(),
                            itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
                } else if (dX < 0) { // Swiping to the left
                    int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                    int iconRight = itemView.getRight() - iconMargin;
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                            itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else { // view is unSwiped
                    background.setBounds(0, 0, 0, 0);
                }

                background.draw(c);
                icon.draw(c);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                        break;
                    case ItemTouchHelper.RIGHT:
                        Note note = adapter.getNoteAt(viewHolder.getAdapterPosition());
                        noteViewModel.delete(note);
                        break;

                }
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(getActivity(), NoteActivity.class);
                intent.putExtra(NoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(NoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(NoteActivity.EXTRA_DESCRIPTION, note.getBody());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });

        adapter.setTlistener(new NoteAdapter.ToggleListener() {
            @Override
            public void onItemToggle(Note note, boolean isChecked) {
                if (isChecked) {
//                    Toast.makeText(getContext(), "Added to Favourite", Toast.LENGTH_SHORT).show();
                    note.setIsFavourite(1);
                    noteViewModel.addToFavourite(note);
                } else {
//                    Toast.makeText(getContext(), "Removed from Favourites", Toast.LENGTH_SHORT).show();
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
            noteViewModel.update(note);

        } else if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_CANCELED) {
            Toast.makeText(getContext(), "Saved To Draft", Toast.LENGTH_SHORT).show();

            String title = data.getStringExtra(NoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(NoteActivity.EXTRA_DESCRIPTION);
            String timeStamp = data.getStringExtra(NoteActivity.EXTRA_DATE);

            Note note = new Note(title, description, timeStamp);
            noteViewModel.insert(note);


        } else {
            Toast.makeText(getActivity(), "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Notes");
    }


}
