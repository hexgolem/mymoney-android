package com.saberix.mymoney;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveSharedPreferences {

    static SharedPreferences getSharedPreferences(Context ctx) {
        return ctx.getSharedPreferences("loggedInUser",Context.MODE_PRIVATE);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString("username",userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString("username","");
    }

    public static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
