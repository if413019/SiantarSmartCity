package com.yohanes.siantarsmartcity.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.yohanes.siantarsmartcity.R;
import com.yohanes.siantarsmartcity.app.AppConfig;

import java.io.File;

public class DetailPengaduanSKPD extends AppCompatActivity {
    private TextView judul;
    private TextView alamat;
    private TextView tanggal;
    private TextView deskripsi;
    private TextView nama;
    private ImageView imageView;
    private int idPengaduan;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pengaduan_skpd);

        intent = getIntent();
        idPengaduan = intent.getIntExtra("id", 0);
        judul = (TextView) findViewById(R.id.judulPengaduan);
        alamat = (TextView) findViewById(R.id.alamatPengaduan);
        tanggal = (TextView) findViewById(R.id.createdAt);
        deskripsi = (TextView) findViewById(R.id.deskripsiPengaduan);
        nama = (TextView) findViewById(R.id.createdBy);
        imageView =(ImageView) findViewById(R.id.imageDesc);

        judul.setText       (intent.getStringExtra("judul"));
        alamat.setText      ("Lokasi : " + intent.getStringExtra("alamat"));
        nama.setText        ("Oleh     : " + intent.getStringExtra("nama"));
        tanggal.setText     ("Dibuat pada: " + intent.getStringExtra("tanggal"));
        deskripsi.setText   (intent.getStringExtra("deskripsi"));

        if(intent.getStringExtra("image")!="")
        {
            Picasso.with(this).load(AppConfig.URL_API+intent.getStringExtra("image")).placeholder
                    (R.drawable.empty).into
                    (imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog builder = new Dialog(DetailPengaduanSKPD.this);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                builder.setOnDismissListener(new DialogInterface.OnDismissListener(){
                    @Override
                    public void onDismiss(DialogInterface dialogInterface)
                    {
                        //nothing to do
                    }
                });

                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Picasso.with(getApplicationContext()).load(AppConfig.URL_API+intent.getStringExtra("image")).resize(500,
                        0).placeholder
                        (R.drawable.def_image).into
                        (imageView);

                builder.addContentView(imageView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams
                        .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                builder.show();
            }
        });
    }

    public void Respon(View v)
    {
        Intent intentBaru = new Intent(this, ResponPengaduan.class);
        intentBaru.putExtra("id", idPengaduan);
        intentBaru.putExtra("status", "Dikerjakan");
        startActivity(intentBaru);
    }

    public void Close(View v)
    {
        Intent intent = new Intent(this, ClosePengaduan.class);
        intent.putExtra("id", idPengaduan);
        intent.putExtra("status", "Ditolak");
        startActivity(intent);
    }

    public void track(View v)
    {
        Intent sendIntent = new Intent(this, TindakLanjutActivity.class);
        sendIntent.putExtra("judul", intent.getStringExtra("judul"));
        sendIntent.putExtra("alamat", intent.getStringExtra("alamat"));
        sendIntent.putExtra("nama", intent.getStringExtra("nama"));
        sendIntent.putExtra("tanggal", intent.getStringExtra("tanggal"));
        sendIntent.putExtra("id", intent.getIntExtra("id",0));
        sendIntent.putExtra("image", intent.getStringExtra("image"));
        startActivity(sendIntent);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
