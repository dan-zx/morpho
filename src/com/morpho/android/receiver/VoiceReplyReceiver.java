package com.morpho.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class VoiceReplyReceiver extends BroadcastReceiver {

    private static final String TAG = VoiceReplyReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive, intent=" + intent.getExtras().get("extra_voice_reply"));
        String voiceOutput = intent.getExtras().getString("extra_voice_reply");
    }
}