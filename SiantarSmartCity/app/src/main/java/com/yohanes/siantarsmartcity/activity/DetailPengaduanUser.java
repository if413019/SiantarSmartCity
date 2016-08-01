package com.yohanes.siantarsmartcity.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yohanes.siantarsmartcity.R;
import com.yohanes.siantarsmartcity.app.AppConfig;

public class DetailPengaduanUser extends AppCompatActivity {
    private TextView judul;
    private TextView alamat;
    private TextView tanggal;
    private TextView deskripsi;
    private TextView nama;
    private ImageView imageView;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pengaduan_user);
        intent = getIntent();
        judul = (TextView) findViewById(R.id.judulPengaduan);
        alamat = (TextView) findViewById(R.id.alamatPengaduan);
        tanggal = (TextView) findViewById(R.id.createdAt);
        deskripsi = (TextView) findViewById(R.id.deskripsiPengaduan);
        nama = (TextView) findViewById(R.id.createdBy);
        imageView = (ImageView) findViewById(R.id.imageDesc);
        if(intent.getStringExtra("image")!="")
        {
            Picasso.with(this).load(intent.getStringExtra("image")).placeholder
                    (R.drawable.def_image).into
                    (imageView);
        }

        judul.setText       (intent.getStringExtra("judul"));
        alamat.setText      ("Lokasi : " + intent.getStringExtra("alamat"));
        nama.setText        ("Oleh     : " + intent.getStringExtra("nama"));
        tanggal.setText     ("Dibuat pada: " + intent.getStringExtra("tanggal"));
        deskripsi.setText   (intent.getStringExtra("deskripsi"));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog builder = new Dialog(DetailPengaduanUser.this);
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

    public void TrackPengaduan(View v)
    {
        Intent sendIntent = new Intent(this, TindakLanjutActivity.class);
        sendIntent.putExtra("judul", intent.getStringExtra("judul"));
        sendIntent.putExtra("alamat", intent.getStringExtra("alamat"));
        sendIntent.putExtra("nama", intent.getStringExtra("nama"));
        sendIntent.putExtra("tanggal", intent.getStringExtra("tanggal"));
        sendIntent.putExtra("image", intent.getStringExtra("image"));
        sendIntent.putExtra("id", intent.getIntExtra("id",0));
        startActivity(sendIntent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
