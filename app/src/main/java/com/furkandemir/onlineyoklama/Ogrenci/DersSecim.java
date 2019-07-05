package com.furkandemir.onlineyoklama.Ogrenci;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.furkandemir.onlineyoklama.R;
import com.furkandemir.onlineyoklama.Yoklama.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DersSecim extends Fragment {

    ListView listView;
    Context context;
    OnLogoutListener logoutListener;
    ArrayAdapter<String> arrayAdapter;
    int a = 0, b = 0, c = 0, x = 0;
    private Button BnLogOut;
    SwipeRefreshLayout swipeRefresh;
    TextView tv;
    CollapsingToolbarLayout ct;
    String derslik_adi;
    String derslik_id;
    String[] heroes_id, heroes, temp_heroes_id;
    String[] ders_id, heroes_ogr, heroes_ogr_id;
    int k = 0, m = 0, f = 0, p = 0;

    ArrayList<String> selectedItems = new ArrayList<>();

    public interface OnLogoutListener {
        public void logoutPerform();
    }

    public DersSecim() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ders_secim, container, false);

        Intent intent = getActivity().getIntent();
        derslik_adi = intent.getStringExtra("derslikadi");

        listView = (ListView) view.findViewById(R.id.listView);
        getJSON_adi("http://192.168.137.1/yoklama/getdata_adi.php");
        getJSON_ogr("http://192.168.137.1/yoklama/getdata_ogr.php");
        getJSON("http://192.168.137.1/yoklama/getdata.php");

        BnLogOut = view.findViewById(R.id.bn_logout);

        ct = view.findViewById(R.id.colappsingtoolbar);

        BnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Dikkat");
                builder.setMessage("Oturumu kapatmak istiyor musunuz?");
                builder.setNegativeButton("Hayır", null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logoutListener.logoutPerform();
                    }
                });
                builder.show();

            }
        });

        swipeRefresh = view.findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(R.color.refresh, R.color.refresh1, R.color.refresh2);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                arrayAdapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        });


        return view;
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

        for (int i = 0; i < deger; i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            //final String ogr_id = Login.prefConfig.readName();
            final String ders_ogr_id = obj.getString("sinif");
            //int i_ogr_id = Integer.parseInt(ogr_id);
            //int i_ders_ogr_id = Integer.parseInt(ders_ogr_id);

            if (ders_ogr_id.equals(derslik_id)) {
                b++;
            }
        }

        heroes = new String[b];
        heroes_id = new String[b];

        for (int i = 0; i < deger; i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            //final String ogr_id = Login.prefConfig.readName();
            final String ders_ogr_id = obj.getString("sinif");
            //int i_ogr_id = Integer.parseInt(ogr_id);
            //int i_ders_ogr_id = Integer.parseInt(ders_ogr_id);

            if (ders_ogr_id.equals(derslik_id)) {
                heroes[a] = obj.getString("ders_adi");
                heroes_id[a] = obj.getString("ders_id");
                a++;

            }
        }

        int h = 0;

        for (int i = 0; i < a; i++) {
            String temp = heroes_id[i];

            for (int j = 0; j < m; j++) {
                String temp1 = ders_id[j];
                if (temp.equals(temp1)) {
                    h++;
                }
            }
        }

        heroes_ogr = new String[h];
        heroes_ogr_id = new String[h];

        for (int i = 0; i < a; i++) {
            String temp = heroes_id[i];

            for (int j = 0; j < m; j++) {
                String temp1 = ders_id[j];
                if (temp.equals(temp1)) {
                    heroes_ogr[p] = heroes[i];
                    heroes_ogr_id[p] = heroes_id[i];
                    p++;
                }
            }

        }

        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item_2, R.id.txt_list, heroes_ogr);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectItem = ((TextView) view).getText().toString();
                if (selectedItems.contains(selectItem)) {
                    selectedItems.remove(selectItem);
                } else {
                    selectedItems.add(selectItem);
                }

                String[] items;
                items = new String[selectedItems.size()];
                temp_heroes_id = new String[selectedItems.size()];

                for (String item : selectedItems) {
                    items[0] = item;
                }

                for (int i = 0; i < p; i++) {
                    if (heroes_ogr[i] == items[0]) {
                        temp_heroes_id[x] = heroes_ogr_id[i];
                        x++;

                    }
                }


                performRegistration();

                x = 0;
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        logoutListener = (OnLogoutListener) activity;
    }

    public void performRegistration() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Dikkat");
        builder.setMessage("Yoklamayı kaydetmek istiyor musunuz?");
        builder.setNegativeButton("Hayır", null);
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int z = 0; z < 1; z++) {
                    final String ogr_no = Login.prefConfig.readName();
                    Call<User> call = Login.apiInterface.performRegistration(temp_heroes_id[z], ogr_no);

                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.body().getResponse().equals("ok")) {
                                Login.prefConfig.displayToast("Yoklama başarıyla kaydedildi.");
                            } else if (response.body().getResponse().equals("error")) {
                                Login.prefConfig.displayToast("Lütfen tekrar deneyiniz.");
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });

                }
                c = 0;
            }
        });
        builder.show();

    }

    private void getJSON_adi(final String urlWebService) {

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
                    loadIntoListView_adi(s);
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

    private void loadIntoListView_adi(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        int deger = jsonArray.length();

        for (int i = 0; i < deger; i++) {
            JSONObject obj = jsonArray.getJSONObject(i);


            //final String ogr_id = Login.prefConfig.readName();
            final String ders_ogr_id = obj.getString("sinif_ip");
            //int i_ders_ogr_id = Integer.parseInt(ders_ogr_id);
            //int i_ogr_id = Integer.parseInt(derslik_adi);

            if (ders_ogr_id.equals(derslik_adi)) {
                derslik_id = obj.getString("sinif_id");
                ct.setTitle(obj.getString("sinif_adi"));
            }
        }

    }


    private void getJSON_ogr(final String urlWebService) {

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
                    loadIntoListView_ogr(s);
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

    private void loadIntoListView_ogr(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        int deger = jsonArray.length();

        for (int i = 0; i < deger; i++) {
            JSONObject obj = jsonArray.getJSONObject(i);


            //final String ogr_id = Login.prefConfig.readName();
            final String ders_ogrenci = obj.getString("ogrenci");
            final String ogr_id = Login.prefConfig.readName();
            //int i_ders_ogr_id = Integer.parseInt(ders_ogr_id);
            //int i_ogr_id = Integer.parseInt(derslik_adi);

            if (ders_ogrenci.equals(ogr_id)) {
                k++;
            }
        }

        ders_id = new String[k];

        for (int i = 0; i < deger; i++) {
            JSONObject obj = jsonArray.getJSONObject(i);


            //final String ogr_id = Login.prefConfig.readName();
            final String ders_ogrenci = obj.getString("ogrenci");
            final String ogr_id = Login.prefConfig.readName();
            //int i_ders_ogr_id = Integer.parseInt(ders_ogr_id);
            //int i_ogr_id = Integer.parseInt(derslik_adi);

            if (ders_ogrenci.equals(ogr_id)) {
                ders_id[m] = obj.getString("ders");
                m++;
            }
        }

    }

}