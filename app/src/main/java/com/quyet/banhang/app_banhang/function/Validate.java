package com.quyet.banhang.app_banhang.function;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class Validate {
    public boolean checkEmail(String Email) {
        String s = "\\w+@\\w+\\.\\w+";
        return Email.matches(s);
    }

    public void resetEditText(EditText... editTexts) {
        for (int i = 0; i < editTexts.length; i++) {
            editTexts[i].setText("");
        }
    }

    public boolean checkNotEmpty(EditText... editTexts) {
        for (int i = 0; i < editTexts.length; i++) {
            if (editTexts[i].getText().toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean checkLengthPassword(String password) {
        if (password.length() >= 6) {
            return true;
        }
        return false;
    }

}
