package com.Amira.Relax.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.Amira.Relax.utilities.DatabaseHelper;
import com.Amira.Relax.R;
import com.Amira.Relax.activities.Home;
import com.Amira.Relax.utilities.GlobalVariables;

public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        final EditText et_name = v.findViewById(R.id.et_name);
        et_name.setText("amira");
        final String userName=et_name.getText().toString();
        final EditText et_password = v.findViewById(R.id.et_password);
        et_password.setText("123");
        final String userPass= et_password.getText().toString();
        Button b = v.findViewById(R.id.btn_login);

        final DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check user login, if true redirect to home
                boolean isExist = databaseHelper.CheckUserExist(userName,userPass );

                if (isExist) {
//                    Toast.makeText(getActivity(), "username= "+ userName + "\npass= "+ userPass,
//                            Toast.LENGTH_LONG).show();
                    GlobalVariables.username=userName;
                    Intent intent = new Intent(getActivity(), Home.class);
                    //intent.putExtra("username", userName);
                    startActivity(intent);
                } else {
                    et_name.setText("");
                    et_name.requestFocus();
                    et_password.setText("");
                    Toast.makeText(getActivity(), "Login failed. Invalid username or password.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return v;
    }
}
