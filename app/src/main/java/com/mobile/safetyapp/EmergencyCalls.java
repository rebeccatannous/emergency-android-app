package com.mobile.safetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class EmergencyCalls extends AppCompatActivity {
    ImageView embrace;
    ImageView kafa;
    ImageView isf;
    ImageView covid;
    ImageView redcross;
    ImageView fire;
    ImageView himaya;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_calls);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        redcross=findViewById(R.id.call_redcross);
        redcross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone="140";
                String call="tel:"+phone;
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                startActivity(intent);
            }
        });
        kafa=findViewById(R.id.call_kafa);
        kafa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone="01392220";
                String call="tel:"+phone;
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                startActivity(intent);
            }
        });
        covid=findViewById(R.id.call_covid);
        covid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone="1787";
                String call="tel:"+phone;
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                startActivity(intent);
            }
        });
        isf=findViewById(R.id.call_isf);
        isf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone="112";
                String call="tel:"+phone;
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                startActivity(intent);
            }
        });
        embrace=findViewById(R.id.call_embrace);
        embrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone="1564";
                String call="tel:"+phone;
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                startActivity(intent);
            }
        });
        fire=findViewById(R.id.call_firebrigade);
        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone="175";
                String call="tel:"+phone;
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                startActivity(intent);
            }
        });
        himaya=findViewById(R.id.call_Himaya);
        himaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone="01395315";
                String call="tel:"+phone;
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calls, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);


    }

    public void call(){

    }
}