package com.relax.utilities;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.relax.ui.chatFiles.chatListAdapter;
import com.relax.ui.chatFiles.chatMsgClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class globalVariables extends AppCompatActivity {

    public globalVariables() {
    }

    public static int userID;
    public static String userName;

    public static int physicalTotal = 0;
    public static int sleepTotal = 0;
    public static int behaviorTotal = 0;
    public static int emotionalTotal = 0;

    public static String physicalFlag = "";
    public static String sleepFlag = "";
    public static String behaviorFlag = "";
    public static String emotionalFlag = "";

    public static Map<String, Boolean> Flag_map = new HashMap<>();

    public static int physicalHandled = 0;
    public static int sleepHandled = 0;
    public static int behaviorHandled = 0;
    public static int emotionalHandled = 0;

    public static boolean IsSurvey = false;
    public static String backURL = "";

    public static RecyclerView globalRecyclerView;
    public static List<chatMsgClass> globalChatList = new ArrayList<>();
    public static chatListAdapter globalMsgAdapter;

    public static String notificationTime = "";

    public static String lastUserChoice = "";

    public static String stressCause = "";

    public static String sessionStart ="";

    public static String currentDate="";

    public static int botPrediction=2;//2 as a default for neutral score

    public static String lastUSerMsg="";

    public static String lastBotMsg ="";

    public static int sessionID=0;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";

    public static SharedPreferences sharedpreferences;

}
