package com.yohanes.siantarsmartcity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yohanes.siantarsmartcity.R;
import com.yohanes.siantarsmartcity.model.Skpd;
import com.yohanes.siantarsmartcity.model.TindakLanjut;

import java.util.ArrayList;

/**
 * Created by Yohanes_marthin on 13/06/2016.
 */
public class SkpdAdapter extends ArrayAdapter<Skpd> {
    Context context;
    ArrayList<Skpd> skpds;
    //View holder digunakan untuk menyimpan objek item yang sudah    pernah dibuat
    static class ViewHolder {
        public TextView nama;
        public ImageView gambar;
    }

    //konstuktor
    public SkpdAdapter(Context context, ArrayList<Skpd> skpds) {
        super(context, R.layout.item_skpd, skpds);
        this.context = context;
        this.skpds = skpds;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup
            parent) {
        // Get the data item for this position
        Skpd skpd = getItem(position);
        // Cek data, jika item sudah pernah dibuat maka akan digunakan ulang, jika tidak maka akan dibuat
        ViewHolder viewHolder;
        if (convertView == null) {
            //membuat baru item
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_skpd, parent,
                    false);
            viewHolder.nama = (TextView)
                    convertView.findViewById(R.id.namaskpd);
            viewHolder.gambar = (ImageView) convertView.findViewById(R.id.gambardinas);
            convertView.setTag(viewHolder);
        } else {
            //menggunakan item yang sudah pernah dibuat
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Set item dengan value dari objek
        viewHolder.nama.setText(skpd.getNama());
        viewHolder.gambar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(skpd.getImage() != "") {
            Picasso.with(getContext()).load(skpd.getImage()).resize(500,
                    0).placeholder
                    (R.drawable.def_image).into
                    (viewHolder.gambar);
        }
        return convertView;
    }
}
