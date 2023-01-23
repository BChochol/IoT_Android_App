package com.example.iotapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.HttpResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.loginButton);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView username = (TextView) findViewById(R.id.username);
                TextView password = (TextView) findViewById(R.id.password);
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();

                JSONArray jsonArray = new JSONArray();
                jsonArray.put("user");

                JSONObject postData = new JSONObject();
                try {
                    postData.put("username", usernameText);
                    postData.put("password", passwordText);
                    try{
                        URL url = new URL("http://54.194.132.27:8080/api/auth/signin");

                        HttpURLConnection conn2 = (HttpURLConnection) url.openConnection();
                        conn2.setDoOutput(true);
                        conn2.setRequestMethod("POST");
                        conn2.setRequestProperty("Content-Type", "application/json");

                        OutputStreamWriter out = new OutputStreamWriter(conn2.getOutputStream());
                        out.write(postData.toString());
                        out.close();


                        if(conn2.getResponseCode() == 200){
                            Intent intent = new Intent(LoginActivity.this, ConnectActivity.class);
                            intent.putExtra("userID", usernameText);
                            intent.putExtra("message", conn2.getResponseMessage());
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Incorrect login", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}