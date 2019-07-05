package com.furkandemir.onlineyoklama;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;

import com.furkandemir.onlineyoklama.BLE.BTLE_Device;
import com.furkandemir.onlineyoklama.BLE.BroadcastReceiver_BTState;
import com.furkandemir.onlineyoklama.BLE.ListAdapter_BTLE_Devices;
import com.furkandemir.onlineyoklama.BLE.Scanner_BTLE;
import com.furkandemir.onlineyoklama.BLE.Utils;
import com.furkandemir.onlineyoklama.Ogrenci.Login;

import java.util.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private final static String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_ENABLE_BT = 1;

    private HashMap<String, BTLE_Device> mBTDevicesHashMap;
    private ArrayList<BTLE_Device> mBTDevicesArrayList;
    private ListAdapter_BTLE_Devices adapter;

    private Button btn_Scan;

    private BroadcastReceiver_BTState mBTStateUpdateReceiver;
    private Scanner_BTLE mBTLeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Utils.toast(getApplicationContext(), "BLE desteklenmiyor");
            finish();
        }

        mBTStateUpdateReceiver = new BroadcastReceiver_BTState(getApplicationContext());
        mBTLeScanner = new Scanner_BTLE(this, 7500, -75);

        mBTDevicesHashMap = new HashMap<>();
        mBTDevicesArrayList = new ArrayList<>();

        adapter = new ListAdapter_BTLE_Devices(this, R.layout.btle_device_list_item, mBTDevicesArrayList);


        ListView listView = new ListView(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //ArrayList<BTLE_Device> devices;
                //devices = new ArrayList<>();
                ////TextView tv_name = (TextView) findViewById(R.id.tv_name);
                //BTLE_Device device = mBTDevicesArrayList.get(position);
                //String name = device.getName();
                Intent i = new Intent(MainActivity.this,Login.class);
                BTLE_Device device = mBTDevicesArrayList.get(position);
                String address = device.getAddress();
                i.putExtra("derslikadi", address);
                startActivity(i);
            }
        });
        ((ScrollView) findViewById(R.id.scrollView)).addView(listView);

        btn_Scan = (Button) findViewById(R.id.btn_scan);
        findViewById(R.id.btn_scan).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(mBTStateUpdateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopScan();
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(mBTStateUpdateReceiver);
        stopScan();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_ENABLE_BT) {
            if (requestCode == RESULT_OK) {

            } else if (requestCode == RESULT_CANCELED) {
                Utils.toast(getApplicationContext(), "Lütfen Bluetooth'u açın");
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                Utils.toast(getApplicationContext(), "Arama düğmesine basıldı");

                if(!mBTLeScanner.isScanning()){
                    startScan();
                }
                else {
                    stopScan();
                }

                break;

            default:
                break;
        }
    }

    public void addDevice(BluetoothDevice device, int new_rssi){

        String address = device.getAddress();

        if(!mBTDevicesHashMap.containsKey(address)){
            BTLE_Device btle_device = new BTLE_Device(device);
            btle_device.setRSSI(new_rssi);

            mBTDevicesHashMap.put(address, btle_device);
            mBTDevicesArrayList.add(btle_device);
        }
        else {
            mBTDevicesHashMap.get(address).setRSSI(new_rssi);
        }

        adapter.notifyDataSetChanged();
    }

    public void startScan(){
        btn_Scan.setText("Aranıyor...");

        mBTDevicesArrayList.clear();
        mBTDevicesHashMap.clear();

        adapter.notifyDataSetChanged();

        mBTLeScanner.start();
    }

    public void stopScan(){
        btn_Scan.setText("Tekrar ara");

        mBTLeScanner.stop();
    }


}