package com.yohanes.siantarsmartcity.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.yohanes.siantarsmartcity.R;
import com.yohanes.siantarsmartcity.activity.ActivityProsesPengaduan;
import com.yohanes.siantarsmartcity.activity.DeatailPengaduanOnProgress;
import com.yohanes.siantarsmartcity.activity.DetailPengaduanSKPD;
import com.yohanes.siantarsmartcity.activity.DetailPengaduanUser;
import com.yohanes.siantarsmartcity.adapter.PengaduanAdapter;
import com.yohanes.siantarsmartcity.app.AppConfig;
import com.yohanes.siantarsmartcity.app.AppController;
import com.yohanes.siantarsmartcity.helper.SQLiteHandler;
import com.yohanes.siantarsmartcity.model.Pengaduan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OnProgressFragment extends ListFragment implements AdapterView.OnItemClickListener {
    ArrayList<Pengaduan> pengaduans;
    PengaduanAdapter adapter;
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onprogress, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new SQLiteHandler(getActivity().getApplicationContext());
        pengaduans = new ArrayList<>();
        pDialog = new ProgressDialog(getActivity().getApplicationContext());
        //pDialog.show();
        fullpengaduan();
        adapter = new PengaduanAdapter(getActivity().getApplicationContext(), pengaduans);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        HashMap<String, String> user = db.getUserDetails();
        //Action for User with ROLE user
        if(String.valueOf(user.get("role")).equals("0")) {
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
        //Action for User with ROLE skpd administrator
        else {
            //If there is no respond yet
            if (pengaduans.get(position).getStatus().equals("Menunggu Tindakan")) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ActivityProsesPengaduan.class);
                intent.putExtra("id", pengaduans.get(position).getId());
                intent.putExtra("judul", pengaduans.get(position).getJudul());
                intent.putExtra("deskripsi", pengaduans.get(position).getDeskripsi());
                intent.putExtra("tanggal", pengaduans.get(position).getTanggal());
                intent.putExtra("alamat", pengaduans.get(position).getAlamat());
                intent.putExtra("nama", pengaduans.get(position).getNamaUser());
                intent.putExtra("status", pengaduans.get(position).getStatus());
                intent.putExtra("image", pengaduans.get(position).getImage());
                startActivity(intent);
            }
            else if (pengaduans.get(position).getStatus().equals("Dikerjakan")) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DeatailPengaduanOnProgress.class);
                intent.putExtra("id", pengaduans.get(position).getId());
                intent.putExtra("judul", pengaduans.get(position).getJudul());
                intent.putExtra("deskripsi", pengaduans.get(position).getDeskripsi());
                intent.putExtra("tanggal", pengaduans.get(position).getTanggal());
                intent.putExtra("alamat", pengaduans.get(position).getAlamat());
                intent.putExtra("nama", pengaduans.get(position).getNamaUser());
                intent.putExtra("status", pengaduans.get(position).getStatus());
                intent.putExtra("image", pengaduans.get(position).getImage());
                startActivity(intent);
            }
            //if found any Tindak Lanjut record that corresponding with selected pengaduan
            else {
                Intent intent = new Intent(getActivity().getApplicationContext(), DetailPengaduanSKPD.class);
                intent.putExtra("id", pengaduans.get(position).getId());
                intent.putExtra("judul", pengaduans.get(position).getJudul());
                intent.putExtra("deskripsi", pengaduans.get(position).getDeskripsi());
                intent.putExtra("tanggal", pengaduans.get(position).getTanggal());
                intent.putExtra("alamat", pengaduans.get(position).getAlamat());
                intent.putExtra("nama", pengaduans.get(position).getNamaUser());
                intent.putExtra("image", pengaduans.get(position).getImage());
                startActivity(intent);
            }
        }
    }

    private void fullpengaduan(){
        pengaduans.clear();
        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_GET_LIST_PENGADUAN,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject pengaduan = response.getJSONObject(i);
                                    if(pengaduan.getString("status").equals("Diproses") || pengaduan
                                            .getString("status").equals("Dikerjakan")) {
                                        String judul = pengaduan.getString("judul");
                                        String deskripsi = pengaduan.getString("deskripsi");
                                        String alamat = pengaduan.getString("alamat");
                                        String tanggal_pengaduan = pengaduan.getString("tanggal_pengaduan");
                                        String namalengkap = pengaduan.getString("username");
                                        String gambar;
                                        if(pengaduan.getString("image") != "")
                                            gambar = pengaduan.getString("image");
                                        else
                                            gambar = "";
                                        int idpengaduan = pengaduan.getInt("idpengaduan");
                                        String status = pengaduan.getString("status");
                                        Pengaduan p = new Pengaduan(idpengaduan, judul, deskripsi,
                                                alamat,
                                                tanggal_pengaduan, namalengkap, status);
                                        p.setImage(gambar);
                                        pengaduans.add(p);
                                    }
                                } catch (JSONException e) {
                                    Log.e("Error di JSON", "JSON Parsing error: " + e.getMessage());
                                }
                                adapter.notifyDataSetChanged();
                            }
//                            if(pengaduans.isEmpty())
//                                Toast.makeText(getActivity().getApplicationContext(), "Tidak ada " +
//                                        "pengaduan yang ditemukan pada halaman ini!", Toast
//                                        .LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Ahahahahahha", "Server Error: " + error.getMessage());
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

}
