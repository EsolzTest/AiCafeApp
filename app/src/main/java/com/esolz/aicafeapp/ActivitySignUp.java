package com.esolz.aicafeapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.esolz.aicafeapp.Adapter.AdapterCustomSpinner;
import com.esolz.aicafeapp.Customviews.OpenSansSemiboldEditText;
import com.esolz.aicafeapp.Datatype.LoginDataType;
import com.esolz.aicafeapp.Datatype.RegistrationDataType;
import com.esolz.aicafeapp.Helper.AppController;
import com.esolz.aicafeapp.Helper.AppData;
import com.esolz.aicafeapp.Helper.CircleTransform;
import com.esolz.aicafeapp.Helper.ConnectionDetector;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ltp on 09/07/15.
 */
public class ActivitySignUp extends AppCompatActivity {

    LinearLayout llCancel;
    ImageView imgProfile, imgSet;
    OpenSansSemiboldEditText etName, etDOB, etEmail, etPass, etPassConform, etAbout, etCurrentBusiness;
    Spinner spnSex;
    Button btnSignUp;

    ConnectionDetector cd;

    Dialog dialogChooser, dialogDatePicker;
    LinearLayout llGallery, llCamera, llCancelDialog;

    // --- For Camera and Gallery ---
    String Current_PATH = "", imgTYPE = "", registrationURL = "", exception = "";
    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final int ACTION_TAKE_GALLERY = 2;
    // --- End ---

    DatePicker datePicker;
    LinearLayout llCancelPicker, llDonePicker;
    ProgressBar pbarReg;

    ArrayList<String> sexArraylist;
    String gender = "";

    RegistrationDataType registrationDataType;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        cd = new ConnectionDetector(ActivitySignUp.this);

        sharedPreferences = getSharedPreferences("AppCredit", Context.MODE_PRIVATE);

        Log.v("SignUpActivity MY", "Registration ID: " + AppData.appRegId);

        llCancel = (LinearLayout) findViewById(R.id.ll_cancel);
        imgProfile = (ImageView) findViewById(R.id.img_profile);
        imgSet = (ImageView) findViewById(R.id.img_set);
        etName = (OpenSansSemiboldEditText) findViewById(R.id.et_name);
        etDOB = (OpenSansSemiboldEditText) findViewById(R.id.et_dob);
        etEmail = (OpenSansSemiboldEditText) findViewById(R.id.et_email);
        etPass = (OpenSansSemiboldEditText) findViewById(R.id.et_pass);
        etPassConform = (OpenSansSemiboldEditText) findViewById(R.id.et_pass_conform);
        etAbout = (OpenSansSemiboldEditText) findViewById(R.id.et_about);
        etCurrentBusiness = (OpenSansSemiboldEditText) findViewById(R.id.et_current_business);

        spnSex = (Spinner) findViewById(R.id.spn_sex);
        sexArraylist = new ArrayList<String>();
        sexArraylist.add("Male");
        sexArraylist.add("Female");
        AdapterCustomSpinner adapterCustomSpinner = new AdapterCustomSpinner(ActivitySignUp.this, 0, sexArraylist);
        spnSex.setAdapter(adapterCustomSpinner);
        spnSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                if (sexArraylist.get(arg2).equals("Male")) {
                    gender = "M";
                } else {
                    gender = "F";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                gender = "M";
            }
        });


        btnSignUp = (Button) findViewById(R.id.btn_signup);
        pbarReg = (ProgressBar) findViewById(R.id.prg_reg);
        pbarReg.setVisibility(View.GONE);

        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dialogDatePicker = new Dialog(ActivitySignUp.this);
        dialogDatePicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDatePicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogDatePicker.setContentView(R.layout.dialog_datepicker);
        dialogDatePicker.setCanceledOnTouchOutside(true);
        datePicker = (DatePicker) dialogDatePicker.findViewById(R.id.date_picker);
        llCancelPicker = (LinearLayout) dialogDatePicker.findViewById(R.id.ll_cancel);
        llDonePicker = (LinearLayout) dialogDatePicker.findViewById(R.id.ll_done);


        //------------------------------

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

        etDOB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialogDatePicker.show();
                return false;
            }
        });

        llCancelPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDatePicker.cancel();
            }
        });

        llDonePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDatePicker.cancel();

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();

                String dateString = day + "-" + month + "-" + year;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
                Date date = null;
                try {
                    date = dateFormat.parse(dateString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String finalDate = dateFormat.format(date);
                etDOB.setText(finalDate);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             if (cd.isConnectingToInternet()) {
                                                 if (!etName.getText().toString().trim().equals("")) {
                                                     if (!etDOB.getText().toString().trim().equals("")) {
                                                         if (!etEmail.getText().toString().trim().equals("")) {
                                                             if (isEmailValid(etEmail.getText().toString().trim())) {
                                                                 if (!etPass.getText().toString().trim().equals("")) {
                                                                     if (etPass.getText().toString().trim().equals(etPassConform.getText().toString().trim())) {
                                                                         if (!etAbout.getText().toString().trim().equals("")) {
                                                                             if (!etCurrentBusiness.getText().toString().trim().equals("")) {
                                                                                 try {
                                                                                     registrationURL = "http://www.esolz.co.in/lab9/aiCafe/iosapp/registration.php?name="
                                                                                             + URLEncoder.encode(etName.getText().toString().trim(), "UTF-8")
                                                                                             + "&sex=" + URLEncoder.encode(gender, "UTF-8")
                                                                                             + "&email=" + URLEncoder.encode(etEmail.getText().toString().trim(), "UTF-8")
                                                                                             + "&password=" + URLEncoder.encode(etPass.getText().toString().trim(), "UTF-8")
                                                                                             + "&about=" + URLEncoder.encode(etAbout.getText().toString().trim(), "UTF-8")
                                                                                             + "&business=" + URLEncoder.encode(etCurrentBusiness.getText().toString().trim(), "UTF-8")
                                                                                             + "&dob=" + etDOB.getText().toString().trim()
                                                                                             + "&device_type=1&device_token=" + AppData.appRegId;

                                                                                     Log.d("DOB WITH ENCODER", URLEncoder.encode(etDOB.getText().toString().trim(), "UTF-8"));
                                                                                     Log.d("DOB ", etDOB.getText().toString().trim());
                                                                                     Log.d("reg url", registrationURL);
                                                                                     new AiCafeRegistration().execute();

                                                                                 } catch (Exception e) {
                                                                                     Log.d("Encoader exception : ", e.toString());
                                                                                 }
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
                                                         //etDOB.requestFocus();
                                                         dialogDatePicker.show();
                                                         // etDOB.setError("Please enter your date of birth.");
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

                Picasso.with(getApplicationContext())
                        .load("file://" + Current_PATH.trim()).fit()
                        .centerCrop()
                        .transform(new CircleTransform()).into(imgProfile);
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

                Picasso.with(getApplicationContext())
                        .load("file://" + Current_PATH.trim()).fit()
                        .centerCrop()
                        .transform(new CircleTransform()).into(imgProfile);

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

    class AiCafeRegistration extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbarReg.setVisibility(View.VISIBLE);
            Log.d("GROUP : ", registrationURL);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            exception = "";

            try {
                HttpParams params = new BasicHttpParams();
                params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                        HttpVersion.HTTP_1_1);

                HttpClient client = new DefaultHttpClient();
                client = new DefaultHttpClient(params);
                HttpPost post = new HttpPost(registrationURL);

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                Log.d("Upload Image : ", Current_PATH);
                if (!Current_PATH.equals("")) {
                    File file12 = new File(compressImage(Current_PATH));
                    builder.addBinaryBody("photo", file12, ContentType.APPLICATION_OCTET_STREAM, "aicafe.png");
                }

                HttpEntity entity = builder.build();

                post.setEntity(entity);

                HttpResponse response = client.execute(post);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();

                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                }
                JSONObject jOBJ = new JSONObject(s.toString());
                JSONObject jsonObject = jOBJ.getJSONObject("details");
                registrationDataType = new RegistrationDataType(
                        jsonObject.getString("id"),
                        jsonObject.getString("name"),
                        jsonObject.getString("sex"),
                        jsonObject.getString("email"),
                        jsonObject.getString("about"),
                        jsonObject.getString("business"),
                        jsonObject.getString("dob"),
                        jsonObject.getString("photo"),
                        jsonObject.getString("photo_thumb"),
                        jsonObject.getString("registerDate"),
                        jsonObject.getString("facebookid"),
                        jsonObject.getString("last_sync"),
                        jsonObject.getString("fb_pic_url"),
                        jsonObject.getString("status"),
                        jsonObject.getString("visible")
                );

                Log.d("Registration Response: ", s.toString());
            } catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
                exception = ex.toString();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void resultt) {
            super.onPostExecute(resultt);
            pbarReg.setVisibility(View.GONE);
            if (exception.equals("")) {
                Current_PATH = "";
                Toast.makeText(getApplicationContext(), "Registration success...", Toast.LENGTH_SHORT).show();

                String[] dob = registrationDataType.getDob().split("-");
                Calendar dateOfYourBirth = new GregorianCalendar(
                        Integer.parseInt(dob[0]),
                        Integer.parseInt(dob[1]),
                        Integer.parseInt(dob[2]));
                Calendar today = Calendar.getInstance();
                int yourAge = today.get(Calendar.YEAR) - dateOfYourBirth.get(Calendar.YEAR);
                dateOfYourBirth.add(Calendar.YEAR, yourAge);
                if (today.before(dateOfYourBirth)) {
                    yourAge--;
                }
                Log.d("@@ Age : ", "" + yourAge);

                AppData.loginDataType = new LoginDataType(
                        registrationDataType.getId(),
                        registrationDataType.getName(),
                        registrationDataType.getSex(),
                        registrationDataType.getEmail(),
                        "",
                        registrationDataType.getAbout(),
                        registrationDataType.getBusiness(),
                        registrationDataType.getDob(),
                        registrationDataType.getPhoto(),
                        registrationDataType.getPhoto_thumb(),
                        registrationDataType.getRegisterDate(),
                        registrationDataType.getFacebookid(),
                        registrationDataType.getLast_sync(),
                        registrationDataType.getFb_pic_url(),
                        "" + yourAge,
                        ""
                );

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ID", registrationDataType.getId());
                editor.putString("NAME", registrationDataType.getName());
                editor.putString("SEX", registrationDataType.getSex());
                editor.putString("EMAIL", registrationDataType.getEmail());
                editor.putString("PASSWORD", "");
                editor.putString("ABOUT", registrationDataType.getAbout());
                editor.putString("BUSINESS", registrationDataType.getBusiness());
                editor.putString("DOB", registrationDataType.getDob());
                editor.putString("PHOTO", registrationDataType.getPhoto());
                editor.putString("PHOYOTHUMB", registrationDataType.getPhoto_thumb());
                editor.putString("REG_DATE", registrationDataType.getRegisterDate());
                editor.putString("FACEBOOKID", registrationDataType.getFacebookid());
                editor.putString("LASTSYNC", registrationDataType.getLast_sync());
                editor.putString("FBURL", registrationDataType.getFb_pic_url());
                editor.putString("AGE", "" + yourAge);
                editor.putString("ONLINE", "");
                editor.commit();

                Intent intent = new Intent(ActivitySignUp.this, ActivityLandingPage.class);
                startActivity(intent);
                finish();
            } else {
                Log.d("Registration Exception", exception);
                Toast.makeText(getApplicationContext(), "Server not responding...", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        // by setting this field as true, the actual bitmap pixels are not
        // loaded in the memory. Just the bounds are loaded. If
        // you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        // max Height and width values of the compressed image is taken as
        // 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        // width and height values are set maintaining the aspect ratio of the
        // image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        // setting inSampleSize value allows to load a scaled down version of
        // the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth,
                actualHeight);

        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        // this options allow android to claim the bitmap memory if it runs low
        // on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            // load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));

        // check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

            // write the compressed bitmap at the destination specified by
            // filename.
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), "Lasso/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".png");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null,
                null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
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

}
//http://www.esolz.co.in/lab9/aiCafe/iosapp/registration.php?name=name
// &sex=F
// &email=anjana.maiti7771@esolzmail.com
// &password=123456
// &about=php_developer
// &business=student
// &dob=12-04-1998