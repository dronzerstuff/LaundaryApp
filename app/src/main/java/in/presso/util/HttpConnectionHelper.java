package in.presso.util;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;

/**
 * @author Nagen Biswal
 * 
 */
public class HttpConnectionHelper {

	/**
	 * 
	 */
	public HttpConnectionHelper(Context context) {
		mContext = context;
	}

	/**
	 * isNetworkAvailable is used to Check n/w connection available in Device.
	 * 
	 * @return Boolean
	 **/
	public boolean isNetworkAvailable() {
		boolean available = false;
		/** Getting the system's connectivity service */
		ConnectivityManager connMgr = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		/** Getting active network interface to get the network's status */
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isAvailable())
			available = true;

		/** Returning the status of the network */
		return available;
	}

	public void manualTimeout() {
		new CountDownTimer(20000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// Log.v("INSPECT", "Conunter time: "+millisUntilFinished);

			}

			@Override
			public void onFinish() {
				connectionDisconnect();

			}
		}.start();
	}

	public void connectionDisconnect() {
		if (mHttpURLConnection != null) {
			mHttpURLConnection.disconnect();
			mHttpURLConnection = null;
		}
	}

	/**
	 * 
	 * @param jsonData
	 * @param url
	 * @return JSONObject
	 * 
	 * 
	 */
	@SuppressWarnings("deprecation")
	public JSONObject getConnection(String url, JSONObject jsonData) {
		try {

			String messageLog = (jsonData != null) ? jsonData.toString()
					: "No JSON is sending";

			// Log.i("INSPECT", "JSON :" + messageLog + "\n URL :" + url);

			URL urlToConnect = new URL(url);
			mHttpURLConnection = (HttpURLConnection) urlToConnect
					.openConnection();
			mHttpURLConnection.setReadTimeout(15000); /* milliseconds */
			mHttpURLConnection.setConnectTimeout(20000); /* milliseconds */

			// System.setProperty("http.maxConnections", String.valueOf(5));

			mHttpURLConnection.setRequestProperty("Content-Type",
					"application/json");
			mHttpURLConnection.setRequestProperty("Accept", "application/json");
			if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
				mHttpURLConnection.setRequestProperty("Connection", "close");
			}
			// not required now bcz we are supporting from 9
			if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
				System.setProperty("http.keepAlive", "false");

			}
			mHttpURLConnection.setRequestMethod("POST");
			// set buffer size
			String sendingData = jsonData.toString();
			final byte[] utf8Bytes = sendingData.getBytes("UTF-8");
			mHttpURLConnection.setFixedLengthStreamingMode(utf8Bytes.length);
			// Log.e("INSPECT", "utf8Bytes :"+utf8Bytes.length);
			mHttpURLConnection.setDoInput(true); // allows receiving data.
			mHttpURLConnection.setDoOutput(true); // allows sending data.
			mHttpURLConnection.connect();

			if (jsonData != null) {
				OutputStreamWriter os = new OutputStreamWriter(
						mHttpURLConnection.getOutputStream(), "UTF-8");
				os.write(jsonData.toString());
				os.flush();
				os.close();
			}
			// Log.e("INSPECT",
			// "Connection :"+mHttpURLConnection.getResponseMessage());
			sInputStream = mHttpURLConnection.getInputStream();
			if (sInputStream == null) {
				throw new IOException();
			}
			// Log.e("INSPECT",
			// "Connection :"+mHttpURLConnection.getResponseMessage());
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					sInputStream, "UTF-8"));
			// Log.v("INSPECT", "receiving" + reader.toString());
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");

			}
			sInputStream.close();
			sJson = sb.toString();
			if (sJson != null) {
				if (sJson.length() != 0) {
					try {
						sJsonObj = new JSONObject(sJson.substring(
								sJson.indexOf("{"), sJson.lastIndexOf("}") + 1));

					} catch (StringIndexOutOfBoundsException e) {
						//mExceptionCode = InspectConstants.EXCEPTION_CODE_FOR_UNSUPPORTED_ENCODING;
						connectionDisconnect();
						return null;
					}
				} else {
					throw new UnsupportedEncodingException();
				}

			} else {
				throw new UnsupportedEncodingException();
			}

		} catch (UnsupportedEncodingException e) {
			Log.i("Inspect", "UnsupportedEncodingException:" + e.getMessage());
			e.printStackTrace();
		//	mExceptionCode = InspectConstants.EXCEPTION_CODE_FOR_UNSUPPORTED_ENCODING;
			connectionDisconnect();
			return null;

		} catch (SocketTimeoutException e) {
			Log.i("Inspect", "SocketTimeoutException:" + e.getMessage());
			e.printStackTrace();
			//mExceptionCode = InspectConstants.EXCEPTION_CODE_FOR_NW_TIMEOUT;
			connectionDisconnect();
			return null;

		} catch (ClientProtocolException e) {
			Log.i("Inspect", "ClientProtocolException:" + e.getMessage());
			e.printStackTrace();
			//mExceptionCode = InspectConstants.EXCEPTION_CODE_FOR_CLIENT_PROTOCOL;
			connectionDisconnect();
			return null;
		} catch (SocketException e) {
			Log.i("Inspect", "SocketException:" + e.getMessage());
			e.printStackTrace();
			//mExceptionCode = InspectConstants.EXCEPTION_CODE_FOR_NW_TIMEOUT;
			connectionDisconnect();
			return null;
		} catch (EOFException e) {
			Log.i("Inspect", "IOException:" + e.getMessage());
			e.printStackTrace();
			//mExceptionCode = InspectConstants.EXCEPTION_CODE_FOR_EOF;
			connectionDisconnect();
			return null;
		} catch (IOException e) {

			Log.i("Inspect", "IOException:" + e.getMessage());
			e.printStackTrace();
			//mExceptionCode = InspectConstants.EXCEPTION_CODE_FOR_IO;
			connectionDisconnect();
			return null;
		} catch (JSONException e) {
			Log.i("Inspect", "JSONException:" + e.getMessage());
			e.printStackTrace();
			//mExceptionCode = InspectConstants.EXCEPTION_CODE_FOR_JSON;
			connectionDisconnect();
			return null;
		} finally {
			connectionDisconnect();
		}
		connectionDisconnect();
		return sJsonObj;
	}

	public int getExceptionCode() {

		return mExceptionCode;
	}

	private int mExceptionCode = 500;
	private Context mContext;
	private static InputStream sInputStream = null;
	private static String sJson = null;
	private static JSONObject sJsonObj = null;
	private HttpURLConnection mHttpURLConnection;
}
