package com.finance.tracker;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.content.Intent;
import android.os.Bundle;

public class NotificationListener extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        if (sbn.getNotification() == null) return;

        Bundle extras = sbn.getNotification().extras;
        if (extras == null) return;

        CharSequence text = extras.getCharSequence("android.text");
        CharSequence title = extras.getCharSequence("android.title");

        if (text == null) return;

        String message = text.toString().toLowerCase();

        // Filtro simples (podemos melhorar depois)
        if (message.contains("pix") || message.contains("r$")) {

            Intent intent = new Intent("NEW_TRANSACTION");
            sendBroadcast(intent);
        }
    }
}
