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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class TemperatureActivity extends AppCompatActivity {

    Button btn_refresh;
    TextView et_temperature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        btn_refresh = findViewById(R.id.refreshButton);
        et_temperature = findViewById(R.id.temperature);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String deviceID = intent.getStringExtra("deviceID");
        String token = intent.getStringExtra("message");
        System.out.println(token + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        RequestQueue queue = Volley.newRequestQueue(TemperatureActivity.this);
        String url = "http://54.194.132.27:8080/api/devices";
        sendData(userID, deviceID, token);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String temp = "";
                        try {
                            JSONObject info = response.getJSONObject(response.length()-1);
                            temp = info.getString("temp");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Toast.makeText(TemperatureActivity.this, temp, Toast.LENGTH_SHORT).show();
                        et_temperature.setText(temp);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });

// Add the request to the RequestQueue.
        queue.add(request);

        btn_refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                queue.add(request);
            }
        });
    }

    public void sendData(String userID, String deviceID, String token){
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
                conn2.setRequestProperty("Authentication", "Bearer " + token);
                OutputStreamWriter out = new OutputStreamWriter(conn2.getOutputStream());
                System.out.println(postData.toString());
                out.write(postData.toString());
                out.close();

                if(conn2.getResponseCode() == 200){
                    Toast.makeText(TemperatureActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    System.out.println("SuccessSuccessSuccessSuccessSuccessSuccessSuccessSuccessSuccessSuccessSuccessSuccessSuccessSuccessSuccess");
                } else {
                    Toast.makeText(TemperatureActivity.this, "Incorrect login", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Toast.makeText(TemperatureActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}