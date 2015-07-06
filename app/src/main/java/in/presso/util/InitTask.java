package in.presso.util;

import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;

import com.google.android.gms.gcm.GoogleCloudMessaging;
//import android.util.Log;


/**
 * @author Nagen Biswal
 * 
 *         This class incorporates about GCM Registration ,
 * 
 */
public class InitTask extends AsyncTask<String, Void, String> {

	public InitTask(Context context, GoogleCloudMessaging gcm) {
		mContext = context;
		mGcm = gcm;
		mConnectionHelper = new HttpConnectionHelper(mContext);
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		if (mConnectionHelper.isNetworkAvailable()) {
			try {
				if (mGcm == null) {
					mGcm = GoogleCloudMessaging.getInstance(mContext);
				}

				mRegistration_id = mGcm.register(params[0]);

				return mRegistration_id;
				
			} catch (IOException e) {
				e.printStackTrace();
				if (mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}

				return null;
			}
		} else {
			// showDialogForNetworkError();
			if (mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
			return null;

		}

	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		// Log.i("INSPECT", result);
		Message message = new Message();
		mProgressDialog.dismiss();
		message.obj = result;
		handler.sendMessage(message);

		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.getWindow().setGravity(Gravity.TOP);
		mProgressDialog.setMessage("Loading...");
		//mProgressDialog.show();

		super.onPreExecute();
	}

	private ProgressDialog mProgressDialog;
	private Context mContext;
	public static Handler handler;
	private GoogleCloudMessaging mGcm;
	private String mRegistration_id;
	private HttpConnectionHelper mConnectionHelper;

}
