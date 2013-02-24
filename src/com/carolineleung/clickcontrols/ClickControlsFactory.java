package com.carolineleung.clickcontrols;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

// Only used if AppWidget is a list view

public class ClickControlsFactory implements RemoteViewsFactory {

	private static final String[] items = { "wifi", "3G", "vibrate", "battery" };

	private Context context = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public int getCount() {
		return items.length;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		RemoteViews entry = new RemoteViews(context.getPackageName(), R.layout.widget_item);

		String item = items[position];

		// TODO check text1 viewId
		entry.setTextViewText(android.R.id.text1, item);

		Intent intent = new Intent();
		Bundle extras = new Bundle();
		extras.putString(ClickControlsWidgetProvider.EXTRA_CONTROL, item);
		intent.putExtras(extras);
		entry.setOnClickFillInIntent(android.R.id.text1, intent);
		return entry;
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onDataSetChanged() {
		// TODO Auto-generated method stub
	}

}
