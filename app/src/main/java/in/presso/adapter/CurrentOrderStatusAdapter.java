package in.presso.adapter;

import java.util.ArrayList;

import in.presso.laundryapp.R;
import in.presso.pojo.OrderData;
import in.presso.util.JobStatus;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CurrentOrderStatusAdapter extends BaseAdapter {

	private static Context mContext;
	private ArrayList<OrderData> jobDatas;

	public CurrentOrderStatusAdapter(Context context,
			ArrayList<OrderData> jobDatas) {
		mContext = context;
		this.jobDatas = jobDatas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jobDatas.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return jobDatas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_current_order, null);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvStatus = (TextView) convertView
					.findViewById(R.id.tvStatus);
            holder.tvDeliveryDate = (TextView) convertView.findViewById(R.id.tvDeliveryDate);
            holder.tvDeliveryTime = (TextView) convertView.findViewById(R.id.tvDeliveryTime);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

        String express="";
        if(jobDatas.get(position).isExpress())
            express=" (E)";
		holder.tvTitle.setText(jobDatas.get(position).getTitle() +express);

		/*if (jobDatas.get(position).getOrderType() == 1) {
			holder.tvTitle.setTextColor(Color.parseColor("#0074CD"));
		}

		if (jobDatas.get(position).getOrderType() == 2) {
			holder.tvTitle.setTextColor(Color.parseColor("#d35400"));
		}

		if (jobDatas.get(position).getOrderType() == 3) {
			holder.tvTitle.setTextColor(Color.parseColor("#27ae60"));
		}*/

		holder.tvStatus.setText(JobStatus.getStatusMessage(jobDatas.get(
				position).getStatusCode()));
        if(jobDatas.get(position).getStatusCode()>1) {
            holder.tvDeliveryDate.setText("Delivery: " + jobDatas.get(position).getDeliveryDate());
            holder.tvDeliveryTime.setText(jobDatas.get(position).getDeliveryTime());
        }
        else{
            holder.tvDeliveryDate.setText("Pickup: " + jobDatas.get(position).getPickUpDate());
            holder.tvDeliveryTime.setText(jobDatas.get(position).getPickUpTime());
        }

		return convertView;
	}

	static class ViewHolder {
		TextView tvTitle;
		TextView tvStatus;
        TextView tvDeliveryDate;
        TextView tvDeliveryTime;
	}

}
