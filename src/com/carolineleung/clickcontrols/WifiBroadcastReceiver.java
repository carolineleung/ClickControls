package com.carolineleung.clickcontrols;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.RemoteViews;

public class WifiBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			Log.d("WifiReceiver", "Have Wifi Connection");
			remoteViews.setImageViewResource(R.id.toggleWifi, R.drawable.toggle_wifi_on);

		} else {
			Log.d("WifiReceiver", "Don't have Wifi Connection");
			remoteViews.setImageViewResource(R.id.toggleWifi, R.drawable.toggle_wifi_off);
		}

		// ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// NetworkInfo netInfo = conMan.getActiveNetworkInfo();
		//
		// if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
		// Log.d("WifiReceiver", "Have Wifi Connection");
		// remoteViews.setImageViewResource(R.id.toggleWifi, R.drawable.toggle_wifi_on);
		// } else {
		// Log.d("WifiReceiver", "Don't have Wifi Connection");
		// remoteViews.setImageViewResource(R.id.toggleWifi, R.drawable.toggle_wifi_off);
		// }
	}

}
