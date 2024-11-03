package com.example.tasktracker;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager
{

    private static final String PREFERENCES_NAME = "MyAppPreferences";
    private static SharedPreferencesManager instance;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context.getApplicationContext());
        }
        return instance;
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    // Add methods for other data types as needed (boolean, long, float)

    // Example of removing a preference
    public void removePreference(String key) {
        editor.remove(key);
        editor.apply();
    }

    // Clear all preferences
    public void clearPreferences() {
        editor.clear();
        editor.apply();
    }
}
