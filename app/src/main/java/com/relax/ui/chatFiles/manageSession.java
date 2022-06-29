package com.relax.ui.chatFiles;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.relax.R;
import com.relax.activities.chatPage;
import com.relax.utilities.botScript;
import com.relax.utilities.dbHelper;
import com.relax.utilities.globalVariables;
import com.relax.utilities.updateChatView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class manageSession {

    Context context;
    RecyclerView msgRecyclerView;
    List<chatMsgClass> chatList;
    chatListAdapter msgAdapter;
    public View view;

    List<Object> listOfObjects;
    ArrayList<Button> buttonArrayList;
    String[] btnNames;

    String botMessage;
    int Wait4Sec = 4000;
    int Wait8Sec = 8000;
    com.relax.utilities.dbHelper dbHelper;

    public manageSession(Context context) {
        this.context = context;
        dbHelper = new dbHelper(context);
    }

    public manageSession(Context context, RecyclerView msgRecyclerView, List<chatMsgClass> chatList, chatListAdapter msgAdapter) {
        this.context = context;
        dbHelper = new dbHelper(context);
        this.msgRecyclerView = msgRecyclerView;
        this.chatList = chatList;
        this.msgAdapter = msgAdapter;
    }

    public void displayChat(RecyclerView recyclerView, List<Object> listOfObjects) {
        Object object = listOfObjects.get(0);
        if (object instanceof String) chatPage.updateIsTyping();

        Handler h = new Handler();

        if (restoreChatStatus(recyclerView)) {//chatListHasAtLeastOneStatement=true
            for (int i = 0; i < listOfObjects.size(); i++) {
                Object o = listOfObjects.get(i);
                if (o instanceof String) chatPage.updateIsTyping();
                h.postDelayed(() -> updateChatView.appendBotMessage(o, recyclerView, chatList, msgAdapter), i * 6000L + Wait4Sec);
            }
        } else {//chatListHasAtLeastOneStatement=false ==> this is the first statement in the whole chat, wait only for 4 seconds

            h.postDelayed(() -> {
                Object o = listOfObjects.get(0);
                if (o instanceof String) chatPage.updateIsTyping();
                updateChatView.appendBotMessage(o, recyclerView, chatList, msgAdapter);
            }, Wait4Sec);

            for (int i = 1; i < listOfObjects.size(); i++) {
                Object o = listOfObjects.get(i);
                if (o instanceof String) chatPage.updateIsTyping();
                h.postDelayed(() -> updateChatView.appendBotMessage(o, recyclerView, chatList, msgAdapter), i * 6000L + Wait4Sec);
            }
        }

        saveChatStatus();
    }

    public void saveChatStatus() {
        globalVariables.backURL = "manageSession";
        globalVariables.globalRecyclerView = msgRecyclerView;
        globalVariables.globalChatList = chatList;
        globalVariables.globalMsgAdapter = msgAdapter;
    }

    public boolean restoreChatStatus(RecyclerView msgRecyclerView) {
        boolean chatListHasAtLeastOneStatement = false;
        if (globalVariables.globalChatList != null && globalVariables.globalMsgAdapter != null) {
            chatList = globalVariables.globalChatList;
            msgAdapter = globalVariables.globalMsgAdapter;
            int pos = chatList.size() - 1;
            msgAdapter.notifyItemInserted(pos);
            msgRecyclerView.setAdapter(msgAdapter);
            msgRecyclerView.scrollToPosition(pos);
            if (pos >= 1) chatListHasAtLeastOneStatement = true;
        }
        return chatListHasAtLeastOneStatement;
    }

    public void loopThroughStressPointsOrTalk(RecyclerView msgRecyclerView) {
        // if user has stress factors, loop through them, otherwise invite him to talk openly
        //count how many problems
        //if key=false then this problem has not been handled by the bot
        if (globalVariables.Flag_map.containsValue(false)) {

            if (globalVariables.backURL.equals("chatPage")) {
                //restoreChatStatus(msgRecyclerView);
                listOfObjects = new ArrayList<>();
                listOfObjects.add("Thank you for taking the survey.");
                listOfObjects.add("Now let's walk through your results.");
                displayChat(msgRecyclerView, listOfObjects);
            } else if (globalVariables.backURL.equals("Home")) {
                listOfObjects = new ArrayList<>();
                listOfObjects.add("Well..let's start with you survey results.");
                displayChat(msgRecyclerView, listOfObjects);
            } else {
                //user didn't leave the current chat. if we got here, this means that the bot either already handled one of user's
                // stress causes, or the bot got it wrong and the user said he was fine, and he doesn't need any treatment,
                //so the bot had crossed off that stress cause from the list and started
                //to loop through other stress factors the user maybe having.
                listOfObjects = new ArrayList<>();
                listOfObjects.add("Now, let's see what else can we talk about.");
                displayChat(msgRecyclerView, listOfObjects);
            }

            //now loop through user problems
            //wait a little until the above code gets executed and then run the next few lines
            Runnable r = () -> {
                for (Map.Entry<String, Boolean> entry : globalVariables.Flag_map.entrySet()) {
                    Boolean val = entry.getValue();
                    if (!val) {
                        globalVariables.stressCause = entry.getKey();//sleep-physical-emotion-behavior
                        break;
                    }
                }
                saveChatStatus();
                //Toast.makeText(context, "Stress = "+globalVariables.stressCause, Toast.LENGTH_SHORT).show();
                stressHandling(msgRecyclerView, globalVariables.stressCause);
            };
            Handler h = new Handler();
            h.postDelayed(r, Wait4Sec);

        } else {
            inviteToTalk(msgRecyclerView);
        }

    }

    public void stressHandling(RecyclerView msgRecyclerView, String stressCause) {
//These two buttons will appear after each question, whatever the stressCause is.
        buttonArrayList = new ArrayList<>();
        btnNames = new String[]{"A little", "No, I'm fine"};
        for (String btnName : btnNames) {
            Button btn = new Button(context);
            btn.setBackgroundResource(R.drawable.btn_custom);
            btn.setText(btnName);
            buttonArrayList.add(btn);
        }

        switch (stressCause) {
            case "physical":
                listOfObjects = new ArrayList<>();
                listOfObjects.add("It looks like you're struggling to do daily routines.");
                listOfObjects.add("Am I right?");
                listOfObjects.add(buttonArrayList);
                displayChat(msgRecyclerView, listOfObjects);
                break;

            case "emotion":
                listOfObjects = new ArrayList<>();
                listOfObjects.add("You often get so angry over little things?");
                listOfObjects.add("Is that true?");
                listOfObjects.add(buttonArrayList);
                displayChat(msgRecyclerView, listOfObjects);
                break;

            case "behavior":
                listOfObjects = new ArrayList<>();
                listOfObjects.add("Do you often feel critical about the way you look?");
                listOfObjects.add("Or do you doubt your abilities, especially when dealing with changes in your life?");
                listOfObjects.add(buttonArrayList);
                displayChat(msgRecyclerView, listOfObjects);
                break;

            case "sleep":
                listOfObjects = new ArrayList<>();
                listOfObjects.add("It appears that you can't sleep well.");
                listOfObjects.add("I bet you surf the web just before going to sleep.");
                listOfObjects.add("Am I right?");
                listOfObjects.add(buttonArrayList);
                displayChat(msgRecyclerView, listOfObjects);
                break;
        }
    }

    public void afterAddNote(RecyclerView msgRecyclerView) {
        listOfObjects = new ArrayList<>();
        listOfObjects.add("I can see that you are open minded when it comes to new experiences.");
        listOfObjects.add("Now I know something new about you!");
        listOfObjects.add("So");
        listOfObjects.add(" Have you enjoyed it? writing your thoughts I mean.");
        buttonArrayList = new ArrayList<>();
        btnNames = new String[]{"I guess", "Not much"};
        for (String btnName : btnNames) {
            Button btn = new Button(context);
            btn.setBackgroundResource(R.drawable.btn_custom);
            btn.setText(btnName);
            buttonArrayList.add(btn);
        }
        listOfObjects.add(buttonArrayList);
        displayChat(msgRecyclerView, listOfObjects);
    }

    public void afterSpiritual(RecyclerView msgRecyclerView) {
        listOfObjects = new ArrayList<>();
        listOfObjects.add("Although I have many ways to help people ease their stress, yet this is my favorite one.");
        listOfObjects.add("It takes neither time nor effort to do.");
        listOfObjects.add("It has to come from the heart. and that's all.");
        listOfObjects.add("Do you promise to go through them at least once a day?");
        buttonArrayList = new ArrayList<>();
        btnNames = new String[]{"I promise", "Umm..."};
        for (String btnName : btnNames) {
            Button btn = new Button(context);
            btn.setBackgroundResource(R.drawable.btn_custom);
            btn.setText(btnName);
            buttonArrayList.add(btn);
        }
        listOfObjects.add(buttonArrayList);
        displayChat(msgRecyclerView, listOfObjects);
    }

    public void wasThatHelpful(RecyclerView msgRecyclerView) {
        listOfObjects = new ArrayList<>();
        buttonArrayList = new ArrayList<>();

        listOfObjects.add("Was that helpful?");
        btnNames = new String[]{"Kind of", "Nope"};
        for (String btnName : btnNames) {
            Button btn = new Button(context);
            btn.setBackgroundResource(R.drawable.btn_custom);
            btn.setText(btnName);
            buttonArrayList.add(btn);
        }
        listOfObjects.add(buttonArrayList);
        displayChat(msgRecyclerView, listOfObjects);
    }

    public void callSOS() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "0914981270"));
        context.startActivity(intent);
    }

    public void afterSOS(RecyclerView msgRecyclerView) {
        listOfObjects = new ArrayList<>();
        listOfObjects.add(globalVariables.userName + ", I hope you got the help you needed.");
        listOfObjects.add("although it would be nice if problems can be solved overnight. but in reality things take time to change.");
        listOfObjects.add("I think you need to give yourself and my recommendations a little time.");
        listOfObjects.add("you'd be pleased later. I promise!");
        listOfObjects.add("If you need any further help, please type it in.");
        displayChat(msgRecyclerView, listOfObjects);

    }

    public void inviteToTalk(RecyclerView msgRecyclerView) {

        Handler h = new Handler();
        //Show First Statement after Two Seconds
        h.postDelayed(() -> {
            listOfObjects = new ArrayList<>();
            botMessage = "If you need any further help, please type it in.";
            listOfObjects.add(botMessage);
            displayChat(msgRecyclerView, listOfObjects);
        }, Wait8Sec);

    }

    public void justTalk(RecyclerView msgRecyclerView) {
        listOfObjects = new ArrayList<>();
        listOfObjects.add(botScript.Greeting_FirstTime());  //so, how r u? so, what's up?
        displayChat(msgRecyclerView, listOfObjects);

    }

    public String loadLastSession(int userID) {
        return dbHelper.loadLastSession(userID);
    }

    public String summarizeChat(String sessionText) {
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(context));
        }
        Python py = Python.getInstance();
        PyObject module = py.getModule("scrape").callAttr("summarize", sessionText, 0.5);
        return "Last session summary: \n" + module.toString();
    }

    public void resumeChat(RecyclerView msgRecyclerView) {
        listOfObjects = new ArrayList<>();
        String sessionText = loadLastSession(globalVariables.userID);
        String sessionSummary = summarizeChat(sessionText);
        listOfObjects.add(sessionSummary);
        String lastMsg = lastChatMsg(globalVariables.userID);
        listOfObjects.add(lastMsg);
       listOfObjects.add("Go ahead and say whatever comes to your mind..");
        displayChat(msgRecyclerView, listOfObjects);
    }

    public String lastChatMsg(int userID) {
        String lastMsg;
        String persona = dbHelper.lastPerson(userID);
        if (persona.equals("Bot") || persona.equals("")) {
            lastMsg = "Last sentence was send by me: ";
        } else {
            lastMsg = "Last sentence was send by you: ";
        }
        int statementID= dbHelper.getLastStatementID(persona);
        lastMsg += dbHelper.lastStatement(statementID);
        return lastMsg;
    }

    public void yes(RecyclerView msgRecyclerView) {
        String botReply;
        if (globalVariables.IsSurvey && globalVariables.Flag_map.size() > 0) { //user still has some unresolved issues since last session
            manageStressCauses manageStressCauses = new manageStressCauses(context);
            manageStressCauses.Populate_Flag_Map();
            loopThroughStressPointsOrTalk(msgRecyclerView);
        } else {
            //Either the user hasn't taken the survey yet
            //OR user does not have any unresolved issues to deal with
            //So depend on sentiment analysis on user's answer when asked about how he is today.
            switch (globalVariables.botPrediction) {
                case 2://neutral
                case 3://positive
                case 4://very positive
                    botReply = "Okay, I'm listening...";
                    break;

                default:
                    botReply = "";
                    break;
            }

            listOfObjects = new ArrayList<>();
            listOfObjects.add(botReply);
            displayChat(msgRecyclerView, listOfObjects);
        }
    }

    public void no(RecyclerView msgRecyclerView) {
        listOfObjects = new ArrayList<>();
        listOfObjects.add("It's ok, maybe another time will do.");
        listOfObjects.add("Would you like to pick a time for us to talk later?");
        buttonArrayList = new ArrayList<>();
        btnNames = new String[]{"Set time", "No thanks"};
        for (String btnName : btnNames) {
            Button btn = new Button(context);
            btn.setBackgroundResource(R.drawable.btn_custom);
            btn.setText(btnName);
            buttonArrayList.add(btn);
        }
        listOfObjects.add(buttonArrayList);
        displayChat(msgRecyclerView, listOfObjects);
    }

}
