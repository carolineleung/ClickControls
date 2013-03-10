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

public class ClickControlsActivity extends Activity {

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
		items.add(new SatelliteMenuItem(4, R.drawable.ic_1));
		items.add(new SatelliteMenuItem(4, R.drawable.ic_3));
		items.add(new SatelliteMenuItem(4, R.drawable.ic_4));
		items.add(new SatelliteMenuItem(3, R.drawable.ic_5));
		items.add(new SatelliteMenuItem(2, R.drawable.ic_6));
		items.add(new SatelliteMenuItem(1, R.drawable.ic_2));
		// items.add(new SatelliteMenuItem(5, R.drawable.sat_item));
		clickControlsMenu.addItems(items);

		clickControlsMenu.setOnItemClickedListener(new SateliteClickedListener() {

			public void eventOccured(int id) {
				Log.i("sat", "Clicked on " + id);
				if (id == 1) {
					Intent intent = new Intent(ClickControlsActivity.this.getApplicationContext(), StyledFragmentActivity.class);
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
