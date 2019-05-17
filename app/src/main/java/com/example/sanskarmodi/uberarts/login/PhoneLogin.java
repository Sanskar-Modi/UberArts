package com.example.sanskarmodi.uberarts.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanskarmodi.uberarts.util.CountryData;
import com.example.sanskarmodi.uberarts.LoginActivity;
import com.example.sanskarmodi.uberarts.R;
import com.google.firebase.auth.FirebaseAuth;

public class PhoneLogin extends AppCompatActivity {
    private Spinner spinner;
    private EditText editText;
    TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        textViewLogin = findViewById(R.id.textViewlogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(PhoneLogin.this, LoginActivity.class));
            }
        });

        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));

        editText = findViewById(R.id.editTextPhone);

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];

                String number = editText.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    editText.setError("Valid number is required");
                    editText.requestFocus();
                    return;
                }

                String phoneNumber = "+" + code + number;
                Toast.makeText(PhoneLogin.this, "" + phoneNumber, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(PhoneLogin.this, VerifyPhoneLogin.class);
                intent.putExtra("phonenumber", phoneNumber);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, VerifyPhoneLogin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(intent);
        }
    }
}


