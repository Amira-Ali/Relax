package com.relax.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.relax.R;
import com.relax.utilities.globalVariables;

public class recommendation_Nutrition extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommendation_nutrition);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.NutritionTitle);

        Button btn = findViewById(R.id.btn_home);
        Intent intent = new Intent();

        if (globalVariables.backURL != null && !globalVariables.backURL.equals("")) {

            if (globalVariables.backURL.equals("Home")) {
                btn.setText(getString(R.string.back_home));
                intent = new Intent(getApplicationContext(), Home.class);
            } else if (globalVariables.backURL.equals("manageSession")) {
                btn.setText(getString(R.string.back_chat));
                intent = new Intent(getApplicationContext(), chatPage.class);
                globalVariables.backURL = "recommendation_Nutrition";
            }

            Intent finalIntent = intent;
            btn.setOnClickListener(view -> startActivity(finalIntent));
        }

    }
}