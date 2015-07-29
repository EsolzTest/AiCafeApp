package com.esolz.aicafeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.esolz.aicafeapp.Customviews.OpenSansSemiboldEditText;
import com.esolz.aicafeapp.Datatype.LoginDataType;
import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.Helper.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ltp on 08/07/15.
 */
public class ActivityLogin extends AppCompatActivity {

    OpenSansSemiboldEditText etEmail, etPassword;
    Button btnLogin;
    LinearLayout llSignup, llFBLogin;
    ProgressBar pBar;

    ConnectionDetector cd;

    String exception = "", loginURL = "";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        cd = new ConnectionDetector(ActivityLogin.this);

        sharedPreferences = getSharedPreferences("AppCredit", Context.MODE_PRIVATE);

        Log.v("LoginActivity MY", "Registration ID: " + AppData.appRegId);

        etEmail = (OpenSansSemiboldEditText) findViewById(R.id.et_email);
        etPassword = (OpenSansSemiboldEditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        llSignup = (LinearLayout) findViewById(R.id.ll_signup);
        //llFBLogin = (LinearLayout) findViewById(R.id.ll_fblogin);
        pBar = (ProgressBar) findViewById(R.id.pbar);

        etEmail.setText("soutrik@esolzmail.com");
        etPassword.setText("123456");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    if (!etEmail.getText().toString().trim().equals("")) {
                        if (isEmailValid(etEmail.getText().toString().trim())) {
                            if (!etPassword.getText().toString().trim().equals("")) {
//                                loginURL = AppData.HOST + "login.php?email=" + etEmail.getText().toString().trim() +
//                                        "&password=" + etPassword.getText().toString().trim();
                                loginURL = AppData.HOST + "login.php";
                                makeJsonObjectRequest(loginURL, etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
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

//    @Override
//    protected void onStart() {
//        super.onStart();
//        AppController.setIsAppRunning("YES");
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        AppController.setIsAppRunning("NO");
//    }

    private void makeJsonObjectRequest(final String URL, final String email, final String password) {

        pBar.setVisibility(View.VISIBLE);
        btnLogin.setClickable(false);

        Log.d("Before TAG", email + "  " + password);
        Log.d("Before TAG", URL);

        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        Log.d("Response ", stringResponse);
                        try {
                            JSONObject response = new JSONObject(stringResponse);
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
                                        "" + jsonObject.getInt("age"),
                                        jsonObject.getString("online")
                                );

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("ID", AppData.loginDataType.getId());
                                editor.putString("NAME", AppData.loginDataType.getName());
                                editor.putString("SEX", AppData.loginDataType.getSex());
                                editor.putString("EMAIL", AppData.loginDataType.getEmail());
                                editor.putString("PASSWORD", AppData.loginDataType.getPassword());
                                editor.putString("ABOUT", AppData.loginDataType.getAbout());
                                editor.putString("BUSINESS", AppData.loginDataType.getBusiness());
                                editor.putString("DOB", AppData.loginDataType.getDob());
                                editor.putString("PHOTO", AppData.loginDataType.getPhoto());
                                editor.putString("PHOYOTHUMB", AppData.loginDataType.getPhoto_thumb());
                                editor.putString("REG_DATE", AppData.loginDataType.getRegisterDate());
                                editor.putString("FACEBOOKID", AppData.loginDataType.getFacebookid());
                                editor.putString("LASTSYNC", AppData.loginDataType.getLast_sync());
                                editor.putString("FBURL", AppData.loginDataType.getFb_pic_url());
                                editor.putString("AGE", AppData.loginDataType.getAge());
                                editor.putString("ONLINE", AppData.loginDataType.getOnline());
                                editor.commit();

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
                        btnLogin.setClickable(true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Output : ", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Server not responding...!", Toast.LENGTH_LONG)
                        .show();

                btnLogin.setClickable(true);
                // pBAR.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("device_type", "1");
                params.put("device_token", AppData.appRegId);

                Log.d("After TAG", email + "  " + password);
                Log.d("device_type", "1");
                Log.d("device_token", AppData.appRegId);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(sr);

//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                URL, null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("TAG", response.toString());
//                Log.d("TAG", URL);
////                try {
////                    if (response.getString("auth").equals("login success")) {
////                        JSONObject jsonObject = response.getJSONObject("details");
////                        AppData.loginDataType = new LoginDataType(
////                                jsonObject.getString("id"),
////                                jsonObject.getString("name"),
////                                jsonObject.getString("sex"),
////                                jsonObject.getString("email"),
////                                jsonObject.getString("password"),
////                                jsonObject.getString("about"),
////                                jsonObject.getString("business"),
////                                jsonObject.getString("dob"),
////                                jsonObject.getString("photo"),
////                                jsonObject.getString("photo_thumb"),
////                                jsonObject.getString("registerDate"),
////                                jsonObject.getString("facebookid"),
////                                jsonObject.getString("last_sync"),
////                                jsonObject.getString("fb_pic_url"),
////                                "" + jsonObject.getInt("age"),
////                                jsonObject.getString("online")
////                        );
////
////                        Toast.makeText(getApplicationContext(), response.getString("auth"), Toast.LENGTH_SHORT).show();
////                        Intent intent = new Intent(ActivityLogin.this, ActivityLandingPage.class);
////                        startActivity(intent);
////                        finish();
////                    } else {
////                        Toast.makeText(getApplicationContext(), response.getString("auth"), Toast.LENGTH_SHORT).show();
////                    }
////                } catch (JSONException e) {
////                    Log.d("JSONException", e.toString());
////                    Toast.makeText(getApplicationContext(), "Server not responding...", Toast.LENGTH_SHORT).show();
////                }
////                pBar.setVisibility(View.GONE);
////                btnLogin.setClickable(true);
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("TAG", "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("email", email);
//                params.put("password", password);
//
//                Log.d("TAG", email + "  " + password);
//
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                return params;
//            }
//
//        };
//
//        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

}
