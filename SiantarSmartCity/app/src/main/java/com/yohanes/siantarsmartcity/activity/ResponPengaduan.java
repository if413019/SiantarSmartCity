package com.yohanes.siantarsmartcity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yohanes.siantarsmartcity.R;
import com.yohanes.siantarsmartcity.app.AppConfig;
import com.yohanes.siantarsmartcity.app.AppController;
import com.yohanes.siantarsmartcity.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResponPengaduan extends AppCompatActivity {
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private EditText comment;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respon_pengaduan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intent = getIntent();
        pDialog = new ProgressDialog(this);
        comment = (EditText) findViewById(R.id.response);
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
    }


    public void responpengaduan(View v)
    {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        pDialog.setMessage("Menambahkan tindak lanjut ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_TINDAKLANJUT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Respons Pengaduan", "Tindak Lanjut Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Berhasil!", Toast
                                .LENGTH_LONG)
                                .show();
                        Intent intent = new Intent(ResponPengaduan.this,
                                PengaduanSKPD.class);
                        startActivity(intent);
                        finish();

                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Oops.. Terjadi kesalahan, silahkan periksa koneksi Anda", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Proses Pengaduan", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Oops.. Terjadi kesalahan, silahkan periksa koneksi Anda", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                HashMap<String, String> user = db.getUserDetails();
                Map<String, String> params = new HashMap<String, String>();

                hideDialog();
                params.put("idPengaduan", String.valueOf(intent.getIntExtra("id", 0)));
                params.put("idSKPD", user.get("uid").toString());
                params.put("status", intent.getStringExtra("status"));
                params.put("comment", comment.getText().toString());
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
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
}
