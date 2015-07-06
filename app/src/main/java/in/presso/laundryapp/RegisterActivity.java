package in.presso.laundryapp;

import in.presso.util.FireToast;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class RegisterActivity extends Activity {
    private LinearLayout layStep1, layStep2;
    private EditText edtName, edtEmail, edtPassword, edtConPassword,
            edtContact, edtRefCode;
    private EditText edtHome, edtAddress, edtArea, edtSociety;
    private static Context mContext;
    private static final int Register_code = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
    }

    private void init() {

        mContext = this;

        layStep1 = (LinearLayout) findViewById(R.id.layStep1);

        edtName = (EditText) findViewById(R.id.edtName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConPassword = (EditText) findViewById(R.id.edtConPassword);
        edtContact = (EditText) findViewById(R.id.edtContact);
        edtRefCode = (EditText) findViewById(R.id.edtRefCode);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContinue:

                String name = edtName.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String emailId = edtEmail.getText().toString().trim();
                String contactNo = edtContact.getText().toString().trim();
                String confirmPassword = edtConPassword.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    FireToast.makeToast(mContext, "Please enter name.");
                    return;
                }
                if (TextUtils.isEmpty(emailId)) {
                    FireToast.makeToast(mContext, "Please enter email id.");
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailId)
                        .matches()) {
                    FireToast.makeToast(this,"Please enter valid email id.");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    FireToast.makeToast(mContext, "Please enter password.");
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    FireToast.makeToast(mContext, "Please enter confirm password.");
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    FireToast.makeToast(getApplicationContext(),
                            "Password not match.");
                    return;
                }
                if (TextUtils.isEmpty(contactNo)) {
                    FireToast.makeToast(mContext,"Please enter contact no.");
                    return;
                }
                if(!isValidMobile(contactNo))
                {
                    FireToast.makeToast(mContext,"Please enter a 10 digit contact no.");
                    return;
                }



                // {"Id":0,"Name":"Vinod","Password":"password","EmailId":"surivin@gmail.com","ContactNumber":null,"Addresses":[{"Title":"Home","Address":"Address Line","HouseType":0,"Id":0,"Area":{"AreaId":1,"AreaName":"Pimple Saudagar","City":0},"Society":{"SocietyId":1,"SocietyName":"Ginger","AreaId":0}}]}

                JSONObject object = new JSONObject();
                try {

                    object.put("Name", edtName.getText().toString().trim());
                    object.put("password", edtPassword.getText().toString().trim());
                    object.put("EmailId", edtEmail.getText().toString().trim());
                    object.put("ContactNumber", edtContact.getText().toString()
                            .trim());
                    object.put("ReferralCode", edtRefCode.getText().toString());
                   /* startActivity(new Intent(this, RegisterAddressActivity.class)
                            .putExtra("customer", object.toString()));*/
                    startActivityForResult(new Intent(this, RegisterAddressActivity.class).putExtra("customer", object.toString()),Register_code);


                    //.putExtra("customer", object.toString()));

                    // finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    FireToast.makeToast(mContext, "Fail to create customer object");
                }

                break;
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("intent", resultCode + " register code " + requestCode);
        if(requestCode == Register_code){
            if(resultCode == RESULT_OK){

                setResult(RESULT_OK);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
