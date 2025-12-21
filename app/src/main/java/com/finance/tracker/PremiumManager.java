package com.finance.tracker;

import android.content.Context;
import android.content.SharedPreferences;

public class PremiumManager {

    private static final String PREF = "premium_pref";
    private static final String KEY_PREMIUM = "is_premium";

    public static boolean isPremium(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_PREMIUM, false);
    }

    // Usaremos no futuro (Play Store)
    public static void activatePremium(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_PREMIUM, true).apply();
    }
}
