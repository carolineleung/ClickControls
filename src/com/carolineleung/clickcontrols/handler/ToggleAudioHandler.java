package com.carolineleung.clickcontrols.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.carolineleung.clickcontrols.ClickControlsWidgetProvider;
import com.carolineleung.clickcontrols.R;

public class ToggleAudioHandler implements WidgetActionHandler {

	@Override
	public void run(Context context, Intent intent, RemoteViews remoteViews) {
		Log.i("onReceive", ClickControlsWidgetProvider.ACTION_WIDGET_TOGGLE_AIRPLANE);
		AudioManager audioManager = (AudioManager) context.getSystemService(Activity.AUDIO_SERVICE);
		if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
			remoteViews.setImageViewResource(R.id.toggleAudio, R.drawable.toggle_sound_on);
			audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		} else {
			remoteViews.setImageViewResource(R.id.toggleAudio, R.drawable.toggle_sound_off);
			audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		}
	}

}
