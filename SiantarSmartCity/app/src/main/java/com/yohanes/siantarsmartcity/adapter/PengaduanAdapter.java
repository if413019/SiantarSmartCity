package com.yohanes.siantarsmartcity.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yohanes.siantarsmartcity.R;
import com.yohanes.siantarsmartcity.app.AppConfig;
import com.yohanes.siantarsmartcity.model.Pengaduan;

import java.util.ArrayList;

/**
 * Created by Yohanes_marthin on 26/03/2016.
 */
public class PengaduanAdapter extends ArrayAdapter<Pengaduan> {

    Context context;
    ArrayList<Pengaduan> pengaduans;
    //View holder digunakan untuk menyimpan objek item yang sudah    pernah dibuat
    static class ViewHolder {
        public TextView judul;
        public TextView deskripsi;
        public TextView tanggal;
        public TextView status;
        public TextView username;
        public TextView alamat;
        public ImageView gambar;
    }
    //konstuktor
    public PengaduanAdapter(Context context, ArrayList<Pengaduan> pengaduans) {
        super(context, R.layout.item_pengaduan_user, pengaduans);
        this.context = context;
        this.pengaduans = pengaduans;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup
            parent) {
        // Get the data item for this position
        Pengaduan pengaduan = getItem(position);
        // Cek data, jika item sudah pernah dibuat maka akan digunakan ulang, jika tidak maka akan dibuat
        ViewHolder viewHolder;
        if (convertView == null) {
            //membuat baru item
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_pengaduan_user, parent,
                    false);
            viewHolder.judul = (TextView)
                    convertView.findViewById(R.id.judul);
            viewHolder.deskripsi = (TextView)
                    convertView.findViewById(R.id.deskripsi);
            viewHolder.tanggal = (TextView)
                    convertView.findViewById(R.id.tanggal);
            viewHolder.status = (TextView)
                    convertView.findViewById(R.id.status);
            viewHolder.username = (TextView)
                    convertView.findViewById(R.id.txtUsername);
            viewHolder.alamat = (TextView) convertView.findViewById(R.id.alamat);
            viewHolder.gambar = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            //menggunakan item yang sudah pernah dibuat
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Set item dengan value dari objek
        if(pengaduan.getJudul().length() > 25)
        {
            viewHolder.judul.setText(pengaduan.getJudul().substring(0,22)+ "...");
        }
        else{
            viewHolder.judul.setText(pengaduan.getJudul());
        }

        viewHolder.tanggal.setText(pengaduan.getTanggal());
        if(pengaduan.getDeskripsi().length() > 90)
        {
            viewHolder.deskripsi.setText(pengaduan.getDeskripsi().substring(0,90)+ "...(Lihat " +
                    "selengkapnya)");
        }
        else {
            viewHolder.deskripsi.setText(pengaduan.getDeskripsi());
        }
        String status = pengaduan.getStatus();
        if(status.equals("Menunggu Tindakan"))
        {
            viewHolder.status.setBackgroundColor(Color.parseColor("#db463f"));
        }
        if(status.equals("Diproses"))
        {
            viewHolder.status.setBackgroundColor(Color.parseColor("#3b4148"));
        }
        if(status.equals("Siap"))
        {
            viewHolder.status.setBackgroundColor(Color.parseColor("#ff669900"));
        }
        if(status.equals("Tidak Selesai"))
        {
            viewHolder.status.setBackgroundColor(Color.parseColor("#db463f"));
        }
        viewHolder.status.setText(status);
        viewHolder.username.setText(pengaduan.getNamaUser());
        viewHolder.alamat.setText(pengaduan.getAlamat());
        viewHolder.gambar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(pengaduan.getImage() != "") {
            Picasso.with(getContext()).load(pengaduan.getImage()).resize(500,
                    0).placeholder
                    (R.drawable.def_image).into
                    (viewHolder.gambar);
        }
        return convertView;
    }
}
