package com.olabode.wilson.notekeep.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.olabode.wilson.notekeep.models.Note;

/**
 * Created by OLABODE WILSON on 2019-06-26.
 */
@Database(entities = Note.class, version = 3, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase INSTANCE;


    public static synchronized NoteDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }


    public abstract NoteDao noteDao();

}
