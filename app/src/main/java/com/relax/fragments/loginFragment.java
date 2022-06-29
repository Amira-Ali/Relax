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

public class loginFragment extends Fragment {

    EditText loginUserName, loginUserPass;
    String username, userpass = "";
    dbHelper dbHelper;

    public loginFragment() {
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new dbHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        loginUserName = v.findViewById(R.id.loginUserName);
        loginUserName.requestFocus();
        loginUserPass = v.findViewById(R.id.loginUserPass);
        Button btn_login = v.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(v1 -> checkUser());
        dbHelper.close();
        return v;
    }

    void checkUser() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        int userID;
        if (!loginUserName.getText().toString().isEmpty() && (!loginUserPass.getText().toString().isEmpty())) {
            username = loginUserName.getText().toString().trim();
            userpass = loginUserPass.getText().toString().trim();
            userID = dbHelper.getUserID(username, userpass);
            if (userID != 0) {
                globalVariables.userID = userID;
                globalVariables.IsSurvey = dbHelper.checkUserSurvey(globalVariables.userID);
                globalVariables.userName = username;
                globalVariables.currentDate = String.valueOf(LocalDate.now());
                globalVariables.sessionStart = String.valueOf(LocalTime.now().format(formatter));
                setPreference();
                dbHelper.insertNewSession(userID);
                Intent intent = new Intent(getActivity(), Home.class);
                startActivity(intent);
                requireActivity().finish();
            } else {
                Toast.makeText(getActivity(), "username \\ password are incorrect!", Toast.LENGTH_LONG).show();
                ClrFocus();
            }

        } else {
            Toast.makeText(getActivity(), "username and password are both required!", Toast.LENGTH_LONG).show();
            ClrFocus();
        }
    }

    private void ClrFocus() {
        loginUserName.setText("");
        loginUserPass.setText("");
        loginUserName.requestFocus();
    }

    public void setPreference() {
        globalVariables.sharedpreferences = requireActivity().getSharedPreferences(globalVariables.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = globalVariables.sharedpreferences.edit();
        editor.putString(globalVariables.Name, globalVariables.userName);
        editor.apply();
    }
}
