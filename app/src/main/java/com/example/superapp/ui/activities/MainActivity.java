package com.example.superapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.superapp.R;
import com.example.superapp.TTS;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SpeechRecognizer recognize;
    boolean isListening = false;
    TTS tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tts = TTS.get(this, null);
        ImageView button = findViewById(R.id.button);

        recognize = SpeechRecognizer.createSpeechRecognizer(this);
        recognize.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
            Log.d("a", "begin");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                Log.d("a", "end");

            }

            @Override
            public void onError(int i) {
                tts.speak("Ошибка ввода");
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> output = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Log.d("d", String.valueOf(output));
               // Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
                String fullstring = "";
                for (String str : output) {
                    fullstring = fullstring.concat(str).concat(" ");
                };
                System.out.println(fullstring);
                if (fullstring.contains("курс")) {
                    tts.speak("Уже ищу!");
                    startActivity(new Intent(MainActivity.this, PagerActivity.class));
                } else if(fullstring.contains("настройки")){
                    tts.speak("Перехожу в настройки!");
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                }
                else
                    tts.speak("Извините, я не поняла Вас");
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe02154F3, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });


        ImageView image = findViewById(R.id.arcs);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
        image.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.startAnimation(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void buttonEffect(View button) {

     //   startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        startActivity(new Intent(MainActivity.this, PagerActivity.class));
        recognize.startListening(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }
}