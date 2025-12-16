package com.finance.tracker;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationListener extends NotificationListenerService {

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d("MF", "NotificationListener conectado com sucesso");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn == null || sbn.getNotification() == null) return;

        CharSequence text = sbn.getNotification().extras.getCharSequence("android.text");
        if (text == null) return;

        String msg = text.toString().toLowerCase();

        if (msg.contains("pix") || msg.contains("r$")) {
            Log.d("MF", "Notificação financeira detectada: " + msg);
        }
    }
}
