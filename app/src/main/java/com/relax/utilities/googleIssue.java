package com.relax.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.relax.R;
import com.relax.ui.chatFiles.manageSession;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class googleIssue extends AsyncTask<Void, Integer, Map<String, String>> {

    public final WeakReference<Context> context;
    String SearchTerm;
    List<Object> listOfObjects;
    ArrayList<Button> buttonArrayList;
    String[] btnNames;
    manageSession manageSession;

    public googleIssue(String searchTerm, Context context, List<Object> listOfObjects) {
        super();
        this.context = new WeakReference<>(context);
        this.SearchTerm = searchTerm;
        this.listOfObjects = listOfObjects;
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
        String firstUrl = "";
        if (map.size() == 0) {
            listOfObjects.add("Lost Internet Connection!");
        } else {
            listOfObjects.add("Please take your time reading next lines and articles. I'm sure you'll find answers here:");
            //get first map entry
            for (Map.Entry<String, String> entry : map.entrySet()) {
                firstUrl = entry.getValue();
                //summarize first website content
                if (!Python.isStarted()) {
                    Python.start(new AndroidPlatform(context.get()));
                }
                Python py = Python.getInstance();
                PyObject module = py.getModule("scrape").callAttr("parseWebToText", firstUrl);
                listOfObjects.add(module.toString());//add summary to the listOfObjects
                break;
            }

            map.values().remove(firstUrl); //remove first entry from map

            // return the remaining 4 links in the listOfObjects
            //loop through map and append each item in a new string

            listOfObjects.add(map);
            listOfObjects.add("Were you able to benefit from these articles?");
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
        }
        manageSession.displayChat(globalVariables.globalRecyclerView, listOfObjects);

    }
}
