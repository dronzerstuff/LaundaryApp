package in.presso.laundryapp;

import in.presso.pojo.OrderData;
import in.presso.util.APICallTaskForPost;
import in.presso.util.APIResponseListener;
import in.presso.util.FireToast;
import in.presso.util.SharedPrefUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UpdateorderFragment extends Fragment implements OnClickListener {
	private static Context mContext;
	private static View mView;
	public RelativeLayout spDeliveryType, spOrderType;
	private String[] deliveryList, orderList;
	public static boolean flagPickupDate, flagPickupTime, flagDeliveryDate,
			flagDeliveryTime;
	public static TextView edtPickupDate, edtPickupTime, edtDeliveryDate,
			edtDeliveryTime;
	public RelativeLayout pickupdate_layout, pickuptime_layout,
			deliverydate_layout, deliverytime_layout;
	public Button btnSchedule;
	public TextView text_deliverytype, text_ordertype;
	public static boolean flagnormalDeliverytype = false;
	public static boolean flagexpressDeliverytype = false;
	int pickup_start_hour;
	int pickup_end_hour;
	int delivery_start_hour;
	int delivery_end_hour;
	String[] pickup_slot_list;
	String[] pickup_slot_display_list2;
	String[] delivery_slot_display_list2;
	String pickup_minute ="00";
	String pickup_am_pm = "";
	int selectedOrderType=0;
	
	public UpdateorderFragment() {

	}

	private OrderData data;
    @SuppressLint("ValidFragment")
	public UpdateorderFragment(OrderData orderData) {
        data = orderData;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_update_order,
				container, false);
		mView = rootView;
		mContext = rootView.getContext();

		init();

		
		// String
		// arrayOrderType[]=getResources().getStringArray(R.array.orderType);
		//spnOrderType.setSelection(data.getOrderType());

		return rootView;
	}

	private void init() {
		mView.findViewById(R.id.btnMenu).setOnClickListener(this);

		edtPickupDate = (TextView) mView.findViewById(R.id.edtPickupDate);
		edtPickupTime = (TextView) mView.findViewById(R.id.edtPickupTime);
		edtDeliveryDate = (TextView) mView.findViewById(R.id.edtDeliveryDate);
		edtDeliveryTime = (TextView) mView.findViewById(R.id.edtDeliveryTime);

		pickupdate_layout = (RelativeLayout) mView.findViewById(R.id.pickupdate_layout);
		pickuptime_layout = (RelativeLayout) mView.findViewById(R.id.pickuptime_layout);
		deliverydate_layout = (RelativeLayout) mView.findViewById(R.id.deliverydate_layout);
		deliverytime_layout = (RelativeLayout) mView.findViewById(R.id.deliverytime_layout);



		text_deliverytype = (TextView) mView.findViewById(R.id.text_deliverytype);
		text_ordertype = (TextView) mView.findViewById(R.id.text_ordertype);

		btnSchedule = (Button) mView.findViewById(R.id.btnSchedule);
		
		String deliveryType;
		if(data.isExpress()){
			deliveryType="Express";
			flagnormalDeliverytype=false;
			flagexpressDeliverytype=true;
		}
		else{
			deliveryType="Normal";
			flagnormalDeliverytype=true;
			flagexpressDeliverytype=false;
		}
		text_deliverytype.setText(deliveryType);
		
		int ordertype=data.getOrderType();
		if(ordertype==1)
			text_ordertype.setText("Ironing");
		else if(ordertype==2)
			text_ordertype.setText("Wash and Iron");
		else if(ordertype==3)
			text_ordertype.setText("Dry Cleaning");
		

        SimpleDateFormat sdf=new SimpleDateFormat("dd MMM yyyy");

		try {
			edtPickupDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(sdf.parse(data.getPickUpDate())));
			edtDeliveryDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(sdf.parse(data.getDeliveryDate())));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		edtPickupTime.setText(data.getPickUpTime());
		edtDeliveryTime.setText(data.getDeliveryTime());
		
		int orderstatus=data.getStatusCode();
        //Log.e("status code",String.valueOf(data.getStatusCode()));
        //Log.e("Order Id",String.valueOf(data.getOrderId()));
		if (orderstatus>1){
			pickupdate_layout.setClickable(false);
			pickupdate_layout.setFocusable(false);
			pickupdate_layout.setFocusableInTouchMode(false);
			
			pickuptime_layout.setClickable(false);
			pickuptime_layout.setFocusable(false);
			pickuptime_layout.setFocusableInTouchMode(false);
		}
		if(orderstatus>=4){
			deliverydate_layout.setClickable(false);
			deliverydate_layout.setFocusable(false);
			deliverydate_layout.setFocusableInTouchMode(false);
			
			deliverytime_layout.setClickable(false);
			deliverytime_layout.setFocusable(false);
			deliverytime_layout.setFocusableInTouchMode(false);
		}
		
		btnSchedule = (Button) mView.findViewById(R.id.btnSchedule);
		if (orderstatus==1){
            pickupdate_layout.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (!text_deliverytype.getText().toString().equals("Delivery type") && !text_ordertype.getText().toString().equals("Order type"))
                    {
                        showDatePickerDialog(v);
                        flagPickupDate = true;
                        flagDeliveryDate = false;
                    }
                    else
                    {
                        if (text_deliverytype.getText().toString().equals("Delivery type"))
                            HomeActivity.show_dialog(mContext,"Please select delivery type first.");
                        else
                            HomeActivity.show_dialog(mContext,"Please select order type first.");
                        return;
                    }
                }
            });
		}
		if(orderstatus<4){
		deliverydate_layout.setOnClickListener(new OnClickListener()
		{
			@Override
            public void onClick(View v)
            {
                if (!text_deliverytype.getText().toString().equals("Delivery type"))
                {
                    if(flagnormalDeliverytype)
                    {
                        if (!edtPickupDate.getText().toString().equals("Date") && !edtPickupTime.getText().toString().equals("Time") && !text_ordertype.getText().toString().equals("Order type"))
                        {
                            showDatePickerDialog(v);
                            flagPickupDate = false;
                            flagDeliveryDate = true;
                        }
                        else
                        {
                            if(edtPickupDate.getText().toString().equals("Date"))
                                HomeActivity.show_dialog(mContext,"Please select pickup date first.");
                            else if(edtPickupTime.getText().toString().equals("Time"))
                                HomeActivity.show_dialog(mContext,"Please select pickup time first.");
                            else
                                HomeActivity.show_dialog(mContext,"Please select order type first.");
                            return;
                        }
                    }
                }
                else
                {
                    HomeActivity.show_dialog(mContext,"Please select delivery type first.");
                    return;
                }
			}
		});
		}
		if (orderstatus==1){
		pickuptime_layout.setOnClickListener(new OnClickListener() {
			@Override
            public void onClick(View v) {
                if (isDeliveryTypeOrderTypeNotSpecified()) return;

                String pickUpDate = edtPickupDate.getText().toString();
                if (pickUpDate.equals("Date")) {
                    HomeActivity.show_dialog(mContext, "Please select pickup date first.");
                    return;
                }

                if (ifExpressDeliveryNotPossible()) {
                    HomeActivity.show_dialog(mContext, "Express delivery can only be placed between 8 Am to 7 PM.");
                    return;
                }

                try {
                    //cache current value of pickup time
                    final String currentSelection = edtPickupTime.getText().toString();

                    Calendar c = Calendar.getInstance();

                    pickup_minute = getPickupTimeIn15MinSlot(c.get(c.MINUTE));
                    int currentHour = Integer.parseInt(new SimpleDateFormat("HH").format(c.getTime()));

                    final Date pickupDate = new SimpleDateFormat("dd/MM/yyyy").parse(pickUpDate);
                    final Date today = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(c.getTime().toString());
                    if (currentHour <= 7 || pickupDate.after(today)) {
                        pickup_start_hour = 8;
                        pickup_minute = "00";
                    } else if (pickup_minute == "00") {
                        pickup_start_hour = currentHour + 2;
                    } else {
                        pickup_start_hour = currentHour + 1;
                    }

                    pickup_end_hour = pickup_start_hour + 2;

                    // No pickup allowed for today, after 7 PM
                    if (pickup_end_hour > 22 || (pickup_end_hour == 22 && pickup_minute != "00")) {
                        HomeActivity
                                .show_dialog(
                                        mContext,
                                        "Pickup for selected date cannot be scheduled. Please schedule a pickup for some other date.");
                    } else {
                        ArrayList<String> pickup_slot_display_list1 = getTimeSlotWith2HoursInterval(pickup_start_hour, pickup_minute);
                        pickup_slot_display_list2 = new String[pickup_slot_display_list1
                                .size()];
                        pickup_slot_display_list2 = pickup_slot_display_list1.toArray(pickup_slot_display_list2);

                        new AlertDialog.Builder(mContext)
                                .setSingleChoiceItems(
                                        pickup_slot_display_list2,
                                        0,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int whichButton) {
                                                dialog.dismiss();
                                                int selectedPosition = ((AlertDialog) dialog)
                                                        .getListView()
                                                        .getCheckedItemPosition();
                                                edtPickupTime
                                                        .setText(pickup_slot_display_list2[selectedPosition]);
                                                if (edtPickupTime.getText().toString() != currentSelection) {
                                                    edtDeliveryDate.setText("Date");
                                                    edtDeliveryTime.setText("Time");
                                                }

                                            }
                                        }).show();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
		}
		if(orderstatus<4){
		deliverytime_layout.setOnClickListener(new OnClickListener() {
			@Override
            public void onClick(View v) {

                if (isDeliveryTypeOrderTypeNotSpecified()) return;

                if (isPickupDetailsNotSpecified()) return;

                if (edtDeliveryDate.getText().toString().equals("Date")) {
                    HomeActivity.show_dialog(mContext,
                            "Please select delivery date first.");
                    return;
                }

                String pickupStartTime=(String) edtPickupTime.getText();

                int spaceIndexAfterDash=pickupStartTime.indexOf(' ',0);
                delivery_start_hour = Integer.parseInt(pickupStartTime.substring(0,pickupStartTime.indexOf(':',0)))+2;// pickup_start_hour + 2;
                delivery_end_hour = delivery_start_hour + 2;
                String delivery_am_pm=pickupStartTime.substring(spaceIndexAfterDash+1,spaceIndexAfterDash+3);
                String delivery_minute=(pickupStartTime.substring(pickupStartTime.indexOf(':')+1,pickupStartTime.indexOf(' ')));

                Date pickupDate = null;
                Date deliveryDate = null;
                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
                try {
                    pickupDate = sdf.parse((String)edtPickupDate.getText());
                    deliveryDate = sdf.parse((String)edtDeliveryDate.getText());
                } catch (ParseException e) {

                    e.printStackTrace();
                }

                Calendar c=Calendar.getInstance();
                c.setTime(pickupDate);
                c.add(Calendar.DATE, 2);
                try {
                    if(deliveryDate.after(sdf.parse(sdf.format(c.getTime()))) || deliveryDate.equals(sdf.parse(sdf.format(c.getTime())))){
                        delivery_start_hour=8;
                        delivery_am_pm="AM";
                        delivery_minute="00";
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if(delivery_start_hour >=8 && delivery_am_pm.equals("PM") && Integer.parseInt(delivery_minute)>0 && delivery_start_hour!=12){
                    HomeActivity.show_dialog(mContext,
                            "No delivery slot available for "+edtDeliveryDate.getText() +". Please select next date.");
                    return;
                }

                if(delivery_am_pm.equals("PM") && delivery_start_hour!=12 ) {
                    delivery_start_hour += 12;
                }

                ArrayList<String> delivery_slot_display_list1 = getTimeSlotWith2HoursInterval(delivery_start_hour,delivery_minute);
                delivery_slot_display_list2 = new String[delivery_slot_display_list1
                        .size()];
                delivery_slot_display_list2=delivery_slot_display_list1.toArray(delivery_slot_display_list2);


                new AlertDialog.Builder(mContext).setSingleChoiceItems(
                        delivery_slot_display_list2, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                                int deliverySlot_selectedPosition = ((AlertDialog) dialog)
                                        .getListView()
                                        .getCheckedItemPosition();
                                // Do something useful with the
                                // position of the selected radio button

                                edtDeliveryTime.setText(delivery_slot_display_list2[deliverySlot_selectedPosition]);

                            }
                        }).show();
            }
        });
	}
		btnSchedule.setOnClickListener(this);

		spDeliveryType = (RelativeLayout) mView
				.findViewById(R.id.deliverytype_layout);
		deliveryList = getResources().getStringArray(R.array.deliveryType);
		new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,
				deliveryList);



		spOrderType = (RelativeLayout) mView
				.findViewById(R.id.ordertype_layout);
		orderList = getResources().getStringArray(R.array.orderType);
		new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,
				orderList);

	}
    private boolean isPickupDetailsNotSpecified() {
        if (edtPickupDate.getText().toString().equals("Date")) {
            HomeActivity.show_dialog(mContext,
                    "Please select pickup date first.");
            return true;
        }
        if (edtPickupTime.getText().toString().equals("Time")) {
            HomeActivity.show_dialog(mContext,
                    "Please select pickup time first.");
            return true;
        }
        return false;
    }
    private boolean isDeliveryTypeOrderTypeNotSpecified() {
        if (text_deliverytype.getText().toString().equals("Delivery type")) {
            HomeActivity.show_dialog(mContext, "Please select delivery type first.");
            return true;
        }

        if (text_ordertype.getText().toString().equals("Order type")) {
            HomeActivity.show_dialog(mContext, "Please select order type first.");
            return true;
        }
        return false;
    }
	public static class MyDatePickerFragment extends DialogFragment implements
			OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datepicker=new DatePickerDialog(getActivity(), this, year, month, day);
            datepicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
			return datepicker;
		}

        private static void ResetFieldValues() {
            edtPickupTime.setText("Time");
            edtDeliveryDate.setText("Date");
            edtDeliveryTime.setText("Time");
        }
		@Override
		public void onDateSet(android.widget.DatePicker arg0, int arg1,int arg2, int arg3)
		{
			try
			{
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				Date date1 = formatter.parse(arg3 + "-" + (arg2 + 1) + "-"	+ arg1);
				Calendar c = Calendar.getInstance();
				String current = formatter.format(c.getTime());
				Date now = formatter.parse(current);
                Calendar cDay = Calendar.getInstance();
                cDay.setTime(date1);
                int day = cDay.get(Calendar.DAY_OF_WEEK);
                if(day== Calendar.THURSDAY)
                {
                    HomeActivity.show_dialog(mContext,"It's Thursday, our weekly off. Pickup and Delivery service is not available on Thursday's.");
                    return;
                }
				if (flagPickupDate) 
				{
					if (flagnormalDeliverytype) 
					{
						if (now.after(date1))
						{
							HomeActivity.show_dialog(mContext,"Please select date greater than or equal to today's date.");
							return;
						} 
						else 
						{
                            if(edtPickupDate.getText() !=((arg3) + "/" + (arg2 + 1) + "/"+ arg1))
                                ResetFieldValues();
                            edtPickupDate.setText((arg3) + "/" + (arg2 + 1) + "/"+ arg1);
						}
					}
				}
				if(flagDeliveryDate)
				{
					if (flagnormalDeliverytype) 
					{
						Date outputdate = null;
						Calendar c1 = Calendar.getInstance();
					    String[] d = edtPickupDate.getText().toString().split("/");
					    if(d.length==3)
					    {
					    		String dt = d[0]+"-"+d[1]+"-"+d[2];
					    		c1.setTime(formatter.parse(dt));
					    		c1.add(Calendar.DATE, 1);
					    		String output = formatter.format(c1.getTime()); 
							    outputdate = formatter.parse(output); 
					    }
					    if(outputdate.after(date1))
						{
							HomeActivity.show_dialog(mContext,"Normal orders can be delivered 24 hours after pickup time.");
							return;
						} 
						else 
						{
                            edtDeliveryDate.setText( (arg3)+ "/" + (arg2 + 1) + "/"+ arg1);
						}
					}
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	};

	public void showDatePickerDialog(View v) {
		DialogFragment dialogFragment = new MyDatePickerFragment();
		dialogFragment.show(getFragmentManager(), "datePicker");
	}
	private String getDate(String date)
	{
		String[] d = date.split("/");
		//return d[0]+" "+getMonthShortName(Integer.parseInt(d[1])-1)+" "+d[2];
		return d[1]+"/"+d[0]+"/"+d[2];
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnMenu:
			((HomeActivity) mContext).openDrawer();
			break;

		case R.id.btnSchedule:

			if (!text_deliverytype.getText().toString().equals("Delivery type")) {
				if (!text_ordertype.getText().toString().equals("Order type")) {
					if (!edtPickupDate.getText().toString().equals("Date")) {
						if (!edtPickupTime.getText().toString().equals("Time")) {
							if (!edtDeliveryDate.getText().toString()
									.equals("Date")) {
								if (!edtDeliveryTime.getText().toString()
										.equals("Time")) {
									try {
										JSONObject objectOrder = new JSONObject();




                                        objectOrder.put("PickupDate", getDate(edtPickupDate.getText().toString()));

										objectOrder.put("PickupTimeSlot",
												edtPickupTime.getText()
														.toString());



                                        objectOrder.put("DeliveryDate",getDate(edtDeliveryDate.getText().toString()));

										objectOrder.put("DeliveryTimeSlot",
												edtDeliveryTime.getText()
														.toString());
                                        //Log.e("Order Id",String.valueOf(data.getOrderId()));
                                        objectOrder.put("Id", data.getOrderId());

                                        objectOrder.put("Status", "1");
										objectOrder.put("OrderType",selectedOrderType );
                                        objectOrder.put("BillAmount", "0");

											if (flagexpressDeliverytype) {
											objectOrder
													.put("IsExpress", "true");
										} else {
											objectOrder.put("IsExpress",
													"false");
										}

                                        //Log.e("order tpyr update1",String.valueOf(selectedOrderType));
                                        //Log.e("order tpyr update2",String.valueOf(text_ordertype.getText().toString()));

                                        objectOrder.put("AssignedTo", "0");

                                        //Log.e("Customer Object upate",objectOrder.toString());

											APICallTaskForPost apiCallTask = new APICallTaskForPost(
												mContext, listener1, null,
												true, objectOrder);
										apiCallTask
												.execute("http://www.presso.in/service.svc/UpdateOrder/"
														+ SharedPrefUtils
																.getKey_id(mContext));
                                        //Log.e("update ","sucessfully");
                                        //Log.e("update order",String.valueOf(SharedPrefUtils.getKey_id(mContext)));

									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} else {
									HomeActivity.show_dialog(mContext,
											"Please select delivery time.");
								}
							} else {
								HomeActivity.show_dialog(mContext,
										"Please select delivery date.");
							}
						} else {
							HomeActivity.show_dialog(mContext,
									"Please select pickup time.");
						}
					} else {
						HomeActivity.show_dialog(mContext,
								"Please select pickup date.");
					}
				} else {
					HomeActivity.show_dialog(mContext,
							"Please select order type.");
				}
			} else {
				HomeActivity.show_dialog(mContext,
						"Please select delivery type.");
			}
			break;
		}
	}

	APIResponseListener listener1 = new APIResponseListener() {

		@Override
		public void onSuccess(Object profile) {
			JSONObject object = (JSONObject) profile;
			try {
				if (object.getBoolean("success")) {

                    CustomDialogBox box = new CustomDialogBox(getActivity(),"Order Update successfully !!!",R.style.CustomDialogsTheme) ;
                    box.show();
/*
                    HomeActivity.show_dialog(mContext,
							"Order Update successfully.");*/
                    ((HomeActivity) mContext).displayView(1);


				} else {

                    CustomDialogBox box = new CustomDialogBox(getActivity(),"Unable to update order. Try again !!!",R.style.CustomDialogsTheme) ;
                    box.show();
                    /*HomeActivity.show_dialog(mContext,
							"Unable to update order. Try again!");*/
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
    private ArrayList<String> getTimeSlotWith2HoursInterval(int pickup_start_hour, String pickup_minute) {
        ArrayList<String> timeSlotList = new ArrayList<String>();
        int pickup_end_hour = pickup_start_hour + 2;

        while (pickup_end_hour < 22) {
            timeSlotList.add(getDisplayEntryFor(pickup_start_hour, pickup_end_hour, pickup_minute, pickup_minute));
            pickup_start_hour = pickup_end_hour;
            pickup_end_hour += 2;
        }
        if (pickup_end_hour == 22 && pickup_minute == "00") {
            timeSlotList.add(getDisplayEntryFor(20, 22, "00", "00"));
        }
        return timeSlotList;
    }

    private String getDisplayEntryFor(int start_hour,int end_hour, String minute,String endMinute) {
        String timeRangeInAmPm = "" + get12HourFormatFrom24HourFormat(start_hour,minute) + " - " + get12HourFormatFrom24HourFormat(end_hour,endMinute);
        return timeRangeInAmPm;
    }

    private String get12HourFormatFrom24HourFormat(int hour, String minute) {
        if (hour < 12) return hour + ":" + minute + " AM";
        else if (hour == 12) return hour + ":" + minute + " PM";
        return hour - 12 + ":" + minute + " PM";
    }




    private String getPickupTimeIn15MinSlot(int minuteComponent) {
        if (isBetweenInterval(minuteComponent,0,14)) return  "15";
        else if (isBetweenInterval(minuteComponent,15,29)) {
            return "30";
        } else if (isBetweenInterval(minuteComponent,30,44)) {
            return "45";
        } else //if (isBetweenInterval(minuteComponent,45,59))
        {
            return "00";   //30 to 0
        }
    }

    private boolean isBetweenInterval(int number, int start, int end)
    {
        return (number >= start
                && number <= end);
    }

    public boolean ifExpressDeliveryNotPossible() {
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mm aa");
            Date time1 = formatter1.parse(formatter1.format(c.getTime()));
            if (flagexpressDeliverytype) {
                if (time1.after(formatter1.parse("07:00 PM"))) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
	public boolean if_possible_express_delivery() {
		try {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mm aa");
			Date time1 = formatter1.parse(formatter1.format(c.getTime()));
			if (flagexpressDeliverytype) {
				if (time1.after(formatter1.parse("07:00 PM"))) {
					return true;
				} else {
					return false;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean if_possible_normal_delivery() {
		try {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mm aa");
			Date time1 = formatter1.parse(formatter1.format(c.getTime()));
			if (flagnormalDeliverytype) {
				if (time1.after(formatter1.parse("07:00 PM"))) {
					return true;
				} else {
					return false;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;
	}

}
