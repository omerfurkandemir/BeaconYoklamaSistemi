package com.furkandemir.onlineyoklama;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

import com.furkandemir.onlineyoklama.Ogrenci.Login;
import com.furkandemir.onlineyoklama.Ogretmen.LoginOgr;

public class AnaSayfa extends AppCompatActivity {

    CardView ogrenci, ogretmen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);

        ogrenci = (CardView) findViewById(R.id.cv_ogrenci);
        ogretmen = (CardView) findViewById(R.id.cv_ogretmen);

        ogrenci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AnaSayfa.this,MainActivity.class);
                startActivity(i);
            }
        });

        ogretmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AnaSayfa.this,LoginOgr.class);
                startActivity(i);
            }
        });

    }
}
