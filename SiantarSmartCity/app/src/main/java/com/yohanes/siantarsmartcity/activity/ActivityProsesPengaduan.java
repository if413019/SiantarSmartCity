package com.yohanes.siantarsmartcity.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.yohanes.siantarsmartcity.R;
import com.yohanes.siantarsmartcity.app.AppConfig;
import com.yohanes.siantarsmartcity.app.AppController;
import com.yohanes.siantarsmartcity.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ActivityProsesPengaduan extends AppCompatActivity implements View.OnTouchListener{
    private ProgressDialog pDialog;
    Intent intent;
    private SQLiteHandler db;
    private TextView judul;
    private TextView alamat;
    private TextView tanggal;
    private TextView deskripsi;
    private TextView nama;
    private ImageView imageView;

    private EditText fromDateEtxt;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_activity_proses_pengaduan);
        intent = getIntent();

        judul = (TextView) findViewById(R.id.judulPengaduan);
        alamat = (TextView) findViewById(R.id.alamatPengaduan);
        tanggal = (TextView) findViewById(R.id.createdAt);
        deskripsi = (TextView) findViewById(R.id.deskripsiPengaduan);
        nama = (TextView) findViewById(R.id.createdBy);

        fromDateEtxt = (EditText) findViewById(R.id.txtPerkiraan);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        setDateTimeField();

        judul.setText       (intent.getStringExtra("judul"));
        alamat.setText      ("Lokasi : " + intent.getStringExtra("alamat"));
        nama.setText        ("Oleh     : " + intent.getStringExtra("nama"));
        tanggal.setText     ("Dibuat pada: " + intent.getStringExtra("tanggal"));
        deskripsi.setText   (intent.getStringExtra("deskripsi"));

        imageView = (ImageView) findViewById(R.id.imageDesc);
        if(intent.getStringExtra("image")!="")
        {
            Picasso.with(this).load(AppConfig.URL_API+intent.getStringExtra("image")).placeholder
                    (R.drawable.def_image).into
                    (imageView);
        }

        db = new SQLiteHandler(getApplicationContext());
    }

    public void terima(View v)
    {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Memproses pengaduan ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_TINDAKLANJUT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Proses Pengaduan", "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Berhasil!", Toast
                                .LENGTH_LONG)
                                .show();

                            Intent intent = new Intent(ActivityProsesPengaduan.this,
                                    PengaduanSKPD.class);
                            startActivity(intent);
                            finish();

                    } else {
                        // Error in login. Get the error message

                        HashMap<String, String> user = db.getUserDetails();
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
                params.put("idSKPD", String.valueOf(user.get("uid")));
                //params.put("idSKPD", String.valueOf(1));
                params.put("status", "Diproses");
                params.put("comment", "Pengaduan berhasil diterima dan sedang diproses." +
                        "Silahkan menunggu respon berikutnya dari pihak terkait.");
                params.put("nama", intent.getStringExtra("nama"));
                return params;
            }
        };
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

    public void TindakPengaduan(View v){
        Intent sendIntent = new Intent(this, TindakLanjutActivity.class);
        sendIntent.putExtra("judul", intent.getStringExtra("judul"));
        sendIntent.putExtra("alamat", intent.getStringExtra("alamat"));
        sendIntent.putExtra("nama", intent.getStringExtra("nama"));
        sendIntent.putExtra("tanggal", intent.getStringExtra("tanggal"));
        sendIntent.putExtra("image", intent.getStringExtra("image"));
        sendIntent.putExtra("id", intent.getIntExtra("id",0));
        startActivity(sendIntent);
    }

    private void setDateTimeField() {
        fromDateEtxt.setOnTouchListener(this);
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar curDate = Calendar.getInstance();
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                if((newDate.before(curDate))) {
                    Toast.makeText(getApplicationContext(), "Tanggal yang Anda masukan tidak " +
                            "valid!", Toast.LENGTH_LONG).show();
                }
                else {
                    fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                }
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v == fromDateEtxt) {
            fromDatePickerDialog.show();
        }
        return true;
    }
}
