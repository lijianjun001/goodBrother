package com.antelope.goodbrother.utils;

import android.content.Context;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class InputMethodUtil {
    public static void showInput(EditText editText) {
        new Handler().postDelayed(() -> {
            InputMethodManager inputManager =
                    (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, 0);
        }, 1000);
    }
}
