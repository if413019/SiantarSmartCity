package com.yohanes.siantarsmartcity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yohanes.siantarsmartcity.R;
import com.yohanes.siantarsmartcity.app.AppConfig;

public class DeskripsiSKPD extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deskripsi_skpd);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        ImageView image = (ImageView) findViewById(R.id.imgSKPD);
        TextView nama = (TextView) findViewById(R.id.txtNama);
        TextView deskripsi = (TextView) findViewById(R.id.txtDeskripsi);
        TextView alamat = (TextView) findViewById(R.id.txtAlamatSkpd);
        TextView telepon = (TextView) findViewById(R.id.txtTeleponSkpd);


        if(intent.getStringExtra("image")!="")
        {
            Picasso.with(this).load(intent.getStringExtra("image")).placeholder
                    (R.drawable.def_image).into
                    (image);
        }
        nama.setText(intent.getStringExtra("name"));
        deskripsi.setText(intent.getStringExtra("deskripsi"));
        alamat.setText("Alamat : "+intent.getStringExtra("alamat"));
        telepon.setText("Telepon : "+intent.getStringExtra("telepon"));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
    @Override
    public void onBackPressed() {
        finish();
    }

}
