package com.example.iotapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TemperatureActivity extends AppCompatActivity {

    Button btn_refresh;
    TextView et_temperature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        btn_refresh = findViewById(R.id.refreshButton);
        et_temperature = findViewById(R.id.temperature);

        RequestQueue queue = Volley.newRequestQueue(TemperatureActivity.this);
        String url = "http://54.194.132.27:8080/api/v1/records";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string.
                        //et_temperature.setText("Response is: " + response.substring(0,500));
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
                et_temperature.setText(error.toString());
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
}