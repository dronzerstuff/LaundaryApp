package in.presso.laundryapp;

import in.presso.adapter.CurrentOrderStatusAdapter;
import in.presso.adapter.DeliveredOrderStatusAdapter;
import in.presso.pojo.OrderData;
import in.presso.util.APICallTask;
import in.presso.util.APIResponseListener;
import in.presso.util.FireToast;
import in.presso.util.SharedPrefUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MyOrdersFragment extends Fragment implements OnClickListener,OnItemClickListener {

	private static Context mContext;
	private static View mView;

	private ListView currentOrderList;
	private ArrayList<OrderData> currentOrderDatas;
	private CurrentOrderStatusAdapter currentOrderStatusAdapter;

	private ListView deliverdOrderList;
	private ArrayList<OrderData> deliverdOrderDatas;
	private DeliveredOrderStatusAdapter deliveredOrderStatusAdapter;

	public MyOrdersFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_my_orders,
				container, false);
		mView = rootView;
		mContext = mView.getContext();
		init();
		return rootView;
	}

	private void init() {
		currentOrderList = (ListView) mView.findViewById(R.id.lstCurrentOrder);
		currentOrderList.setOnItemClickListener(this);
		
		currentOrderDatas = new ArrayList<OrderData>();
		currentOrderStatusAdapter = new CurrentOrderStatusAdapter(mContext,
				currentOrderDatas);
		currentOrderList.setAdapter(currentOrderStatusAdapter);

		deliverdOrderList = (ListView) mView
				.findViewById(R.id.lstDeliveredOrder);
		deliverdOrderDatas = new ArrayList<OrderData>();
		deliveredOrderStatusAdapter = new DeliveredOrderStatusAdapter(mContext,
				deliverdOrderDatas);
		deliverdOrderList.setAdapter(deliveredOrderStatusAdapter);

		myOrdersWebserviceData();

		mView.findViewById(R.id.btnMenu).setOnClickListener(this);
	}

	private void myOrdersWebserviceData() {

		APICallTask task = new APICallTask(getActivity(), listener, null, true);
		task.execute("http://presso.in/service.svc/OrderHistory/"+ SharedPrefUtils.getKey_id(mContext));
	}

	APIResponseListener listener = new APIResponseListener() {

		@Override
		public void onSuccess(Object profile) {
			JSONObject object = (JSONObject) profile;
			try {
				JSONArray arrayOrders = object.getJSONArray("data");

				for (int i = 0; i < arrayOrders.length(); i++) {
					JSONObject objOrder = arrayOrders.getJSONObject(i);
					OrderData data = new OrderData();
					
					if (objOrder.getInt("OrderType") == 1) {
						data.setTitle("Ironing");
					}

					if (objOrder.getInt("OrderType") == 2) {
						data.setTitle("Wash and Iron");
					}

					if (objOrder.getInt("OrderType") == 3) {
						data.setTitle("Dry Cleaning");
					}
					data.setOrderId(objOrder.getInt("Id"));
					data.setExpress(objOrder.getBoolean("IsExpress"));
					data.setOrderType(objOrder.getInt("OrderType"));
					
					data.setStatusCode(objOrder.getInt("Status"));
					data.setNoOfItems(objOrder.getInt("ItemCount"));
					String deliveryDate = objOrder.getString("DeliveryDate").replace("/Date(", "").replace(")/", "").replace("+0530", "");
					String pickupDate = objOrder.getString("PickupDate").replace("/Date(", "").replace(")/", "").replace("+0530", "");
					data.setPickUpTime(objOrder.getString("PickupTimeSlot"));
					data.setDeliveryTime(objOrder.getString("DeliveryTimeSlot"));

					Date date = new Date(Long.parseLong(deliveryDate));

					SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
					data.setDeliveryDate(dateFormat.format(date));
					data.setPickUpDate(dateFormat.format(new Date(Long.parseLong(pickupDate))));

					data.setAmount(objOrder.getString("BillAmount"));

					if (objOrder.getInt("Status") == 5
							|| objOrder.getInt("Status") == 7) {
						deliverdOrderDatas.add(data);
					} else {
						currentOrderDatas.add(data);
					}

				}

				currentOrderStatusAdapter.notifyDataSetChanged();
				deliveredOrderStatusAdapter.notifyDataSetChanged();

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		@Override
		public void onError(String message) {
			FireToast.makeToast(getActivity(), message);

		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnMenu:
			((HomeActivity) mContext).openDrawer();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

		((HomeActivity) mContext).updateOrder(currentOrderDatas.get(pos));
	}

}
