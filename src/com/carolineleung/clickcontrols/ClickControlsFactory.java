package com.carolineleung.clickcontrols;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class ClickControlsFactory implements RemoteViewsFactory {

	private static final String[] items = { "wifi", "3G", "vibrate", "battery" };
	private Context context = null;
	private int appWidgetId;

	public ClickControlsFactory(Context context, Intent intent) {
		this.context = context;
		appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
	}

	@Override
	public void onCreate() {
		// no op
	}

	@Override
	public void onDestroy() {
		// no op
	}

	@Override
	public int getCount() {
		return items.length;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		RemoteViews entry = new RemoteViews(context.getPackageName(), R.layout.widget_item);

		String item = items[position];

		entry.setTextViewText(R.id.controlName, item);
		entry.setViewVisibility(R.id.toggleImage, View.VISIBLE);
		entry.setViewVisibility(R.id.indicatorText, View.VISIBLE);

		Intent intent = new Intent();
		Bundle extras = new Bundle();

		extras.putString(ClickControlsWidgetProvider.EXTRA_CONTROL, item);
		intent.putExtras(extras);
		entry.setOnClickFillInIntent(R.id.control, intent);

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
