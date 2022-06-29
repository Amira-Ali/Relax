package com.relax.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

import com.relax.R;
import com.relax.ui.chatFiles.manageSession;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class asyncGoogle extends AsyncTask<Void, Integer, Map<String, String>> {

    private final WeakReference<Context> context;
    String SearchTerm;
    List<Object> listOfObjects;
    ArrayList<Button> buttonArrayList;
    String[] btnNames;
    manageSession manageSession;

    public asyncGoogle(String searchTerm, Context context) {
        super();
        this.context = new WeakReference<>(context);
        this.SearchTerm = searchTerm;
        manageSession = new manageSession(context, globalVariables.globalRecyclerView, globalVariables.globalChatList, globalVariables.globalMsgAdapter);
    }

    @Override
    public Map<String, String> doInBackground(Void... voids) {
        Map<String, String> map;
        map = googleResults.result(SearchTerm);
        return map;
    }

    @Override
    protected void onPostExecute(Map<String, String> map) {
        if (map.size() == 0) {
            listOfObjects = new ArrayList<>();
            listOfObjects.add("Lost Internet Connection!");
            manageSession.displayChat(globalVariables.globalRecyclerView, listOfObjects);
        } else {
            listOfObjects = new ArrayList<>();
            listOfObjects.add("umm...");
            listOfObjects.add("okay, I have one last trick up my sleeve.");
            listOfObjects.add("Google!");
            listOfObjects.add("Next are top 5 google search results that relate to you problem:");
            listOfObjects.add(map);
            //ask if user is satisfied or maybe he'd like to call someone for help? provide him 2 buttons {satisfied - call sos}
            listOfObjects.add("Did these links help you?");
            listOfObjects.add("If no, then maybe you'd like to call someone for help?");
            buttonArrayList = new ArrayList<>();
            btnNames = new String[]{"I'm ok", "Call help"};
            for (String btnName : btnNames) {
                Button btn = new Button(context.get());
                btn.setBackgroundResource(R.drawable.btn_custom);
                btn.setText(btnName);
                buttonArrayList.add(btn);
            }
            listOfObjects.add(buttonArrayList);
            manageSession.displayChat(globalVariables.globalRecyclerView, listOfObjects);
        }

    }
}
