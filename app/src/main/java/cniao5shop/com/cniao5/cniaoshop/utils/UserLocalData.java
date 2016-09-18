package cniao5shop.com.cniao5.cniaoshop.utils;

import android.content.Context;
import android.text.TextUtils;

import cniao5shop.com.cniao5.cniaoshop.Constants;
import cniao5shop.com.cniao5.cniaoshop.bean.User;


public class UserLocalData {

    public static void putUser(Context context, User user) {
        String user_json = JSONUtil.toJSON(user);
        PreferencesUtils.putString(context, Constants.USER_JSON, user_json);
    }

    public static void putToken(Context context, String token) {
        PreferencesUtils.putString(context, Constants.TOKEN, token);
    }

    public static User getUser(Context context) {
        String user_json = PreferencesUtils.getString(context, Constants.USER_JSON);
        if (!TextUtils.isEmpty(user_json)) {
            return JSONUtil.fromJson(user_json, User.class);
        }
        return null;
    }

    public static String getToken(Context context) {
        return PreferencesUtils.getString(context, Constants.TOKEN);
    }

    public static void clearUser(Context context) {
        PreferencesUtils.putString(context, Constants.USER_JSON, "");
    }

    public static void clearToken(Context context) {
        PreferencesUtils.putString(context, Constants.TOKEN, "");
    }
}
