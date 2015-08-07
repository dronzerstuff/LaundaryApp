package in.presso.laundryapp;

import in.presso.adapter.CustomSpinnerAdapter;
import in.presso.pojo.PojoArea;
import in.presso.pojo.PojoSociety;
import in.presso.util.APICallTask;
import in.presso.util.APICallTaskForPost;
import in.presso.util.APIResponseListener;
import in.presso.util.FireToast;
import in.presso.util.SharedPrefUtils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

public class RegisterAddressActivity extends Activity implements
        OnClickListener, OnItemSelectedListener {

    private JSONObject objectCustomer;
    private EditText  edtAddress;
    private TextView txtHome,txtTerm;
    private Spinner spnArea, spnSociety;
    private RadioButton rdRentedHouse, rdOwnHouse;
    private ArrayList<PojoArea> listAreas;
    private ArrayList<String> listAreasName;

    private ArrayList<PojoSociety> listSociety;
    private ArrayList<String> listSocietyName;
    private String termncondinUrl = "http://www.presso.in/TermsAndConditions.aspx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_address);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        txtHome = (TextView) findViewById(R.id.txtHome);
        rdOwnHouse = (RadioButton) findViewById(R.id.rdOwnHouse);
        rdRentedHouse = (RadioButton) findViewById(R.id.rdRentedHouse);
        spnArea = (Spinner) findViewById(R.id.spnArea);
        spnSociety = (Spinner) findViewById(R.id.spnSociety);
        txtTerm = (TextView) findViewById(R.id.tv_term);
        txtTerm.setPaintFlags(txtTerm.getPaintFlags()
                | Paint.UNDERLINE_TEXT_FLAG);
        spnArea.setOnItemSelectedListener(this);
        txtTerm.setOnClickListener(termnCondinListener);

        APICallTask apiCallTask = new APICallTask(this, listenerGetAreaList,
                null, true);
       apiCallTask.execute("http://www.presso.in/service.svc/GetAreaList");


    try {
            objectCustomer = new JSONObject(getIntent().getStringExtra(
                    "customer"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    APIResponseListener listenerGetAreaList = new APIResponseListener() {

        @Override
        public void onSuccess(Object profile) {
            JSONObject object = (JSONObject) profile;
            try {
                JSONArray arrayAreaList = object.getJSONArray("data");
                listAreas = new ArrayList<PojoArea>();
                listAreasName = new ArrayList<String>();
                listAreasName.add("Please Select Area");

                for (int i = 0; i < arrayAreaList.length(); i++) {
                    PojoArea pojoArea;
                    listAreas.add(pojoArea = new Gson().fromJson(arrayAreaList
                            .getJSONObject(i).toString(), PojoArea.class));
                    listAreasName.add(pojoArea.getAreaName());

                    spnArea.setAdapter(new CustomSpinnerAdapter(RegisterAddressActivity.this,listAreasName,R.layout.spinner_layout,R.layout.spinner_dropview_layout));

                   /* spnArea.setAdapter(new ArrayAdapter<String>(
                            RegisterAddressActivity.this,
                            R.layout.spinner_item,R.id.txt1, listAreasName));*/

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(String message) {
            FireToast.makeToast(getApplicationContext(), message);
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnContinue) {

            if (TextUtils.isEmpty(edtAddress.getText().toString())) {
                FireToast.makeToast(getApplicationContext(),
                        "Please enter address");
                return;

            }

            if (spnArea.getSelectedItem().toString()
                    .equals("Please Select Area")) {

                FireToast.makeToast(getApplicationContext(),
                        "Please Select Area");
                return;
            }

            if (spnSociety.getSelectedItem().toString()
                    .equals("Please Select Society")) {
                FireToast.makeToast(getApplicationContext(),
                        "Please Select Society");
                return;
            }


		/*	if (TextUtils.isEmpty(txtHome.getText().toString())) {
				FireToast.makeToast(getApplicationContext(),
						"Please enter home");
				return;

			}*/
            try {
                JSONArray arrayAddres = new JSONArray();

                // {"Id":0,"Name":"Vinod","Password":"password","EmailId":"surivin@gmail.com","ContactNumber":null,
                // "Addresses":[{"Title":"Home","Address":"Address Line","HouseType":0,"Id":0,
                // "Area":{"AreaId":1,"AreaName":"Pimple Saudagar","City":0},
                // "Society":{"SocietyId":1,"SocietyName":"Ginger","AreaId":0}}]}

                JSONObject objectAddress = new JSONObject();
                objectAddress.put("Address", edtAddress.getText().toString()
                        .trim());

                if (rdOwnHouse.isChecked()) {
                    objectAddress.put("HouseType", "0");
                } else {
                    objectAddress.put("HouseType", "1");
                }

                objectAddress.put("Id", "0");
                objectAddress.put("Title", txtHome.getText().toString().trim());

                JSONObject objectArea = new JSONObject();
                objectArea
                        .put("AreaId", listAreas.get(spnArea
                                .getSelectedItemPosition()-1).AreaId);
                objectArea
                        .put("AreaName", spnArea.getSelectedItem().toString());
                objectArea.put("City", "1");
                objectAddress.put("Area", objectArea);

                JSONObject objectSociety = new JSONObject();
                objectSociety
                        .put("SocietyId", listSociety.get(spnSociety
                                .getSelectedItemPosition()-1).SocietyId);
                objectSociety.put("SocietyName", spnSociety.getSelectedItem()
                        .toString());

                objectAddress.put("Society", objectSociety);

                arrayAddres.put(objectAddress);

                objectCustomer.put("Addresses", arrayAddres);

                //Log.e("Customer Object", objectCustomer.toString());
                /*startActivity(new Intent(this, TermsConditionActivity.class)
                        .putExtra("customerAddressDetails", objectCustomer.toString()));*/

                APICallTaskForPost apiCallTask = new APICallTaskForPost(this,
                        listener, null, true, objectCustomer);
                apiCallTask
                        .execute("http://www.presso.in/service.svc/RegisterCustomer");
                //APICallTaskForPost apitask = new APICallTaskForPost(this,listener1,null,true,objectCustomer);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    APIResponseListener listener = new APIResponseListener() {

        @Override
        public void onSuccess(Object profile) {
            FireToast.makeToast(RegisterAddressActivity.this,
                    "Register successfully");
            SharedPrefUtils.setKey_login(true, RegisterAddressActivity.this);

            JSONObject objectCustomer = (JSONObject) profile;
            try {
                SharedPrefUtils.setKey_id(objectCustomer.getString("data"),
                        RegisterAddressActivity.this);

                startActivity(new Intent(RegisterAddressActivity.this,
                        HomeActivity.class));

                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(String message) {
            FireToast.makeToast(RegisterAddressActivity.this, message);

        }
    };

    APIResponseListener listenerGetSocietyList = new APIResponseListener() {

        @Override
        public void onSuccess(Object profile) {
            JSONObject object = (JSONObject) profile;
            try {
                JSONArray arrayAreaList = object.getJSONArray("data");
                listSociety = new ArrayList<PojoSociety>();
                listSocietyName = new ArrayList<String>();
                listSocietyName.add("Please Select Society");
                for (int i = 0; i < arrayAreaList.length(); i++) {
                    PojoSociety pojoSociety;
                    listSociety.add(pojoSociety = new Gson().fromJson(
                            arrayAreaList.getJSONObject(i).toString(),
                            PojoSociety.class));
                    listSocietyName.add(pojoSociety.getSocietyName());

                    spnSociety.setAdapter(new CustomSpinnerAdapter(RegisterAddressActivity.this, listSocietyName, R.layout.spinner_layout, R.layout.spinner_dropview_layout));


                    /*spnSociety.setAdapter(new ArrayAdapter<String>(
                            RegisterAddressActivity.this,
                            R.layout.spinner_item,R.id.txt1,
                            listSocietyName));*/

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(String message) {
            FireToast.makeToast(getApplicationContext(), message);
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                               long arg3) {


        if (pos == 0) {
            if(listSocietyName!=null){
                spnSociety.setSelection(0);
            }

        }
        if (pos != 0) {
            APICallTask apiCallTask = new APICallTask(this,
                    listenerGetSocietyList, null, true);
            apiCallTask
                    .execute("http://www.presso.in/service.svc/GetSocietyByArea/"
                            + listAreas.get(pos-1).AreaId);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    private OnClickListener termnCondinListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent tmcondintent = new Intent(Intent.ACTION_VIEW,Uri.parse(termncondinUrl));
            startActivity(tmcondintent);
        }
    };
}
