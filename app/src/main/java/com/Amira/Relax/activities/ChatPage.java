package com.Amira.Relax.activities;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.Amira.Relax.R;
import com.Amira.Relax.ui.chatFiles.ChatAppMsgAdapter;
import com.Amira.Relax.ui.chatFiles.ChatAppMsgDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatPage extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Live Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            // Get RecyclerView object.
            final RecyclerView msgRecyclerView = findViewById(R.id.chat_recycler_view);

            // Set RecyclerView layout manager.
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            msgRecyclerView.setLayoutManager(linearLayoutManager);

            Python py = Python.getInstance();
            PyObject module = py.getModule("lito");//python file
            if (!Python.isStarted()) {
                Python.start(new AndroidPlatform(this));
            }

            PyObject obj = module.callAttr("start_bot");

            // Create the initial data list.
            final List<ChatAppMsgDTO> msgDtoList = new ArrayList<>();
            ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, obj.toString());
            msgDtoList.add(msgDto);

            // Create the data adapter with above data list.
            final ChatAppMsgAdapter chatAppMsgAdapter = new ChatAppMsgAdapter(msgDtoList);

            // Set data adapter to RecyclerView.
            msgRecyclerView.setAdapter(chatAppMsgAdapter);
            final EditText msgInputText = findViewById(R.id.userInput);

            Button msgSendButton = findViewById(R.id.btnSendMsg);

            msgSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String msgContent = msgInputText.getText().toString();
                    if (!TextUtils.isEmpty(msgContent)) {
                        // Add a new sent message to the list.
                        ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, msgContent);
                        msgDtoList.add(msgDto);

                        int newMsgPosition = msgDtoList.size() - 1;

                        // Notify recycler view insert one new data.
                        chatAppMsgAdapter.notifyItemInserted(newMsgPosition);

                        // Scroll RecyclerView to the last message.
                        msgRecyclerView.scrollToPosition(newMsgPosition);

                        Python py = Python.getInstance();
                        PyObject module = py.getModule("lito");//python file
                        PyObject obj = module.callAttr("start_chat",msgContent);
                        ChatAppMsgDTO botmsg = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, obj.toString());
                        msgDtoList.add(botmsg);
                        newMsgPosition = msgDtoList.size() - 1;
//                        // Notify recycler view insert one new data.
                        chatAppMsgAdapter.notifyItemInserted(newMsgPosition);
//                        // Scroll RecyclerView to the last message.
                        msgRecyclerView.scrollToPosition(newMsgPosition);

                        // Empty the input edit text box.
                        msgInputText.setText("");


                    }
                }
            });
        } catch (Exception ex) {
           Log.e("python error",ex.toString());
        }
    }

}


