package com.synack.clipgrabber;

import android.app.*;
import android.content.*;
import android.os.*;
import android.text.ClipboardManager;
import android.util.Log;
import androidx.core.app.NotificationCompat;

import java.io.*;

public class ClipboardService extends Service {

    ClipboardManager clipboard;
    String lastClip = "";
    File saveFile;

    Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();

        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        saveFile = new File("/storage/emulated/0/Download/clipboard.txt");

        startForeground(1, createNotification());
        startMonitoring();
    }

    private Notification createNotification() {
        String channelId = "clip_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                channelId, "Clipboard Monitor", NotificationManager.IMPORTANCE_LOW);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
        }

        return new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Clipboard Monitoring")
                .setContentText("Running in background")
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .build();
    }

    private void startMonitoring() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (clipboard.hasText()) {
                    String newClip = clipboard.getText().toString();
                    if (!newClip.equals(lastClip)) {
                        lastClip = newClip;
                        appendToFile(newClip);
                    }
                }
                handler.postDelayed(this, 3000);
            }
        }, 3000);
    }

    private void appendToFile(String text) {
        try {
            FileWriter writer = new FileWriter(saveFile, true);
            writer.append(text).append("\n---\n");
            writer.close();
            Log.d("CLIPLOG", "Saved: " + text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
