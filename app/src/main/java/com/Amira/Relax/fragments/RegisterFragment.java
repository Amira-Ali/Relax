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


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private EditText username, userpass;
    private DatabaseHelper databaseHelper;
    private String name = "";
    private String password = "";

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        username = v.findViewById(R.id.et_name);
        userpass = v.findViewById(R.id.et_password);
        Button b = v.findViewById(R.id.btn_register);
        databaseHelper = new DatabaseHelper(getActivity());

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!username.getText().toString().equals("") && !userpass.getText().toString().equals("")) {
                    name = username.getText().toString();
                    password = userpass.getText().toString();

                    String status = databaseHelper.RegisterNewUser(name, password);
                    if (status!=null) {
                        Toast.makeText(getActivity(), "Registration Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), Home.class);
                        intent.putExtra("username", username.getText().toString());
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "Registration Failed, Details: ", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Insert Name and password", Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }
}
