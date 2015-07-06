package in.presso.adapter;

import in.presso.laundryapp.R;
import in.presso.pojo.RatelistData;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RatelistAdapter extends BaseAdapter
{
	private static Context mContext;
	private ArrayList<RatelistData> listData;
	
	public RatelistAdapter(Context context, ArrayList<RatelistData> listData) {
		mContext = context;
		this.listData = listData;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();
		if (convertView == null) 
		{
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_rate_list, null);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvAmount = (TextView) convertView.findViewById(R.id.tvAmount);
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvTitle.setText(listData.get(position).getTitle());
		holder.tvAmount.setText(listData.get(position).getAmount());
		
		return convertView;
	}

	static class ViewHolder {
		TextView tvTitle;
		TextView tvAmount;
	}
}
