package com.olabode.wilson.notekeep.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.olabode.wilson.notekeep.database.NoteDao;
import com.olabode.wilson.notekeep.database.NoteDatabase;
import com.olabode.wilson.notekeep.models.Note;

import java.util.List;

/**
 * Created by OLABODE WILSON on 2019-06-26.
 */
public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;
    private LiveData<List<Note>> allFavouriteNotes;
    private LiveData<List<Note>> allFavouriteNotesFromTrash;

    // liva data object to observe the numbers of all this fields
    private LiveData<Integer> noteCount;
    private LiveData<Integer> favouriteCount;
    private LiveData<Integer> trashCount;


    public NoteRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDao = noteDatabase.noteDao();
        allNotes = noteDao.getAllNotes();
        allFavouriteNotes = noteDao.getAllFavouriteNotes();
        allFavouriteNotesFromTrash = noteDao.getAllNotesFromTrash();
        //

        noteCount = noteDao.checkNoteTable();
        favouriteCount = noteDao.checkFavouriteNotes();
        trashCount = noteDao.checkTrashNotes();

    }


    public void insert(Note note) {
        new insertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(Note note) {
        new updateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(Note note) {
        new deleteNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(noteDao).execute();
    }

    public void addNoteToFavourite(Note note) {
        new addToFavouritesAsyncTask(noteDao).execute(note);
    }

    public void removeNoteFromFavourite(Note note) {
        new removeFavouritesAsyncTask(noteDao).execute(note);
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<Note>> getAllFavouriteNotes() {
        return allFavouriteNotes;
    }


    public LiveData<Integer> getFavouriteCount() {
        return favouriteCount;
    }

    public LiveData<Integer> getNoteCount() {
        return noteCount;
    }

    public LiveData<Integer> getTrashCount() {
        return trashCount;
    }

    public void emptyFavourite() {
        new DeleteAllFavouriteAsyncTask(noteDao).execute();
    }


    /**
     * add to trash by changing the isTrash  value from zero to 1
     *
     * @param note
     */
    public void addToTrash(Note note) {
        new AddNoteToTrashAsyncTask(noteDao).execute(note);
    }

    public void removeFromTrash(Note note) {
        new RemoveNoteFromTrashAsyncTask(noteDao).execute(note);
    }

    public void undoDeleteFavourite(Note note) {
        new UndoDeleteFavAsyncTask(noteDao).execute(note);
    }


    public LiveData<List<Note>> getAllNotesFromTrash() {
        return allFavouriteNotesFromTrash;
    }

    public void emptyTrash() {
        new EmptyTrashAsyncTask(noteDao).execute();
    }







    /***
     * Database CRUD AsyncTasks
     */


    private static class insertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        public insertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class updateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        public updateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class addToFavouritesAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        public addToFavouritesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.addToFavourite(notes[0]);
            return null;
        }
    }


    public static class removeFavouritesAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        public removeFavouritesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.removeFavouriteNotes(notes[0]);
            return null;
        }
    }




    private static class deleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        public deleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }


    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        private DeleteAllNotesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }

    private static class AddNoteToTrashAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        public AddNoteToTrashAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            Note note = notes[0];
            note.setIsMovedToTrash(1);
            note.setIsFavourite(0);
            noteDao.insert(note);
            return null;
        }
    }


    private static class EmptyTrashAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        public EmptyTrashAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.deleteAllFromTrash();
            return null;
        }
    }



    private static class RemoveNoteFromTrashAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        public RemoveNoteFromTrashAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            Note note = notes[0];
            note.setIsMovedToTrash(0);
            note.setIsFavourite(0);
            noteDao.insert(note);
            return null;
        }
    }


    private static class UndoDeleteFavAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        public UndoDeleteFavAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            Note note = notes[0];
            note.setIsMovedToTrash(0);
            note.setIsFavourite(1);
            noteDao.insert(note);
            return null;
        }
    }


    private static class DeleteAllFavouriteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        public DeleteAllFavouriteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;

        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.deleteAllfavouriteList();
            return null;
        }
    }




}
