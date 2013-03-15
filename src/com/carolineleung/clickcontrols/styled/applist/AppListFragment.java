package com.carolineleung.clickcontrols.styled.applist;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class AppListFragment extends SherlockListFragment implements LoaderManager.LoaderCallbacks<List<AppEntry>> {

	private AppListAdapter mAdapter;
	private String mCurrFilter;

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

		// Show the list now
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