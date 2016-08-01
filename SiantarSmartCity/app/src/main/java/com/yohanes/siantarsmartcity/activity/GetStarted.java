package com.yohanes.siantarsmartcity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yohanes.siantarsmartcity.R;

public class GetStarted extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
    }

    public void login(View v){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void register(View v){
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }
}