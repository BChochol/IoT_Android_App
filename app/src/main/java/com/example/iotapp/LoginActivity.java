package com.example.iotapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.core.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);

        Button loginButton = (Button) findViewById(R.id.loginButton);

        String url="http://dummyinfo";


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();
//
//                if (usernameText.equals("admin") && passwordText.equals("admin")) {
//                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(LoginActivity.this, TemperatureActivity.class);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
//                }
                if (usernameText.equals("admin") && passwordText.equals("admin")) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, TemperatureActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
//                JSONObject data = new JSONObject();
//                try {
//                    data.put("username", usernameText);
//                    data.put("password", passwordText);
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
//                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//}