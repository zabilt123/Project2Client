package com.example.clientapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CommunicateActivity extends AppCompatActivity {

    private Socket socket;
    private PrintWriter out;
    private Scanner in;
    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        resultView = findViewById(R.id.text_result);

        Intent intent = getIntent();
        String hostname = intent.getStringExtra(MainActivity.HOST_NAME);
        int port = intent.getIntExtra(MainActivity.PORT, 4000);

        try {
            socket = new Socket(hostname, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new Scanner(new InputStreamReader(socket.getInputStream()));
            resultView.setText("Connected to server.");
        } catch (Exception e) {
            resultView.setText("Connection error: " + e.getMessage());
        }
    }

    public void sendRequest(View view) {
        if (socket == null) {
            resultView.setText("Not connected.");
            return;
        }

        EditText opInput = findViewById(R.id.edit_operation);
        EditText fileInput = findViewById(R.id.edit_filename);

        String operation = opInput.getText().toString().trim();
        String fileName = fileInput.getText().toString().trim();

        if (operation.isEmpty() || fileName.isEmpty()) {
            resultView.setText("Please fill in both fields.");
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(fileName)));
            int lineCount = 0;
            while (reader.readLine() != null) {
                lineCount++;
            }
            reader.close();

            // Send operation and paragraph to server
            out.println(operation);
            out.println(lineCount);

            reader = new BufferedReader(new InputStreamReader(getAssets().open(fileName)));
            String line;
            while ((line = reader.readLine()) != null) {
                out.println(line);
            }

            // Read response
            if (in.hasNextLine()) {
                int responseLines = Integer.parseInt(in.nextLine());
                StringBuilder response = new StringBuilder();
                for (int i = 0; i < responseLines && in.hasNextLine(); i++) {
                    response.append(in.nextLine()).append("\n");
                }
                resultView.setText(response.toString().trim());
            } else {
                resultView.setText("No response from server.");
            }

            // Disconnect after one request
            socket.close();
            socket = null;
            in.close();
            out.close();

        } catch (Exception e) {
            resultView.setText("Error: " + e.getMessage());
        }
    }
}
