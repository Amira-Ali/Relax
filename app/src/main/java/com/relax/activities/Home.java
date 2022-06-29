package com.relax.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.relax.R;
import com.relax.utilities.dbHelper;
import com.relax.utilities.globalVariables;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    int userID= globalVariables.userID;
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent timePicker = new Intent(this, com.relax.activities.timePicker.class);
                startActivity(timePicker);
                break;

            case R.id.feedback:
                Intent feedback = new Intent(this, Feedback.class);
                startActivity(feedback);
                break;

            case R.id.journaling:
                globalVariables.backURL = "Home";
                Intent addNote = new Intent(this, addNote.class);
                startActivity(addNote);
                break;

            case R.id.thoughts:
                Intent thoughts = new Intent(this, noteList.class);
                startActivity(thoughts);
                break;

            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", (dialog, id) -> {
                    saveSessionEndData();
                    cleanUpAllActivities();
                });

                builder.setNegativeButton("No", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveSessionEndData() {
        dbHelper dbHelper = new dbHelper(this);
        dbHelper.updateSessionEndTime(userID);
    }

    private void cleanUpAllActivities() {
        Intent intent = new Intent(Home.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// To clean up all activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        globalVariables.globalRecyclerView = null;
        globalVariables.globalChatList = new ArrayList<>();
        globalVariables.globalMsgAdapter = null;
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_myJourney)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.container);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

}
