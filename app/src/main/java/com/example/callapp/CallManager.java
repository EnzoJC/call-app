package com.example.callapp;

import android.content.Context;
import android.media.AudioManager;
import android.telecom.Call;
import android.telecom.VideoProfile;

public class CallManager {
    public static Call call;
    private Context context;
    private static AudioManager audioManager;

    public CallManager(Context context) {
        this.context = context;
    }

    public void contestarLlamada() {
        if (call != null) {
            call.answer(VideoProfile.STATE_AUDIO_ONLY);
            activarBocinaLlamada();
        }
    }

    public void colgarLlamada() {
        if (call != null) {
            call.disconnect();
        }
    }

    public void activarBocinaLlamada() {
        if (audioManager != null) {
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.setSpeakerphoneOn(true);
        }
    }

    public void destroy() {
        call = null;
        context = null;
        audioManager = null;
    }
}
