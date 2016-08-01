package com.yohanes.siantarsmartcity.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yohanes.siantarsmartcity.R;
import com.yohanes.siantarsmartcity.app.AppConfig;
import com.yohanes.siantarsmartcity.app.AppController;
import com.yohanes.siantarsmartcity.helper.GetAddressTask;
import com.yohanes.siantarsmartcity.helper.SQLiteHandler;
import com.yohanes.siantarsmartcity.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddPengaduan extends AppCompatActivity implements View.OnTouchListener,
        OnItemSelectedListener{
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnSubmit;
    private EditText judul;
    private EditText deskripsi;
    private EditText alamat;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private Bitmap bitmap;
    private ImageView imageView;
    private boolean errorShown = false;
    private Intent intent;
    private String txtKec;
    private String txtDes;

    //UI References
    private EditText fromDateEtxt;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    GoogleMap map;
    @SuppressLint("NewApi")

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pengaduan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Spinner element
        Spinner kecamatan = (Spinner) findViewById(R.id.txtKecamatan);
        Spinner desa = (Spinner) findViewById(R.id.txtDesa);

        // Spinner click listener
        kecamatan.setOnItemSelectedListener(this);
        desa.setOnItemSelectedListener(this);


        // Spinner Drop down elements
        List<String> kecamatans = new ArrayList<String>();
        kecamatans.add("Kecamatan");
        kecamatans.add("Siantar Barat");
        kecamatans.add("Siantar Marihat");
        kecamatans.add("Siantar Martoba");
        kecamatans.add("Siantar Selatan");
        kecamatans.add("Siantar Timur");
        kecamatans.add("Siantar Utara");

        List<String> desas = new ArrayList<String>();
        desas.add("Desa");
        desas.add("Desa Bantan");
        desas.add("Desa Banjar");
        desas.add("Desa Simarito");
        desas.add("Desa Sipinggol");
        desas.add("Desa Teladan");
        desas.add("Desa Timbang");
        desas.add("Desa Proklamasi");
        desas.add("Desa Dwikora");
        desas.add("Desa Pematang Marihat");
        desas.add("Desa Sukamaju");
        desas.add("Desa Pardamean");
        desas.add("Desa Bp Nauli");
        desas.add("Desa Nagahuta");
        desas.add("Desa Simarimbun");
        desas.add("Desa Bukit Shofa");
        desas.add("Desa Gurilla");
        desas.add("Desa Naga Pita");
        desas.add("Desa Pondok");
        desas.add("Desa Satia Nagara");
        desas.add("Desa Sumber Jaya");
        desas.add("Desa Tampun Nabolon");
        desas.add("Desa Bah Kapul");
        desas.add("Desa Simalungun");
        desas.add("Desa Karo");
        desas.add("Desa Toba");
        desas.add("Desa Kristen");
        desas.add("Desa Martimbang");
        desas.add("Desa Aek Nauli");
        desas.add("Desa Pardomuan");
        desas.add("Desa Pahlawan");
        desas.add("Desa Tomuan");
        desas.add("Desa Kebun Sayur");
        desas.add("Desa Merdeka");
        desas.add("Desa Siopat Suhu");
        desas.add("Desa Sigulang gulang I");
        desas.add("Desa Bane");
        desas.add("Desa Martoba");
        desas.add("Desa Melayu");
        desas.add("Desa Baru");
        desas.add("Desa Sukadame");
        desas.add("Desa Kahean");

        // Creating adapter for spinner
        ArrayAdapter<String> kecamatanAdapter = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_item, kecamatans);

        ArrayAdapter<String> desaAdapter = new ArrayAdapter<String>(this, android.R.layout
                .simple_spinner_item, desas);
        // Drop down layout style - list view with radio button
        kecamatanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        desaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        kecamatan.setAdapter(kecamatanAdapter);
        desa.setAdapter(desaAdapter);



        judul = (EditText) findViewById(R.id.judul);
        deskripsi = (EditText) findViewById(R.id.deskripsi);
        alamat = (EditText) findViewById(R.id.alamat);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        imageView = (ImageView) findViewById(R.id.imageView3);
        fromDateEtxt = (EditText) findViewById(R.id.txtTanggal);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        setDateTimeField();
        intent = getIntent();

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);

        if (map != null) {
            map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                Marker marker;
                @Override
                public void onMyLocationChange(Location arg0) {
                    // TODO Auto-generated method stub
                    if (marker != null) {
                        marker.remove();
                    }

                    LatLng latLng = new LatLng(arg0.getLatitude(),arg0.getLongitude());

                    marker = map.addMarker(new MarkerOptions().position(latLng).title("You Are " +
                            "Here"));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                    map.animateCamera(cameraUpdate);
                    new GetAddressTask(AddPengaduan.this).execute(String.valueOf(arg0.getLatitude()), String
                            .valueOf(arg0.getLongitude()));
                }
            });
        }



        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Register Button Click event
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String judulPengaduan = judul.getText().toString().trim();
                String deskripsiPengaduan = deskripsi.getText().toString().trim();
                String alamatPengaduan = alamat.getText().toString().trim();
                String tanggalPengaduan = fromDateEtxt.getText().toString().trim();
                if (!judulPengaduan.isEmpty() && !deskripsiPengaduan.isEmpty() && !alamatPengaduan.isEmpty()) {
                    createPengaduan(judulPengaduan, deskripsiPengaduan, alamatPengaduan, tanggalPengaduan);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Silahkan lengkapi form pengaduan!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void createPengaduan(final String judul, final String deskripsi, final String alamat,
     final String tanggal) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Sedang membuat pengaduan ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADDPENGADUAN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    //Toast.makeText(getApplicationContext(), response.toString(), Toast
                      //      .LENGTH_LONG).show();
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONObject pengaduan = jObj.getJSONObject("pengaduan");
                        String id = pengaduan.getString("id");
                        String judul = pengaduan.getString("judul");
                        Toast.makeText(getApplicationContext(), "pengaduan berhasil dibuat!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                AddPengaduan.this,
                                MainActivity.class);
                        hideDialog();
                        startActivity(intent);
                        finish();
                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        hideDialog();
                        Toast.makeText(getApplicationContext(),
                                "Oops.. Terjadi kesalahan, silahkan periksa koneksi Anda", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    hideDialog();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Add Pengaduan Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Oops.. Terjadi kesalahan, silahkan periksa koneksi Anda", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                HashMap<String, String> user = db.getUserDetails();
                Map<String, String> params = new HashMap<String, String>();
                params.put("judul", judul);
                params.put("deskripsi", deskripsi);
                params.put("alamat", alamat);
                params.put("masyarakat", user.get("uid"));
                params.put("dinasskpd", String.valueOf(intent.getIntExtra("idskpd", 1)));
                params.put("tanggal", tanggal);
                Log.d("Parameter: ", judul +" "+ deskripsi +" "+ alamat +" "+ user.get("uid")+" "+ String
                        .valueOf
                        (intent.getIntExtra("idskpd", 1)) +" "+ tanggal);

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
        // Adding request to request queue
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

    public void showFileChooser(View v) {
        imageView.setImageBitmap(null);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    public void showCamera(View v) {
        imageView.setImageBitmap(null);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 2);
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
        if(requestCode == 2 && resultCode == RESULT_OK)
        {   try {
                bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
            } catch(Exception e){
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void callBackDataFromAsyncTask(String address) {
        if(address != "IOE EXCEPTION") {
            if(alamat.getText().toString().length() == 0) {
                alamat.setText(address);
            }
        }        else {
            if(errorShown == false)
            Toast.makeText(getApplicationContext(),
                    "Maaf, kami tidak dapat menemukan lokasi anda. Silahkan periksa koneksi " +
                            "internet anda!", Toast
                            .LENGTH_LONG)
                    .show();
            errorShown = true;
        }
    }

    private void setDateTimeField() {
        fromDateEtxt.setOnTouchListener(this);
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar curDate = Calendar.getInstance();
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                if((newDate.after(curDate))) {
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        if(id == R.id.txtKecamatan)
            txtKec = item;
        else
            txtDes = item;
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
