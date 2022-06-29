package com.relax.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.relax.R;
import com.relax.models.Note;
import com.relax.ui.chatFiles.nlpPipeline;
import com.relax.utilities.dbHelper;
import com.relax.utilities.globalVariables;

public class addNote extends AppCompatActivity {
    int userID = globalVariables.userID;
    private EditText inputNote;
    private Note note;
    public static final String NOTE_EXTRA_Key = "note_id";
    com.relax.utilities.dbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Add thoughts");

        inputNote = findViewById(R.id.input_note);

        dbHelper = new dbHelper(this);

        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_Key, 0);
            note = dbHelper.getLastNote(id);
            inputNote.setText(note.getNoteText());
        } else {
            inputNote.setFocusable(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_note) {
            onSaveNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSaveNote() {
        boolean flag;
        String text = inputNote.getText().toString();
        if (!text.isEmpty()) {
            String date = globalVariables.currentDate;
            if (note == null) {
                note = new Note(text, date);
                flag = dbHelper.addNote(userID, note);
            } else {
                note.setNoteText(text);
                note.setNoteDate(date);
                flag = dbHelper.updateNote(userID, note);
            }

            if (flag) {//if user note has been added or altered successfully, go ahead and analyse the note
                summarizeAnalyzeNote(text);
            }

            String backUrl = globalVariables.backURL;
            if ("manageSession".equals(backUrl)) {
                globalVariables.backURL = "addNote";
                startActivity(new Intent(this, chatPage.class));
            } else {
                startActivity(new Intent(this, noteList.class));
            }

        } else {
            Toast.makeText(this, "Nothing to save!", Toast.LENGTH_SHORT).show();
        }

    }

    public void summarizeAnalyzeNote(String text) {
        //first summarize user entry
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py = Python.getInstance();
        PyObject module = py.getModule("scrape").callAttr("summarize", text, 0.5);
        String summary = module.toString();

        if (summary.isEmpty()) {
            summary = text;
        }
        //analyze summary and return prediction
        int prediction = getPrediction(summary);
        dbHelper.insertNoteAnalysis(userID, summary, prediction);

    }

    public static int getPrediction(String userMsg) {
        nlpPipeline.init();
        return nlpPipeline.estimatingSentiment(userMsg);
    }

}
