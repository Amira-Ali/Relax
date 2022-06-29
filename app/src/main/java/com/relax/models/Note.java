package com.relax.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;


@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id; // default value
    @ColumnInfo(name = "text")
    private String noteText;
    @ColumnInfo(name = "date")
    private String noteDate;

    @Ignore // we don't want to store this value on database so ignore it
    private boolean checked = false;

    @Ignore
    public Note() {
    }

    public Note(String noteText, String noteDate) {
        this.noteText = noteText;
        this.noteDate = noteDate;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @NotNull
    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", noteDate=" + noteDate +
                '}';
    }
}