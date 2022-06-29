package com.relax.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.relax.R;
import com.relax.ui.chatFiles.chatListAdapter;
import com.relax.ui.chatFiles.chatMsgClass;
import com.relax.ui.chatFiles.manageSession;
import com.relax.ui.chatFiles.manageStressCauses;
import com.relax.ui.chatFiles.typeWriter;
import com.relax.utilities.botScript;
import com.relax.utilities.dbHelper;
import com.relax.utilities.globalVariables;
import com.relax.utilities.updateBotReply;
import com.relax.utilities.updateChatView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//This class manages user bot interaction but does not connect directly to python
//it connects indirectly to python through updateBotReply which extends CharPage(Current Class)
public class chatPage extends AppCompatActivity {

    public Context context;
    com.relax.utilities.dbHelper dbHelper;
    List<Object> listOfObjects;
    ArrayList<Button> buttonArrayList;
    String[] btnNames;
    int pos;
    boolean IsSurvey = globalVariables.IsSurvey;
    public boolean User_Old_Sessions = false;
    List<chatMsgClass> chatList = new ArrayList<>();
    chatListAdapter msgAdapter;
    RecyclerView msgRecyclerView;
    static typeWriter isTyping;
    static String isTypingText;
    public LinearLayout layoutChatBox;
    public EditText userInput;
    LinearLayoutManager linearLayoutManager;
    Button btnSendMsg;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Chat with Lito");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        msgAdapter = new chatListAdapter(chatList, context);
        msgAdapter.notifyDataSetChanged();
        dbHelper = new dbHelper(context);
        msgRecyclerView = findViewById(R.id.chat_recycler_view);
        linearLayoutManager = new LinearLayoutManager(context);
        msgRecyclerView.setLayoutManager(linearLayoutManager);
        msgRecyclerView.setAdapter(msgAdapter);
        userInput = findViewById(R.id.userInput);
        layoutChatBox = findViewById(R.id.layoutChatBox);
        isTyping = findViewById(R.id.isTyping);
        isTypingText = getResources().getString(R.string.isTyping);
        btnSendMsg = findViewById(R.id.btnSendMsg);
        guideConversation();
    }

    private void guideConversation() {
        manageSession manageSession = new manageSession(context, msgRecyclerView, chatList, msgAdapter);
        User_Old_Sessions = dbHelper.checkPreviousSession(globalVariables.userName);
        if (globalVariables.backURL.equals("Home")) {
            backIsHome(manageSession);
        } else {
            backIsActivity(manageSession);
        }
    }

    private void backIsHome(manageSession manageSession) {
        if (IsSurvey) {
            if (User_Old_Sessions) {
                //Clear out the screen before anything
                clearChatStatus();
                listOfObjects = new ArrayList<>();
                listOfObjects.add(botScript.Greeting_SecondTime());
                listOfObjects.add(getString(R.string.Offer_New_Proceed));
                buttonArrayList = new ArrayList<>();
                btnNames = new String[]{"New chat", "Resume chat"};
                for (String btnName : btnNames) {
                    Button btn = new Button(context);
                    btn.setBackgroundResource(R.drawable.btn_custom);
                    btn.setText(btnName);
                    buttonArrayList.add(btn);
                }
                listOfObjects.add(buttonArrayList);
            } else {//No Old Sessions
                listOfObjects = new ArrayList<>();
                listOfObjects.add("Well..let's start with you survey results.");
                manageSession = new manageSession(context, msgRecyclerView, chatList, msgAdapter);
            }
            manageSession.displayChat(msgRecyclerView, listOfObjects);
        } else {//No Survey
            if (User_Old_Sessions) {
                //No Survey But he had a session before

                listOfObjects = new ArrayList<>();
                listOfObjects.add(botScript.Greeting_SecondTime());
                listOfObjects.add("Take a quick survey, pick up where we left off or start a new chat.");
                listOfObjects.add("It's your call!");
                buttonArrayList = new ArrayList<>();
                btnNames = new String[]{"Take survey", "New chat", "Resume chat"};
                for (String btnName : btnNames) {
                    Button btn = new Button(context);
                    btn.setBackgroundResource(R.drawable.btn_custom);
                    btn.setText(btnName);
                    buttonArrayList.add(btn);
                }
                listOfObjects.add(buttonArrayList);
                manageSession = new manageSession(context, msgRecyclerView, chatList, msgAdapter);
                manageSession.displayChat(msgRecyclerView, listOfObjects);

            } else {//No Survey No Old Session ==> New user
                listOfObjects = new ArrayList<>();
                listOfObjects.add(botScript.Introduce_Bot());
                listOfObjects.add(getString(R.string.Offer_Survey_New));
                buttonArrayList = new ArrayList<>();
                btnNames = new String[]{"Take survey", "Just talk"};
                for (String btnName : btnNames) {
                    Button btn = new Button(context);
                    btn.setBackgroundResource(R.drawable.btn_custom);
                    btn.setText(btnName);
                    buttonArrayList.add(btn);
                }
                listOfObjects.add(buttonArrayList);
                manageSession = new manageSession(context, msgRecyclerView, chatList, msgAdapter);
                manageSession.displayChat(msgRecyclerView, listOfObjects);
            }
        }
    }

    private void backIsActivity(manageSession manageSession) {
        manageStressCauses manageStressCauses = new manageStressCauses(context);

        if (globalVariables.backURL.equals("Results") || globalVariables.backURL.equals("timePicker")
                || globalVariables.backURL.equals("recommendation_Spiritual") || globalVariables.backURL.equals("recommendation_Nutrition")
                || globalVariables.backURL.equals("recommendation_Sleeping") || globalVariables.backURL.equals("recommendation_Breathing")
                || globalVariables.backURL.equals("recommendation_Sport") || globalVariables.backURL.equals("recommendation_SelfEsteem")
                || globalVariables.backURL.equals("CopingWithChanges") || globalVariables.backURL.equals("recommendation_Journaling")
                || globalVariables.backURL.equals("addNote")) {

            if (globalVariables.globalChatList != null && globalVariables.globalMsgAdapter != null) {

                chatList = globalVariables.globalChatList;
                msgAdapter = globalVariables.globalMsgAdapter;
                pos = chatList.size() - 1;
                msgAdapter.notifyItemInserted(pos);
                msgRecyclerView.setAdapter(msgAdapter);
                msgRecyclerView.scrollToPosition(pos);

                //manageSession = new manageSession(context, msgRecyclerView, chatList, msgAdapter);

                switch (globalVariables.backURL) {
                    case "Results":
                        saveChatStatus();//backURL = chatPage
                        manageStressCauses.Populate_Flag_Map();
                        manageSession.loopThroughStressPointsOrTalk(msgRecyclerView);
                        break;
                    case "timePicker":
                        listOfObjects = new ArrayList<>();
                        listOfObjects.add("Okay then, I'll be reaching out to you every day at: " + dbHelper.getUserNotificationTime(globalVariables.userID));
                        listOfObjects.add("See you later.");
                        listOfObjects.add("bye bye!");
                        saveChatStatus();//backURL = chatPage
                        manageSession.displayChat(msgRecyclerView, listOfObjects);
                        break;
                    case "addNote":
                        saveChatStatus();//backURL = chatPage
                        manageSession.afterAddNote(msgRecyclerView);
                        break;
                    case "recommendation_Spiritual":
                        saveChatStatus();//backURL = chatPage
                        manageSession.afterSpiritual(msgRecyclerView);
                        break;
                    case "recommendation_Nutrition":
                    case "recommendation_Sleeping":
                    case "recommendation_Sport":
                    case "recommendation_Breathing":
                    case "recommendation_SelfEsteem":
                    case "CopingWithChanges":
                    case "recommendation_Journaling":
                        saveChatStatus();//backURL = chatPage
                        manageSession = new manageSession(context, msgRecyclerView, chatList, msgAdapter);
                        manageSession.wasThatHelpful(msgRecyclerView);
                        break;
                }
            }
        }

    }

    public void sendBtnClick(View view) {
         /* Yes-No Buttons are answers for next questions:
            user mood is positive/neutral ==>
                "Are you here just for a chat?"
               "Would you like to talk about a specific subject?"
            user mood is negative ==>
           "Would you like me to help you?",
                "Would you like to talk about it?",
                "Would you give me a chance to change how you feel?",
                "Do you feel like sharing your thoughts with me?"*/
        String userMsg = userInput.getText().toString();
        listOfObjects = new ArrayList<>();
        if (!TextUtils.isEmpty(userMsg)) {
            globalVariables.backURL = "Machine_Learning";
            updateChatView.AppendUserMsg(userMsg, msgRecyclerView, chatList, msgAdapter);
            userInput.setText("");

            if (userMsg.contains("thank") || userMsg.contains("bye")) {
                if (userMsg.contains("thank")) {
                    listOfObjects.add("You're welcome");
                } else {
                    listOfObjects.add(botScript.Bye_List());
                }
                manageSession manageSession = new manageSession(context, msgRecyclerView, chatList, msgAdapter);
                manageSession.displayChat(msgRecyclerView, listOfObjects);
            } else {
                updateBotReply updateBotReply = new updateBotReply(context);
                updateBotReply.PredictUserReturnBotReply(userMsg);
            }

        } else {
            Toast.makeText(context, "You have to type something first!", Toast.LENGTH_SHORT).show();
        }
    }

    public static void updateIsTyping() {
        isTyping.setText("");
        isTyping.setCharacterDelay(200);
        isTyping.animateText(isTypingText);
    }

    public void saveChatStatus() {
        globalVariables.backURL = "chatPage";
        globalVariables.globalRecyclerView = msgRecyclerView;
        globalVariables.globalChatList = chatList;
        globalVariables.globalMsgAdapter = msgAdapter;
    }

    private void clearChatStatus() {
        int size = globalVariables.globalChatList.size();
        if (size > 0) {
            globalVariables.globalChatList.clear();
            globalVariables.globalMsgAdapter.notifyItemRangeRemoved(0, size);
        }
    }

}





