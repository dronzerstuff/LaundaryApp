package in.presso.util;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import in.presso.laundryapp.CircleDialog;
import in.presso.laundryapp.R;

public class APICallTaskForPost extends AsyncTask<String, Object, JSONObject> {

	private String TAG = "APICallTask";
	private APIResponseListener mApiCallResponse;
	private CircleDialog mDialog;
	private Context mContext;
	private List<NameValuePair> mListParameters;
	private boolean showProgressDialog = true;
	private String ERROR_FAILED;
	private JSONObject objectData;

	public APICallTaskForPost(Context context,
			APIResponseListener apiCallResponse, List<NameValuePair> param,
			boolean showProgressDialog, JSONObject object) {

		this.mApiCallResponse = apiCallResponse;
		this.mContext = context;
		this.mListParameters = param;
		this.showProgressDialog = showProgressDialog;
		this.objectData = object;

		mDialog = new CircleDialog(mContext,0);
		mDialog.setCancelable(false);
		mDialog.show();


		mDialog.show();

		mDialog.setCanceledOnTouchOutside(false);

		if (!this.showProgressDialog)
			mDialog.cancel();

	}

	public APICallTaskForPost(Context context,
			APIResponseListener apiCallResponse, List<NameValuePair> param,
			boolean showProgressDialog) {

		this.mApiCallResponse = apiCallResponse;
		this.mContext = context;
		this.mListParameters = param;
		this.showProgressDialog = showProgressDialog;

		mDialog = new CircleDialog(mContext,0);
		mDialog.setCancelable(false);
		mDialog.show();

		mDialog.setCanceledOnTouchOutside(false);

		if (!this.showProgressDialog)
			mDialog.cancel();

	}

	public APICallTaskForPost(Context context, String loadingMessage,
			APIResponseListener apiCallResponse, List<NameValuePair> param,
			boolean showProgressDialog) {

		this.mApiCallResponse = apiCallResponse;
		this.mContext = context;
		this.mListParameters = param;
		this.showProgressDialog = showProgressDialog;

		mDialog = new CircleDialog(mContext,0);
		mDialog.setCancelable(false);
		mDialog.show();

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
	protected JSONObject doInBackground(String... urlPassed) {

		try {
			String url = urlPassed[0];
			url = url.replace(" ", "%20");
			HttpPost httpPost = new HttpPost(url);

			if (objectData != null) {
				// StringEntity se = new StringEntity(objectData.toString());
				// se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
				// "application/json"));
				httpPost.setEntity(new ByteArrayEntity(objectData.toString()
						.getBytes("UTF8")));

				// List<NameValuePair> nameValuePairs = new
				// ArrayList<NameValuePair>(
				// 2);
				// nameValuePairs.add(new BasicNameValuePair("request",
				// objectData
				// .toString()));
				// httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				Log.d(TAG, "Input object:" + objectData.toString());
			}

			Log.d(TAG, "URL" + url);

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);

			if (httpResponse != null) {

				String _response = EntityUtils.toString(httpResponse
						.getEntity()); // content will be consume only once
				Log.e(TAG, "responce:" + _response);
				final JSONObject jObject = new JSONObject(_response);
				return jObject;
			}

			int responseCode = httpResponse.getStatusLine().getStatusCode();
			Log.e(TAG, "SC:" + responseCode);
			switch (responseCode) {
			case HttpStatus.SC_OK:// OK
				try {
					String response = IOUtils.streamToString(httpResponse
							.getEntity().getContent(), null);

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

			} else {

				if (result.getBoolean("success")) {
					mApiCallResponse.onSuccess(result);
				} else {
					mApiCallResponse.onError(result.getString("message"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mApiCallResponse.onError(e.getMessage());
		}
	}

}
