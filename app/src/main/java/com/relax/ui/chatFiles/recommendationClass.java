package com.relax.ui.chatFiles;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.relax.R;
import com.relax.utilities.dbHelper;

import java.util.ArrayList;
import java.util.List;

public class recommendationClass {

    com.relax.utilities.dbHelper dbHelper;
    List<Object> listOfObjects;
    ArrayList<Button> buttonArrayList;
    String[] btnNames;
    Context context;
    RecyclerView msgRecyclerView;
    List<chatMsgClass> chatList;
    chatListAdapter msgAdapter;

    recommendationClass(Context context, RecyclerView msgRecyclerView, List<chatMsgClass> chatList, chatListAdapter msgAdapter) {
        this.context = context;
        this.msgRecyclerView = msgRecyclerView;
        this.chatList = chatList;
        this.msgAdapter = msgAdapter;
        dbHelper = new dbHelper(context);
    }

    public void showPhysicalRecommendations(manageSession manageSession) {
        listOfObjects = new ArrayList<>();
        listOfObjects.add("Well, I can tell you from experience that it's not hard at all to have a shape like 'The Rock'.");

        Drawable rockImage = ContextCompat.getDrawable(context, R.drawable.rock);
        listOfObjects.add(rockImage);

        listOfObjects.add("I'm kidding! of course it's hard. besides you're not an actress and your life is not a movie!");
        listOfObjects.add("What really matters is to have a healthy body, and healthy body comes from three main factors; a balance diet, enough sleep hours and a moderate exercise routine.");
        listOfObjects.add("So I have a few advices for you. Just click on one of these buttons:");

        buttonArrayList = new ArrayList<>();
        btnNames = new String[]{"recommendation_Nutrition", "Exercise", "recommendation_Sleeping"};
        for (String btnName : btnNames) {
            Button btn = new Button(context);
            btn.setBackgroundResource(R.drawable.btn_custom);
            btn.setText(btnName);
            buttonArrayList.add(btn);
        }
        listOfObjects.add(buttonArrayList);
        manageSession.displayChat(msgRecyclerView, listOfObjects);
    }

    public void showSleepRecommendations(manageSession manageSession) {
        listOfObjects = new ArrayList<>();
        listOfObjects.add("I thought so!");
        listOfObjects.add("And I have a couple of recommendation to help you sleep better.");
        listOfObjects.add("Just click the next button. read the advices and I'll get you back here afterwards.");

        buttonArrayList = new ArrayList<>();
        btnNames = new String[]{"recommendation_Sleeping", "recommendation_Breathing"};
        for (String btnName : btnNames) {
            Button btn = new Button(context);
            btn.setBackgroundResource(R.drawable.btn_custom);
            btn.setText(btnName);
            buttonArrayList.add(btn);
        }
        listOfObjects.add(buttonArrayList);
        manageSession.displayChat(msgRecyclerView, listOfObjects);
    }

    public void showEmotionRecommendations(manageSession manageSession) {
        //user doesn't know journaling, introduce him to it, offer him to do it, otherwise redirect him to spiritual
        listOfObjects = new ArrayList<>();
        //  listOfObjects.add("You see, "+ globalVariables.username+", in general, people prefer to keep their emotions to themselves.");
        // listOfObjects.add("But keeping them does NOT mean that they will go away.");
        // listOfObjects.add("On the contrary, hiding your emotions especially the negative ones will disrupt the normal function of your stress hormones called cortisol.");
        //  listOfObjects.add("And that will lead to a lowered immune function, put your body at risk of developing a chronic illness, and eventually developing mental health conditions.");
        //  listOfObjects.add("One of the scientifically proven methods to deal with inner emotions is recommendation_Journaling.");

        buttonArrayList = new ArrayList<>();
        //btnNames = new String[]{"Okay", "Not now"};
        btnNames = new String[]{"What is it?", "Try it", "Skip it"};
        for (String btnName : btnNames) {
            Button btn = new Button(context);
            btn.setBackgroundResource(R.drawable.btn_custom);
            btn.setText(btnName);
            buttonArrayList.add(btn);
        }
        listOfObjects.add(buttonArrayList);
        manageSession.displayChat(msgRecyclerView, listOfObjects);
    }

    public void showBehaviorRecommendations(manageSession manageSession) {
        listOfObjects = new ArrayList<>();
        listOfObjects.add("Yeah, I know how you feel.");
        listOfObjects.add("To be fair, coping with changes has never been an easy task for everyone.");
        listOfObjects.add("People tend to cope with change in one of two ways:\n" +
                "1. Escape coping.\n" +
                "2. Control coping.");
        listOfObjects.add("- Escape coping is based on avoidance. You take deliberate actions to avoid the difficulties of the change.");
        listOfObjects.add("For instance, you might deliberately show up late to attend a meeting about an upcoming restructure. or ignore calls from a co-worker who's just got the promotion that you wanted.");
        listOfObjects.add("- Control coping, on the other hand, is positive and proactive. You refuse to behave like a \"victim\" of change.");
        listOfObjects.add("Instead, you manage your feelings, and do whatever you can to be part of the change.");
        listOfObjects.add("In reality, most of us respond to major change with a mixture of escape and control coping.");
        listOfObjects.add("But control coping is generally the better option, as it's impossible to avoid the reality of change for long without becoming exhausted and stressed.");
        listOfObjects.add("That being said, I created two sets of recommendations especially for you. take your pick!");

        buttonArrayList = new ArrayList<>();
        btnNames = new String[]{"Face changes", "Self esteem"};
        for (String btnName : btnNames) {
            Button btn = new Button(context);
            btn.setBackgroundResource(R.drawable.btn_custom);
            btn.setText(btnName);
            buttonArrayList.add(btn);
        }
        listOfObjects.add(buttonArrayList);
        manageSession.displayChat(msgRecyclerView, listOfObjects);
    }
}
