package com.furkandemir.onlineyoklama.Ogretmen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.furkandemir.onlineyoklama.Ogrenci.DersSecim;
import com.furkandemir.onlineyoklama.Ogrenci.LoginFragment;
import com.furkandemir.onlineyoklama.R;
import com.furkandemir.onlineyoklama.Yoklama.ApiClient;
import com.furkandemir.onlineyoklama.Yoklama.ApiInterface;
import com.furkandemir.onlineyoklama.Yoklama.PrefConfig;

public class LoginOgr extends AppCompatActivity implements LoginOgrFragment.OnLoginFormActivityListener,OgrDersSecim.OnLogoutListener {

    public static PrefConfig prefConfig;
    public static ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ogr);

        prefConfig = new PrefConfig(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        if(findViewById(R.id.fragment_container) != null){
            if(savedInstanceState != null){
                return;
            }
            if (prefConfig.readLoginStatus()){
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new OgrDersSecim()).commit();
            }
            else {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new LoginOgrFragment()).commit();
            }
        }

    }

    @Override
    public void performLogin(String name) {
        prefConfig.writeName(name);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new OgrDersSecim()).commit();
    }

    @Override
    public void logoutPerform() {
        prefConfig.writeLoginStatus(false);
        prefConfig.writeName("User");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new LoginOgrFragment()).commit();
    }
}