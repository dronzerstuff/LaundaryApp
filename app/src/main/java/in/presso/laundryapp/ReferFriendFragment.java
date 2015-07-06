package in.presso.laundryapp;

import org.json.JSONObject;

import in.presso.util.APICallTask;
import in.presso.util.APIResponseListener;
import in.presso.util.FireToast;
import in.presso.util.SharedPrefUtils;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


public class ReferFriendFragment extends Fragment implements OnClickListener
{

	private static Context mContext;
	private static View mView;
	Button btn_refer_msg,btn_refer_whatsapp;
	String Referal_msg = "";
	public ReferFriendFragment() 
	{
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_refer_friend, container,false);
		mView = rootView;
		mContext = rootView.getContext();
		init();
		return rootView;
	}

	private void init() 
	{
		mView.findViewById(R.id.btnMenu).setOnClickListener(this);
		
		btn_refer_msg = (Button) mView.findViewById(R.id.btn_refer_msg);
		btn_refer_whatsapp = (Button) mView.findViewById(R.id.btn_refer_whatsapp);
		btn_refer_msg.setOnClickListener(this);
		btn_refer_whatsapp.setOnClickListener(this);
		
		APICallTask apiCallTask = new APICallTask(mContext, listener, null,true);
		apiCallTask.execute("http://www.presso.in/service.svc/GerReferralMessage/"+ SharedPrefUtils.getKey_id(mContext));
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
            case R.id.btn_refer_msg:
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("vnd.android-dir/mms-sms");
                sendIntent.putExtra("sms_body",Referal_msg);
                startActivity(sendIntent);

                break;
            case R.id.btn_refer_whatsapp:
                PackageManager pm=getActivity().getPackageManager();
                try {
                    PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    Intent sendIntent1 = new Intent();
                    sendIntent1.setAction(Intent.ACTION_SEND);
                    sendIntent1.putExtra(Intent.EXTRA_TEXT,Referal_msg);
                    sendIntent1.setPackage("com.whatsapp");
                    sendIntent1.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent1,"Share via"));
                }
                catch (NameNotFoundException e) {
                    FireToast.makeToast(mContext,"WhatsApp not Installed");
                }
                break;

        }
	}
	
	APIResponseListener listener = new APIResponseListener()
	{

		@Override
		public void onSuccess(Object profile) 
		{
			JSONObject object=(JSONObject) profile;
			Referal_msg = object.optString("data");
			System.out.println("Referal_msg = "+Referal_msg);
		}

		@Override
		public void onError(String message) {
			FireToast.makeToast(mContext, message);
		}
	};
}
