package in.presso.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.presso.laundryapp.R;

/**
 * Created by mahesh on 5/20/2015.
 */
public class AppRater {

    private final static String APP_TITLE = "Presso";
    private final static String APP_PNAME = "in.presso.laundryapp";


    ///public final static String RATTING_FLAG_NO_THANKS = "NO Thanks";
    public final static String RATTING_FLAG_REMIND = "Remind me later";
    public final static String RATTING_FLAG_RATED = "Rated";
    public final static String RATTING_FLAG_KEY = "RATING_FLAG";
    private static Button ratenow_btn,later_btn;
    static TextView tv;
    public static void showRateDialog(final Context mContext,
                               final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Rate " + APP_TITLE);

        dialog.setContentView(R.layout.ratingdialog);

        ratenow_btn = (Button) dialog.findViewById(R.id.btn_ratenw);
        later_btn = (Button) dialog.findViewById(R.id.btn_ltr);

        tv = (TextView)dialog.findViewById(R.id.tv_rating);



        tv.setText("If you enjoy using " + APP_TITLE
                + ", please take a moment to rate it. Thanks for your support!");



        ratenow_btn.setText("Rate " + APP_TITLE);
        ratenow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(RATTING_FLAG_KEY, RATTING_FLAG_RATED);
                editor.commit();
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("market://details?id=" + APP_PNAME)));
                dialog.dismiss();
            }
        });
        ;


        later_btn.setText("Remind me later");
        later_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editor != null) {
                    editor.putString(RATTING_FLAG_KEY, RATTING_FLAG_REMIND);
                    editor.commit();
                }
                dialog.dismiss();
                ((Activity) mContext).finish();
            }
        });



		/*nothanx_btn.setText("No, thanks");
		nothanx_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (editor != null) {
					editor.putString(RATTING_FLAG_KEY, RATTING_FLAG_NO_THANKS);
					// editor.putBoolean("dontshowagain", true);
					editor.commit();
				}
				dialog.dismiss();
				((Activity) mContext).finish();
			}
		});*/



        dialog.show();
    }
}
