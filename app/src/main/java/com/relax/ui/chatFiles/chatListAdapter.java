package com.relax.ui.chatFiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.relax.R;
import com.relax.activities.Home;
import com.relax.activities.addNote;
import com.relax.activities.recommendation_Breathing;
import com.relax.activities.recommendation_Coping;
import com.relax.activities.recommendation_Journaling;
import com.relax.activities.recommendation_Nutrition;
import com.relax.activities.recommendation_SelfEsteem;
import com.relax.activities.recommendation_Sleeping;
import com.relax.activities.recommendation_Spiritual;
import com.relax.activities.recommendation_Sport;
import com.relax.activities.surveyPhysical;
import com.relax.activities.timePicker;
import com.relax.utilities.asyncGoogle;
import com.relax.utilities.dbHelper;
import com.relax.utilities.globalVariables;
import com.relax.utilities.updateChatView;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class chatListAdapter extends RecyclerView.Adapter<chatListViewHolder> {

    private final WeakReference<Context> context;
    private final List<chatMsgClass> chatList;
    List<Object> listOfObjects;
    ArrayList<Button> buttonArrayList;
    String seeList = "See list";
    String SearchTerm;
    com.relax.utilities.dbHelper dbHelper;


    public chatListAdapter(List<chatMsgClass> chatList, Context context) {
        super();
        this.chatList = chatList;
        this.context = new WeakReference<>(context);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (chatList == null) {
            return 0;
        }
        return chatList.size();
    }

    @Override
    public void onBindViewHolder(@NotNull chatListViewHolder holder, int position) {
        chatMsgClass msgDto = this.chatList.get(position);
        holder.setIsRecyclable(false);

        recommendationClass recommendationClass = new recommendationClass(context.get(), globalVariables.globalRecyclerView, globalVariables.globalChatList, globalVariables.globalMsgAdapter);
        manageStressCauses manageStressCauses = new manageStressCauses(context.get());
        manageSession manageSession = new manageSession(context.get(), globalVariables.globalRecyclerView, globalVariables.globalChatList, globalVariables.globalMsgAdapter);

        // If the message is a received message == Bot msg
        if (chatMsgClass.received_Bot_Msg.equals(msgDto.getMsgType())) {
            // Show received message in left linearlayout.
            holder.leftMsgLayout.setVisibility(LinearLayout.VISIBLE);
            holder.leftMsgTextView.setVisibility(View.VISIBLE);
            holder.leftMsgTextView.setText(msgDto.getMsgContent());
            // Remove right linearlayout.The value should be GONE, can not be INVISIBLE
            // Otherwise each item view's distance is too big.
            holder.rightMsgLayout.setVisibility(LinearLayout.GONE);
        }

        // If the message is a sent message == User msg
        else if (chatMsgClass.sent_User_Msg.equals(msgDto.getMsgType())) {
            // Show sent message in right linearlayout.
            holder.rightMsgLayout.setVisibility(LinearLayout.VISIBLE);
            holder.rightMsgTextView.setText(msgDto.getMsgContent());
            // Remove left linearlayout.The value should be GONE, can not be INVISIBLE
            // Otherwise each item view's distance is too big.
            holder.leftMsgLayout.setVisibility(LinearLayout.GONE);
        }

        // If bot message is img
        else if (chatMsgClass.botImgView.equals(msgDto.getMsgType())) {
            // Show received message in left linearlayout.
            holder.leftMsgLayout.setVisibility(LinearLayout.VISIBLE);
            holder.botImgView.setImageDrawable(msgDto.getMsgImg());
            holder.botImgView.setVisibility(View.VISIBLE);
            holder.leftMsgTextView.setVisibility(View.GONE);
            holder.rightMsgLayout.setVisibility(LinearLayout.GONE);
        }

        //bot msg is list of buttons
        else if (chatMsgClass.listOfButtons.equals(msgDto.getMsgType())) {
            holder.rightMsgLayout.setVisibility(LinearLayout.GONE);
            holder.leftMsgTextView.setVisibility(View.GONE);
            holder.botImgView.setVisibility(View.GONE);

            for (Button btn : msgDto.getListOfActualButtons()) {
                String ButtonName = btn.getText().toString();
                btn.setTextColor(Color.WHITE);
                btn.setTextSize(12);
                RelativeLayout.LayoutParams btnLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                btnLayout.setMargins(10, 6, 10, 6);
                btn.setLayoutParams(btnLayout);

                if (holder.leftMsgLayout != null) {
                    holder.leftMsgLayout.setVisibility(LinearLayout.VISIBLE);
                    holder.horizontalBtns.setVisibility(View.VISIBLE);
                    //these buttons were created in the chatPage class, so the current parent is the chatPage Class
                    //but we need to add them to horizontal Btns, not the ChatPAge. therefore we had to remove the child from it's current parent.
                    if (btn.getParent() != null) {
                        ((ViewGroup) btn.getParent()).removeView(btn);
                    }
                    holder.horizontalBtns.addView(btn);//chat_left_msg_layout
                }

                btn.setOnClickListener(v -> {
                    updateChatView.AppendUserMsg(ButtonName, globalVariables.globalRecyclerView, globalVariables.globalChatList, globalVariables.globalMsgAdapter);
                    globalVariables.lastUserChoice = ButtonName;

                    Intent intent;
                    switch (ButtonName) {

                        case "Take survey":
                            intent = new Intent(context.get(), surveyPhysical.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.get().startActivity(intent);
                            break;

                        case "recommendation_Nutrition":
                            intent = new Intent(context.get(), recommendation_Nutrition.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.get().startActivity(intent);
                            break;

                        case "Exercise":
                            intent = new Intent(context.get(), recommendation_Sport.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.get().startActivity(intent);
                            break;

                        case "recommendation_Sleeping":
                            intent = new Intent(context.get(), recommendation_Sleeping.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.get().startActivity(intent);
                            break;

                        case "recommendation_Breathing":
                            intent = new Intent(context.get(), recommendation_Breathing.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.get().startActivity(intent);
                            break;

                        case "Face changes":
                            intent = new Intent(context.get(), recommendation_Coping.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.get().startActivity(intent);
                            break;

                        case "Self esteem":
                            intent = new Intent(context.get(), recommendation_SelfEsteem.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.get().startActivity(intent);
                            break;

                        case "See list":
                            intent = new Intent(context.get(), recommendation_Spiritual.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.get().startActivity(intent);
                            break;

                        case "Set time":
                            intent = new Intent(context.get(), timePicker.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.get().startActivity(intent);
                            break;

                        case "New chat"://answer to You like to start a new chat or pick up where we left off?
                        case "Just talk"://answer to would you prefer to chat or take survey?
                            manageSession.justTalk(globalVariables.globalRecyclerView);
                            break;

                        case "Resume chat":
                            manageSession.resumeChat(globalVariables.globalRecyclerView);
                            break;

                        case "No thanks":
                           // manageSession.hideChatBox();
                            listOfObjects = new ArrayList<>();
                            listOfObjects.add("It's okay, I'll go now but I'll always be here for you.");
                            listOfObjects.add("Just say (Lito) and I'll be there.");
                            listOfObjects.add("See you later, bye!");
                            buttonArrayList = new ArrayList<>();
                            Button button = new Button(context.get());
                            button.setBackgroundResource(R.drawable.btn_custom);
                            button.setText(R.string.bye);
                            buttonArrayList.add(button);
                            listOfObjects.add(buttonArrayList);

                            manageSession.displayChat(globalVariables.globalRecyclerView, listOfObjects);
                            break;

                        case "bye-bye":
                            intent = new Intent(context.get(), Home.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.get().startActivity(intent);
                            break;

                        case "A little"://answer to Am I right? ==> after taking survey
                            switch (globalVariables.stressCause) {
                                case "physical":
                                    recommendationClass.showPhysicalRecommendations(manageSession);
                                    break;

                                case "emotion":
                                    recommendationClass.showEmotionRecommendations(manageSession);
                                    break;

                                case "sleep":
                                    recommendationClass.showSleepRecommendations(manageSession);
                                    break;

                                case "behavior":
                                    recommendationClass.showBehaviorRecommendations(manageSession);
                                    break;
                            }
                            break;

                        case "No, I'm fine"://answer to Am I right? ==> after taking survey
                            manageStressCauses.CrossOffTheList(globalVariables.stressCause);
                            break;

                        case "Kind of"://answer for was that helpful?
                        case "I'm ok"://answer to AfterGoogle question
                            listOfObjects = new ArrayList<>();
                            listOfObjects.add("Happy to help!");
                            manageSession.displayChat(globalVariables.globalRecyclerView, listOfObjects);
                            manageStressCauses.UpdateFlagMapDB(globalVariables.stressCause);
                            manageSession.loopThroughStressPointsOrTalk(globalVariables.globalRecyclerView);
                            break;

                        case "I guess"://Did u like journaling?
                            listOfObjects = new ArrayList<>();
                            listOfObjects.add("Oh, great! okay then, here is another idea that will make journaling even more appealing to you;");
                            listOfObjects.add("Try always to end your journaling sessions with a few words about potential solutions to your problems,");
                            listOfObjects.add("Things you appreciate in your life, or things that give you hope in life.");
                            listOfObjects.add("Now that you've known all about journaling, do it regularly and you'll sense it's affect on your peace of mind.");
                            manageSession.displayChat(globalVariables.globalRecyclerView, listOfObjects);
                            Handler h = new Handler();
                            Runnable runnable = () -> {
                                manageStressCauses.UpdateFlagMapDB(globalVariables.stressCause);
                                manageSession.loopThroughStressPointsOrTalk(globalVariables.globalRecyclerView);
                            };
                            h.postDelayed(runnable, 17000);
                            break;

                        case "Not much"://Did u like journaling?
                            listOfObjects = new ArrayList<>();
                            listOfObjects.add("Okay, no problem.");
                            listOfObjects.add("Let me just say that I strongly advice you to keep doing it and to do it honestly and openly as much as you can.");
                            listOfObjects.add("No one will read this but you, so please use the journaling feature in the app and I promise you, over time you'll sense it's affect on your peace of mind.");
                            listOfObjects.add("Now let's try another tactic to left up your spirits.");
                            listOfObjects.add("I have a list of Surah and Hadith of the Prophet Muhammad that I specially collected to heal wounds of troubled souls like you.");
                            listOfObjects.add("Click next button to see my list:");
                            buttonArrayList = new ArrayList<>();
                            Button myButton = new Button(context.get());
                            myButton.setBackgroundResource(R.drawable.btn_custom);
                            myButton.setText(seeList);
                            buttonArrayList.add(myButton);
                            listOfObjects.add(buttonArrayList);
                            manageSession.displayChat(globalVariables.globalRecyclerView, listOfObjects);
                            break;

                        case "Nope"://answer for was this helpful?
                        case "Umm..."://answer for Do you promise to go through them at least once a day?

                            switch (globalVariables.stressCause) {
                                case "physical":
                                    SearchTerm = "physical health";
                                    break;

                                case "emotion":
                                    SearchTerm = "how to solve emotional problems";
                                    break;

                                case "behavior":
                                    SearchTerm = "boost your self esteem";
                                    break;

                                case "sleep":
                                    SearchTerm = "solve sleep problems";
                                    break;

                                default:
                                    SearchTerm = "how to be happy";
                                    break;
                            }

                            try {
                                manageSession.saveChatStatus();
                                asyncGoogle asyncGoogle = new asyncGoogle(SearchTerm, context.get());
                                asyncGoogle.execute();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;

                        case "Yes"://
                            manageSession.yes(globalVariables.globalRecyclerView);
                            break;

                        case "No"://
                            manageSession.no(globalVariables.globalRecyclerView);
                            break;

                        case "Call help"://answer to AfterGoogle question
                            manageSession.callSOS();
                            manageSession.afterSOS(globalVariables.globalRecyclerView);
                            break;

                        case "I promise"://answer for Do you promise to go through them at least once a day?
                            listOfObjects = new ArrayList<>();
                            listOfObjects.add("Great, so we'll check off emotion issues from your stress causes list.");
                            listOfObjects.add("Of course that doesn't mean you can't come talk to me about it or about anything else if that matters.");
                            manageSession.displayChat(globalVariables.globalRecyclerView, listOfObjects);

                            h = new Handler();
                            runnable = () -> {
                                manageStressCauses.UpdateFlagMapDB(globalVariables.stressCause);
                                manageSession.loopThroughStressPointsOrTalk(globalVariables.globalRecyclerView);
                            };
                            h.postDelayed(runnable, 7000);
                            break;

                        case "What is it?"://try journaling now? answer #1
                            intent = new Intent(context.get(), recommendation_Journaling.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.get().startActivity(intent);
                            break;

                        case "Try it"://try journaling now? answer #2
                            intent = new Intent(context.get(), addNote.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.get().startActivity(intent);
                            break;

                        case "Skip it"://try journaling now? answer #3
                            listOfObjects = new ArrayList<>();
                             listOfObjects.add("Okay, let's try another tactic to left up your spirits.");
                             listOfObjects.add("I'll show you a list of Surah and Hadith of the Prophet Muhammad that I specially collected to heal wounds of troubled souls like you.");
                             listOfObjects.add("Don't worry, we'll come back here later.");
                            buttonArrayList = new ArrayList<>();
                            button = new Button(context.get());
                            button.setBackgroundResource(R.drawable.btn_custom);
                            button.setText(seeList);
                            buttonArrayList.add(button);
                            listOfObjects.add(buttonArrayList);
                            manageSession.displayChat(globalVariables.globalRecyclerView, listOfObjects);
                            break;
                    }

                });

            }

        }

        //if the message is google results from searching a specific term
        else if (chatMsgClass.googleResults.equals(msgDto.getMsgType())) {//searchKey + "@" + searchValue
            holder.leftMsgTextView.setVisibility(LinearLayout.VISIBLE);
            holder.leftMsgTextView.setMovementMethod(LinkMovementMethod.getInstance());

            //split string on [@] and extract key, val
            String url = msgDto.getMsgContent();
            String[] parts = url.split("@", 2);
            String key = parts[0]; // Title
            String val = parts[1]; // href
            Spanned txt = Html.fromHtml("<a href=\"" + val + "\">" + key + "</a>", HtmlCompat.FROM_HTML_MODE_LEGACY);
            holder.leftMsgTextView.setText(txt);

            holder.rightMsgLayout.setVisibility(LinearLayout.GONE);
        }
    }

    @NotNull
    @Override
    public chatListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.activity_chat_item, parent, false);
        dbHelper = new dbHelper(v.getContext());
        return new chatListViewHolder(v);
    }


}