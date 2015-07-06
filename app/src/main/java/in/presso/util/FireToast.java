package in.presso.util;

import android.content.Context;
import android.widget.Toast;

public class FireToast {

	public static void makeToast(Context context,String message){
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	

}
