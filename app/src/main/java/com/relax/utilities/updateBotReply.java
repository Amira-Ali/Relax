package com.relax.utilities;

import android.content.Context;
import android.widget.Button;

import com.relax.R;
import com.relax.activities.chatPage;
import com.relax.ui.chatFiles.manageSession;
import com.relax.ui.chatFiles.nlpPipeline;

import java.util.ArrayList;
import java.util.List;

//This Class updates the chat view with bot replies and perform the connection with the Python
public class updateBotReply extends chatPage {
    static List<Object> listOfObjects;
    private final Context context;

    public updateBotReply(Context context) {
        this.context = context;
    }

    public static int getPrediction(String userMsg) {
        nlpPipeline.init();
        return nlpPipeline.estimatingSentiment(userMsg);
    }

    public void PredictUserReturnBotReply(String searchTerm) {
        int botPrediction = getPrediction(searchTerm);
        globalVariables.botPrediction = botPrediction;
        if (botPrediction == 0 || botPrediction == 1) {//Very Negative - Negative
            negative(searchTerm);
        } else {
            if (botPrediction == 3 || botPrediction == 4) {//Very Positive - Positive
                positive();
            } else {//neutral 2, like global warming, google it
                neutral(searchTerm);
            }
        }
    }

    public void positive() {
        String[] btnNames;
        ArrayList<Button> buttonArrayList = new ArrayList<>();
        listOfObjects = new ArrayList<>();
        listOfObjects.add(botScript.BotReplyOnPositive());
        listOfObjects.add(botScript.Bot_SuggestList());
        btnNames = new String[]{"Yes", "No"};
        for (String btnName : btnNames) {
            Button btn = new Button(context);
            btn.setBackgroundResource(R.drawable.btn_custom);
            btn.setText(btnName);
            buttonArrayList.add(btn);
        }
        listOfObjects.add(buttonArrayList);
        manageSession manageSession = new manageSession(context, globalVariables.globalRecyclerView, globalVariables.globalChatList, globalVariables.globalMsgAdapter);
        manageSession.displayChat(globalVariables.globalRecyclerView, listOfObjects);
    }

    public void negative(String searchTerm) {
        listOfObjects = new ArrayList<>();
        listOfObjects.add(botScript.BotReplyOnNegative());//Consolidate
        summarizeFirstLinkThenListTheOthers(searchTerm, listOfObjects);
    }

    public void neutral(String searchTerm) {
        listOfObjects = new ArrayList<>();
        listOfObjects.add("I don't know how to answer that. I'll tell my programmer I need more tuning!");
        listOfObjects.add("Meanwhile, I'll look for what you just said on the internet...");
        summarizeFirstLinkThenListTheOthers(searchTerm, listOfObjects);
    }

    public void summarizeFirstLinkThenListTheOthers(String searchTerm, List<Object> listOfObjects) {//i feel tired
        if (context != null) {
            googleIssue googleIssue = new googleIssue(searchTerm, context, listOfObjects);
            googleIssue.execute();
        }
    }

}



