package com.nauproject.myproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView remindRv;
    private ArrayList<item_reminder> alarms;
    private reminderAdapter reminderAdapter;
    private Button btTampil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton buttonCustomReminder = findViewById(R.id.btAdd);
        buttonCustomReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomReminderActivity.class);
                startActivity(intent);
            }
        });

        remindRv = findViewById(R.id.remindRV);
        btTampil = findViewById(R.id.btTampil);
        btTampil.setOnClickListener(this);

        alarms = new ArrayList<>();
        reminderAdapter = new reminderAdapter(this, alarms);
        remindRv.setLayoutManager(new LinearLayoutManager(this));
        remindRv.setAdapter(reminderAdapter);
    }

    @Override
    public void onClick(View v) {
        String url = "http://172.16.153.225/myAlarm/alarm.php";
        new FetchDataTask().execute(url);
    }

    private class FetchDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                try {
                    Gson gson = new Gson();
                    item_reminder[] alarmArray = gson.fromJson(response, item_reminder[].class);
                    alarms.clear();
                    for (item_reminder b : alarmArray) {
                        alarms.add(b);
                    }
                    reminderAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
