package com.relax.ui.chatFiles;

import android.graphics.drawable.Drawable;
import android.widget.Button;

import java.util.ArrayList;

public class chatMsgClass {

    public final static String sent_User_Msg = "MSG_TYPE_SENT";

    public final static String received_Bot_Msg = "MSG_TYPE_RECEIVED";

    public final static String googleResults = "Google_Results";

    public final static String botImgView = "botImgView";

    public final static String listOfButtons = "listOfButtons";

    // Message content.
    private String msgContent;

    // Message type.
    private String msgType;

    //message resource
    private Drawable msgImg;

    private ArrayList<Button> listOfActualButtons;

    public chatMsgClass() {

    }

    public chatMsgClass(String msgType, String msgContent) {
        this.msgType = msgType;
        this.msgContent = msgContent;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public chatMsgClass(String msgType, Drawable drawable) {
        this.msgType = msgType;
        this.msgImg = drawable;
    }

    public void setMsgImg(Drawable drawable) {
        this.msgImg = drawable;
    }

    public Drawable getMsgImg() {
        return msgImg;
    }

    public chatMsgClass(String msgType, ArrayList<Button> buttons) {
        this.msgType = msgType;
        this.listOfActualButtons = buttons;
    }

    public void setListOfActualButtons(ArrayList<Button> buttons) {
        this.listOfActualButtons = buttons;
    }

    public ArrayList<Button> getListOfActualButtons() {
        return listOfActualButtons;
    }



}
