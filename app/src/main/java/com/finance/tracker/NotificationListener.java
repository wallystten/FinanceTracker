package com.finance.tracker;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class NotificationListener extends NotificationListenerService {

    private static final String TAG = "FinanceTracker";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        if (sbn == null || sbn.getNotification() == null) return;

        Bundle extras = sbn.getNotification().extras;
        if (extras == null) return;

        CharSequence text = extras.getCharSequence("android.text");
        CharSequence title = extras.getCharSequence("android.title");

        if (text == null) return;

        String message = text.toString().toLowerCase();

        // LOG para teste (confirma se o serviço está funcionando)
        Log.d(TAG, "Notificação capturada: " + message);

        // Filtro simples (vamos melhorar depois)
        if (message.contains("pix") || message.contains("r$")) {

            Log.d(TAG, "Possível transação detectada");

            Intent intent = new Intent("NEW_TRANSACTION");
            sendBroadcast(intent);
        }
    }
}
