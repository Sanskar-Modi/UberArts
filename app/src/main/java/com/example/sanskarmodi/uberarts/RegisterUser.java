package com.example.sanskarmodi.uberarts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanskarmodi.uberarts.util.Validate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    // LoginButton fbBtn;
    private FirebaseAuth mAuth;
    private static final String TAG = "";
    Button register;
    String userName, password;
    EditText emailField, passwordField;
    FirebaseApp firebaseApp;
    TextView textView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(this);
        register = findViewById(R.id.register);
        emailField = findViewById(R.id.uname);
        passwordField = findViewById(R.id.pass);
        firebaseApp = FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        register.setOnClickListener(this);
        textView1 = findViewById(R.id.textViewlogin);
        textView1.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void register() {
        userName = emailField.getText().toString().trim();
        password = passwordField.getText().toString().trim();
        if(Validate.check(userName, password, emailField, passwordField)) {
            mAuth.createUserWithEmailAndPassword(userName, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        finish();
                        startActivity(new Intent(RegisterUser.this, HomeActivity.class));
                        Toast.makeText(getApplicationContext(), "User registered succesfull", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                    } else {
                        Log.w(TAG, "failure", task.getException());
                        Toast.makeText(getApplicationContext(), "User registeration failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                register();
                break;
            case R.id.textViewlogin:
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}
