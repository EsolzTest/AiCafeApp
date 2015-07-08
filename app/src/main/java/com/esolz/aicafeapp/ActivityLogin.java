package com.esolz.aicafeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.esolz.aicafeapp.Helper.ConnectionDetector;

/**
 * Created by ltp on 08/07/15.
 */
public class ActivityLogin extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    LinearLayout llSignup, llFBLogin;

    ConnectionDetector cd;

    String loginURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        cd = new ConnectionDetector(ActivityLogin.this);

        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        llSignup = (LinearLayout) findViewById(R.id.ll_signup);
        llFBLogin = (LinearLayout) findViewById(R.id.ll_fblogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    loginURL = etEmail.getText().toString() + etPassword.getText().toString();
                } else {
                    Toast.makeText(ActivityLogin.this, "No internet connection. Please try again.", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(ActivityLogin.this, ActivityLandingPage.class);
                startActivity(intent);
                finish();
            }
        });

        llSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        llFBLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
