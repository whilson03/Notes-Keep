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

    public NoteRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDao = noteDatabase.noteDao();
        allNotes = noteDao.getAllNotes();
        allFavouriteNotes = noteDao.getAllFavouriteNotes();
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


}
