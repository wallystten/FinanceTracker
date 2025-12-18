package com.finance.tracker;

import android.content.Context;
import android.content.SharedPreferences;

public class PremiumManager {

    private static final String PREF_NAME = "premium_prefs";
    private static final String KEY_PREMIUM = "is_premium";

    // Verifica se o usuário é Premium
    public static boolean isPremium(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_PREMIUM, false);
    }

    // Ativa Premium (usaremos depois com pagamento)
    public static void activatePremium(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_PREMIUM, true).apply();
    }

    // Remove Premium (opcional, para testes)
    public static void deactivatePremium(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_PREMIUM, false).apply();
    }
}
