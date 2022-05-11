package project.volunion.util;

import android.annotation.SuppressLint;
import android.content.Context;

public class PreferencesManagerInstance {
    @SuppressLint("StaticFieldLeak")
    private static PreferencesManager INSTANCE;

    private PreferencesManagerInstance() {
    }

    public static PreferencesManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PreferencesManager(context);
        }
        return INSTANCE;
    }
}
