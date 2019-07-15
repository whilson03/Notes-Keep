package com.olabode.wilson.notekeep.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.olabode.wilson.notekeep.utils.Constants;

/**
 * Created by OLABODE WILSON on 2019-06-26.
 */
@Entity(tableName = "notes_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = Constants.DatabaseColumns.COLUMN_TITLE)
    private String title;

    @ColumnInfo(name = Constants.DatabaseColumns.COLUMN_BODY)
    private String body;

    @ColumnInfo(name = "favourite")
    private int isFavourite = 0;

    @ColumnInfo(name = "trash")
    private int isMovedToTrash = 0;

    @ColumnInfo(name = Constants.DatabaseColumns.COLUMN_TIMESTAMP)
    private String timeStamp;

    public Note(String title, String body, String timeStamp) {
        this.title = title;
        this.body = body;
        this.timeStamp = timeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }

    public int getIsMovedToTrash() {
        return isMovedToTrash;
    }

    public void setIsMovedToTrash(int isMovedToTrash) {
        this.isMovedToTrash = isMovedToTrash;
    }
}


