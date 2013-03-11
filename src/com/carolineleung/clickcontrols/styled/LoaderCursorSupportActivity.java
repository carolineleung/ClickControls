package com.carolineleung.clickcontrols.styled;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

@SuppressWarnings("deprecation")
public class LoaderCursorSupportActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FragmentManager fragmentMgr = getSupportFragmentManager();

		if (fragmentMgr.findFragmentById(android.R.id.content) == null) {
			CursorLoaderListFragment listFrag = new CursorLoaderListFragment();
			fragmentMgr.beginTransaction().add(android.R.id.content, listFrag).commit();
		}
	}

	public static class CursorLoaderListFragment extends SherlockListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

		private SimpleCursorAdapter mAdapter;
		private String mCurrFilter;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			setEmptyText("Dummy text");
			setHasOptionsMenu(true);

			mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, new String[] { People.DISPLAY_NAME },
					new int[] { android.R.id.text1 }, 0);
			setListAdapter(mAdapter);
			setListShown(false); // show progress loader first

			getLoaderManager().initLoader(0, null, this);
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			MenuItem searchItem = menu.add("Search");
			searchItem.setIcon(android.R.drawable.ic_menu_search);
			searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			View searchView = SearchViewCompat.newSearchView(getActivity());
			if (searchView != null) {
				SearchViewCompat.setOnQueryTextListener(searchView, new SearchViewCompat.OnQueryTextListenerCompat() {

					@Override
					public boolean onQueryTextChange(String newText) {
						mCurrFilter = !TextUtils.isEmpty(newText) ? newText : null;
						getLoaderManager().restartLoader(0, null, CursorLoaderListFragment.this);
						return true;
					}
				});
				searchItem.setActionView(searchView);
			}
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Log.i("LoaderCursorList", "Item clicked: " + id);
		}

		private static final String[] CONTACTS_SUMMARY_PROJECT = new String[] { People._ID, People.DISPLAY_NAME, };

		@Override
		public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
			Uri baseUri;
			if (mCurrFilter != null) {
				baseUri = Uri.withAppendedPath(People.CONTENT_FILTER_URI, Uri.encode(mCurrFilter));
			} else {
				baseUri = People.CONTENT_URI;
			}

			String select = "((" + People.DISPLAY_NAME + " NOTNULL) AND (" + People.DISPLAY_NAME + " != '' ))";
			return new CursorLoader(getActivity(), baseUri, CONTACTS_SUMMARY_PROJECT, select, null, People.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
		}

		@Override
		public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
			mAdapter.swapCursor(data);
			if (isResumed()) {
				setListShown(true);
			} else {
				setListShownNoAnimation(true);
			}
		}

		@Override
		public void onLoaderReset(Loader<Cursor> arg0) {
			mAdapter.swapCursor(null);
		}
	}

}
