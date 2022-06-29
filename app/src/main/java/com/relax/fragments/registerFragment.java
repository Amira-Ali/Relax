package com.relax.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.relax.R;
import com.relax.activities.Home;
import com.relax.utilities.dbHelper;
import com.relax.utilities.globalVariables;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class registerFragment extends Fragment {

    private EditText userName, userPass;

    public registerFragment() {
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        userName = v.findViewById(R.id.userName);
        userPass = v.findViewById(R.id.userPass);

        Button btn_register = v.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(v1 -> registerBtn());
        return v;
    }

    private void registerBtn() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        dbHelper databaseHelper = new dbHelper(getContext());
        if (!userName.getText().toString().equals("") && !userPass.getText().toString().equals("")) {
            String username = userName.getText().toString();
            int userID = databaseHelper.checkUserName(username);
            if (userID == 0) {//New User
                String userPass = this.userPass.getText().toString();
                String userJoiningDate = String.valueOf(LocalDate.now());
                userID = (int) databaseHelper.addUser(username, userPass, userJoiningDate);
                if (userID != -1) {
                    globalVariables.userID = userID;
                    globalVariables.userName = username;
                    globalVariables.currentDate = userJoiningDate;
                    globalVariables.sessionStart = String.valueOf(LocalTime.now().format(formatter));
                    setPreference();
                    databaseHelper.insertNewSession(userID);
                    Toast.makeText(getActivity(), "Registration Success!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Registration Failed, Please try again later!", Toast.LENGTH_SHORT).show();
                }
            } else {//User already exists
                Toast.makeText(getActivity(), "Registration Failed, User already exists!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Please Insert both of name and password", Toast.LENGTH_SHORT).show();
        }
        databaseHelper.close();
    }

    public void setPreference() {
        globalVariables.sharedpreferences = requireActivity().getSharedPreferences(globalVariables.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = globalVariables.sharedpreferences.edit();
        editor.putString(globalVariables.Name, globalVariables.userName);
        editor.apply();
    }

}
