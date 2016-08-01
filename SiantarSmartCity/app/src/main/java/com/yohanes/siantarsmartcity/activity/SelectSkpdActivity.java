package com.yohanes.siantarsmartcity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yohanes.siantarsmartcity.R;
import com.yohanes.siantarsmartcity.adapter.SkpdAdapter;
import com.yohanes.siantarsmartcity.app.AppConfig;
import com.yohanes.siantarsmartcity.app.AppController;
import com.yohanes.siantarsmartcity.helper.JsonArrayPostRequest;
import com.yohanes.siantarsmartcity.model.Skpd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectSkpdActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private Intent intent;
    private ProgressDialog pDialog;
    private SkpdAdapter adapter;
    ArrayList<Skpd> skpds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_skpd);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Memuat Dinas SKPD...");
        intent = getIntent();
        skpds = new ArrayList<>();
        loadSkpd();
        showDialog();
        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new SkpdAdapter(getApplicationContext(), skpds);
        //set adapter pada listview
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, AddPengaduan.class);
        intent.putExtra("idskpd", skpds.get(position).getId());
        startActivity(intent);
    }

    private void loadSkpd() {
        skpds.clear();
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", "true");
        JsonArrayPostRequest req = new JsonArrayPostRequest(AppConfig.URL_DINAS_SKPD,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject skpd = response.getJSONObject(i);
                                int id = skpd.getInt("idskpd");
                                String nama = skpd.getString("namaskpd");
                                String deskripsi = skpd.getString("deskripsiskpd");
                                String image = skpd.getString("image");
                                String alamat =  skpd.getString("alamatskpd");
                                String telepon =  skpd.getString("teleponskpd");
                                Skpd p = new Skpd(id, nama, deskripsi, image, alamat, telepon);
                                skpds.add(p);
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
                Log.d("Pilih SKPD", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        }, params);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}