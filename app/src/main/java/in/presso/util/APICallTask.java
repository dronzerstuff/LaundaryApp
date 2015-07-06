package in.presso.util;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import in.presso.laundryapp.R;

public class APICallTask extends AsyncTask<Object, Object, JSONObject> {

	private String TAG = "APICallTask";
	private APIResponseListener mApiCallResponse;
	private ProgressDialog mDialog;
	private Context mContext;
	private List<NameValuePair> mListParameters;
	private boolean showProgressDialog = true;
	private String ERROR_FAILED;

	public APICallTask(Context context, APIResponseListener apiCallResponse,List<NameValuePair> param, boolean showProgressDialog)
	{
		this.mApiCallResponse = apiCallResponse;
		this.mContext = context;
		this.mListParameters = param;
		this.showProgressDialog = showProgressDialog;

		mDialog = new ProgressDialog(mContext,R.style.StyledDialog);
		mDialog.setCancelable(false);
		mDialog.setMessage("Loading...");
		mDialog.show();
		
		
		mDialog.setCanceledOnTouchOutside(false);
		
		if (!this.showProgressDialog)
			mDialog.cancel();

	}

	public APICallTask(Context context, String loadingMessage,
			APIResponseListener apiCallResponse, List<NameValuePair> param,
			boolean showProgressDialog) {

		this.mApiCallResponse = apiCallResponse;
		this.mContext = context;
		this.mListParameters = param;
		this.showProgressDialog = showProgressDialog;

		mDialog = new ProgressDialog(mContext);
		mDialog.setMessage("Loading...");
		mDialog.setCancelable(false);
		
		if (!this.showProgressDialog)
			mDialog.cancel();

	}

	protected void onPreExecute() {
		super.onPreExecute();
		// mDialog.show();
		mDialog.setCancelable(true);
		ERROR_FAILED = null;
	}

	@Override
	protected JSONObject doInBackground(Object... arg0) {

		try {
			String url = arg0[0].toString();
			url = url.replace(" ", "%20");
			HttpGet httpGet = new HttpGet(url);
			Log.d(TAG, "URL" + url);

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpGet);

			int responseCode = httpResponse.getStatusLine().getStatusCode();
			Log.e(TAG, "SC:" + responseCode);
			switch (responseCode) {
			case HttpStatus.SC_OK:// OK
				try 
				{
					String response = IOUtils.streamToString(httpResponse.getEntity().getContent(), null);
					Log.e(TAG, "response:" + response);
					JSONObject obj = new JSONObject(response);
					Log.d(TAG, response.toString());
					return obj;
				} catch (Exception ex) {
					ex.printStackTrace();
					return null;
				}
			case HttpStatus.SC_INTERNAL_SERVER_ERROR:
				return null;
			default:
				return null;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			ERROR_FAILED = "Network connection failed";
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			ERROR_FAILED = "Network connection failed";
		} catch (ConnectException e) {
			e.printStackTrace();
			ERROR_FAILED = "Network connection failed";
		} catch (Exception ex) {
			ex.printStackTrace();
			ERROR_FAILED = ex.getMessage();
			return null;
		}
		return null;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		try {
			if (mDialog != null && mDialog.isShowing())
				mDialog.dismiss();
			mDialog = null;

			if (result == null) {
				mApiCallResponse.onError(ERROR_FAILED);
			}
			else 
			{
				if (result.getBoolean("success")) 
				{
					mApiCallResponse.onSuccess(result);
				}
				else
				{
					mApiCallResponse.onError(result.getString("message"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			mApiCallResponse.onError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			mApiCallResponse.onError(e.getMessage());
		}
	}

}
