package com.synack.clipgrabber;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;

public class ClipboardMonitorService extends Service {
    private static final String CH_ID = "clipgrabber_channel";
    private static final int NOTIF_ID = 101;

    private ClipboardManager cm;
    private final ClipboardManager.OnPrimaryClipChangedListener listener = () -> {
        ClipData data = cm.getPrimaryClip();
        if (data != null && data.getItemCount() > 0) {
            CharSequence text = data.getItemAt(0).coerceToText(this);
            if (!TextUtils.isEmpty(text)) {
                StorageUtil.appendToDownloads(this, "clipboard.txt", text.toString() + "\n");
            }
        }
    };

    @Override public void onCreate() {
        super.onCreate();
        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) cm.addPrimaryClipChangedListener(listener);
        startInForeground();
    }

    private void startInForeground() {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel ch = new NotificationChannel(
                    CH_ID, "ClipGrabber", NotificationManager.IMPORTANCE_LOW);
            if (nm != null) nm.createNotificationChannel(ch);
        }
        Notification notif = new Notification.Builder(this, CH_ID)
                .setContentTitle("ClipGrabber running")
                .setContentText("Saving copies to Downloads/clipboard.txt")
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setOngoing(true)
                .build();
        startForeground(NOTIF_ID, notif);
    }

    @Override public int onStartCommand(Intent i, int f, int id) { return START_STICKY; }

    @Override public void onDestroy() {
        if (cm != null && listener != null) cm.removePrimaryClipChangedListener(listener);
        super.onDestroy();
    }

    @Override public IBinder onBind(Intent intent) { return null; }
}
