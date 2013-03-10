package com.carolineleung.clickcontrols.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.carolineleung.clickcontrols.R;

public class MenuListFragment extends ListFragment {

	private static final String[] MENU_ITEMS = new String[] { "Red", "Orange", "Yellow", "Green", "Cyan", "Blue", "Purple" };

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MenuAdapter adapter = new MenuAdapter(getActivity());
		for (String menu : MENU_ITEMS) {
			adapter.add(new MenuItem(menu, android.R.drawable.ic_menu_search));
		}
		setListAdapter(adapter);
	}

	private class MenuItem {
		public String menuText;
		public int menuImageId;

		public MenuItem(String tag, int iconRes) {
			this.menuText = tag;
			this.menuImageId = iconRes;
		}
	}

	public class MenuAdapter extends ArrayAdapter<MenuItem> {

		public MenuAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_row, null);
			}

			MenuItem menuItem = getItem(position);
			TextView title = (TextView) convertView.findViewById(R.id.menu_row_text);
			title.setText(menuItem.menuText);
			Drawable image = this.getContext().getResources().getDrawable(menuItem.menuImageId);
			image.setBounds(0, 0, 48, 48);
			title.setCompoundDrawables(image, null, null, null);

			return convertView;
		}

	}
}
