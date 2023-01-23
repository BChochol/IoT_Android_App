package com.example.iotapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectActivity extends AppCompatActivity {


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String token = intent.getStringExtra("message");

        Button connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send message when button is clicked
                TextView username = (TextView) findViewById(R.id.networkName);
                TextView password = (TextView) findViewById(R.id.networkPassword);


                String networkName = username.getText().toString();
                String networkPassword = password.getText().toString();

                //connectToNetwork("AGHIOT_EXAMPLE", "", "");
                sendMessage("SSID=" + "Kasia" + ",Password=" + "Kasia441984" + ",UserID=" + userID);
            }
//        });

//        connectButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ConnectToWifiTask connectToWifiTask = new ConnectToWifiTask(ConnectActivity.this);
//                connectToWifiTask.execute("AGHIOT_EXAMPLE");
//            }
//        });
//    }

            private void sendMessage(final String message) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Socket socket = new Socket();
                        try {
                            socket.bind(null);
                            socket.connect((new InetSocketAddress("192.168.4.1", 4444)), 500);


                            OutputStream outputStream = socket.getOutputStream();
                            outputStream.write(message.getBytes());
//                            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();
                            InputStream inputStream = socket.getInputStream();
                            byte[] response = new byte[1024];
                            int bytesRead = inputStream.read(response);
                            String responseString = new String(response, 0, bytesRead);
                            //System.out.println(responseString + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                            outputStream.close();
//                            Toast.makeText(getApplicationContext(), responseString, Toast.LENGTH_LONG).show();
                            if (!responseString.equals("")) {
                                Intent intent = new Intent(ConnectActivity.this, TemperatureActivity.class);
                                intent.putExtra("userID", userID);
                                intent.putExtra("deviceID", responseString);
                                intent.putExtra("message", token);
                                startActivity(intent);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }


            private void connectToNetwork(String networkSSID, String networkPass, String networkCapabilities) {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiConfiguration wifiConfig = new WifiConfiguration();
                wifiConfig.SSID = String.format("\"%s\"", networkSSID);

                // Toast.makeText(getApplicationContext(), wifiConfig.SSID+ "", Toast.LENGTH_SHORT).show();
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    Toast.makeText(getApplicationContext(), "Main Thread", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Not Main Thread", Toast.LENGTH_SHORT).show();
                }
                if (!wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(true);
                }

//        if (networkCapabilities.toUpperCase().contains("WEP")) { // WEP Network.
//            Toast.makeText(this, "WEP Network", Toast.LENGTH_SHORT).show();
//
//            wifiConfig.wepKeys[0] = String.format("\"%s\"", networkPass);
//            ;
//            wifiConfig.wepTxKeyIndex = 0;
//            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
//        } else if (networkCapabilities.toUpperCase().contains("WPA")) { // WPA Network
//            Toast.makeText(this, "WPA Network", Toast.LENGTH_SHORT).show();
//            wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);
//            ;
//        } else { // OPEN Network.
                //Toast.makeText(this, "OPEN Network", Toast.LENGTH_SHORT).show();
                wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//        }

                int netId = wifiManager.addNetwork(wifiConfig);//

                wifiManager.disconnect();
                wifiManager.enableNetwork(netId, true);
                System.out.println(wifiConfig);
                wifiManager.reconnect();
            }
        });

//        class ConnectToWifiTask extends AsyncTask<String, Void, Boolean> {
//            private Context context;
//
//            public ConnectToWifiTask(Context context) {
//                this.context = context;
//            }
//
//            @Override
//            protected Boolean doInBackground(String... params) {
//                WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                if (wifiManager.isWifiEnabled()) {
//                    wifiManager.setWifiEnabled(false);
//                }
//
//                WifiConfiguration wifiConfig = new WifiConfiguration();
//                wifiConfig.SSID = String.format("\"%s\"", "AGHIOT_EXAMPLE");
//                ;
//                wifiConfig.status = WifiConfiguration.Status.DISABLED;
//                wifiConfig.priority = 40;
//                wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//                wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
//                wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
//                wifiConfig.allowedAuthAlgorithms.clear();
//                wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
//                wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//                wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
//                wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
//                wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
//                wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
//
//                int netId = wifiManager.addNetwork(wifiConfig);
//                System.out.println("THE NETWORK ID IS" + netId + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//                System.out.println(wifiConfig);
//                wifiManager.disconnect();
//                wifiManager.enableNetwork(netId, true);
//                wifiManager.reconnect();
//                return wifiManager.reconnect();
//            }
//
//            @Override
//            protected void onPostExecute(Boolean result) {
//                // Handle the result on the UI thread
//            }
//        }
    }
}