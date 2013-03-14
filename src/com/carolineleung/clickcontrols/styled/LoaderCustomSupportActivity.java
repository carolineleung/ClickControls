package com.carolineleung.clickcontrols.styled;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.pm.ActivityInfoCompat;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.carolineleung.clickcontrols.R;

public class LoaderCustomSupportActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentManager fragmentMgr = getSupportFragmentManager();

		// Create the fragment as content
		if (fragmentMgr.findFragmentById(android.R.id.content) == null) {

		}
	}

	public static class SubscribedConfigChanges {
		private Configuration mLastConfiguration = new Configuration();
		private int mLastDensity;

		boolean applyNewConfig(Resources resources) {
			int configChanges = mLastConfiguration.updateFrom(resources.getConfiguration());
			boolean densityChanged = mLastDensity != resources.getDisplayMetrics().densityDpi;
			if (densityChanged
					|| (configChanges & (ActivityInfo.CONFIG_LOCALE | ActivityInfoCompat.CONFIG_UI_MODE | ActivityInfo.CONFIG_SCREEN_LAYOUT)) != 0) {
				mLastDensity = resources.getDisplayMetrics().densityDpi;
				return true;
			}
			return false;
		}
	}

	public static class AppListLoader extends AsyncTaskLoader<List<AppEntry>> {

		private SubscribedConfigChanges mLastConfig = new SubscribedConfigChanges();
		private PackageManager mPackageManager;
		private List<AppEntry> mInstalledApps;

		public AppListLoader(Context context) {
			super(context);
			mPackageManager = getContext().getPackageManager();
			// NOTE: use global getContext() instead of the passed in context
		}

		@Override
		public List<AppEntry> loadInBackground() {
			// Retrieve all applications, including disabled and uninstalled ones
			List<ApplicationInfo> apps = mPackageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES
					| PackageManager.GET_DISABLED_COMPONENTS);
			if (apps == null) {
				apps = new ArrayList<ApplicationInfo>();
			}

			Context context = getContext();
			List<AppEntry> entries = new ArrayList<LoaderCustomSupportActivity.AppEntry>(apps.size());
			for (ApplicationInfo appInfo : apps) {
				AppEntry appEntry = new AppEntry(this, appInfo);
				appEntry.loadLabel(context);
				entries.add(appEntry);
			}

			Collections.sort(entries, APP_LABEL_COMPARATOR);

			return null;
		}
	}

	public static final Comparator<AppEntry> APP_LABEL_COMPARATOR = new Comparator<LoaderCustomSupportActivity.AppEntry>() {
		private Collator sCollator = Collator.getInstance();

		@Override
		public int compare(AppEntry entry1, AppEntry entry2) {
			return sCollator.compare(entry1.getLabel(), entry2.getLabel());
		}
	};

	public static class AppEntry {

		private AppListLoader mLoader;
		private ApplicationInfo mInfo;
		private File mApkFile;
		private String mLabel;
		private Drawable mIcon;
		private boolean mMounted;

		public AppEntry(AppListLoader loader, ApplicationInfo info) {
			this.mLoader = loader;
			this.mInfo = info;
			this.mApkFile = new File(info.sourceDir);
		}

		public ApplicationInfo getApplicationInfo() {
			return mInfo;
		}

		public String getLabel() {
			return mLabel;
		}

		public Drawable getIcon() {
			if (mIcon == null) {
				if (mApkFile.exists()) {
					mIcon = mInfo.loadIcon(mLoader.mPackageManager);
					return mIcon;
				} else {
					mMounted = false;
				}
			} else if (!mMounted) {
				// Reload app icon as app has changed from !mounted -> mounted
				if (mApkFile.exists()) {
					mMounted = true;
					mIcon = mInfo.loadIcon(mLoader.mPackageManager);
					return mIcon;
				}
			} else {
				return mIcon;
			}
			return mLoader.getContext().getResources().getDrawable(android.R.drawable.sym_def_app_icon);
		}

		@Override
		public String toString() {
			return mLabel;
		}

		private void loadLabel(Context context) {
			if (mLabel == null || !mMounted) {
				if (!mApkFile.exists()) {
					mMounted = false;
					mLabel = mInfo.packageName;
				} else {
					mMounted = true;
					CharSequence label = mInfo.loadLabel(context.getPackageManager());
					mLabel = label != null ? label.toString() : mInfo.packageName;
				}
			}
		}
	}

	public static class AppListAdapter extends ArrayAdapter<AppEntry> {
		private LayoutInflater mInflater;

		public AppListAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_2);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void setData(List<AppEntry> appEntriesData) {
			clear();
			if (appEntriesData != null) {
				for (AppEntry appEntry : appEntriesData) {
					add(appEntry);
				}
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView != null) {
				view = convertView;
			} else {
				view = mInflater.inflate(R.layout.app_list_item_icon_text, parent, false);
			}
			AppEntry item = getItem(position);
			((ImageView) view.findViewById(R.id.app_entry_icon)).setImageDrawable(item.getIcon());
			((TextView) view.findViewById(R.id.app_entry_text)).setText(item.getLabel());
			return view;
		}
	}

	public static class InstalledAppListFragment extends SherlockListFragment implements LoaderManager.LoaderCallbacks<List<AppEntry>> {

		private AppListAdapter mAdapter;
		private String mCurrFilter;
		private OnQueryTextListener mOnQueryTextListenerCompat;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			setEmptyText("No applications");
			setHasOptionsMenu(true);

			mAdapter = new AppListAdapter(getActivity());
			setListAdapter(mAdapter);

			setListShown(false);
			getLoaderManager().initLoader(0, null, this);
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			MenuItem menuItem = menu.add("Search");
			menuItem.setIcon(android.R.drawable.ic_menu_search);
			menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

			View searchView = SearchViewCompat.newSearchView(getActivity());
			if (searchView != null) {
				SearchViewCompat.setOnQueryTextListener(searchView, new OnQueryTextListenerCompat() {

					@Override
					public boolean onQueryTextChange(String newText) {
						mCurrFilter = !TextUtils.isEmpty(newText) ? newText : null;
						mAdapter.getFilter().filter(mCurrFilter);
						return true;
					}
				});
				menuItem.setActionView(searchView);
			}
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// TODO Implement handling
			Log.i("LoaderCustomSupport", "Item clicked: " + id);
		}

		@Override
		public Loader<List<AppEntry>> onCreateLoader(int arg0, Bundle arg1) {
			return new AppListLoader(getActivity());
		}

		@Override
		public void onLoadFinished(Loader<List<AppEntry>> loader, List<AppEntry> appEntriesData) {
			// Set the new data in the adapter
			mAdapter.setData(appEntriesData);
			if (isResumed()) {
				setListShown(true);
			} else {
				setListShownNoAnimation(true);
			}
		}

		@Override
		public void onLoaderReset(Loader<List<AppEntry>> loader) {
			mAdapter.setData(null);
		}

	}

}
