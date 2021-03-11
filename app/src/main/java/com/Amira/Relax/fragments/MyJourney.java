package com.Amira.Relax.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.Amira.Relax.R;

public class MyJourney extends Fragment {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_myjourney, container, false);
        requireActivity().setTitle("xxxxx");

        return inflater.inflate(R.layout.fragment_myjourney, container, false);
    }
}
