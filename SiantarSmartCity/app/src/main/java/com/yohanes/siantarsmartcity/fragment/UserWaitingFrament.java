package com.yohanes.siantarsmartcity.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yohanes.siantarsmartcity.R;
import com.yohanes.siantarsmartcity.activity.ActivityProsesPengaduan;
import com.yohanes.siantarsmartcity.activity.DetailPengaduanSKPD;
import com.yohanes.siantarsmartcity.activity.DetailPengaduanUser;
import com.yohanes.siantarsmartcity.adapter.PengaduanAdapter;
import com.yohanes.siantarsmartcity.app.AppConfig;
import com.yohanes.siantarsmartcity.app.AppController;
import com.yohanes.siantarsmartcity.helper.JsonArrayPostRequest;
import com.yohanes.siantarsmartcity.helper.SQLiteHandler;
import com.yohanes.siantarsmartcity.model.Pengaduan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserWaitingFrament extends ListFragment implements AdapterView.OnItemClickListener {
    ArrayList<Pengaduan> pengaduans;
    PengaduanAdapter adapter;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_waiting_frament, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new SQLiteHandler(getActivity().getApplicationContext());
        pengaduans = new ArrayList<>();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Memuat Daftar Pengaduan...");        showDialog();
        fullpengaduan();
        adapter = new PengaduanAdapter(getActivity().getApplicationContext(), pengaduans);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Intent intent = new Intent(getActivity().getApplicationContext(), DetailPengaduanUser.class);
        intent.putExtra("id", pengaduans.get(position).getId());
        intent.putExtra("judul", pengaduans.get(position).getJudul());
        intent.putExtra("deskripsi", pengaduans.get(position).getDeskripsi());
        intent.putExtra("tanggal", pengaduans.get(position).getTanggal());
        intent.putExtra("alamat", pengaduans.get(position).getAlamat());
        intent.putExtra("nama", pengaduans.get(position).getNamaUser());
        intent.putExtra("image", pengaduans.get(position).getImage());
        startActivity(intent);
    }

    private void fullpengaduan() {
        pengaduans.clear();
        HashMap<String, String> user = db.getUserDetails();
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", user.get("uid"));
        JsonArrayPostRequest req = new JsonArrayPostRequest(AppConfig.URL_GET_PENGADUAN_USER,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject pengaduan = response.getJSONObject(i);
                                if(pengaduan.getString("status").equals("Menunggu Tindakan")) {
                                    String judul = pengaduan.getString("judul");
                                    String deskripsi = pengaduan.getString("deskripsi");
                                    String alamat = pengaduan.getString("alamat");
                                    String tanggal_pengaduan = pengaduan.getString("tanggal_pengaduan");
                                    String namalengkap = pengaduan.getString("username");
                                    String gambar;
                                    if (pengaduan.getString("image") != "")
                                        gambar = pengaduan.getString("image");
                                    else
                                        gambar = "";
                                    int idpengaduan =
                                            pengaduan.getInt("idpengaduan");
                                    String status = pengaduan.getString("status");
                                    Pengaduan p = new Pengaduan(idpengaduan, judul, deskripsi,
                                            alamat,
                                            tanggal_pengaduan, namalengkap, status);
                                    p.setImage(gambar);
                                    pengaduans.add(p);
                                }
                                adapter.notifyDataSetChanged();
                            }
                            hideDialog();
//                            if(pengaduans.isEmpty())
//                                Toast.makeText(getActivity().getApplicationContext(), "Tidak ada " +
//                                        "pengaduan yang ditemukan pada halaman ini!", Toast
//                                        .LENGTH_LONG).show();
                        } catch (JSONException e) {
                            hideDialog();
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.d("Fragment User Completed", "Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        "Oops, ada kesalahan. Silahkan periksa koneksi Anda", Toast.LENGTH_SHORT)
                        .show();
            }
        }, params);
        req.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
