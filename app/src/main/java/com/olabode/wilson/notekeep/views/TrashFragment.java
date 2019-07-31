package com.olabode.wilson.notekeep.views;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.olabode.wilson.notekeep.R;
import com.olabode.wilson.notekeep.TrashBottomSheet;
import com.olabode.wilson.notekeep.adapters.NoteAdapter;
import com.olabode.wilson.notekeep.models.Note;
import com.olabode.wilson.notekeep.viewmodels.NoteViewModel;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrashFragment extends Fragment {

    private NoteViewModel noteViewModel;
    private NoteAdapter adapter;


    public TrashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_trash, container, false);
        setHasOptionsMenu(true);


        final ImageView emptyTrashIcon = rootView.findViewById(R.id.empty_trash_table_icon);


        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new NoteAdapter(getContext());
        recyclerView.setAdapter(adapter);


        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllTrashNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter.submitList(notes);
            }
        });


        noteViewModel.getTrashCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer > 0) {
                    emptyTrashIcon.setVisibility(View.INVISIBLE);


                } else {
                    emptyTrashIcon.setVisibility(View.VISIBLE);
                }
            }
        });


        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(getActivity(), ViewTrashNoteActivity.class);
                intent.putExtra("id", note.getId());
                intent.putExtra("title", note.getTitle());
                intent.putExtra("description", note.getBody());
                intent.putExtra("date", note.getTimeStamp());
                startActivity(intent);
            }
        });


        adapter.setLongListener(new NoteAdapter.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(Note note) {
                showBottomSheet(note);
            }
        });


        return rootView;
    }

    private void showBottomSheet(final Note note) {
        TrashBottomSheet bottomSheet = new TrashBottomSheet();
        assert getFragmentManager() != null;
        bottomSheet.show(getFragmentManager(), bottomSheet.getTag());

        bottomSheet.setOnBottomSheetItemClickedListener(new TrashBottomSheet.BottomSheetListener() {
            @Override
            public void onBottomSheetItemClicked(int id) {
                switch (id) {
                    case R.id.restore_note:
                        // res= insert note after deleting
                        note.setIsMovedToTrash(0);
                        noteViewModel.update(note);
                        Toast.makeText(getContext(), "Restored", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.delete_note:
                        noteViewModel.delete(note);
                        Toast.makeText(getContext(), "Deleting", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Trash");
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_trash_frag, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_all_from_trash) {
            confirmDeleteDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmDeleteDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Empty Trash")
                .setMessage("Are you sure want to empty trash ?\n" +
                        "Notes will be deleted permanently")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Emptying Trash...", Toast.LENGTH_SHORT).show();
                        noteViewModel.emptyTrash();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

}
