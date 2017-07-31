package com.app.devrah.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.devrah.Network.End_Points;
import com.app.devrah.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;
    EditText etEmail, etPsw;
    TextView tvCancel;
    boolean checkBoxValue;
    SharedPreferences.Editor editor;
    CheckBox cbRememberMe;
    String strEmail, strPassword;
    Button btnLogin, btnSignUp;
    ImageView googleSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = (EditText) findViewById(R.id.input_email);
        etPsw = (EditText) findViewById(R.id.input_password);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        cbRememberMe = (CheckBox) findViewById(R.id.cbrememberMe);


        googleSignIn = (ImageView) findViewById(R.id.imgViewGSignIn);
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, TestActivity.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = etEmail.getText().toString();
                strPassword = etPsw.getText().toString();
                if (strEmail.equals("") || strPassword.equals("")) {
                    Toast.makeText(Login.this, "Fields cannot be empty ", Toast.LENGTH_SHORT).show();
                } else {
                    login();
                }
            }
        });
    }

    public void login() {

        ringProgressDialog = ProgressDialog.show(this, "Please wait ...", "Checking Credentials ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        if (response.equals("false")) {
                            new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText("Incorrect Username or Password")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();

                                        }
                                    })
                                    .show();
                        } else {

                            String userid;

                            try {

                                String firstname, email, lastName, profilePic;

                                JSONObject object = new JSONObject(response);

                                userid = object.getString("id");
                                firstname = object.getString("first_name");
                                email = object.getString("email");
                                lastName = object.getString("last_name");
                                profilePic = object.getString("profile_pic").trim();


                                SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                editor = pref.edit();
//
//                                SharedPreferences.Editor

                                editor.putString("user_id", userid);// Saving string
                                editor.putString("email", email);
                                editor.putString("first_name", firstname);
                                editor.putString("last_name", lastName);
                                editor.putString("profile_pic", profilePic);
                                editor.apply();

                                if (cbRememberMe.isChecked()) {
                                    checkBoxValue = true;

                                    editor.putString("Checkbox_value", String.valueOf(checkBoxValue));
                                    editor.apply();
                                } else {
                                    checkBoxValue = false;
                                    editor.putString("Checkbox_value", String.valueOf(checkBoxValue));
                                    editor.apply();
                                }


//                                if (checkBoxValue== true){
//
//                                    Intent intent = new Intent(Login.this,Dashboard.class);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                                else {
//
//
//                                }


                                Intent intent = new Intent(Login.this, Dashboard.class);
                                intent.putExtra("activityName", "");
                                startActivity(intent);
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("No Internet Connection")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else if (error instanceof TimeoutError) {

                    new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("Connection TimeOut! Please check your internet connection.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("user_name", strEmail);
                params.put("password", strPassword);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }
}