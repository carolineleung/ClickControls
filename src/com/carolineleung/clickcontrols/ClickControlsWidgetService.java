package com.carolineleung.clickcontrols;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class ClickControlsWidgetService extends RemoteViewsService {

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new ClickControlsFactory(getApplicationContext(), intent);
	}
}
