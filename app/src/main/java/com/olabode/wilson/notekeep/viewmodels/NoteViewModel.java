package com.olabode.wilson.notekeep.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.olabode.wilson.notekeep.models.Note;
import com.olabode.wilson.notekeep.repository.NoteRepository;

import java.util.List;

/**
 * Created by OLABODE WILSON on 2019-06-26.
 */
public class NoteViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;
    private LiveData<List<Note>> allFavourites;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
        allFavourites = repository.getAllFavouriteNotes();
    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public void addToFavourite(Note note) {
        repository.addNoteToFavourite(note);
    }

    public void removeFromFavourite(Note note) {
        repository.removeNoteFromFavourite(note);

    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<Note>> getAllFavouriteNotes() {
        return allFavourites;
    }
}
