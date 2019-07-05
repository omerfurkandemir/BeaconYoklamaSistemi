package com.furkandemir.onlineyoklama.Yoklama;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("devamsizlik.php")
    Call<User> performRegistration(@Query("dersogrenci") String Name, @Query("dersogrenci_no") String Number);


    @GET("login.php")
    Call<User> perforUserLogin(@Query("ogrenci_kullaniciadi") String UserName, @Query("ogrenci_sifre") String UserPassword);


    @GET("login_ogr.php")
    Call<User> perforUserLogin_Ogr(@Query("ogretmen_kullaniciadi") String UserName, @Query("ogretmen_sifre") String UserPassword);


}
