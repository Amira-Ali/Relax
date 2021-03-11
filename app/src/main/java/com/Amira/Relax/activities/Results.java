package com.Amira.Relax.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.Amira.Relax.utilities.DatabaseHelper;
import com.Amira.Relax.utilities.GlobalVariables;
import com.Amira.Relax.R;

import java.util.ArrayList;
import java.util.List;

public class Results extends AppCompatActivity {
    List<String> areas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ShowSurveyResults();
        Button btn = findViewById(R.id.btn_home);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                databaseHelper.SvUserTotal(GlobalVariables.username, GlobalVariables.Physical_total,
                        GlobalVariables.Sleep_total,
                        GlobalVariables.Behavior_total, GlobalVariables.Emotional_total);
                ClrTotal();
                Intent intent = new Intent(Results.this, Home.class);
                startActivity(intent);
            }
        });
    }

    private void ShowSurveyResults() {
        int Physical_total = GlobalVariables.Physical_total;
        int Sleep_total = GlobalVariables.Sleep_total;
        int Behavior_total = GlobalVariables.Behavior_total;
        int Emotional_total = GlobalVariables.Emotional_total;

        if (Physical_total > 0) {
            areas.add(" • Physical Factor");
        }
        if (Sleep_total > 0) {
            areas.add(" • Sleep Factor");
        }
        if (Behavior_total > 0) {
            areas.add(" • Behavioral Factor");
        }
        if (Emotional_total > 0) {
            areas.add(" • Emotional Factor");
        }

        if (areas.size() == 0) {
            TextView textView =  findViewById(R.id.txt);
            textView.setText(R.string.Result_Replacement);
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    R.layout.activity_listview, areas);
            ListView listView = findViewById(R.id.area_list);
            listView.setAdapter(adapter);
        }

    }

    private void ClrTotal() {
        try {
            GlobalVariables.Physical_total = 0;
            GlobalVariables.Sleep_total = 0;
            GlobalVariables.Behavior_total = 0;
            GlobalVariables.Emotional_total = 0;
        } catch (Exception ex) {
            Log.d("ClrTotal Error", ex.toString());
        }
    }

}
