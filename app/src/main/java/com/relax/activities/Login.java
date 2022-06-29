package com.relax.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.relax.R;
import com.relax.fragments.loginFragment;
import com.relax.fragments.registerFragment;

import java.util.ArrayList;

public class Login extends AppCompatActivity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewPager viewPager = findViewById(R.id.viewPager);

        ClassPagerAdapter pagerAdapter = new ClassPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragmet(new loginFragment());
        pagerAdapter.addFragmet(new registerFragment());
        viewPager.setAdapter(pagerAdapter);

    }


    static class ClassPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragmentList = new ArrayList<>();

        ClassPagerAdapter(FragmentManager fm) {
            //noinspection deprecation
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragmet(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }
}




