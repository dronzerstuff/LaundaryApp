package in.presso.laundryapp;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.presso.adapter.RatelistAdapter;
import in.presso.pojo.RatelistData;
import in.presso.util.APICallTask;
import in.presso.util.APIResponseListener;
import in.presso.util.FireToast;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

public class RateListFragment extends Fragment implements OnClickListener
{
	private static Context mContext;
	private static View mView;
	private LinearLayout rate_layout;
	private ListView lstmencloths,lstwemencloth,lstkidcloth,lstothercloth;
	private ArrayList<RatelistData> dryclin_mencloths = new ArrayList<RatelistData>();
	private ArrayList<RatelistData> iron_mencloths = new ArrayList<RatelistData>();
	private ArrayList<RatelistData> wash_mencloths = new ArrayList<RatelistData>();
	private ArrayList<RatelistData> dryclin_womencloths = new ArrayList<RatelistData>();
	private ArrayList<RatelistData> iron_womencloths = new ArrayList<RatelistData>();
	private ArrayList<RatelistData> wash_womencloths = new ArrayList<RatelistData>();
	//private ArrayList<RatelistData> dryclin_kidcloths = new ArrayList<RatelistData>();
    //private ArrayList<RatelistData> iron_kidcloths = new ArrayList<RatelistData>();
    //private ArrayList<RatelistData> wash_kidcloths = new ArrayList<RatelistData>();
    //private ArrayList<RatelistData> dryclin_othercloths = new ArrayList<RatelistData>();
    //private ArrayList<RatelistData> iron_othercloths = new ArrayList<RatelistData>();
    //private ArrayList<RatelistData> wash_othercloths = new ArrayList<RatelistData>();

	private RatelistAdapter kidadapter,menadapter,womenadapter,otheradapter;
	
	public RateListFragment() 
	{
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_rate_list, container,false);
		mView = rootView;
		mContext = rootView.getContext();
		init();
		return rootView;
	}

	private void init() 
	{
		mView.findViewById(R.id.btnMenu).setOnClickListener(this);
		mView.findViewById(R.id.btn_iron).setOnClickListener(this);
		mView.findViewById(R.id.btn_washandiron).setOnClickListener(this);
		mView.findViewById(R.id.btn_dryclining).setOnClickListener(this);
		
		lstmencloths = (ListView) mView.findViewById(R.id.lstmencloths);
		lstwemencloth = (ListView) mView.findViewById(R.id.lstwemencloth);
		//lstkidcloth = (ListView) mView.findViewById(R.id.lstkidcloth);
		//lstothercloth = (ListView) mView.findViewById(R.id.lstothercloth);
		
		rate_layout = (LinearLayout) mView.findViewById(R.id.ratebtn_layout);
		
		APICallTask apiCallTask = new APICallTask(mContext, listener, null,true);
		apiCallTask.execute("http://www.presso.in/service.svc/GetRateListNew");

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
			case R.id.btn_iron:
				rate_layout.setBackgroundResource(R.drawable.ironing_onn);

                //kidadapter = new RatelistAdapter(mContext, iron_kidcloths);
                //lstkidcloth.setAdapter(kidadapter);

				menadapter = new RatelistAdapter(mContext, iron_mencloths);
				lstmencloths.setAdapter(menadapter);
				
				womenadapter = new RatelistAdapter(mContext, iron_womencloths);
				lstwemencloth.setAdapter(womenadapter);

                //otheradapter = new RatelistAdapter(mContext, iron_othercloths);
                //lstothercloth.setAdapter(otheradapter);
				break;
			case R.id.btn_washandiron:
				rate_layout.setBackgroundResource(R.drawable.wash_onn);

                //kidadapter = new RatelistAdapter(mContext, wash_kidcloths);
                //lstkidcloth.setAdapter(kidadapter);

				menadapter = new RatelistAdapter(mContext, wash_mencloths);
				lstmencloths.setAdapter(menadapter);
				
				womenadapter = new RatelistAdapter(mContext, wash_womencloths);
				lstwemencloth.setAdapter(womenadapter);

                //otheradapter = new RatelistAdapter(mContext, wash_othercloths);
                //lstothercloth.setAdapter(otheradapter);
				break;
			case R.id.btn_dryclining:
				rate_layout.setBackgroundResource(R.drawable.dry_onn);

                //kidadapter = new RatelistAdapter(mContext, dryclin_kidcloths);
                //lstkidcloth.setAdapter(kidadapter);

				menadapter = new RatelistAdapter(mContext, dryclin_mencloths);
				lstmencloths.setAdapter(menadapter);
				
				womenadapter = new RatelistAdapter(mContext, dryclin_womencloths);
				lstwemencloth.setAdapter(womenadapter);

                //otheradapter = new RatelistAdapter(mContext, dryclin_othercloths);
                //lstothercloth.setAdapter(otheradapter);
				break;
		}
	}

	APIResponseListener listener = new APIResponseListener()
	{

		@Override
		public void onSuccess(Object profile) 
		{
			JSONObject object=(JSONObject) profile;
			try 
			{
				String data = object.getString("data");
				
				String DryCleaning = new JSONObject(data).getString("DryCleaning");
				String Ironing = new JSONObject(data).getString("Ironing");
				String WashAndIron = new JSONObject(data).getString("WashAndIron");

                //JSONArray dryclin_Kids = new JSONObject(DryCleaning).getJSONArray("Kids");
                //parse_array(dryclin_Kids,dryclin_kidcloths);
                //JSONArray iron_Kids = new JSONObject(Ironing).getJSONArray("Kids");
                //parse_array(iron_Kids,iron_kidcloths);
                //JSONArray wash_Kids = new JSONObject(WashAndIron).getJSONArray("Kids");
                //parse_array(wash_Kids,wash_kidcloths);

                //JSONArray dryclin_other = new JSONObject(DryCleaning).getJSONArray("Others");
                //parse_array(dryclin_other,dryclin_othercloths);
                //JSONArray iron_other = new JSONObject(Ironing).getJSONArray("Others");
                //parse_array(iron_other,iron_othercloths);
                //JSONArray wash_other = new JSONObject(WashAndIron).getJSONArray("Others");
                //parse_array(wash_other,wash_othercloths);
				
				JSONArray dryclin_men = new JSONObject(DryCleaning).getJSONArray("Normal");
				parse_array(dryclin_men,dryclin_mencloths);
				JSONArray iron_men = new JSONObject(Ironing).getJSONArray("Normal");
				parse_array(iron_men,iron_mencloths);
				JSONArray wash_men = new JSONObject(WashAndIron).getJSONArray("Normal");
				parse_array(wash_men,wash_mencloths);
				
				JSONArray dryclin_women = new JSONObject(DryCleaning).getJSONArray("HouseHold");
				parse_array(dryclin_women,dryclin_womencloths);
				JSONArray iron_women = new JSONObject(Ironing).getJSONArray("HouseHold");
				parse_array(iron_women,iron_womencloths);
				JSONArray wash_women = new JSONObject(WashAndIron).getJSONArray("HouseHold");
				parse_array(wash_women,wash_womencloths);

                //kidadapter = new RatelistAdapter(mContext, iron_kidcloths);
                //lstkidcloth.setAdapter(kidadapter);

				menadapter = new RatelistAdapter(mContext, iron_mencloths);
				lstmencloths.setAdapter(menadapter);
				
				womenadapter = new RatelistAdapter(mContext, iron_womencloths);
				lstwemencloth.setAdapter(womenadapter);

                //otheradapter = new RatelistAdapter(mContext, iron_othercloths);
                //lstothercloth.setAdapter(otheradapter);
				
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
	
	public void parse_array(JSONArray array,ArrayList<RatelistData> list)
	{
		for(int i=0;i<array.length();i++)
		{
			try 
			{
				JSONObject obj = array.getJSONObject(i);
				RatelistData lst = new RatelistData();
				lst.setTitle(obj.optString("Key"));
				lst.setAmount("Rs "+obj.optString("Value"));
				list.add(lst);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
