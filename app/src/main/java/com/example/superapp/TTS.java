package com.example.superapp;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TTS {

    private TextToSpeech textToSpeech;
    private boolean ttsEnabled;
    private InitListener initListener;
    static  private TTS self;

    public interface InitListener {
        void initCompleted(boolean success);
    }

    private TTS(Context context, InitListener initListener) {
        textToSpeech = new TextToSpeech(context, onInitListener);
        this.initListener = initListener;
        ttsEnabled = false;
    }

    public static TTS get(Context context, InitListener initListener) {
        if (self == null) {
            self = new TTS(context, initListener);
        }
        return self;
    }

    private TextToSpeech.OnInitListener onInitListener = (initStatus) -> {
        if (initStatus == TextToSpeech.SUCCESS) {
            if (textToSpeech.isLanguageAvailable(new Locale(Locale.getDefault().getLanguage()))
                    == TextToSpeech.LANG_AVAILABLE) {
                textToSpeech.setLanguage(new Locale(Locale.getDefault().getLanguage()));
            } else {
                textToSpeech.setLanguage(Locale.US);
            }
            textToSpeech.setPitch(1.3f);
            textToSpeech.setSpeechRate(0.7f);
            ttsEnabled = true;
        } else if (initStatus == TextToSpeech.ERROR) {
            ttsEnabled = false;
        }
        if (initListener != null) {
            initListener.initCompleted(ttsEnabled);
        }
    };

    public void speak(String text) {
        if (ttsEnabled) {
            String utteranceId = this.hashCode() + "";
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD,null,  utteranceId);
        }
    }

    public void addInitListener(InitListener initListener) {
        this.initListener = initListener;
    }

    public boolean isTtsEnabled() {
        return ttsEnabled;
    }
}
