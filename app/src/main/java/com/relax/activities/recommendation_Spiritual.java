package com.relax.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.relax.R;
import com.relax.utilities.globalVariables;

public class recommendation_Spiritual extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommendation_spirtual);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.SpiritualTitle);

        Button btn = findViewById(R.id.btn_home);
        Intent intent = null;

        if (globalVariables.backURL.equals("Home")) {
            btn.setText(getString(R.string.back_home));
            intent = new Intent(this, Home.class);
        } else if (globalVariables.backURL.equals("manageSession")) {
            btn.setText(getString(R.string.back_chat));
            intent = new Intent(this, chatPage.class);
            globalVariables.backURL = "recommendation_Spiritual";
        }

        Intent finalIntent = intent;
        btn.setOnClickListener(view -> startActivity(finalIntent));

        int color = Color.parseColor("#2ba6c8");

        CharSequence t1 = getText(R.string.DuaTitle);
        SpannableString s1 = new SpannableString(t1);
        s1.setSpan(new BulletSpan(15, color, 10), 0, t1.length(), 0);
        TextView txt1 = findViewById(R.id.DuaTitle);
        txt1.setText(TextUtils.concat(s1));

        CharSequence t2 = getText(R.string.PerfumeTitle);
        SpannableString s2 = new SpannableString(t2);
        s2.setSpan(new BulletSpan(15, color, 10), 0, t2.length(), 0);
        TextView txt2 = findViewById(R.id.PerfumeTitle);
        txt2.setText(TextUtils.concat(s2));

        CharSequence t3 = getText(R.string.SmileTitle);
        SpannableString s3 = new SpannableString(t3);
        s3.setSpan(new BulletSpan(15, color, 10), 0, t3.length(), 0);
        TextView txt3 = findViewById(R.id.SmileTitle);
        txt3.setText(TextUtils.concat(s3));

        CharSequence t4 = getText(R.string.WuduTitle);
        SpannableString s4 = new SpannableString(t4);
        s4.setSpan(new BulletSpan(15, color, 10), 0, t4.length(), 0);
        TextView txt4 = findViewById(R.id.WuduTitle);
        txt4.setText(TextUtils.concat(s4));

        CharSequence t5 = getText(R.string.ForgiveTitle);
        SpannableString s5 = new SpannableString(t5);
        s5.setSpan(new BulletSpan(15, color, 10), 0, t5.length(), 0);
        TextView txt5 = findViewById(R.id.ForgiveTitle);
        txt5.setText(TextUtils.concat(s5));


    }
}