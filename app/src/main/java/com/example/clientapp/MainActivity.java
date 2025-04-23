package com.example.clientapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public final static String HOST_NAME = "com.example.paragraphclient.HOSTNAME";
    public final static String PORT = "com.example.paragraphclient.PORT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void connectServer(View view) {
        EditText etHost = findViewById(R.id.edit_host);
        EditText etPort = findViewById(R.id.edit_port);

        String hostname = etHost.getText().toString();
        int port = Integer.parseInt(etPort.getText().toString());

        Intent intent = new Intent(this, CommunicateActivity.class);
        intent.putExtra(HOST_NAME, hostname);
        intent.putExtra(PORT, port);
        startActivity(intent);
    }
}
