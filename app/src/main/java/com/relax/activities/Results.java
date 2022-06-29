package com.relax.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.relax.R;
import com.relax.utilities.dbHelper;
import com.relax.utilities.globalVariables;

import java.util.ArrayList;
import java.util.List;

public class Results extends AppCompatActivity {
    List<String> areas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Button btn = findViewById(R.id.btn_home);
        Intent intent = null;

        if (globalVariables.backURL.equals("Home")) {
            btn.setText(getString(R.string.back_home));
            intent = new Intent(Results.this, Home.class);
        } else if (globalVariables.backURL.equals("manageSession")) {
            btn.setText(getString(R.string.back_chat));
            intent = new Intent(Results.this, chatPage.class);
            globalVariables.backURL = "Results";
        }

        ShowSurveyResults();

        Intent finalIntent = intent;
        btn.setOnClickListener(view -> {
            dbHelper databaseHelper = new dbHelper(getApplicationContext());
            databaseHelper.insertUserFlags(globalVariables.userID, globalVariables.physicalFlag, globalVariables.sleepFlag, globalVariables.behaviorFlag, globalVariables.emotionalFlag);
            ClrTotal();
            if (finalIntent != null) {
                startActivity(finalIntent);
            }
        });

    }

    private void ShowSurveyResults() {
        int Physical_total = globalVariables.physicalTotal;
        int Sleep_total = globalVariables.sleepTotal;
        int Behavior_total = globalVariables.behaviorTotal;
        int Emotional_total = globalVariables.emotionalTotal;

        if (Physical_total == 0) {
            globalVariables.physicalFlag = "N";
        } else {
            areas.add(" • Physical Issues");

            if (Physical_total > 0 && Physical_total < 10) {
                globalVariables.physicalFlag = "L";
            } else {
                if (Physical_total >= 10 && Physical_total < 15) {
                    globalVariables.physicalFlag = "M";
                } else {
                    if (Physical_total >= 15) {
                        globalVariables.physicalFlag = "H";
                    }
                }
            }
        }

        if (Sleep_total == 0) {
            globalVariables.sleepFlag = "N";
        } else {
            areas.add(" • Sleep Issues");
            if (Sleep_total > 0 && Sleep_total < 8) {
                globalVariables.sleepFlag = "L";
            } else {
                if (Sleep_total >= 8 && Sleep_total < 12) {
                    globalVariables.sleepFlag = "M";
                } else {
                    if (Sleep_total >= 12) {
                        globalVariables.sleepFlag = "H";
                    }
                }
            }
        }

        if (Behavior_total == 0) {
            globalVariables.behaviorFlag = "N";
        } else {
            areas.add(" • Behavioral Issues");
            if (Behavior_total > 0 && Behavior_total < 17) {
                globalVariables.behaviorFlag = "L";
            } else {
                if (Behavior_total >= 17 && Behavior_total < 25) {
                    globalVariables.behaviorFlag = "M";
                } else {
                    if (Behavior_total >= 25) {
                        globalVariables.behaviorFlag = "H";
                    }
                }
            }
        }

        if (Emotional_total == 0) {
            globalVariables.emotionalFlag = "N";
        } else {
            areas.add(" • Emotional Issues");
            if (Emotional_total > 0 && Emotional_total < 17) {
                globalVariables.emotionalFlag = "L";
            } else {
                if (Emotional_total >= 17 && Emotional_total < 25) {
                    globalVariables.emotionalFlag = "M";
                } else {
                    if (Emotional_total >= 25) {
                        globalVariables.emotionalFlag = "H";
                    }
                }
            }
        }

        if (areas.size() == 0) {
            TextView textView = findViewById(R.id.txt);
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
            globalVariables.physicalTotal = 0;
            globalVariables.sleepTotal = 0;
            globalVariables.behaviorTotal = 0;
            globalVariables.emotionalTotal = 0;
        } catch (Exception ex) {
            Log.d("ClrTotal Error", ex.toString());
        }
    }

}
