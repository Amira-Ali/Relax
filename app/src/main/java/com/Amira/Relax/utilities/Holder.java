package com.Amira.Relax.utilities;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.Amira.Relax.R;

public class Holder {
    TextView question;
    RadioGroup rg;
    RadioButton rb1, rb2, rb3, rb4, rb5;

    Holder(View view) {
        question = (TextView) view.findViewById(R.id.question);
        rb1 = (RadioButton) view.findViewById(R.id.Never);
        rb2 = (RadioButton) view.findViewById(R.id.Almost_never);
        rb3 = (RadioButton) view.findViewById(R.id.Some_of_the_time);
        rb4 = (RadioButton) view.findViewById(R.id.Most_of_the_time);
        rb5 = (RadioButton) view.findViewById(R.id.Almost_always);
        rg = (RadioGroup) view.findViewById(R.id.RadioG);
    }

}
