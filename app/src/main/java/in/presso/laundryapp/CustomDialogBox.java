package in.presso.laundryapp;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by Android on 23-07-2015.
 */public class CustomDialogBox extends Dialog {


    TextView txtRate,txtItemName;
    public Activity act;
    String msg;
    public CustomDialogBox(Activity context,String value,int theme) {
        super(context, theme);
        this.act = context;
        this.msg = value;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        txtItemName= (TextView)findViewById(R.id.txtItemName);

        txtItemName.setText(msg);


        txtRate = (TextView)findViewById(R.id.txtRate);

        txtRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    //end of main class
}