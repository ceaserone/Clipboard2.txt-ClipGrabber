package com.synack.clipgrabber;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ClipKeyboardService extends InputMethodService {

    @Override
    public View onCreateInputView() {
        View root = getLayoutInflater().inflate(R.layout.keyboard_view, null);

        View.OnClickListener key = v -> {
            if (!(v instanceof Button)) return;
            String t = ((Button) v).getText().toString();
            int id = v.getId();
            if (id == R.id.keyBackspace) {
                getCurrentInputConnection().deleteSurroundingText(1, 0);
            } else if (id == R.id.keyEnter) {
                getCurrentInputConnection().commitText("\n", 1);
            } else if (id == R.id.keySpace) {
                getCurrentInputConnection().commitText(" ", 1);
            } else if (id == R.id.keyComma) {
                getCurrentInputConnection().commitText(",", 1);
            } else if (id == R.id.keyShift) {
                // TODO: caps/shift toggle
            } else {
                getCurrentInputConnection().commitText(t, 1);
            }
        };

        attachListenersRecursively(root, key);
        return root;
    }

    private void attachListenersRecursively(View v, View.OnClickListener key) {
        if (v instanceof Button) ((Button) v).setOnClickListener(key);
        if (v instanceof ViewGroup) {
            ViewGroup g = (ViewGroup) v;
            for (int i = 0; i < g.getChildCount(); i++) {
                attachListenersRecursively(g.getChildAt(i), key);
            }
        }
    }
}
