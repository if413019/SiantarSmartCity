package com.yohanes.siantarsmartcity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yohanes.siantarsmartcity.R;
import com.yohanes.siantarsmartcity.model.TindakLanjut;

import java.util.ArrayList;

/**
 * Created by Yohanes_marthin on 17/05/2016.
 */
public class TindakLanjutAdapter extends ArrayAdapter<TindakLanjut>{
    Context context;
    ArrayList<TindakLanjut> tindakLanjuts;
    //View holder digunakan untuk menyimpan objek item yang sudah    pernah dibuat
    static class ViewHolder {
        public TextView username;
        public TextView komentar;
        public TextView tanggal;
        public TextView status;
        public ImageView gambar;
    }
    //konstuktor
    public TindakLanjutAdapter(Context context, ArrayList<TindakLanjut> tindakLanjuts) {
        super(context, R.layout.item_tindak_lanjut, tindakLanjuts);
        this.context = context;
        this.tindakLanjuts = tindakLanjuts;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup
            parent) {
        // Get the data item for this position
        TindakLanjut tindakLanjut = getItem(position);
        // Cek data, jika item sudah pernah dibuat maka akan digunakan ulang, jika tidak maka akan dibuat
        ViewHolder viewHolder;
        if (convertView == null) {
            //membuat baru item
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_tindak_lanjut, parent,
                    false);
            viewHolder.komentar = (TextView)
                    convertView.findViewById(R.id.deskripsi);
            viewHolder.tanggal = (TextView)
                    convertView.findViewById(R.id.tanggal);
            viewHolder.status = (TextView) convertView.findViewById(R.id.status);
            viewHolder.username = (TextView)
                    convertView.findViewById(R.id.txtUsername);
            viewHolder.gambar = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            //menggunakan item yang sudah pernah dibuat
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Set item dengan value dari objek
        viewHolder.tanggal.setText(tindakLanjut.getWaktu());
        viewHolder.komentar.setText(tindakLanjut.getKomentar());

        String status = tindakLanjut.getStatus();
        if(status.equals("Menunggu Tindakan"))
        {
            viewHolder.status.setTextColor(Color.parseColor("#db463f"));
        }
        if(status.equals("Diproses"))
        {
            viewHolder.status.setTextColor(Color.parseColor("#FF1E91FD"));
        }
        if(status.equals("Dikerjakan"))
        {
            viewHolder.status.setTextColor(Color.parseColor("#ffff8800"));
        }
        if(status.equals("Siap"))
        {
            viewHolder.status.setTextColor(Color.parseColor("#ff669900"));
        }
        if(status.equals("Tidak Selesai"))
        {
            viewHolder.status.setTextColor(Color.parseColor("#db463f"));
        }

        viewHolder.status.setText(tindakLanjut.getStatus());
        if(tindakLanjut.getId_skpd()==null)
            viewHolder.username.setText("Pemko Siantar");
        else
            viewHolder.username.setText(tindakLanjut.getId_skpd());

        //viewHolder.gambar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return convertView;
    }
}
