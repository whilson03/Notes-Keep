package com.olabode.wilson.notekeep.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.olabode.wilson.notekeep.utils.Constants;

/**
 * Created by OLABODE WILSON on 2019-07-02.
 */


@Entity(tableName = "trash_table")
public class Trash {

    @ColumnInfo(name = Constants.DatabaseColumns.COLUMN_ID)
    private int id;

    @ColumnInfo(name = Constants.DatabaseColumns.COLUMN_TITLE)
    private String title;

    @ColumnInfo(name = Constants.DatabaseColumns.COLUMN_BODY)
    private String body;

    @ColumnInfo(name = Constants.DatabaseColumns.COLUMN_TIMESTAMP)
    private String date;

    public Trash(int id, String title, String body, String date) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
