package in.presso.laundryapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.presso.pojo.OrderData;
import in.presso.util.APICallTask;
import in.presso.util.APIResponseListener;
import in.presso.util.FireToast;
import in.presso.util.SharedPrefUtils;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeFragment extends Fragment implements OnClickListener {
	private static Context mContext;
	private static View mView;
	private Button btn_next, btn_prev;
            TextView btnNewOrder, btnCheckStatus;
	private TextView text_ordertype, text_deliveron, text_noofitems,
			text_orderamount,text_orderid,text_deliverytime, lblDateType, lblTimeType;
	private ArrayList<OrderData> listActiveOrders = new ArrayList<OrderData>();
	private int currentOrderIndex = 0;
	private ImageView imgStatus,emptyImg;

    private TextView txt_status,txtNoOrderStatus;

    private LinearLayout layDetails,emptyView;
    private RelativeLayout layStatus;

	public HomeFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);
		mView = rootView;
		mContext = rootView.getContext();
		init();
		return rootView;
	}

	private void init() {

        txt_status  = (TextView)mView.findViewById(R.id.txt_status);
        txtNoOrderStatus = (TextView)mView.findViewById(R.id.txtNoOrderStatus);
		btnNewOrder = (TextView) mView.findViewById(R.id.btnNewOrder);
		btnCheckStatus = (TextView) mView.findViewById(R.id.btnCheckStatus);
		btn_next = (Button) mView.findViewById(R.id.btn_next);
		btn_prev = (Button) mView.findViewById(R.id.btn_prev);
		imgStatus = (ImageView) mView.findViewById(R.id.imgStatus);
        emptyImg = (ImageView)mView.findViewById(R.id.emptyImg);
       // emptyImg.setColorFilter(Color.parseColor("#0266c8"));

		mView.findViewById(R.id.btnMenu).setOnClickListener(this);
		btn_next.setOnClickListener(this);
		btn_prev.setOnClickListener(this);

        lblDateType = (TextView) mView.findViewById(R.id.lblDateType);
        lblTimeType = (TextView) mView.findViewById(R.id.lblTimeType);
		text_ordertype = (TextView) mView.findViewById(R.id.text_ordertype);
		text_deliveron = (TextView) mView.findViewById(R.id.text_deliveron);
		text_noofitems = (TextView) mView.findViewById(R.id.text_noofterms);
		text_orderamount = (TextView) mView.findViewById(R.id.text_orderamount);
		text_orderid= (TextView) mView.findViewById(R.id.text_ordertid);
		text_deliverytime= (TextView) mView.findViewById(R.id.text_orderttime);

        layDetails = (LinearLayout)mView.findViewById(R.id.layDetails);
        emptyView= (LinearLayout)mView.findViewById(R.id.emptyView);
        layStatus= (RelativeLayout)mView.findViewById(R.id.layStatus);

        txt_status = (TextView)mView.findViewById(R.id.txt_status);

		btnNewOrder.setOnClickListener(this);
		btnCheckStatus.setOnClickListener(this);

		
	}

    @Override
    public void onResume() {
        super.onResume();

        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (isNetworkConnected(connMgr)) {
            (new HomeActivity()).updateProfile(mContext);

            APICallTask apiCallTask = new APICallTask(mContext, listener, null,
                    true);
            apiCallTask.execute("http://www.presso.in/service.svc/GetActiveOrders/"
                    + SharedPrefUtils.getKey_id(mContext));


            Log.e("user id", String.valueOf(SharedPrefUtils.getKey_id(mContext)));


            layDetails.setVisibility(View.VISIBLE);
            layStatus.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            txt_status.setVisibility(View.VISIBLE);
        }
        else{
            new AlertDialog.Builder(getActivity())
                    .setTitle("Connection Error")
                    .setMessage("You are not connected to Internet.")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    })
                    .show();
        }
    }

    public  boolean isNetworkConnected(
             ConnectivityManager connectivityManager) {

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	

	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnNewOrder:
			((HomeActivity) mContext).displayView(6);
			break;

		case R.id.btnCheckStatus:
			((HomeActivity) mContext).displayView(1);
			break;

		case R.id.btnMenu:
			((HomeActivity) mContext).openDrawer();
			break;
		case R.id.btn_next:
			if (currentOrderIndex < listActiveOrders.size() - 1) {
				currentOrderIndex++;
				loadOrder();
			}
			break;
		case R.id.btn_prev:
			if (currentOrderIndex > 0) {
				currentOrderIndex--;
				loadOrder();
			}
			break;
		}
	}

	APIResponseListener listener = new APIResponseListener() {

		@Override
		public void onSuccess(Object profile) {

			JSONObject object = (JSONObject) profile;

			try {
				JSONArray arrayOrders = object.getJSONArray("data");

                //Log.e("size of order",String.valueOf(arrayOrders.length()));
              if(arrayOrders.length()==0){
                    layDetails.setVisibility(View.GONE);
                    layStatus.setVisibility(View.GONE);
                    txt_status.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {

                    emptyView.setVisibility(View.GONE);

                    layDetails.setVisibility(View.VISIBLE);
                    layStatus.setVisibility(View.VISIBLE);

                    for (int i = 0; i < arrayOrders.length(); i++) {
                        JSONObject objOrder = arrayOrders.getJSONObject(i);
                        OrderData data = new OrderData();

                        data.setStatusCode(objOrder.getInt("Status"));
                        data.setOrderId(objOrder.getInt("Id"));
                        data.setDeliveryTime(objOrder.getString("DeliveryTimeSlot"));
                        data.setPickUpTime(objOrder.getString("PickupTimeSlot"));
                        //Log.e("order id",String.valueOf(objOrder.getInt("Id")));
                        //Log.e("order type",String.valueOf(objOrder.getInt("OrderType")));
                        //Log.e(" order date --",String.valueOf(objOrder.getString("DeliveryDate")));

                        if (objOrder.getInt("OrderType") == 1) {
                            data.setTitle("Ironing");
                        }

                        if (objOrder.getInt("OrderType") == 2) {
                            data.setTitle("Wash and Iron");
                        }

                        if (objOrder.getInt("OrderType") == 3) {
                            data.setTitle("Dry Cleaning");
                        }

                        data.setExpress(objOrder.getBoolean("IsExpress"));
                        data.setOrderType(objOrder.getInt("OrderType"));
                        data.setNoOfItems(objOrder.getInt("ItemCount"));

                        data.setStatusCode(objOrder.getInt("Status"));

                        String deliveryDate = objOrder.getString("DeliveryDate").replace("/Date(", "").replace(")/", "") .replace("+0530", "");

                        //Log.e(" converted date date --",deliveryDate);

                        String pickupDate = objOrder.getString("PickupDate")
                                .replace("/Date(", "").replace(")/", "")
                                .replace("+0530", "");

                        Date date = new Date(Long.parseLong(deliveryDate));

                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "dd MMM yyyy");
                        data.setDeliveryDate(dateFormat.format(date));
                        data.setPickUpDate(dateFormat.format(new Date(Long
                                .parseLong(pickupDate))));

                        data.setAmount(objOrder.getString("BillAmount"));
                        //Log.e("api listener--data", data.toString());
                        listActiveOrders.add(data);
                        //Log.e("api listener--listactiveorders", listActiveOrders.toString());

                        loadOrder();
                    }
               }

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		@Override
		public void onError(String message) {
			FireToast.makeToast(mContext, message);
		}
	};

	private void loadOrder() {
		OrderData data = listActiveOrders.get(currentOrderIndex);
        String express="";
        if(data.isExpress())
            express=" (E)";
        if(data.getStatusCode()>1) {
            lblDateType.setText(Html.fromHtml("Delivery on"));
            lblTimeType.setText(Html.fromHtml("Delivery Time"));
            text_deliveron.setText(Html.fromHtml("<b>" + "  :  " + "</b>" + data.getDeliveryDate()));
            text_deliverytime.setText(Html.fromHtml("<b>" + "  :  " + "</b>" + data.getDeliveryTime()));
        }
        else
        {
            lblDateType.setText(Html.fromHtml("Pickup on"));
            lblTimeType.setText(Html.fromHtml("Pickup Time"));
            text_deliveron.setText(Html.fromHtml("<b>" + "" + "</b>" + data.getPickUpDate()));
            text_deliverytime.setText(Html.fromHtml("<b>" + "" + "</b>" + data.getPickUpTime()));
        }
		text_noofitems.setText(Html.fromHtml("<b>"+""+"</b>"+((data.getNoOfItems()>0)?data.getNoOfItems():"NA"+"")));
		text_orderamount.setText(Html.fromHtml("<b>"+""+"</b>"+((Integer.parseInt(data.getAmount())>0)?data.getAmount():"NA"+"")));
        String orderId=data.getOrderId()+"";
        int orderIdLength=orderId.length();
        String zeros="000";
        String actualOrderNumber="CRN"+(zeros+orderId).substring(orderIdLength-1);

		text_orderid.setText(Html.fromHtml("<b>"+""+"</b>"+actualOrderNumber+ express));
		

		/*String[] orderType = getActivity().getResources().getStringArray(
				R.array.orderType);
*/
        if (data.getOrderType() == 1) {
            text_ordertype.setText(Html.fromHtml("<b>" + "" + "</b>" + "Ironing"));
        }

       else if (data.getOrderType() == 2) {
            text_ordertype.setText(Html.fromHtml("<b>" + "" + "</b>" + "Wash and Iron"));
        }

        else if (data.getOrderType() == 3) {
            text_ordertype.setText(Html.fromHtml("<b>" + "" + "</b>" + "Dry Cleaning"));
        }



       /* try {
            text_ordertype.setText(Html.fromHtml("<b>" + "  :  " + "</b>" + data.getOrderType()));
        }catch (Exception e)
        {
            Log.e("exc err",e.toString());
        }*/

     /* 1.	Pickup Pending
        2.	Pickup Done
        3.	Processing
        4.	Out for Delivery
        5.	Delivered*/

        /*

 Order Status (id and name)         Part of circle color getting blue
1 - Pickup pending                    0 % blue
2. Pickup done                          25 % blue
3. Processing                           50 % blue
4. Out for delivery                    75% blue
5. Delivery failed                      75% blue
6. Delivered                            100% blue

         */


        imgStatus.setImageResource(R.drawable.ready_pickup_00);
        txt_status.setText("Pickup Pending");

        if (data.getStatusCode() == 1) {
            imgStatus.setImageResource(R.drawable.ready_pickup_00);
            txt_status.setText("");
            txt_status.setText("Pickup Pending");
        }
        else if (data.getStatusCode() == 2) {
            imgStatus.setImageResource(R.drawable.ready_pickup_25);
            txt_status.setText("");
            txt_status.setText("Pickup done");
		}
        else if (data.getStatusCode() == 3) {
			imgStatus.setImageResource(R.drawable.ready_pickup_50);
            txt_status.setText("");
            txt_status.setText("Processing");
		}
        else if (data.getStatusCode() == 4) {
			imgStatus.setImageResource(R.drawable.ready_pickup_75);
            txt_status.setText("");
            txt_status.setText("Out for Delivery");
		}
        else if (data.getStatusCode() == 5) {
            imgStatus.setImageResource(R.drawable.ready_deliver_failed);
            txt_status.setText("");
            txt_status.setText("Delivery failed");
        }
        else if (data.getStatusCode() == 6) {
			imgStatus.setImageResource(R.drawable.ready_pickup_100);
            txt_status.setText("");
            txt_status.setText("Delivered");
		}

	}
}
