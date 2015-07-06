package in.presso.adapter;

import java.util.ArrayList;

import in.presso.laundryapp.R;
import in.presso.pojo.OrderData;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DeliveredOrderStatusAdapter extends BaseAdapter {

	private static Context mContext;
	private ArrayList<OrderData> jobDatas;

	public DeliveredOrderStatusAdapter(Context context,
			ArrayList<OrderData> jobDatas) {
		mContext = context;
		this.jobDatas = jobDatas;
	}

	@Override
	public int getCount() {

		return jobDatas.size();
	}

	@Override
	public Object getItem(int arg0) {

		return jobDatas.get(arg0);
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
			convertView = inflater.inflate(R.layout.view_old_orders, null);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
			holder.tvAmount = (TextView) convertView
					.findViewById(R.id.tvAmount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvTitle.setText(jobDatas.get(position).getTitle());

      /*  if (jobDatas.get(position).getOrderType() == 1) {
            holder.tvTitle.setTextColor(Color.parseColor("#0074CD"));
        }

        if (jobDatas.get(position).getOrderType() == 2) {
            holder.tvTitle.setTextColor(Color.parseColor("#d35400"));
        }

        if (jobDatas.get(position).getOrderType() == 3) {
            holder.tvTitle.setTextColor(Color.parseColor("#27ae60"));
        }*/

		holder.tvDate.setText("Delivered on : "
				+ jobDatas.get(position).getDeliveryDate());
		holder.tvAmount.setText("Rs "+jobDatas.get(position).getAmount());
		
		return convertView;
	}

	static class ViewHolder {
		TextView tvTitle;
		TextView tvDate;
		TextView tvAmount;
	}

}
