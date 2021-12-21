package com.example.callapp;

import android.telecom.Call;

public class CallState extends Call.Callback {
    @Override
    public void onStateChanged(Call call, int state) {
        super.onStateChanged(call, state);
        switch (state) {
            case Call.STATE_ACTIVE: {
                break;
            }
            case Call.STATE_DISCONNECTED: {
                ActivityStack.getInstance().finishActivity(IncomingCallActivity.class);
                break;
            }
        }
    }

}
