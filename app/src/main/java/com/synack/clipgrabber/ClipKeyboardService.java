package com.synack.clipgrabber;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ClipKeyboardService extends InputMethodService {

    @Override
    public View onCreateInputView() {
        View root = getLayoutInflater().inflate(R.layout.keyboard_view, null);

        View qwertyLayout = root.findViewById(R.id.qwerty_layout);
        View symbolsLayout = root.findViewById(R.id.symbols_layout);

        View.OnClickListener key = v -> {
            if (!(v instanceof Button)) return;
            String t = ((Button) v).getText().toString();
            int id = v.getId();
            if (id == R.id.keyBackspace || id == R.id.keyBackspace_symbols) {
                getCurrentInputConnection().deleteSurroundingText(1, 0);
            } else if (id == R.id.keyEnter || id == R.id.keyEnter_symbols) {
                getCurrentInputConnection().commitText("\n", 1);
            } else if (id == R.id.keySpace || id == R.id.keySpace_symbols) {
                getCurrentInputConnection().commitText(" ", 1);
            } else if (id == R.id.key_symbols || id == R.id.key_qwerty) {
                if (qwertyLayout.getVisibility() == View.VISIBLE) {
                    qwertyLayout.setVisibility(View.GONE);
                    symbolsLayout.setVisibility(View.VISIBLE);
                } else {
                    qwertyLayout.setVisibility(View.VISIBLE);
                    symbolsLayout.setVisibility(View.GONE);
                }
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
