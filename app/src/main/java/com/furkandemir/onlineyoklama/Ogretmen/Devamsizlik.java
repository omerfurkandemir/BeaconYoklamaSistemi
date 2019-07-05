package com.furkandemir.onlineyoklama.Ogretmen;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.furkandemir.onlineyoklama.CustomListAdapter;
import com.furkandemir.onlineyoklama.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Devamsizlik extends AppCompatActivity {

    ListView listView;
    Context context;
    ArrayAdapter<String> arrayAdapter;
    int a = 0, b = 0, c = 0, x = 0, y = 0, z = 0, p = 0;
    int f = 0, sayac = 0, temp = 0;
    String lesson_id, ders_id, dersinadi;
    String[] heroes_id, heroes, temp_devam_no, temp_no, ders_adi, devam_no, deneme, yeni;
    SwipeRefreshLayout swipeRefresh;
    CollapsingToolbarLayout ct;

    ArrayList<String> selectedItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devamsizlik);

        Intent intent = getIntent();
        ders_id = intent.getStringExtra("gonderilen_id");
        dersinadi = intent.getStringExtra("gonderilen_adi");

        listView = (ListView) findViewById(R.id.yoklama_lv);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        getJSON_ders("http://192.168.137.1/yoklama/getdata_ders.php");
        getJSON_number("http://192.168.137.1/yoklama/getdata_number.php");
        getJSON("http://192.168.137.1/yoklama/getdata_devam.php");

        ct = (CollapsingToolbarLayout) findViewById(R.id.colappsingtoolbar);
        ct.setTitle(dersinadi);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(R.color.refresh, R.color.refresh1, R.color.refresh2);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                arrayAdapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        });

    }

    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        int deger = jsonArray.length();

        heroes = new String[a];
        heroes_id = new String[a];

        for (int i = 0; i < deger; i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            final String ders_ogr_id = obj.getString("ogrenci_no");

            for (int j = 0; j < a; j++) {
                if (ders_ogr_id.equals(ders_adi[j])) {
                    heroes[c] = obj.getString("ogrenci_adsoyad");
                    heroes_id[c] = String.valueOf(c);
                    c++;

                    for(int s = 0; s < f; s++){
                        if(ders_ogr_id.equals(temp_no[s])){
                            devam_no[z] = temp_devam_no[s];
                            z++;
                        }
                    }
                }
            }

        }

        CustomListAdapter customListAdapter = new CustomListAdapter(this, heroes, devam_no);
        listView.setAdapter(customListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    private void getJSON_ders(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView_ders(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadIntoListView_ders(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        int deger = jsonArray.length();

        for (int i = 0; i < deger; i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            final String ders_ogr_id = obj.getString("ders");

            if (ders_ogr_id.equals(ders_id)) {
                b++;
            }
        }

        ders_adi = new String[b];

        for (int i = 0; i < deger; i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            final String ders_ogr_id = obj.getString("ders");

            if (ders_ogr_id.equals(ders_id)) {
                ders_adi[a] = obj.getString("ogrenci");
                a++;
            }
        }

    }

    private void getJSON_number(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView_number(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadIntoListView_number(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        int deger = jsonArray.length();

        for (int i = 0; i < deger; i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            final String ders_ogr_id = obj.getString("dersogrenci_no");
            final String devam_id = obj.getString("dersogrenci");


            if (devam_id.equals(ders_id)) {
                x++;
            }

            //for (int j = 0; j < a; j++) {
            //    if (ders_ogr_id.equals(ders_adi[j]) && devam_id.equals(ders_id)) {
            //        x++;
            //    }
            //}

        }

        devam_no = new String[x];
        temp_devam_no = new String[x];
        temp_no = new String[x];
        yeni = new String[x];
        //String[] deneme;
        deneme = new String[x];

        for (int i = 0; i < deger; i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            final String ders_ogr_id = obj.getString("dersogrenci_no");
            final String devam_id = obj.getString("dersogrenci");


            if (devam_id.equals(ders_id)) {
                deneme[y] = ders_ogr_id;
                y++;
            }
        }



        for (int i = 0; i < deneme.length; i++) {
            yeni[f] = deneme[i];
            for (int j = 0; j < deneme.length; j++) {
                if (yeni[f].equals(deneme[j])) {
                    sayac++;
                }
            }
            for (int t = 0; t < f + 1; t++) {
                if (yeni[f].equals(yeni[t])) {
                    temp++;
                }
            }
            if(temp <= 1){
                temp_devam_no[f] = String.valueOf(sayac);
                temp_no[f] = yeni[f];
                sayac = 0;
                f++;
                temp = 0;
            }
            else {
                sayac = 0;
                temp = 0;
            }
        }
    }

}
