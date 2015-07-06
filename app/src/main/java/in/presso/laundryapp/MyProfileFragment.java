package in.presso.laundryapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import in.presso.pojo.PojoArea;
import in.presso.pojo.PojoSociety;
import in.presso.util.APICallTask;
import in.presso.util.APICallTaskForPost;
import in.presso.util.APIResponseListener;
import in.presso.util.FireToast;
import in.presso.util.SharedPrefUtils;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class MyProfileFragment extends Fragment implements OnClickListener, OnItemSelectedListener
{

	private static Context mContext;
	private static View mView;
	RelativeLayout changepwd_layout;
	EditText text_email,text_name,text_phno,text_address,text_home;
	String housetype,societyname;
    boolean societySelected=false;
	private RadioButton rdRentedHouse, rdOwnHouse;
	private Spinner spnArea, spnSociety;
	private ArrayList<PojoArea> listAreas;
	private ArrayList<String> listAreasName;
	private ArrayList<PojoSociety> listSociety;
	private ArrayList<String> listSocietyName;
	
	public MyProfileFragment()
	{
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_myprofile, container,false);
		mView = rootView;
		mContext = rootView.getContext();
		init();
		return rootView;
	}
	
	private void init() 
	{
		mView.findViewById(R.id.btnMenu).setOnClickListener(this);
		mView.findViewById(R.id.changepwd_layout).setOnClickListener(this);
		mView.findViewById(R.id.btn_save).setOnClickListener(this);
		
		text_email = (EditText) mView.findViewById(R.id.text_email);
		text_name = (EditText) mView.findViewById(R.id.text_name);
		text_phno = (EditText) mView.findViewById(R.id.text_phno);
		text_address = (EditText) mView.findViewById(R.id.text_address);
		text_home = (EditText) mView.findViewById(R.id.text_home);
		
		rdOwnHouse = (RadioButton) mView.findViewById(R.id.rdOwnHouse);
		rdRentedHouse = (RadioButton) mView.findViewById(R.id.rdRentedHouse);
		
		spnArea = (Spinner) mView.findViewById(R.id.spnArea);
        spnArea.setOnItemSelectedListener(this);
		spnSociety = (Spinner) mView.findViewById(R.id.spnSociety);
		
		text_email.setFocusable(false);
		text_home.setFocusable(false);
		
		APICallTask apiCallTask1 = new APICallTask(mContext, listenerGetAreaList,null, true);
		apiCallTask1.execute("http://www.presso.in/service.svc/GetAreaList");
	
	}

    private boolean isValidMobile(String phone2)
    {
        boolean check;
        if(phone2.length() != 10)
        {
            check = false;
        }
        else
        {
            check = true;
        }
        return check;
    }
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
			case R.id.btnMenu:
				((HomeActivity) mContext).openDrawer();
				break;
				
			case R.id.changepwd_layout:
				((HomeActivity) mContext).displayView(8);
				break;
			case R.id.btn_save:
				if(text_name.getText().toString().length()>0)
				{
                    if(!isValidMobile(text_phno.getText().toString().trim()))
                    {
                        FireToast.makeToast(mContext,"Please enter a 10 digit contact no.");
                        return;
                    }
					if (rdOwnHouse.isChecked()) {
						housetype = "0";
					} else {
						housetype = "1";
					}
					
					//if(text_home.getText().toString().length()>0)
					//{
						if(text_address.getText().toString().length()>0)
						{
							JSONObject objectAddress = new JSONObject();
							try 
							{
								objectAddress.put("Address", text_address.getText().toString().trim());
								if (rdOwnHouse.isChecked())
								{
									objectAddress.put("HouseType", "0");
								} else {
									objectAddress.put("HouseType", "1");
								}

								objectAddress.put("Id", "0");
								objectAddress.put("Title", text_home.getText().toString().trim());

								JSONObject objectArea = new JSONObject();
								objectArea.put("AreaId", listAreas.get(spnArea.getSelectedItemPosition()).AreaId);
								objectArea.put("AreaName", spnArea.getSelectedItem().toString());
								objectArea.put("City", "1");
								objectAddress.put("Area", objectArea);

								JSONObject objectSociety = new JSONObject();
								objectSociety.put("SocietyId", listSociety.get(spnSociety.getSelectedItemPosition()).SocietyId);
								objectSociety.put("SocietyName", spnSociety.getSelectedItem().toString());

								objectAddress.put("Society", objectSociety);
								
								System.out.println("objectAddress = "+objectAddress);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                            String add=objectAddress.toString();
                            //Log.e("Address",objectAddress.toString());
							APICallTaskForPost apiCallTask = new APICallTaskForPost(mContext, listener1, null,true,objectAddress);
							//APICallTaskForPost apiCallTask = new APICallTaskForPost(mContext, listener1, null,true);
							apiCallTask.execute("http://www.presso.in/service.svc/UpdateCustomerDetails/"+text_name.getText().toString()+"/"+text_phno.getText()+"/"+ SharedPrefUtils.getKey_id(mContext));

						}
						else
						{
							HomeActivity.show_dialog(mContext, "Please enter address.");
							return;
						}
					//}
					//else
					//{
					//	HomeActivity.show_dialog(mContext, "Please enter home.");
					//	return;
					//}
				}
				else
				{
					HomeActivity.show_dialog(mContext, "Please enter name.");
					return;
				}
				break;
		}
	}
	
	APIResponseListener listenerGetAreaList = new APIResponseListener() {

		@Override
		public void onSuccess(Object profile) {
			JSONObject object = (JSONObject) profile;
			try 
			{
				JSONArray arrayAreaList = object.getJSONArray("data");
				listAreas = new ArrayList<PojoArea>();
				listAreasName = new ArrayList<String>();

				for (int i = 0; i < arrayAreaList.length(); i++)
				{
					PojoArea pojoArea;
					listAreas.add(pojoArea = new Gson().fromJson(arrayAreaList.getJSONObject(i).toString(), PojoArea.class));
					listAreasName.add(pojoArea.getAreaName());
					spnArea.setAdapter(new ArrayAdapter<String>(mContext,R.layout.spinner_item, listAreasName));
				}
				
				APICallTask apiCallTask = new APICallTask(mContext, listener, null,true);
				apiCallTask.execute("http://www.presso.in/service.svc/GetCustomerDetails/"+ SharedPrefUtils.getKey_id(mContext));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onError(String message) {
			FireToast.makeToast(mContext, message);
		}
	};
	
	APIResponseListener listener = new APIResponseListener()
	{
		@Override
		public void onSuccess(Object profile) 
		{
			JSONObject object=(JSONObject) profile;
			try 
			{
				JSONObject data = object.getJSONObject("data");
				text_email.setText(data.optString("EmailId"));
				text_name.setText(data.optString("Name"));
				text_phno.setText(data.optString("ContactNumber"));
				JSONArray address = data.optJSONArray("Addresses");
				for(int i=0;i<address.length();i++)
				{
					JSONObject obj =  address.getJSONObject(i);
					text_address.setText(obj.optString("Address"));
					housetype = obj.optString("HouseType");
					text_home.setText(obj.optString("Title"));
					
					if(housetype.endsWith("1"))
					{
						rdRentedHouse.setChecked(true);
						rdOwnHouse.setChecked(false);
					}
					else
					{
						rdRentedHouse.setChecked(false);
						rdOwnHouse.setChecked(true);
					}
					
					JSONObject area =  obj.getJSONObject("Area");
					String areaname = area.optString("AreaName");
					System.out.println("area name = "+listAreasName);
					System.out.println("area = "+listAreasName);
					int position = listAreasName.indexOf(areaname);
					spnArea.setSelection(position);
					JSONObject society =  obj.getJSONObject("Society");
                    societyname = society.optString("SocietyName");
					APICallTask apiCallTask = new APICallTask(mContext, listenerGetSocietyList,null, true);
					apiCallTask.execute("http://www.presso.in/service.svc/GetSocietyByArea/"+ listAreas.get(position).AreaId);


				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		@Override
		public void onError(String message) {
			FireToast.makeToast(mContext, message);
		}
	};
	
	APIResponseListener listenerGetSocietyList = new APIResponseListener() 
	{
		@Override
		public void onSuccess(Object profile) 
		{
			JSONObject object = (JSONObject) profile;
			try
			{
				JSONArray arrayAreaList = object.getJSONArray("data");
				listSociety = new ArrayList<PojoSociety>();
				listSocietyName = new ArrayList<String>();

				for (int i = 0; i < arrayAreaList.length(); i++) 
				{
					PojoSociety pojoSociety;
					listSociety.add(pojoSociety = new Gson().fromJson(arrayAreaList.getJSONObject(i).toString(),PojoSociety.class));
					listSocietyName.add(pojoSociety.getSocietyName());
					spnSociety.setAdapter(new ArrayAdapter<String>(mContext,R.layout.spinner_item,listSocietyName));
				}
                if(!societySelected){
                    int position1 = listSocietyName.indexOf(societyname);
                    spnSociety.setSelection(position1);

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

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
	{
		APICallTask apiCallTask = new APICallTask(mContext, listenerGetSocietyList,null, true);
		apiCallTask.execute("http://www.presso.in/service.svc/GetSocietyByArea/"+ listAreas.get(arg2).AreaId);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	APIResponseListener listener1 = new APIResponseListener()
	{

		@Override
		public void onSuccess(Object profile) 
		{
			JSONObject object=(JSONObject) profile;
			try
			{
				if(object.getBoolean("success"))
				{
					HomeActivity.show_dialog(mContext, "Profile updated successfully.");
				}
				else
				{
					HomeActivity.show_dialog(mContext, "Sorry! Unable to update profile. Try again.");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void onError(String message) {
			FireToast.makeToast(mContext, message);
		}
	};
}
