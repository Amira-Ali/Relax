package com.relax.utilities;

public class Option {
    public String Question;
    public int selectedId = -1;

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public Option() {
        super();
    }

    public Option(String Q) {
        super();
        this.Question = Q;
    }

}
