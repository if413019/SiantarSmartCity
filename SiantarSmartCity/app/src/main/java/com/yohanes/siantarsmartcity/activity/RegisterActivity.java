package com.yohanes.siantarsmartcity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.yohanes.siantarsmartcity.R;
import com.yohanes.siantarsmartcity.app.AppConfig;
import com.yohanes.siantarsmartcity.app.AppController;
import com.yohanes.siantarsmartcity.helper.SQLiteHandler;
import com.yohanes.siantarsmartcity.helper.SessionManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements Validator.ValidationListener{
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private RadioGroup sex;
    private Button btnLinkToLogin;
    @NotEmpty(message = "Nama tidak boleh kosong!")
    private EditText inputFullName;
    @NotEmpty(message = "Nomor KTP tidak boleh kosong!")
    @Length(min=16, max = 16,message = "Nomor KTP yang anda masukkan tidak valid!")
    private EditText nomorKTP;
    @NotEmpty(message = "Nomor telepon tidak boleh kosong!")
    @Length(max=12, message = "Nomor telepon maksimum 12")
    private EditText nomorTelepon;
    @NotEmpty(message = "Alamat rumah tidak boleh kosong!")
    private EditText alamatRumah;
    @NotEmpty(message = "Username harus unik, Username tidak boleh kosong!")
    private EditText userName;
    @NotEmpty(message = "Username tidak boleh kosong!")
    @Email(message = "Email yang Anda masukkan tidak valid")
    private EditText inputEmail;
    @NotEmpty(message = "Password tidak boleh kosong")
    @Password(min = 6, message = "Password minimum 6 karakter")
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private Bitmap bitmap;
    private ImageView imageView;
    private String jenisKelamin;

    private Validator validator;
    String nama, nomor_ktp, nomor_telepon, alamat, username, email, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        inputFullName = (EditText) findViewById(R.id.name);
        nomorKTP = (EditText) findViewById(R.id.nomor_ktp);
        nomorTelepon = (EditText) findViewById(R.id.nomor_telepon);
        alamatRumah = (EditText) findViewById(R.id.alamat);
        userName = (EditText) findViewById(R.id.username);
        inputEmail = (EditText) findViewById(R.id.email);
        imageView = (ImageView) findViewById(R.id.imageView3);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        sex = (RadioGroup) findViewById(R.id.radioGroup);

        validator = new Validator(this);
        validator.setValidationListener(this);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int a =  sex.getCheckedRadioButtonId();
                if(a == R.id.rbLaki)
                    jenisKelamin = "Laki-laki";
                else
                    jenisKelamin = "Perempuan";
                validator.validate();
            }
        });
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String nama, final String nomor_ktp, final String nomor_telepon,
                              final String alamat,final String username, final String email,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Oops.. Terjadi kesalahan, silahkan periksa koneksi Anda", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama", nama);
                params.put("nomor_ktp", nomor_ktp);
                params.put("nomor_telepon", nomor_telepon);
                params.put("alamat", alamat);
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("gender", jenisKelamin);
                if(bitmap != null) {
                    String image = getStringImage(bitmap);
                    params.put("image", image);
                }
                else{
                    params.put("image", "");
                }
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    @Override
    public void onValidationSucceeded() {
        nama = inputFullName.getText().toString().trim();
        nomor_ktp = nomorKTP.getText().toString().trim();
        nomor_telepon = nomorTelepon.getText().toString().trim();
        alamat = alamatRumah.getText().toString().trim();
        username = userName.getText().toString().trim();
        email = inputEmail.getText().toString().trim();
        password = inputPassword.getText().toString().trim();

        int a =  sex.getCheckedRadioButtonId();
        if(a == R.id.rbLaki)
            jenisKelamin = "L";
        else
            jenisKelamin = "P";
        registerUser(nama, nomor_ktp, nomor_telepon, alamat, username, email, password);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }


    public void showFileChooser(View v) {
        imageView.setImageBitmap(null);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Uri filePath = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Maaf, terjadi kesalahan saat mengambil " +
                        "gambar.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
