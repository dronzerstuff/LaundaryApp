package in.presso.laundryapp;

import in.presso.adapter.DrawerListAdapter;
import in.presso.pojo.OrderData;
import in.presso.util.APICallTask;
import in.presso.util.APIResponseListener;
import in.presso.util.AppRater;
import in.presso.util.FireToast;
import in.presso.util.SharedPrefUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends Activity {
    private static Context mContext;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    RelativeLayout mDrawerPane;
    static TextView userName, contactNumber, pressoMoney;

    private CharSequence mDrawerTitle;

    private CharSequence mTitle;

    private String[] mDrawerItemList;
    // private TypedArray navMenuIcons;
    private DrawerListAdapter adapter;
    private Fragment currentFragment;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        init(savedInstanceState);

    }

    @SuppressLint("NewApi")
    private void init(Bundle savedInstanceState) {

        mTitle = mDrawerTitle = getTitle();
        mDrawerItemList = getResources().getStringArray(R.array.drawerItemList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);

        userName=(TextView)findViewById(R.id.userName);
        contactNumber=(TextView)findViewById(R.id.contactNumber);

        pressoMoney=(TextView)findViewById(R.id.pressoMoney);
        updateProfile(HomeActivity.this);

        adapter = new DrawerListAdapter(mContext, mDrawerItemList);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(drawerItemClickListener);

        // getActionBar().setDisplayHomeAsUpEnabled(true);
        // getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.menu_icon_old, // nav menu toggle icon
                R.string.app_name, // nav drawer open - description for
                // accessibility
                R.string.app_name // nav drawer close - description for
                // accessibility
        ) {
            public void onDrawerClosed(View view) {
                // getActionBar().setTitle(mTitle);
                // FireToast.makeToast(mContext, "Drawer Closed");
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                // getActionBar().setTitle(mDrawerTitle);
                // FireToast.makeToast(mContext, "Drawer Opened");
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }

    public void updateProfile(Context mContext){
        APICallTask apiCallTask1 = new APICallTask(mContext, listenerProfile, null,false);
        apiCallTask1.execute("http://www.presso.in/service.svc/GetCustomerDetails/"+ SharedPrefUtils.getKey_id(mContext));
    }

    APIResponseListener listenerProfile = new APIResponseListener()
    {
        @Override
        public void onSuccess(Object profile)
        {
            JSONObject object=(JSONObject) profile;
            try
            {
                JSONObject data = object.getJSONObject("data");
                pressoMoney.setText(data.optString("PressoMoney"));
                userName.setText(data.optString("Name"));
                contactNumber.setText(" ("+data.optString("ContactNumber")+")");

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        @Override
        public void onError(String message) {
//            FireToast.makeToast(getApplicationContext(), message);
        }
    };

    OnItemClickListener drawerItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            displayView(arg2);

        }

    };

    public void openDrawer() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    public void updateOrder(OrderData order) {

        Fragment fragment = new UpdateorderFragment(order);

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            currentFragment = fragment;
        }

    }

    public void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new MyOrdersFragment();
                break;
            case 2:
                fragment = new MyProfileFragment();
                break;
            case 3:
                fragment = new RateListFragment();
                break;
            case 4:
                fragment = new ReferFriendFragment();
                break;
            case 5:
                fragment = new HelpFragment();
                break;
            case 6:
                fragment = new NewOrderFragment();
                break;
            case 7:
                fragment = new UpdateorderFragment();
                break;
            case 8:
                fragment = new ChangepwdFragment();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            currentFragment = fragment;

            // update selected item and title, then close the drawer
            if (position < 6) {
                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
                setTitle(mDrawerItemList[position]);
                mDrawerLayout.closeDrawer(mDrawerPane);
            }
        } else {
            // error in creating fragment
            //Log.e("MainActivity", "Error in creating fragment");
        }
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        //boolean drawerOpen = mDrawerLayout.isDrawerVisible(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {


        if (currentFragment.getClass() == HomeFragment.class) {
            count = count + 1;
            SharedPreferences prefs = mContext.getSharedPreferences("apprater", mContext.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            String ratingFlag = prefs.getString(AppRater.RATTING_FLAG_KEY,"");


            if((!ratingFlag.isEmpty())&& (ratingFlag.equals(AppRater.RATTING_FLAG_RATED)) || count == 2 ){
                // skip dialog
                finish();
            }else {
                // display dialog
                AppRater.showRateDialog(mContext, editor);
            }

        }else if(currentFragment.getClass()==UpdateorderFragment.class){
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container,
                            currentFragment = new MyOrdersFragment()).commit();
            //this.getFragmentManager().popBackStack();
        }
        else {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container,
                            currentFragment = new HomeFragment()).commit();
            mDrawerList.setItemChecked(0, true);

            mDrawerList.setSelection(0);
            setTitle(mDrawerItemList[0]);
            mDrawerLayout.closeDrawer(mDrawerPane);
        }

        // AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setMessage("Are you sure you want to exit?")
        // .setPositiveButton("Home", new DialogInterface.OnClickListener() {
        // public void onClick(DialogInterface dialog, int id) {
        // Fragment fragment = new HomeFragment();
        // if (fragment != null)
        // {
        // FragmentManager fragmentManager = getFragmentManager();
        // fragmentManager.beginTransaction().replace(R.id.frame_container,
        // fragment).commit();
        // mDrawerList.setItemChecked(0, true);
        // mDrawerList.setSelection(0);
        // setTitle(mDrawerItemList[0]);
        // mDrawerLayout.closeDrawer(mDrawerList);
        // }
        // }
        // })
        // .setNegativeButton("exit", new DialogInterface.OnClickListener() {
        // public void onClick(DialogInterface dialog, int id) {
        // dialog.cancel();
        // finish();
        // }
        // });
        // AlertDialog alert = builder.create();
        // alert.show();
        return;
    }

    public static void show_dialog(Context cxt, String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(cxt);
        alert.setMessage(msg);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}
