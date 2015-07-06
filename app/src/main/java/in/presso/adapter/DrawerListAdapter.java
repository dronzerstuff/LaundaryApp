package in.presso.adapter;

import in.presso.laundryapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerListAdapter extends BaseAdapter {

	private static Context mContext;
	private String[] mDrawerItemList;
	private int[] mDrawerItemImages;

	public DrawerListAdapter(Context context, String[] mDrawerItemList) {
		mContext = context;
		this.mDrawerItemList = mDrawerItemList;
		mDrawerItemImages = new int[] { R.drawable.my_profile,
				R.drawable.myorder, R.drawable.person,
				R.drawable.description, R.drawable.referapp,
				R.drawable.help };
	}

	@Override
	public int getCount() {
		return mDrawerItemList.length;
	}

	@Override
	public Object getItem(int arg0) {
		return mDrawerItemList[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater
					.inflate(R.layout.view_drawer_list_item, null);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.imgMenu = (ImageView) convertView.findViewById(R.id.imgMenu);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvTitle.setText(mDrawerItemList[position]);
		holder.imgMenu.setImageResource(mDrawerItemImages[position]);

		return convertView;
	}

	static class ViewHolder {
		TextView tvTitle;
		ImageView imgMenu;
	}
}
