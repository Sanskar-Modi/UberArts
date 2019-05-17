package com.example.sanskarmodi.uberarts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanskarmodi.uberarts.login.GoogleLogin;
import com.example.sanskarmodi.uberarts.login.PhoneLogin;
import com.example.sanskarmodi.uberarts.util.Validate;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    LoginButton fbBtn;
    SignInButton gBtn;
    Button register, login;
    CallbackManager callbackManager;
    FirebaseApp firebaseApp;
    FirebaseAuth mAuth;

    String userName, password;
    EditText emailField, passwordField;
    TextView registerTextView, phoneTextView, anonymousTextView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = findViewById(R.id.register);
        emailField = findViewById(R.id.uname);
        passwordField = findViewById(R.id.pass);
        login = findViewById(R.id.loginBtn);
        login.setOnClickListener(this);
        progressBar = findViewById(R.id.prog);
        progressBar.setVisibility(View.GONE);
        registerTextView = findViewById(R.id.registerTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        registerTextView.setOnClickListener(this);
        phoneTextView.setOnClickListener(this);
        anonymousTextView = findViewById(R.id.anonymousTextView);
        anonymousTextView.setOnClickListener(this);
        gBtn = findViewById(R.id.googleBtn);
        gBtn.setOnClickListener(this);

        fbBtn = findViewById(R.id.fbBtn);
        fbBtn.setReadPermissions("email", "public_profile");
        fbBtn.setOnClickListener(this);
        callbackManager = CallbackManager.Factory.create();

        firebaseApp = FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void login() {
        userName = emailField.getText().toString().trim();
        password = passwordField.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        if (Validate.check(userName, password, emailField, passwordField)) {
            mAuth.signInWithEmailAndPassword(userName, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                login();
                break;
            case R.id.registerTextView:
                register();
                break;
            case R.id.phoneTextView:
                phoneLogin();
                break;
            case R.id.anonymousTextView:
                anonymousLogin();
                break;
            case R.id.googleBtn:
                googleLogin();
                break;
            case R.id.fbBtn:
                signInFb();
                break;
        }
    }

    private void anonymousLogin() {
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }
                finish();
            }
        });
    }

    private void googleLogin() {
        startActivity(new Intent(this, GoogleLogin.class));
        finish();
    }

    private void phoneLogin() {
        startActivity(new Intent(this, PhoneLogin.class));
        finish();
    }

    private void register() {
        startActivity(new Intent(this, RegisterUser.class));
        finish();
    }

    //Fb login code
    private void signInFb() {
        fbBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                Log.i(TAG, "onSuccess: " + loginResult);
                Toast.makeText(LoginActivity.this, "SignIn Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Operation Cancelled By User", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Some Error Occured Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Something went wrong while connecting to Facebook", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String email = authResult.getUser().getEmail();
                Toast.makeText(LoginActivity.this, "You are signed in with:" + email, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}

