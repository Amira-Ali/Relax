package com.relax.callbacks;

import com.relax.models.Note;

/**
 * Created by ixi.Dv on 22/07/2018.
 */
public interface NoteEventListener {
    /**
     * call wen note clicked.
     *
     * @param note: note item
     */
    void onNoteClick(Note note);

    /**
     * call wen long Click to note.
     *
     * @param note : item
     */
    void onNoteLongClick(Note note);
}