package com.relax.activities;

import static com.relax.activities.addNote.NOTE_EXTRA_Key;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.relax.R;
import com.relax.callbacks.MainActionModeCallback;
import com.relax.callbacks.NoteEventListener;
import com.relax.models.Note;
import com.relax.utilities.dbHelper;
import com.relax.utilities.globalVariables;
import com.relax.utilities.notesAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class noteList extends AppCompatActivity implements NoteEventListener, Drawer.OnDrawerItemClickListener {
    private RecyclerView recyclerView;
    private ArrayList<Note> notes;
    private notesAdapter adapter;
    com.relax.utilities.dbHelper dbHelper;
    private MainActionModeCallback actionModeCallback;
    private int checkedCount = 0;
    private FloatingActionButton fab;
    int userID = globalVariables.userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("My thoughts");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // init recyclerView
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // init fab Button
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> onAddNewNote());

        dbHelper = new dbHelper(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        this.finish();
        //  return true;
        return super.onOptionsItemSelected(item);
    }

    private void loadNotes() {
        List<Note> list = dbHelper.getNotes(userID);
        this.notes = new ArrayList<>();
        this.notes.addAll(list);
        this.adapter = new notesAdapter(this, this.notes);
        // set listener to adapter
        this.adapter.setListener(this);
        this.recyclerView.setAdapter(adapter);
        showEmptyView();
        // add swipe helper to recyclerView
        swipeToDeleteHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * when no notes show msg in main_layout
     */
    private void showEmptyView() {
        if (notes.size() == 0) {
            this.recyclerView.setVisibility(View.GONE);
            findViewById(R.id.empty_notes_view).setVisibility(View.VISIBLE);

        } else {
            this.recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.empty_notes_view).setVisibility(View.GONE);
        }
    }

    private void onAddNewNote() {
        globalVariables.backURL = "noteList";
        startActivity(new Intent(this, addNote.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    @Override
    public void onNoteClick(Note note) {
        Intent edit = new Intent(this, addNote.class);
        edit.putExtra(NOTE_EXTRA_Key, note.getId());
        startActivity(edit);
    }

    @Override
    public void onNoteLongClick(Note note) {
        note.setChecked(true);
        checkedCount = 1;
        adapter.setMultiCheckMode(true);

        // set new listener to adapter intend off noteList listener that we have implement
        adapter.setListener(new NoteEventListener() {
            @Override
            public void onNoteClick(Note note) {
                note.setChecked(!note.isChecked()); // inverse selected
                if (note.isChecked())
                    checkedCount++;
                else checkedCount--;

                actionModeCallback.changeShareItemVisible(checkedCount <= 1);

                if (checkedCount == 0) {
                    //  finish multi select mode wen checked count =0
                    actionModeCallback.getAction().finish();
                }

                actionModeCallback.setCount(checkedCount + "/" + notes.size());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNoteLongClick(Note note) {
            }
        });

        actionModeCallback = new MainActionModeCallback() {
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_delete_notes)
                    onDeleteMultiNotes();
                else if (menuItem.getItemId() == R.id.action_share_note)
                    onShareNote();

                actionMode.finish();
                return false;
            }

        };

        // start action mode
        startActionMode(actionModeCallback);
        // hide fab button
        fab.setVisibility(View.GONE);
        actionModeCallback.setCount(checkedCount + "/" + notes.size());
    }

    private void onShareNote() {
        // share just one Note not multi

        Note note = adapter.getCheckedNotes().get(0);
        // share note ; on social or something else
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        String NoteText = note.getNoteText() + "\n\n Create on : " +
                note.getNoteDate() + "\n  By: " +
                getString(R.string.app_name);
        share.putExtra(Intent.EXTRA_TEXT, NoteText);
        startActivity(share);
    }

    private void onDeleteMultiNotes() {
        // delete multi notes

        List<Note> checkedNotes = adapter.getCheckedNotes();
        if (checkedNotes.size() != 0) {
            for (Note note : checkedNotes) {
                dbHelper.delNote(userID, note);
            }
            // refresh Notes
            loadNotes();
            Toast.makeText(this, checkedNotes.size() + " Note(s) Delete successfully !", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "No Note(s) selected", Toast.LENGTH_SHORT).show();
        //adapter.setMultiCheckMode(false);
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        adapter.setMultiCheckMode(false); // uncheck the notes
        adapter.setListener(this); // set back the old listener
        fab.setVisibility(View.VISIBLE);
    }

    // swipe to right or to left te delete
    private final ItemTouchHelper swipeToDeleteHelper = new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    // delete note when swipe
                    if (notes != null) {
                        // get swiped note
                        Note swipedNote = notes.get(viewHolder.getAbsoluteAdapterPosition());
                        if (swipedNote != null) {
                            swipeToDelete(swipedNote, viewHolder);
                        }
                    }
                }
            });

    private void swipeToDelete(final Note swipedNote, final RecyclerView.ViewHolder viewHolder) {
        new AlertDialog.Builder(noteList.this)
                .setMessage("Delete Note?")
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    // delete note
                    dbHelper.delNote(userID, swipedNote);
                    notes.remove(swipedNote);
                    adapter.notifyItemRemoved(viewHolder.getAbsoluteAdapterPosition());
                    showEmptyView();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    // Undo swipe and restore swipedNote
                    Objects.requireNonNull(recyclerView.getAdapter()).notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
                })
                .setCancelable(false)
                .create().show();
    }

    @Override
    public boolean onItemClick(View view, int position, @NonNull IDrawerItem<?> drawerItem) {
        Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
        return false;
    }
}



