package com.carolineleung.clickcontrols.styled;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.carolineleung.clickcontrols.R;

public class StyledFragmentActivity extends SherlockFragmentActivity implements ActionBar.TabListener {

	private static final String[] TABS = new String[] { "Left", "Middle", "Right" };
	private static final int MARGIN = 10;
	private static final long ONE_SEC = 1000;

	private final Handler handler = new Handler();

	private RoundedCornerFragment left;
	private RoundedCornerFragment middle;
	private RoundedCornerFragment right;

	private boolean useLogo = false;
	private boolean showHomeAsUp = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.styled_activity);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
		actionBar.setDisplayUseLogoEnabled(useLogo);

		for (String tabName : TABS) {
			actionBar.addTab(actionBar.newTab().setText(tabName).setTabListener(this));
		}

		SpinnerAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.styled_sections, R.layout.sherlock_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(arrayAdapter, new OnNavigationListener() {
			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {
				rotateLeftFragment();
				return false;
			}
		});

		showTabsNav();
		left = new RoundedCornerFragment(getResources().getColor(R.color.styled_purple), 1f, MARGIN, MARGIN / 2, MARGIN, MARGIN);
		middle = new RoundedCornerFragment(getResources().getColor(R.color.styled_turquoise), 1f, MARGIN / 2, MARGIN / 2, MARGIN, MARGIN);
		right = new RoundedCornerFragment(getResources().getColor(R.color.styled_pink), 1f, MARGIN / 2, MARGIN, MARGIN, MARGIN);

		FragmentTransaction fragmentTx = getSupportFragmentManager().beginTransaction();
		fragmentTx.add(R.id.styled, left);
		fragmentTx.add(R.id.styled, middle);
		fragmentTx.add(R.id.styled, right);
		fragmentTx.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.styled_menu, menu);

		final MenuItem refresh = (MenuItem) menu.findItem(R.id.menu_refresh);
		refresh.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			public boolean onMenuItemClick(MenuItem item) {
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						refresh.setActionView(null);
					}
				}, ONE_SEC);
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			return false;
		case R.id.menu_refresh:
			item.setActionView(R.layout.styled_progress_action);
			return true;
		case R.id.menu_both:
			rotateMiddleFragment();
			return true;
		case R.id.menu_text:
			ObjectAnimator alpha = ObjectAnimator.ofFloat(right.getView(), "alpha", 1f, 0f);
			alpha.setRepeatMode(ObjectAnimator.REVERSE);
			alpha.setRepeatCount(3);
			alpha.setDuration(800);
			alpha.start();
			return true;
		case R.id.menu_logo:
			useLogo = !useLogo;
			item.setChecked(useLogo);
			getSupportActionBar().setDisplayUseLogoEnabled(useLogo);
			return true;
		case R.id.menu_up:
			showHomeAsUp = !showHomeAsUp;
			item.setChecked(showHomeAsUp);
			getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp);
			return true;
		case R.id.menu_nav_tabs:
			item.setChecked(true);
			showTabsNav();
			return true;
		case R.id.menu_nav_label:
			item.setChecked(true);
			showStandardNav();
			return true;
		case R.id.menu_nav_drop_down:
			item.setChecked(true);
			showDropDownListNav();
			return true;
		case R.id.menu_bak_none:
			item.setChecked(true);
			getSupportActionBar().setBackgroundDrawable(null);
			return true;
		case R.id.menu_bak_gradient:
			item.setChecked(true);
			getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.styled_ad_action_bar_gradient));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void rotateLeftFragment() {
		rotateFragment(left);
	}

	private void rotateMiddleFragment() {
		rotateFragment(middle);
	}

	private void rotateRightFragment() {
		rotateFragment(right);
	}

	private void rotateFragment(RoundedCornerFragment fragment) {
		if (fragment != null) {
			ObjectAnimator.ofFloat(fragment.getView(), "rotationY", 0, 180).setDuration(500).start();
		}
	}

	private void showStandardNav() {
		setActionBarNavMode(ActionBar.NAVIGATION_MODE_STANDARD, true);
	}

	private void showTabsNav() {
		setActionBarNavMode(ActionBar.NAVIGATION_MODE_TABS, false);
	}

	private void showDropDownListNav() {
		setActionBarNavMode(ActionBar.NAVIGATION_MODE_LIST, false);
	}

	private void setActionBarNavMode(int navMode, boolean displayShowTitle) {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar.getNavigationMode() != navMode) {
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setNavigationMode(navMode);
		}
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		int whichFragment = tab.getPosition();
		switch (whichFragment) {
		case 0:
			rotateLeftFragment();
			break;
		case 1:
			rotateMiddleFragment();
			break;
		case 2:
			rotateRightFragment();
			break;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
