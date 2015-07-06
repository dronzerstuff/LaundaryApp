package in.presso.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class IOUtils {
	public static String streamToString(InputStream is, Charset cs)
			throws IOException, Exception {
		BufferedReader br;
		if (cs == null) {
			br = new BufferedReader(new InputStreamReader(is));
		} else {
			br = new BufferedReader(new InputStreamReader(is, cs));
		}
		StringBuffer sb = new StringBuffer();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}
		return sb.toString();
	}

	public static int getDesityOfScreen(Context context) {

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int densityDpi = (int) (metrics.density * 160f);

		return densityDpi;
	}

	/**
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap getRoundedRectBitmap(Context context, Bitmap bmp,
			int radius) {

		DisplayMetrics metrics = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(metrics);

		radius = (int) (radius * metrics.scaledDensity) - 30;
//		 switch (metrics.densityDpi) {
//		 case DisplayMetrics.DENSITY_LOW:
//		 break;
//		 case DisplayMetrics.DENSITY_MEDIUM:
//		 radius+=10;
//		 break;
//		 case DisplayMetrics.DENSITY_HIGH:
//		 radius+=20;
//		 break;
//		 }

		Bitmap sbmp;
		if (bmp.getWidth() != radius || bmp.getHeight() != radius)
			sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
		else
			sbmp = bmp;
		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xffa19774;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#BAB399"));
		canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f,
				sbmp.getHeight() / 2 + 0.7f, sbmp.getWidth() / 2 + 0.1f, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);

		return output;
	}

	/**
	 * 
	 * @param context
	 * @param contentUri
	 * @return
	 */
	public static String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		Log.d("test", "Image uri:" + contentUri);

		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();

			String path = null;
			path = cursor.getString(column_index);

			if (path == null) {
				// 2:OI FILE Manager --- call method: uri.getPath()
				Log.d("test",
						"Image uri" + contentUri + " path:"
								+ contentUri.getPath());
				return path = contentUri.getPath();
			}

			Log.d("test", "Image uri" + contentUri + " path:" + path);

			return path;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * 
	 * @param context
	 * @param LATITUDE
	 * @param LONGITUDE
	 * @return
	 */
	public static String getCityName(Context context, double LATITUDE,
			double LONGITUDE) {

		Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
		String city = "";
		try {
			List<Address> address = geoCoder.getFromLocation(LATITUDE,
					LONGITUDE, 1);
			if (address.size() > 0) {
				city = address.get(0).getAddressLine(0);
			}
			return city;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * @param context
	 * @param url
	 * @param sampleSize
	 * @return
	 */
	public static Bitmap getSampleBitmap(Context context, String url,
			int sampleSize) {
		Options options = new Options();
		options.inSampleSize = sampleSize;

		Bitmap bitmap = BitmapFactory.decodeFile(url, options);

		return bitmap;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}


	public static boolean isTablet(Context context) {
	    return (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
	public static Bitmap combineImages(Bitmap c, Bitmap s) { // can add a 3rd
		// parameter
		// 'String loc'
		// if you want
		// to save the
		// new image -
		// left some
		// code to do
		// that at the
		// bottom
		Bitmap cs = null;

		int width, height = 0;

		if (c.getWidth() > s.getWidth()) {
			width = c.getWidth();
			height = c.getHeight() + s.getHeight();
		} else {
			width = s.getWidth();
			height = c.getHeight() + s.getHeight();
		}

		cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		Canvas comboImage = new Canvas(cs);

		comboImage.drawBitmap(c, 0f, 0f, null);
		comboImage.drawBitmap(s, 5f, 5f, null);

		// this is an extra bit I added, just incase you want to save the new
		// image somewhere and then return the location
		/*
		 * String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";
		 * 
		 * OutputStream os = null; try { os = new FileOutputStream(loc +
		 * tmpImg); cs.compress(CompressFormat.PNG, 100, os); }
		 * catch(IOException e) { Log.e("combineImages",
		 * "problem combining images", e); }
		 */
		c = null;
		s = null;

		return cs;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
}
