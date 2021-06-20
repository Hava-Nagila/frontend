package com.example.superapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.superapp.R;
import com.example.superapp.TTS;

public class StartActivity extends AppCompatActivity {

    private TTS tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ImageView logo = findViewById(R.id.logo);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        Animation animationAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        TextView text = findViewById(R.id.text);
        text.startAnimation(animationAlpha);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                tts = TTS.get(getApplicationContext(), (s) -> {
                    if (s) {
                        startActivity(MainActivity.newIntent(StartActivity.this));
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        logo.startAnimation(animation);

    }
}