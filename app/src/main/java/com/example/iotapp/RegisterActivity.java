package com.example.iotapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);
        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();
        Button registerButton = (Button) findViewById(R.id.registerButton);

        URL url = null;
        try {
            url = new URL("http://54.194.132.27:8080/api/auth/signup");
        } catch (MalformedURLException e) {
            Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }

        HttpURLConnection con;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }

        try {
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
            Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                String jsonInputString = "{\"name\": \"" + usernameText + ", \"password\": \"" + passwordText +
                        ", \"email\": \"email@gmail.com\", \"roles\": [\"user\"]}";
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                } catch (IOException e) {
                    Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                } catch (IOException e) {
                    Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
//                JSONObject data = new JSONObject();
//                try {
//                    data.put("username", usernameText);
//                    data.put("password", passwordText);
//                    data.put("email", "email");
//                    //put a table of roles using data.put containing one record called 'user'
//                    data.put("roles", "user");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    // create a client
//                    Client client = ClientBuilder.newClient();
//
//                    // create a request object
//                    Entity<JSONObject> jsonEntity = Entity.json(data);
//
//                    Response response = client.target(url)
//                            .request()
//                            .post(jsonEntity);
//
//                    // get the response from the server
//                    int responseCode = response.getStatus();
//                    String responseMessage = response.getStatusInfo().getReasonPhrase();
//                    String responseBody = response.readEntity(String.class);
//
//                    // check if the response is successful
//                    if (responseCode == 200) {
//                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (Exception e) {
//                    Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
            }
        });
    }
}