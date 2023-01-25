package com.example.iotapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

public class ConnectActivity extends AppCompatActivity {
    private static final int RC_LOCATION_PERM = 1;
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    String deviceID = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String token = intent.getStringExtra("message");
        deviceID = intent.getStringExtra("deviceID");

        requestLocationPermission();

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceID == null) {
                    Toast.makeText(ConnectActivity.this, "Please pair with the device first", Toast.LENGTH_SHORT).show();
                } else {
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put("user");

                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("deviceId", deviceID);
                        postData.put("username", userID);
                        try{
                            URL url = new URL("http://54.194.132.27:8080/api/devices");

                            HttpURLConnection conn2 = (HttpURLConnection) url.openConnection();
                            conn2.setDoOutput(true);
                            conn2.setRequestMethod("POST");
                            conn2.setRequestProperty("Content-Type", "application/json");
                            conn2.setRequestProperty("Authorization", "Bearer " + token);
                            OutputStreamWriter out = new OutputStreamWriter(conn2.getOutputStream());
                            System.out.println(token);
                            System.out.println(postData.toString());
                            out.write(postData.toString());
                            out.close();

                            if(conn2.getResponseCode() == 200){
                                Toast.makeText(ConnectActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                System.out.println("SuccessSuccessSuccessSuccessSuccessSuccessSuccessSuccessSuccessSuccessSuccessSuccessSuccessSuccessSuccess");

                            } else {
                            }

                        } catch (Exception e) {
                            Toast.makeText(ConnectActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Button tempButton = findViewById(R.id.tempButton);
        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectActivity.this, TemperatureActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("message", token);
                intent.putExtra("deviceID", deviceID);
                startActivity(intent);
            }
        });

        Button connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView username = (TextView) findViewById(R.id.networkName);
                TextView password = (TextView) findViewById(R.id.networkPassword);
                String networkName = username.getText().toString();
                String networkPassword = password.getText().toString();

                sendMessage(networkName, networkPassword, userID);
            }

            private void sendMessage(String networkName, String networkPassword, String userID) {
                String message = "SSID=" + networkName + ",Password=" + networkPassword + ",UserID=" + userID;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Socket socket = new Socket();
                        try {
                            socket.bind(null);
                            socket.connect((new InetSocketAddress("192.168.4.1", 4444)), 500);


                            OutputStream outputStream = socket.getOutputStream();
                            outputStream.write(message.getBytes());
                            InputStream inputStream = socket.getInputStream();
                            byte[] response = new byte[1024];
                            int bytesRead = inputStream.read(response);
                            String responseString = new String(response, 0, bytesRead);
                            outputStream.close();
                            if (!responseString.equals("")) {
                                //Toast.makeText(ConnectActivity.this, "Succesfully sent", Toast.LENGTH_SHORT).show();
                                deviceID = responseString;
                            }

                            ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
                                @Override
                                public void onAvailable(Network network) {
                                    ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                                    connectivityManager.bindProcessToNetwork(network);
                                }
                            };

                            if (android.os.Build.VERSION.SDK_INT >= 29) {
                                System.out.println("Detected SDK: >=29");

                                WifiNetworkSpecifier.Builder specifierBuilder = new WifiNetworkSpecifier.Builder();
                                specifierBuilder.setSsid(networkName);
                                specifierBuilder.setWpa2Passphrase(networkPassword);
                                WifiNetworkSpecifier specifier = specifierBuilder.build();

                                NetworkRequest.Builder requestBuilder = new NetworkRequest.Builder();
                                requestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
                                requestBuilder.setNetworkSpecifier(specifier);
                                NetworkRequest request = requestBuilder.build();

                                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                connectivityManager.requestNetwork(request, mNetworkCallback);
                                connectivityManager.bindProcessToNetwork(null);
                                connectivityManager.unregisterNetworkCallback(mNetworkCallback);
                                System.out.println("Completed test");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        Button pairButton = findViewById(R.id.pairButton);
        pairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!Settings.System.canWrite(ConnectActivity.this)) {
                    Intent intent2 = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent2.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent2);
                }

                ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        connectivityManager.bindProcessToNetwork(network);
                    }
                };

                if (android.os.Build.VERSION.SDK_INT >= 29) {
                    System.out.println("Detected SDK: >=29");

                    WifiNetworkSpecifier.Builder specifierBuilder = new WifiNetworkSpecifier.Builder();
                    specifierBuilder.setSsid("AGHIOT_EXAMPLE");
                    //specifierBuilder.setWpa2Passphrase("Kasia441984");
                    WifiNetworkSpecifier specifier = specifierBuilder.build();

                    NetworkRequest.Builder requestBuilder = new NetworkRequest.Builder();
                    requestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
                    requestBuilder.setNetworkSpecifier(specifier);
                    NetworkRequest request = requestBuilder.build();

                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    connectivityManager.requestNetwork(request, mNetworkCallback);
                    System.out.println("Completed test");
                }
            }
        });
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.CHANGE_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ConnectActivity.this, LOCATION_PERMS, RC_LOCATION_PERM);

            System.out.println("Permission grantedPermission grantedPermission grantedPermission grantedPermission grantedPermission granted");
        }
    }
}