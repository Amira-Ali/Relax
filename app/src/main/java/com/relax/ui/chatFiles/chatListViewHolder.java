package com.relax.ui.chatFiles;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.relax.R;


public class chatListViewHolder extends RecyclerView.ViewHolder {

    LinearLayout leftMsgLayout, horizontalBtns;

    LinearLayout rightMsgLayout;

    TextView leftMsgTextView;

    TextView rightMsgTextView;

    ImageView botImgView;

    public chatListViewHolder(View itemView) {
        super(itemView);
        leftMsgLayout = itemView.findViewById(R.id.chat_left_msg_layout);
        rightMsgLayout = itemView.findViewById(R.id.chat_right_msg_layout);
        leftMsgTextView = itemView.findViewById(R.id.chat_left_msg_text_view);
        rightMsgTextView = itemView.findViewById(R.id.chat_right_msg_text_view);
        botImgView = itemView.findViewById(R.id.imgView);
        horizontalBtns = itemView.findViewById(R.id.horizontalBtns);
    }
}