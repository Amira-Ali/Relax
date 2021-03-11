package com.Amira.Relax.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.Amira.Relax.utilities.GlobalVariables;
import com.Amira.Relax.utilities.Option;
import com.Amira.Relax.R;
import com.Amira.Relax.utilities.SurveyAdapter;

import java.util.Arrays;

public class Behavior extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button submit = findViewById(R.id.NextBtn);
        Resources res = getResources();
        String[] str = res.getStringArray(R.array.Behavioral_Questions);
        Arrays.sort(str);
        Option[] Behavior_questions_array = new Option[]
                {
                        new Option(str[0]),
                        new Option(str[1]),
                        new Option(str[2]),
                        new Option(str[3]),
                        new Option(str[4]),
                        new Option(str[5]),
                        new Option(str[6]),
                        new Option(str[7]),
                        new Option(str[8]),
                        new Option(str[9])

                };

        final SurveyAdapter surveyAdapter = new SurveyAdapter(this,
                R.layout.list_items, Behavior_questions_array);
        ListView simpleList = findViewById(R.id.simpleListView);
        simpleList.setAdapter(surveyAdapter);
        final Option[] items = surveyAdapter.getItems();
        if (items.length > 0) {
            submit.setEnabled(true);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.NextBtn) {
                    for (Option opt : items) {
                        if (opt.selectedId != -1) {
                            switch (opt.selectedId) {
                                case R.id.Never:
                                    GlobalVariables.Behavior_total += 0;
                                    break;
                                case R.id.Almost_never:
                                    GlobalVariables.Behavior_total += 2;
                                    break;
                                case R.id.Some_of_the_time:
                                    GlobalVariables.Behavior_total += 3;
                                    break;
                                case R.id.Most_of_the_time:
                                    GlobalVariables.Behavior_total += 4;
                                    break;
                                case R.id.Almost_always:
                                    GlobalVariables.Behavior_total += 5;
                                    break;
                            }
                        }
                    }
                    Intent intent = new Intent(getApplicationContext(), Emotion.class);
                    startActivity(intent);
                }
            }
        });
    }
}