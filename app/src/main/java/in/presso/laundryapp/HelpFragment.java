package in.presso.laundryapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HelpFragment extends Fragment implements OnClickListener
{

	private static Context mContext;
	private static View mView;
	Button btn_call_us,btn_raise_issue;
	
	public HelpFragment() 
	{
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		View rootView = inflater.inflate(R.layout.fragment_help, container,false);
		mView = rootView;
		mContext = rootView.getContext();
		init();
		return rootView;
	}

	private void init() 
	{
		mView.findViewById(R.id.btnMenu).setOnClickListener(this);
        TextView definition=(TextView) mView.findViewById(R.id.definition);
		btn_call_us = (Button) mView.findViewById(R.id.btn_call_us);
		btn_raise_issue  = (Button) mView.findViewById(R.id.btn_raise_issue);
		
		btn_call_us.setOnClickListener(this);
		btn_raise_issue.setOnClickListener(this);
        definition.setText(Html.fromHtml("<b>Normal Delivery:</b> Order is picked in a 2 hour time slot starting 1 hour from order placement. Order is delivered on or after 24 hours from pickup time."
                + "<br/><br/><b>Express Delivery:</b> Order is picked within 1 hour from order placed and delivered within 2 hours from pickup time."));

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
			case R.id.btn_call_us:
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:+91 9130024656"));
				startActivity(callIntent);
				break;
			case R.id.btn_raise_issue:
				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@presso.in"});		  
				email.putExtra(Intent.EXTRA_SUBJECT, "Raise issue.");
				email.setType("message/rfc822");
				startActivity(Intent.createChooser(email, "Choose an Email client :"));
				break;
		}
	}
}
