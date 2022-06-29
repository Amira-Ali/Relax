package com.relax.utilities;

public class dataBean {
    Option question;
    static int NONE = 1111;
    static int OPTION_1 = 0;
    static int OPTION_2 = 1;
    static int OPTION_3 = 2;
    static int OPTION_4 = 3;
    static int OPTION_5 = 4;
    int current = NONE;

    dataBean(Option que) {
        question = que;

    }
}
