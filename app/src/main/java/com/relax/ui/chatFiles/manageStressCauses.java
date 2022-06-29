package com.relax.ui.chatFiles;

import android.content.Context;
import android.os.Handler;

import com.relax.utilities.dbHelper;
import com.relax.utilities.globalVariables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class manageStressCauses {

    Context context;
    com.relax.utilities.dbHelper dbHelper;
    int userID= globalVariables.userID;
    List<Object> listOfObjects;

    public manageStressCauses(Context context) {
        this.context = context;
        dbHelper = new dbHelper(context);
    }

    public void Populate_Flag_Map() {
        List<String> Flags = Arrays.asList("L", "M", "H");

        if (Flags.contains(globalVariables.sleepFlag)) {
            globalVariables.Flag_map.put("sleep", false);
        }
        if (Flags.contains(globalVariables.emotionalFlag)) {
            globalVariables.Flag_map.put("emotion", false);
        }
        if (Flags.contains(globalVariables.physicalFlag)) {
            globalVariables.Flag_map.put("physical", false);
        }
        if (Flags.contains(globalVariables.behaviorFlag)) {
            globalVariables.Flag_map.put("behavior", false);
        }
    }

    public void UpdateFlagMapDB(String StressCause) {
        //sleep,emotion,physical,behavior
        // save problem has been solved, so bot will not try to tackle it next session
        for (Map.Entry<String, Boolean> entry : globalVariables.Flag_map.entrySet()) {
            if (entry.getKey().equals(StressCause)) {
                entry.setValue(true);
            }
        }
        switch (StressCause) {
            case "sleep":
                dbHelper.updateUserFlags(userID, 0, 1, 0, 0);
                break;

            case "emotion":
                dbHelper.updateUserFlags( userID,0, 0, 0, 1);
                break;

            case "physical":
                dbHelper.updateUserFlags( userID,1, 0, 0, 0);
                break;

            case "behavior":
                dbHelper.updateUserFlags( userID,0, 0, 1, 0);
                break;
        }
    }

    public void CrossOffTheList(String stressCause) {
        UpdateFlagMapDB(stressCause);

        listOfObjects = new ArrayList<>();
        listOfObjects.add("hmm...interesting! your survey results show the other way around.");
        listOfObjects.add("Anyway, I'm just a bot. I make mistakes occasionally, I hope you still trust me!");
        manageSession manageSession = new manageSession(context, globalVariables.globalRecyclerView, globalVariables.globalChatList, globalVariables.globalMsgAdapter);
        manageSession.displayChat(globalVariables.globalRecyclerView, listOfObjects);

        Runnable r = () -> manageSession.loopThroughStressPointsOrTalk(globalVariables.globalRecyclerView);
        android.os.Handler h = new Handler();
        h.postDelayed(r, 7000);
    }

}
