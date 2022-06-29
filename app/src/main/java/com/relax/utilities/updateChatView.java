package com.relax.utilities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.chaquo.python.PyException;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.relax.activities.chatPage;
import com.relax.ui.chatFiles.chatListAdapter;
import com.relax.ui.chatFiles.chatMsgClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class updateChatView extends chatPage {

    @SuppressWarnings("unchecked")
    public static void appendBotMessage(Object bot_msg, RecyclerView msgRecyclerView, List<chatMsgClass> chatList, chatListAdapter msgAdapter) {
        Context context = msgRecyclerView.getContext();
        chatMsgClass sentMsgObj = new chatMsgClass();

        if (bot_msg instanceof Map) {
            for (Map.Entry<String, String> entry : ((Map<String, String>) bot_msg).entrySet()) {
                AppendUrl(entry.getKey(), entry.getValue(), msgRecyclerView, chatList, msgAdapter);
            }
        } else {
            if (bot_msg instanceof String) {
                String botMsg = (String) bot_msg;
                globalVariables.lastBotMsg = botMsg;
                String user_msg = globalVariables.lastUSerMsg.toLowerCase();
                if (botMsg.length() > 100) {//if > 100 character, then save it into database because it's probably a summarization not just an ordinary statement
                    //update bot database by bot msg
                    try {
                        if (!Python.isStarted()) {
                            Python.start(new AndroidPlatform(context));
                        }
                        Python py = Python.getInstance();
                        py.getModule("chat").callAttr("learn_bot_entry", user_msg, botMsg);

                        //update Statement_session table after inserting the new statement
                        com.relax.utilities.dbHelper dbHelper = new dbHelper(context);

                        int statementID = dbHelper.getLastStatementID("Bot");
                        if (globalVariables.sessionID != 0 && statementID != 0) {
                            dbHelper.insertStatementSession(globalVariables.sessionID, statementID);
                        }

                    } catch (PyException e) {
                        Toast.makeText(msgRecyclerView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                sentMsgObj = new chatMsgClass(chatMsgClass.received_Bot_Msg, (String) bot_msg);

            } else if (bot_msg instanceof Drawable) {
                sentMsgObj = new chatMsgClass(chatMsgClass.botImgView, (Drawable) bot_msg);
            } else if (bot_msg instanceof ArrayList) {
                sentMsgObj = new chatMsgClass(chatMsgClass.listOfButtons, (ArrayList<Button>) bot_msg);
            }

            chatList.add(sentMsgObj);
            int pos = chatList.size() - 1;
            msgAdapter.notifyItemInserted(pos);
            msgRecyclerView.setAdapter(msgAdapter);
            msgRecyclerView.scrollToPosition(pos);

        }
    }

    public static void AppendUrl(String searchKey, String searchValue, RecyclerView msgRecyclerView, List<chatMsgClass> chatList, chatListAdapter msgAdapter) {
        chatMsgClass sentMsgObj = new chatMsgClass(chatMsgClass.googleResults, searchKey + "@" + searchValue);
        chatList.add(sentMsgObj);
        int pos = chatList.size() - 1;
        msgAdapter.notifyItemInserted(pos);
        msgRecyclerView.setAdapter(msgAdapter);
        msgRecyclerView.scrollToPosition(pos);
        //sleep here for 2 seconds
    }

    public static void AppendUserMsg(String user_msg, RecyclerView msgRecyclerView, List<chatMsgClass> chatList, chatListAdapter msgAdapter) {

        Context context = msgRecyclerView.getContext();
        globalVariables.lastUSerMsg = user_msg;
        String bot_msg = globalVariables.lastBotMsg;
        String Persona = globalVariables.userName;

        //update bot database by user msg
        try {
            if (!Python.isStarted()) {
                Python.start(new AndroidPlatform(context));
            }
            Python py = Python.getInstance();
            PyObject module = py.getModule("chat");
            module.callAttr("learn_user_entry", user_msg, bot_msg, Persona);
            //update Statement_session table after inserting the new statement
            com.relax.utilities.dbHelper dbHelper = new dbHelper(context);
            int statementID = dbHelper.getLastStatementID(globalVariables.userName);
            if (globalVariables.sessionID != 0 && statementID != 0) {
                dbHelper.insertStatementSession(globalVariables.sessionID, statementID);
            }
        } catch (PyException e) {
            Toast.makeText(msgRecyclerView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        chatMsgClass sentMsgObj = new chatMsgClass(chatMsgClass.sent_User_Msg, user_msg);
        chatList.add(sentMsgObj);
        int pos = chatList.size() - 1;
        msgAdapter.notifyItemInserted(pos);
        msgRecyclerView.setAdapter(msgAdapter);
        msgRecyclerView.scrollToPosition(pos);
    }


}
