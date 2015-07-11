package com.esolz.aicafeapp;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.esolz.aicafeapp.Helper.ConnectionDetector;

/**
 * Created by ltp on 09/07/15.
 */
public class ActivitySignUp extends AppCompatActivity {

    LinearLayout llCancel;
    ImageView imgProfile, imgSet;
    EditText etName, etSex, etEmail, etPass, etPassConform, etAbout, etCurrentBusiness;
    Button btnSignUp;

    ConnectionDetector cd;

    Dialog dialogChooser;
    LinearLayout llGallery, llCamera, llCancelDialog;

    // --- For Camera and Gallery ---
    String Current_PATH = "", imgTYPE = "";
    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final int ACTION_TAKE_GALLERY = 2;
    // --- End ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        cd = new ConnectionDetector(ActivitySignUp.this);

        llCancel = (LinearLayout) findViewById(R.id.ll_cancel);
        imgProfile = (ImageView) findViewById(R.id.img_profile);
        imgSet = (ImageView) findViewById(R.id.img_set);
        etName = (EditText) findViewById(R.id.et_name);
        etSex = (EditText) findViewById(R.id.et_sex);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPass = (EditText) findViewById(R.id.et_pass);
        etPassConform = (EditText) findViewById(R.id.et_pass_conform);
        etAbout = (EditText) findViewById(R.id.et_about);
        etCurrentBusiness = (EditText) findViewById(R.id.et_current_business);
        btnSignUp = (Button) findViewById(R.id.btn_signup);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             if (cd.isConnectingToInternet()) {
                                                 if (!etName.getText().toString().trim().equals("")) {
                                                     if (!etSex.getText().toString().trim().equals("")) {
                                                         if (!etEmail.getText().toString().trim().equals("")) {
                                                             if (isEmailValid(etEmail.getText().toString().trim())) {
                                                                 if (!etPass.getText().toString().trim().equals("")) {
                                                                     if (etPass.getText().toString().trim().equals(etPassConform.getText().toString().trim())) {
                                                                         if (!etAbout.getText().toString().trim().equals("")) {
                                                                             if (!etCurrentBusiness.getText().toString().trim().equals("")) {
//Soutrik
                                                                             } else {
                                                                                 etCurrentBusiness.requestFocus();
                                                                                 etCurrentBusiness.setError("Please enter your current business.");
                                                                             }
                                                                         } else {
                                                                             etAbout.requestFocus();
                                                                             etAbout.setError("Please enter about yourself.");
                                                                         }
                                                                     } else {
                                                                         etPassConform.requestFocus();
                                                                         etPassConform.setError("Password did not match.");
                                                                     }
                                                                 } else {
                                                                     etPass.requestFocus();
                                                                     etPass.setError("Please enter your password.");
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
                                                         etSex.requestFocus();
                                                         etSex.setError("Please enter.");
                                                     }
                                                 } else {
                                                     etName.requestFocus();
                                                     etName.setError("Please enter your name.");
                                                 }
                                             } else {
                                                 Toast.makeText(ActivitySignUp.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                             }
                                         }
                                     }

        );

        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dialogChooser = new Dialog(ActivitySignUp.this, R.style.DialogSlideAnim);
        dialogChooser.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogChooser.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogChooser.getWindow().setGravity(Gravity.BOTTOM);
        dialogChooser.setContentView(R.layout.dialog_camera_selection);
        dialogChooser.setCanceledOnTouchOutside(true);

        llGallery = (LinearLayout) dialogChooser.findViewById(R.id.ll_gallery);
        llCamera = (LinearLayout) dialogChooser.findViewById(R.id.ll_camera);
        llCancelDialog = (LinearLayout) dialogChooser.findViewById(R.id.ll_cancel);

        imgSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChooser.show();
            }
        });

        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChooser.cancel();
                Intent galleryIntent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, ACTION_TAKE_GALLERY);
            }
        });

        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChooser.cancel();
                Intent jh = new Intent(ActivitySignUp.this, EsolzCamera.class);
                startActivityForResult(jh, ACTION_TAKE_PHOTO_B);
            }
        });

        llCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChooser.cancel();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        // super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_TAKE_PHOTO_B) {

            if (resultCode == 6000) {

                Current_PATH = data.getStringExtra("Path");
                Log.d("CAMERA IMAGE", Current_PATH);
                Toast.makeText(ActivitySignUp.this, "CAMERA..." + Current_PATH, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == ACTION_TAKE_GALLERY) {
            if (resultCode == RESULT_OK) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                Current_PATH = cursor.getString(columnIndex);
                cursor.close();

                Log.d("GALLERY IMAGE", Current_PATH);
                Toast.makeText(ActivitySignUp.this, "Gallery..." + Current_PATH, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ActivitySignUp.this, "Canceled by user",
                        Toast.LENGTH_SHORT).show();
                Current_PATH = "";
            }
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
