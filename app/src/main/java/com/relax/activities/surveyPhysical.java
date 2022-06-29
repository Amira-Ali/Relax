package com.relax.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.relax.R;
import com.relax.utilities.Option;
import com.relax.utilities.globalVariables;
import com.relax.utilities.surveyAdapter;

import java.util.Arrays;

public class surveyPhysical extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_physical);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.Physical_Section);

        Button submit = findViewById(R.id.NextBtn);
        Resources res = getResources();
        String[] str = res.getStringArray(R.array.Physical_Questions);
        Arrays.sort(str);
        Option[] physical_questions_array = new Option[]
                {
                        new Option(str[0]),
                        new Option(str[1]),
                        new Option(str[2]),
                        new Option(str[3]),
                        new Option(str[4]),
                        new Option(str[5])
                };

        final surveyAdapter surveyAdapter = new surveyAdapter(this,
                R.layout.list_items, physical_questions_array);
        ListView simpleList = findViewById(R.id.simpleListView);
        simpleList.setAdapter(surveyAdapter);
        final Option[] items = surveyAdapter.getItems();
        if (items.length > 0) {
            submit.setEnabled(true);
        }

        submit.setOnClickListener(view -> {
            if (view.getId() == R.id.NextBtn) {
                for (Option opt : items) {
                    if (opt.selectedId != -1) {
                        switch (opt.selectedId) {
                            case R.id.Never:
                                globalVariables.physicalTotal = 0;
                                break;
                            case R.id.Almost_never:
                                globalVariables.physicalTotal += 2;
                                break;
                            case R.id.Some_of_the_time:
                                globalVariables.physicalTotal += 3;
                                break;
                            case R.id.Most_of_the_time:
                                globalVariables.physicalTotal += 4;
                                break;
                            case R.id.Almost_always:
                                globalVariables.physicalTotal += 5;
                                break;
                        }
                    }
                }

                Intent intent = new Intent(getApplicationContext(), surveySleep.class);
                startActivity(intent);
            }

        });
    }
}