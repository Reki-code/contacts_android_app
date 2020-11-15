package me.rekii.contacts.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.BaseColumns;

import me.rekii.contacts.R;

public final class User {
    private User() {
    }

    public static String getCurrUser(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.user_data_preference_file_key), Context.MODE_PRIVATE);
        return preferences.getString(context.getString(R.string.user_data_preference_file_key), "");
    }

    public static void setCurrUser(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.user_data_preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.user_data_preference_file_key), name);
        editor.apply();
    }

    public static void removeCurrUser(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.user_data_preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(context.getString(R.string.user_data_preference_file_key));
        editor.apply();
    }

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }
}
