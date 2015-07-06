package in.presso.laundryapp;

import org.json.JSONException;
import org.json.JSONObject;

import com.goka.kenburnsview.KenBurnsView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import in.presso.util.APICallTaskForPost;
import in.presso.util.APIResponseListener;
import in.presso.util.FireToast;
import in.presso.util.InitTask;
import in.presso.util.SharedPrefUtils;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class SplashActivity extends Activity {
    KenBurnsView kenBurnsView ;
    private String mAppsRunningFlag;
    private Context mContext = this;
    protected int mSplashTime = 4000;
    private GoogleCloudMessaging mGcm;
    public static final Integer[] IMAGES_RESOURCE = new Integer[]{
            R.drawable.sample_3
    };
    // private String mSenderID;
    private Dialog mDialog;

    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    String SENDER_ID = "654166451265";
    private Button mYesButton, mNoButton;
    TextView mMessageTextView;
    public static final String REG_ID = "regId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        kenBurnsView = (KenBurnsView) findViewById(R.id.ken_burns_view);

        List<Integer> resourceIDs = Arrays.asList(IMAGES_RESOURCE);
        kenBurnsView.initResourceIDs(resourceIDs);


    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (isNetworkConnected(connMgr)) {


            if (SharedPrefUtils.getKey_IsRegistered(SplashActivity.this).length() == 1) {

                if (checkGoogleAccount()) {
                    if (servicesConnected()) {
                        if (!getRegistrationId(mContext).isEmpty()) {

                            // do
                        } else {
                            // do registration
                            registerInBackground();
                        }
                    } else {
                        // play service chk
                    }
                    //
                } else {
                    // google acnt chk
                    //addGoogleAccount();
                }

			/*
			 * startActivity(new Intent(SplashActivity.this,
			 * LoginActivity.class));
			 */
            }


            // Hander for catch message (gcm Reg. id) after finishing the InitTask
            InitTask.handler = new Handler() {
                @Override
                public void handleMessage(android.os.Message msg) {

                    // String temp = msg.obj.toString();
                    if (msg.obj != null) {
                        String regi_id = msg.obj.toString();
                        // send server
                        Log.v("regi_id", "" + regi_id);
                        storeRegIdinServer(mContext, regi_id);
                        storeRegistrationId(mContext, regi_id);
                        SharedPrefUtils.setKey_IsRegistered(regi_id, mContext);

                        // send gcmid to server

                    } else {
                        finish();
                    }

                }
            };

            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                    if (SharedPrefUtils.getKey_login(SplashActivity.this)) {
                       startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                }
            }, 4000);
        }
        else{
            TextView networkFailed=(TextView)findViewById(R.id.NetworkFailedError);
            networkFailed.setText("Sorry, No network connection available.");
        }
    }

    private JSONObject gcmidObject;
    private String url = "http://presso.in/service.svc/RegisterUserForPushNotification";

    private void storeRegIdinServer(Context mContext2, String regi_id) {
        // TODO Auto-generated method stub
        try {
            gcmidObject = new JSONObject();
            gcmidObject.put("DeviceRegistrationId", regi_id);
            gcmidObject.put("CustomerId", 2);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.v("gcmjson", "" + gcmidObject);

        APICallTaskForPost apiCallTask = new APICallTaskForPost(mContext, gcmlistener,null, false, gcmidObject);
        apiCallTask.execute(url);

    };

    APIResponseListener gcmlistener = new APIResponseListener() {

        @Override
        public void onSuccess(Object profile) {
            // TODO Auto-generated method stub
            finish();
            Log.v("suceess", "" + profile);
            //FireToast.makeToast(SplashActivity.this, "Stored successfully");
            //startActivity(new Intent(SplashActivity.this, LoginActivity.class));

        }

        @Override
        public void onError(String message) {
            // TODO Auto-generated method stub
            finish();
            //FireToast.makeToast(SplashActivity.this, message);
        }

    };

    private void storeRegistrationId(Context mContext2, String regi_id) {
        // TODO Auto-generated method stub
        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regi_id);

        editor.commit();
    }

    private boolean checkGoogleAccount() {
        // TODO Auto-generated method stub
        if (android.os.Build.VERSION.SDK_INT <= 15) {
            AccountManager accMan = AccountManager.get(this);
            Account[] accArray = accMan.getAccountsByType("com.google");
            return accArray.length >= 1 ? true : false;
        } else {
            return true;
        }
    }

    public boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(mContext);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("INSPECT", "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
                    (Activity) mContext, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            if (dialog != null) {
                dialog.show();
            }
            return false;
        }
    }

    private void registerInBackground() {

        InitTask initTask = new InitTask(mContext, mGcm);
        initTask.execute(SENDER_ID);

    }


    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        String registrationId = prefs.getString(
                SharedPrefUtils.GCM_DEVICE_REG_ID, "");
        Log.v("registrationId", "" + registrationId);

        if (registrationId.isEmpty()) {
            // Log.i("INSPECT", "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(
                SharedPrefUtils.INSPECT_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        Log.v("currentVersion", "" + currentVersion);
        if (currentVersion != -1) {
            if (registeredVersion != currentVersion) {

                return "";
            }
        } else {
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's version code from the PackageManager.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            return -1;
        }
    }

    public  boolean isNetworkConnected(
             ConnectivityManager connectivityManager) {

        connectivityManager = (ConnectivityManager) getSystemService(SplashActivity.this.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }



/*
        //Log.d(TAG, "Checking for Mobile Internet Network");
        final android.net.NetworkInfo mobile = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobile.isAvailable() && mobile.isConnected()) {
            val = true;
        } else {
          //  Log.e(TAG, "Mobile Internet Network not Found");
        }

        //Log.d(TAG, "Checking for WI-FI Network");
        final android.net.NetworkInfo wifi = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi.isAvailable() && wifi.isConnected()) {
             val = true;
        } else {
          //  Log.e(TAG, "WI-FI Network not Found");
        }*/


    }

}
