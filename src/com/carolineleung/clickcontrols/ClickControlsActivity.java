package com.carolineleung.clickcontrols;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class ClickControlsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String controlName = getIntent().getStringExtra(ClickControlsWidgetProvider.EXTRA_CONTROL);
		if (controlName == null) {
			controlName = "No name";
		}
		Toast.makeText(this, controlName, Toast.LENGTH_SHORT).show();
		finish();
	}

}
