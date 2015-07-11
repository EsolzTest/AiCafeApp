package com.esolz.aicafeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.esolz.aicafeapp.Datatype.LoginDataType;
import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.Helper.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ltp on 08/07/15.
 */
public class ActivityLogin extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    LinearLayout llSignup, llFBLogin;
    ProgressBar pBar;

    ConnectionDetector cd;

    String exception = "", loginURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        cd = new ConnectionDetector(ActivityLogin.this);

        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        llSignup = (LinearLayout) findViewById(R.id.ll_signup);
        //llFBLogin = (LinearLayout) findViewById(R.id.ll_fblogin);
        pBar = (ProgressBar) findViewById(R.id.pbar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    if (!etEmail.getText().toString().trim().equals("")) {
                        if (isEmailValid(etEmail.getText().toString().trim())) {
                            if (!etPassword.getText().toString().trim().equals("")) {
                                loginURL = AppData.HOST + "login.php?email=" + etEmail.getText().toString().trim() +
                                        "&password=" + etPassword.getText().toString().trim();
                                makeJsonObjectRequest(loginURL);
                            } else {
                                etPassword.requestFocus();
                                etPassword.setError("Please enter your password.");
                            }
                        } else {
                            etEmail.requestFocus();
                            etEmail.setError("Please enter valid email id.");
                        }
                    } else {
                        etEmail.requestFocus();
                        etEmail.setError("Please enter your email id.");
                    }
                } else {
                    Toast.makeText(ActivityLogin.this, "No internet connection. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        llSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivitySignUp.class);
                startActivity(intent);
            }
        });

//        llFBLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void makeJsonObjectRequest(String URL) {

        pBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                try {
                    if (response.getString("auth").equals("login success")) {
                        JSONObject jsonObject = response.getJSONObject("details");
                        AppData.loginDataType = new LoginDataType(
                                jsonObject.getString("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("sex"),
                                jsonObject.getString("email"),
                                jsonObject.getString("password"),
                                jsonObject.getString("about"),
                                jsonObject.getString("business"),
                                jsonObject.getString("dob"),
                                jsonObject.getString("photo"),
                                jsonObject.getString("photo_thumb"),
                                jsonObject.getString("registerDate"),
                                jsonObject.getString("facebookid"),
                                jsonObject.getString("last_sync"),
                                jsonObject.getString("fb_pic_url"),
                                jsonObject.getString("age"),
                                jsonObject.getString("online")
                        );

                        Toast.makeText(getApplicationContext(), response.getString("auth"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityLogin.this, ActivityLandingPage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), response.getString("auth"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("JSONException", e.toString());
                    Toast.makeText(getApplicationContext(), "Server not responding...", Toast.LENGTH_SHORT).show();
                }
                pBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

}
