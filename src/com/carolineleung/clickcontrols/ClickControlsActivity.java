package com.carolineleung.clickcontrols;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.ext.SatelliteMenu;
import android.view.ext.SatelliteMenu.SateliteClickedListener;
import android.view.ext.SatelliteMenuItem;

import com.carolineleung.clickcontrols.menu.CustomScaleAnimation;
import com.carolineleung.clickcontrols.styled.DeviceDataActivity;
import com.carolineleung.clickcontrols.styled.StyledFragmentActivity;

public class ClickControlsActivity extends Activity {

	private static final int SEARCH_ID = 1;
	private static final int CAMERA_ID = 2;
	private static final int BUBBLE_ID = 3;
	private static final int MUSIC_ID = 4;
	private static final int LOCATION_ID = 5;
	private static final int PROFILE_ID = 6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.click_controls);

		SatelliteMenu clickControlsMenu = (SatelliteMenu) findViewById(R.id.click_controls_menu);
		clickControlsMenu.setCloseItemsOnClick(true);
		clickControlsMenu.setExpandDuration(500);
		clickControlsMenu.setMainImage(R.drawable.ic_launcher);
		float distance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, getResources().getDisplayMetrics());
		clickControlsMenu.setSatelliteDistance((int) distance);
		clickControlsMenu.setTotalSpacingDegree(90);

		List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
		items.add(new SatelliteMenuItem(SEARCH_ID, R.drawable.ic_1));
		items.add(new SatelliteMenuItem(BUBBLE_ID, R.drawable.ic_3));
		items.add(new SatelliteMenuItem(MUSIC_ID, R.drawable.ic_4));
		items.add(new SatelliteMenuItem(LOCATION_ID, R.drawable.ic_5));
		items.add(new SatelliteMenuItem(PROFILE_ID, R.drawable.ic_6));
		items.add(new SatelliteMenuItem(CAMERA_ID, R.drawable.ic_2));
		// items.add(new SatelliteMenuItem(5, R.drawable.sat_item));
		clickControlsMenu.addItems(items);

		clickControlsMenu.setOnItemClickedListener(new SateliteClickedListener() {

			public void eventOccured(int id) {
				Log.i("sat", "Clicked on " + id);
				if (id == SEARCH_ID) {
					Intent intent = new Intent(ClickControlsActivity.this.getApplicationContext(), StyledFragmentActivity.class);
					startActivity(intent);
				} else if (id == BUBBLE_ID) {
					Intent intent = new Intent(ClickControlsActivity.this.getApplicationContext(), DeviceDataActivity.class);
					startActivity(intent);

				} else {
					Intent intent = new Intent(ClickControlsActivity.this.getApplicationContext(), CustomScaleAnimation.class);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_click_controls, menu);
		return true;
	}

}
