package in.presso.laundryapp;

import org.json.JSONException;
import org.json.JSONObject;

import in.presso.util.APICallTask;
import in.presso.util.APIResponseListener;
import in.presso.util.FireToast;
import in.presso.util.SharedPrefUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener {

    private EditText edtUsername, edtPassword;
    private static final int LOGININTENT_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtForgotPassword:

                String email = edtUsername.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    FireToast.makeToast(this, "Please enter email id");
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    FireToast.makeToast(this, "Please enter valid email id");
                    return;
                }

                APICallTask apiCallTaskForgotPass = new APICallTask(this,
                        listenerForgotPassword, null, true);
                apiCallTaskForgotPass
                        .execute("http://www.presso.in/service.svc/RecoverPassword/"
                                + email);

                break;
            case R.id.btnLogin:
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    FireToast.makeToast(this, "Please enter email id");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    FireToast.makeToast(this, "Please enter password");
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(username)
                        .matches()) {
                    FireToast.makeToast(this, "Please enter valid email id");
                    return;
                }

                APICallTask apiCallTask = new APICallTask(this, listener, null,
                        true);
                apiCallTask
                        .execute("http://www.presso.in/service.svc/ValidateUser/"
                                + username + "/" + password);

                break;
            case R.id.btnRegister:
                // startActivity(new Intent(this, RegisterActivity.class));
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class),LOGININTENT_CODE);
                break;
            default:
                break;
        }
    }

    APIResponseListener listenerForgotPassword = new APIResponseListener() {

        @Override
        public void onSuccess(Object profile) {
            JSONObject obj = (JSONObject) profile;
            try {
                FireToast.makeToast(LoginActivity.this, obj.getString("data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(String message) {
            FireToast.makeToast(LoginActivity.this, message);
        }
    };
    APIResponseListener listener = new APIResponseListener() {

        @Override
        public void onSuccess(Object profile) {
            SharedPrefUtils.setKey_login(true, LoginActivity.this);
            JSONObject object = (JSONObject) profile;
            try {
                SharedPrefUtils.setKey_id(object.getString("data"),
                        LoginActivity.this);

                APICallTask apiCallTask1 = new APICallTask(LoginActivity.this, listenerProfile, null,true);
                apiCallTask1.execute("http://www.presso.in/service.svc/GetCustomerDetails/"+ SharedPrefUtils.getKey_id(LoginActivity.this));

                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(String message) {
            FireToast.makeToast(getApplicationContext(), message);
        }
    };
    APIResponseListener listenerProfile = new APIResponseListener()
    {
        @Override
        public void onSuccess(Object profile)
        {
            JSONObject object=(JSONObject) profile;
            try
            {
                JSONObject data = object.getJSONObject("data");
                SharedPrefUtils.setKey_userName(data.optString("Name"),LoginActivity.this);
                SharedPrefUtils.setKey_userContactNumber(data.optString("ContactNumber"),LoginActivity.this);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        @Override
        public void onError(String message) {
            FireToast.makeToast(getApplicationContext(), message);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("intent",resultCode+" login code "+requestCode);
        if(requestCode == LOGININTENT_CODE){
            if(resultCode == Activity.RESULT_OK){
                Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
