package com.olabode.wilson.notekeep.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.olabode.wilson.notekeep.models.Note;

import java.util.List;

/**
 * Created by OLABODE WILSON on 2019-06-26.
 */

@Dao
public interface NoteDao {


    @Insert
    void insert(Note note);

    @Delete
    void delete(Note note);

    @Update
    void update(Note note);

    @Query("DELETE FROM notes_table")
    void deleteAllNotes();

    @Query("SELECT * FROM notes_table")
    LiveData<List<Note>> getAllNotes();

//    @Query("SELECT * FROM notes_table WHERE body like % string %")
//    List<Note> searchNote(String string);


}