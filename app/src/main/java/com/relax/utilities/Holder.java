package com.relax.utilities;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.relax.R;

public class Holder {
    TextView question;
    RadioGroup rg;
    RadioButton rb1, rb2, rb3, rb4, rb5;

    Holder(View view) {
        question = view.findViewById(R.id.question);
        rb1 = view.findViewById(R.id.Never);
        rb2 = view.findViewById(R.id.Almost_never);
        rb3 = view.findViewById(R.id.Some_of_the_time);
        rb4 = view.findViewById(R.id.Most_of_the_time);
        rb5 = view.findViewById(R.id.Almost_always);
        rg = view.findViewById(R.id.RadioG);
    }

}
