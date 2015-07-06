package in.presso.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPrefUtils {
    public static final int LOGIN = 100;
    public static final int LOGOUT = 200;
    private SharedPreferences mSharedPreferences;
    public static String pref_name = "Presso";
    private static String key_id = "id";
    private static String key_IsRegistered = "IsRegistered";
    private static String key_login = "login";
    public static final String GCM_DEVICE_REG_ID = "registration_id";
    public static final String INSPECT_APP_VERSION = "appVersion";
    public static final String INSPECT_USER_ID = "inspectUserId";
    public static final String LOGIN_STATUS = "loginStatus";
    private Editor mEditor;

    private static String key_userName="user name";
    private static String key_userContactNumber="user contact number";

	static SharedPreferences getSharedPrefInstance(Context context) {
		return context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
	}

	public static String getKey_id(Context context) {
		return getSharedPrefInstance(context).getString(key_id, "0");
	}

	public static void setKey_id(String id, Context context) {
		getSharedPrefInstance(context).edit().putString(key_id, id).commit();
	}

    public static String getKey_IsRegistered(Context context) {
        return getSharedPrefInstance(context).getString(key_IsRegistered, "0");
    }

    public static void setKey_IsRegistered(String IsRegistered, Context context) {
        getSharedPrefInstance(context).edit().putString(key_IsRegistered, IsRegistered).commit();
    }

	public static boolean getKey_login(Context context) {
		return getSharedPrefInstance(context).getBoolean(key_login, false);
	}

	public static void setKey_login(boolean login, Context context) {
		getSharedPrefInstance(context).edit().putBoolean(key_login, login)
				.commit();
	}

    public static String getKey_userName(Context context) {
        return getSharedPrefInstance(context).getString(key_userName, "User Name");
    }

    public static void setKey_userName(String userName, Context context) {
        getSharedPrefInstance(context).edit().putString(key_userName, userName).commit();
    }

    public static String getKey_userContactNumber(Context context) {
        return getSharedPrefInstance(context).getString(key_userContactNumber, "1234567890");
    }

    public static void setKey_userContactNumber(String userContactNumber, Context context) {
        getSharedPrefInstance(context).edit().putString(key_userContactNumber, userContactNumber).commit();
    }

    public int getInspectUserLoginStatus() {
        if (mSharedPreferences != null) {
            return mSharedPreferences.getInt(LOGIN_STATUS, -1);
        } else {
            return -1;
        }
    }
    public void setInspectUserLoginStatus(int status) {
        mEditor.putInt(LOGIN_STATUS, status);
        mEditor.commit();
    }
}
