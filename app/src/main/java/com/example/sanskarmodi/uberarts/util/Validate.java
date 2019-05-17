package com.example.sanskarmodi.uberarts.util;

import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

public class Validate {

    public static boolean check(String uname, String pass, EditText email, EditText passwordtext) {
        if (uname.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(uname).matches()) {
            email.setError("Please enter a valid email address");
            email.requestFocus();
            return false;
        }

        if (pass.isEmpty()) {
            passwordtext.setError("Password cannot be empty");
            passwordtext.requestFocus();
            return false;
        }

        if (pass.length() < 6) {
            passwordtext.setError("Password must contain more than 6 characters");
            passwordtext.requestFocus();
            return false;
        }
        return true;
    }
}
