package com.yohanes.siantarsmartcity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.yohanes.siantarsmartcity.R;
import com.yohanes.siantarsmartcity.adapter.TindakLanjutAdapter;
import com.yohanes.siantarsmartcity.app.AppConfig;
import com.yohanes.siantarsmartcity.app.AppController;
import com.yohanes.siantarsmartcity.helper.JsonArrayPostRequest;
import com.yohanes.siantarsmartcity.model.TindakLanjut;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TindakLanjutActivity extends AppCompatActivity {
    private TextView judul;
    private TextView alamat;
    private TextView tanggal;
    private TextView nama;
    private ImageView imageView;
    private ProgressDialog pDialog;
    ArrayList<TindakLanjut> tindakLanjuts;
    TindakLanjutAdapter adapter;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tindak_lanjut);
        intent = getIntent();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Memuat tidak lanjut...");
        showDialog();
        tindakLanjuts = new ArrayList<>();
        fillTindakLanjut();
        showDialog();
        ListView listView = (ListView) findViewById(R.id.listTindakLanjut);


        judul = (TextView) findViewById(R.id.judulPengaduan);
        alamat = (TextView) findViewById(R.id.alamatPengaduan);
        tanggal = (TextView) findViewById(R.id.createdAt);
        nama = (TextView) findViewById(R.id.createdBy);
        imageView =(ImageView) findViewById(R.id.imageDesc);

        judul.setText       (intent.getStringExtra("judul"));
        alamat.setText      ("Lokasi : " + intent.getStringExtra("alamat"));
        nama.setText        ("Oleh     : " + intent.getStringExtra("nama"));
        tanggal.setText     ("Dibuat pada: " + intent.getStringExtra("tanggal"));

        if(intent.getStringExtra("image")!="")
        {
            Picasso.with(this).load(intent.getStringExtra("image")).placeholder
                    (R.drawable.def_image).into
                    (imageView);
        }

        pDialog.setMessage("Memuat daftar tindak lanjut ...");

        adapter = new TindakLanjutAdapter(getApplicationContext(), tindakLanjuts);
        //set adapter pada listview
        listView.setAdapter(adapter);
    }

    private void fillTindakLanjut() {
        tindakLanjuts.clear();
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", String.valueOf(intent.getIntExtra("id", 0)));
        JsonArrayPostRequest req = new JsonArrayPostRequest(AppConfig.URL_TINDAK_LANJUT,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Toast.makeText(getApplicationContext(), "Ada "+response.length()+" " +
                                    "respon terkait pengaduan ini", Toast.LENGTH_LONG);
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject pengaduan = response.getJSONObject(i);
                                String status = pengaduan.getString("status_pengaduan");
                                String komentar = pengaduan.getString("komentar");
                                String id_skpd = pengaduan.getString("id_skpd");
                                String tanggal = pengaduan.getString
                                        ("waktu_create_tindaklanjut");
                                TindakLanjut p = new TindakLanjut();
                                p.setStatus(status);
                                p.setKomentar(komentar);
                                p.setSkpd(id_skpd);
                                p.setWaktu(tanggal);
                                tindakLanjuts.add(p);
                            }
                            adapter.notifyDataSetChanged();
                            hideDialog();
                        } catch (JSONException e) {
                            hideDialog();
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Tindak Lanjut Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        }, params);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
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