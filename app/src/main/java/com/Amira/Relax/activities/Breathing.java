package com.Amira.Relax.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.Amira.Relax.R;

import pl.droidsonroids.gif.GifImageView;

public class Breathing extends AppCompatActivity {
    private GifImageView play_btn;
    private MediaPlayer player;
    private final Handler myHandler = new Handler();
    boolean paused = true;

    private GifImageView play_btn2;
    private MediaPlayer player2;
    private final Handler myHandler2 = new Handler();
    boolean paused2 = true;

    private GifImageView play_btn3;
    private MediaPlayer player3;
    private final Handler myHandler3 = new Handler();
    boolean paused3 = true;

    private GifImageView play_btn4;
    private MediaPlayer player4;
    private final Handler myHandler4 = new Handler();
    boolean paused4 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        play_btn = findViewById(R.id.play);
        play_btn.setImageResource(R.drawable.play);
        player = MediaPlayer.create(this, R.raw.exercise1);

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paused) {
                    player.start();
                    paused = false;
                    play_btn.setImageResource(R.drawable.pause);
                    player.getDuration();
                    player.getCurrentPosition();
                    myHandler.postDelayed(UpdateSongTime, 100);
                } else {//when MediaPlayer paused, StartButton is shown
                    if (player != null && player.isPlaying()) {
                        player.pause();
                        paused = true;
                        play_btn.setImageResource(R.drawable.play);
                    }
                }
            }
        });

        play_btn2 = findViewById(R.id.play2);
        play_btn2.setImageResource(R.drawable.play);
        player2 = MediaPlayer.create(this, R.raw.exercise2);

        play_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paused2) {
                    player2.start();
                    paused2 = false;
                    play_btn2.setImageResource(R.drawable.pause);
                    player2.getDuration();
                    player2.getCurrentPosition();
                    myHandler2.postDelayed(UpdateSongTime2, 100);
                } else {//when MediaPlayer paused, StartButton is shown
                    if (player2 != null && player2.isPlaying()) {
                        player2.pause();
                        paused2 = true;
                        play_btn2.setImageResource(R.drawable.play);
                    }
                }
            }
        });


        play_btn3 = findViewById(R.id.play3);
        play_btn3.setImageResource(R.drawable.play);
        player3 = MediaPlayer.create(this, R.raw.exercise3);

        play_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paused3) {
                    player3.start();
                    paused3 = false;
                    play_btn3.setImageResource(R.drawable.pause);
                    player3.getDuration();
                    player3.getCurrentPosition();
                    myHandler3.postDelayed(UpdateSongTime3, 100);
                } else {//when MediaPlayer paused, StartButton is shown
                    if (player3 != null && player3.isPlaying()) {
                        player3.pause();
                        paused3 = true;
                        play_btn3.setImageResource(R.drawable.play);
                    }
                }
            }
        });


        play_btn4 = findViewById(R.id.play4);
        play_btn4.setImageResource(R.drawable.play);
        player4 = MediaPlayer.create(this, R.raw.exercise4);

        play_btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paused4) {
                    player4.start();
                    paused4 = false;
                    play_btn4.setImageResource(R.drawable.pause);
                    player4.getDuration();
                    player4.getCurrentPosition();
                    myHandler4.postDelayed(UpdateSongTime4, 100);
                } else {//when MediaPlayer paused, StartButton is shown
                    if (player4 != null && player4.isPlaying()) {
                        player4.pause();
                        paused4 = true;
                        play_btn4.setImageResource(R.drawable.play);
                    }
                }
            }
        });


        Button btn = findViewById(R.id.btn_home);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.stop();
                player2.stop();
                player3.stop();
                player4.stop();
                Intent intent = new Intent(Breathing.this, Home.class);
                startActivity(intent);
            }
        });

    }

    protected void onDestroy() {
        super.onDestroy();
        // TODO Auto-generated method stub
        if (player != null) {
            player.release();
            player = null;
        }

        if (player2 != null) {
            player2.release();
            player2 = null;
        }

        if (player3 != null) {
            player3.release();
            player3 = null;
        }

        if (player4 != null) {
            player4.release();
            player4 = null;
        }
    }


    private final Runnable UpdateSongTime = new Runnable() {
        public void run() {
            player.getCurrentPosition();
            myHandler.postDelayed(this, 100);
        }
    };

    private final Runnable UpdateSongTime2 = new Runnable() {
        public void run() {
            player2.getCurrentPosition();
            myHandler2.postDelayed(this, 100);
        }
    };

    private final Runnable UpdateSongTime3 = new Runnable() {
        public void run() {
            player3.getCurrentPosition();
            myHandler3.postDelayed(this, 100);
        }
    };

    private final Runnable UpdateSongTime4 = new Runnable() {
        public void run() {
            player4.getCurrentPosition();
            myHandler4.postDelayed(this, 100);
        }
    };
}