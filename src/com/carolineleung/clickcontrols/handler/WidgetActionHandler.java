package com.carolineleung.clickcontrols.handler;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public interface WidgetActionHandler {
	void run(Context context, Intent intent, RemoteViews remoteViews);
}
