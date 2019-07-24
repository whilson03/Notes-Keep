package com.olabode.wilson.notekeep.views;


import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.android.material.snackbar.Snackbar;
import com.olabode.wilson.notekeep.BottomSheetFragment;
import com.olabode.wilson.notekeep.R;
import com.olabode.wilson.notekeep.adapters.NoteAdapter;
import com.olabode.wilson.notekeep.models.Note;
import com.olabode.wilson.notekeep.viewmodels.NoteViewModel;

import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {

    private static final int ADD_NOTE_REQUEST = 1;
    private static final int EDIT_NOTE_REQUEST = 2;
    //private static final String TAG = NotesFragment.class.getSimpleName();

    private NoteViewModel noteViewModel;
    private NoteAdapter adapter;

    private Drawable icon;
    private ColorDrawable background;


    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        setHasOptionsMenu(true);


        //handle icon and background for swipe to delete layout
        icon = ContextCompat.getDrawable(Objects.requireNonNull(getActivity()),
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new NoteAdapter(getContext());
        recyclerView.setAdapter(adapter);


        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter.submitList(notes);
            }
        }); 


        /*
          handles swipe to delete recycler view
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
                        final Note note = adapter.getNoteAt(viewHolder.getAdapterPosition());
                        noteViewModel.delete(note);

                        noteViewModel.addToTrash(note);

                        Snackbar.make(viewHolder.itemView, "Undo Move To Trash", Snackbar.LENGTH_SHORT)
                                .setAction("UND0", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        noteViewModel.insert(note);
                                        noteViewModel.removeFromTrash(note);
                                    }
                                }).show();
                        break;


                    case ItemTouchHelper.RIGHT:
                        final Note note1 = adapter.getNoteAt(viewHolder.getAdapterPosition());
                        noteViewModel.delete(note1);

                        noteViewModel.addToTrash(note1);

                        Snackbar.make(viewHolder.itemView, "Undo Move To Trash", Snackbar.LENGTH_SHORT)
                                .setAction("UND0", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        noteViewModel.insert(note1);
                                        noteViewModel.removeFromTrash(note1);
                                    }
                                }).show();
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


        // Toggle for favourite
        adapter.setOnToggleListener(new NoteAdapter.OnToggleListener() {
            @Override
            public void onItemToggle(Note note, boolean isChecked) {
                if (isChecked) {
                    note.setIsFavourite(1);
                    noteViewModel.addToFavourite(note);
                } else {
                    note.setIsFavourite(0);
                    noteViewModel.removeFromFavourite(note);
                }
            }
        });

        // long click event
        adapter.setLongListener(new NoteAdapter.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(Note note) {
                showBottomSheetDialogFragment(note);
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

        } else {

            Toast.makeText(getActivity(), "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_notes_frag, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                confirmDeleteDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Notes");
    }


    /**
     * inflate and communicate with the bottom sheet fragment via an interface
     */

    private void showBottomSheetDialogFragment(final Note note) {

        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        assert getFragmentManager() != null;
        bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());

        bottomSheetFragment.setOnBottomSheetClickListener(new BottomSheetFragment.BottomSheetListener() {
            @Override
            public void onButtonClicked(int id) {
                switch (id) {
                    case R.id.bottom_sheet_delete:
                        noteViewModel.addToTrash(note);
                        Toast.makeText(getContext(), "Moved To Trash", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.bottom_sheet_copy:
                        copyToClipBoard(note);
                        Toast.makeText(getContext(), "Copied To Clipboard", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.bottom_sheet_share:
                        shareNote(note);
                        break;
                }
            }
        });
    }


    /**
     * helper function to copy note to clip board
     */

    private void copyToClipBoard(@NonNull Note note) {
        String body = note.getTitle() + "\n" + note.getBody();
        ClipboardManager clipboard = (ClipboardManager) Objects.
                requireNonNull(getActivity()).getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText(note.getTitle(), body);

        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
    }


    private void shareNote(@NonNull Note note) {
        String shareBody = note.getTitle() + "\n" + note.getBody();
        String subject = note.getTitle();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
    }


    private void confirmDeleteDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete All Notes")
                .setMessage("Are you sure want to delete all notes ?\n" +
                        "Notes will all be moved to Trash")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Moving To Trash..", Toast.LENGTH_SHORT).show();
                        noteViewModel.moveAllNotesToTrash();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }


}
