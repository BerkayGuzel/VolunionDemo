package project.volunion.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class PreferencesManager {

    private SharedPreferences sharedPreferences;
    private Context context;

    PreferencesManager(Context context) {
        this.context = context;
        initPreferences();
    }

    private void initPreferences() {
        sharedPreferences = context.getSharedPreferences("VOLUNION",
                Context.MODE_PRIVATE);
    }

    private void setStringElement(String key, String val) {
        sharedPreferences.edit().putString(key, val).apply();
    }

    private String getStringElement(String key, String defaultVal) {
        return sharedPreferences.getString(key, defaultVal);
    }

    private boolean getBooleanElement(String key, boolean defaultVal) {
        return sharedPreferences.getBoolean(key, defaultVal);
    }

    private void setBooleanElement(String key, boolean val) {
        sharedPreferences.edit().putBoolean(key, val).apply();
    }

    private long getLongElement(String key, long defaultVal) {
        return sharedPreferences.getLong(key, defaultVal);
    }

    private void setLongElement(String key, long val) {
        sharedPreferences.edit().putLong(key, val).apply();
    }

    private void setIntElement(String key, int val) {
        sharedPreferences.edit().putInt(key, val).apply();
    }

    private int getIntElement(String key, int defaultVal) {
        return sharedPreferences.getInt(key, defaultVal);
    }

    private void setFloatElement(String key, float val) {
        sharedPreferences.edit().putFloat(key, val).apply();
    }

    private float getFloatElement(String key, float defaultVal) {
        return sharedPreferences.getFloat(key, defaultVal);
    }


    public String getGonulluEmail() {
        return getStringElement(Util.GONULLU_EMAIL, "");
    }

    public void setGonulluEmail(String val) {
        setStringElement(Util.GONULLU_EMAIL, val);
    }


    public String getKurumBilgi() {
        return getStringElement(Util.KURUM_BILGI, "");
    }

    public void setKurumBilgi(String val) {
        setStringElement(Util.KURUM_BILGI, val);
    }

}
