package in.presso.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import in.presso.laundryapp.R;

/**
 * Created by Android on 14-07-2015.
 */
public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    LayoutInflater layoutInflator;

    ArrayList<String> values;
    Context ctx;
    int mainSpinnerView,dropSpinnerView;

    public CustomSpinnerAdapter(Context ctx, ArrayList<String> data){
        this.ctx = ctx;
        this.values = data;
    }

    public CustomSpinnerAdapter(Context context,  ArrayList<String>  data, int mainView, int dropView) {
        this.values = data;
        ctx = context;
        mainSpinnerView =mainView;
        dropSpinnerView =dropView;
    }

    public int getCount() {
        return values.size();
    }

    public Object getItem(int i) {
        return values.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }



    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;

        view = layoutInflator.inflate(dropSpinnerView, parent, false);

        TextView txt = (TextView)view.findViewById(R.id.spID);
        txt.setPadding(12,12,12,12);
       // txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(values.get(position));
        //txt.setTextColor(parseColor("#000000"));


        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewgroup) {

        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;

        view = layoutInflator.inflate(mainSpinnerView, viewgroup, false);

        TextView txt = (TextView)view.findViewById(R.id.spID);
       txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setPadding(12, 12, 12, 12);
        txt.setText(values.get(position));
        //txt.setTextColor("#000000");

        return view;
    }
}