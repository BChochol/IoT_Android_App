package com.example.iotapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class TemperatureActivity extends AppCompatActivity {

    Button btn_refresh;
    Button btn_gettemp;
    TextView et_temperature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        btn_gettemp = findViewById(R.id.getTemperatureButton);
        et_temperature = findViewById(R.id.temperature);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String deviceID = intent.getStringExtra("deviceID");
        String token = intent.getStringExtra("message");


        btn_gettemp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getTemperature(token, userID, deviceID);
            }
        });
    }


    public void getTemperature(String token, String userID, String deviceID){
            try {
                URL url = new URL("http://54.194.132.27:8080/api/record/" + userID + "/" + deviceID);

                HttpURLConnection conn3 = (HttpURLConnection) url.openConnection();
                conn3.setDoOutput(false);
                conn3.setRequestMethod("GET");
                conn3.setRequestProperty("Content-Type", "application/json");
                conn3.setRequestProperty("Authorization", "Bearer " + token);
                if (conn3.getResponseCode() == 200) {
                    conn3.getResponseCode();
                    InputStream inputStream = conn3.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    et_temperature.setText(sb);
                } else {
                    System.out.println("Error");
                }

            } catch (Exception e) {
                System.out.println(e.toString());
            }
    }
}