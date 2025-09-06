package com.synack.clipgrabber;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.util.*;
import android.net.Uri;
import android.provider.Settings;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.*;

public class MainActivity extends Activity {

    boolean isMonitoring = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestIgnoreBatteryOptimizations();
        requestAllFilesAccess();
        requestOverlayPermission();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 1);
            }
        }

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 100, 50, 100);

        Button toggleButton = new Button(this);
        toggleButton.setText("Turn ON");

        Button chooseDirButton = new Button(this);
        chooseDirButton.setText("Choose Save Directory");

        layout.addView(toggleButton);
        layout.addView(chooseDirButton);
        setContentView(layout);

        toggleButton.setOnClickListener(v -> {
            isMonitoring = !isMonitoring;
            toggleButton.setText(isMonitoring ? "Turn OFF" : "Turn ON");

            Intent clipIntent = new Intent(this, ClipboardService.class);
            Intent floatIntent = new Intent(this, FloatingOverlayService.class);

            if (isMonitoring) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(clipIntent);
                } else {
                    startService(clipIntent);
                }
                startService(floatIntent);
            } else {
                stopService(clipIntent);
                stopService(floatIntent);
            }
        });

        chooseDirButton.setOnClickListener(v -> {
            File saveFile = new File("/storage/emulated/0/Download/clipboard.txt");
            Toast.makeText(this, "Saving to: " + saveFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        });
    }

    void requestIgnoreBatteryOptimizations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            String packageName = getPackageName();
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }
    }

    void requestAllFilesAccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

    void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}