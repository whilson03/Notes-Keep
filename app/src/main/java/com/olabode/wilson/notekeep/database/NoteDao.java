package com.olabode.wilson.notekeep.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.olabode.wilson.notekeep.models.Note;

import java.util.List;

/**
 * Created by OLABODE WILSON on 2019-06-26.
 */

@Dao
public interface NoteDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);

    @Delete
    void delete(Note note);

    @Update
    void update(Note note);

    @Query("UPDATE NOTES_TABLE SET trash = 1, favourite = 0")
    void deleteAllNotes();

    @Query("SELECT * FROM notes_table WHERE trash == 0  ORDER BY ID DESC ")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes_table WHERE favourite == 1 ")
    LiveData<List<Note>> getAllFavouriteNotes();

    @Query("SELECT * FROM notes_table WHERE trash == 1")
    LiveData<List<Note>> getAllNotesFromTrash();

    @Update
    void addToFavourite(Note note);

    @Update
    void removeFavouriteNotes(Note note);

    @Query("UPDATE NOTES_TABLE SET trash = 1, favourite = 0 WHERE favourite == 1")
    void deleteAllfavouriteList();


    @Query("DELETE  FROM notes_table WHERE trash == 1")
    void deleteAllFromTrash();


    @Query("SELECT COUNT(id) FROM NOTES_TABLE WHERE trash = 0")
    LiveData<Integer> checkNoteTable();


    @Query("SELECT COUNT(id) FROM NOTES_TABLE WHERE favourite =  1 ")
    LiveData<Integer> checkFavouriteNotes();


    @Query("SELECT COUNT(id) FROM NOTES_TABLE WHERE trash =  1 ")
    LiveData<Integer> checkTrashNotes();


}
