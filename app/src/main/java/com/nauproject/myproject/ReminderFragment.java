package com.nauproject.myproject;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ReminderFragment extends Fragment {

    private RecyclerView rvRemind;
    private ArrayList<item_reminder> alarms;
    private reminderAdapter reminderAdapter;

    public ReminderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reminder, container, false);

        rvRemind = v.findViewById(R.id.rvRemind);

        alarms = new ArrayList<>();
        reminderAdapter = new reminderAdapter(getContext(), alarms);
        rvRemind.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRemind.setAdapter(reminderAdapter);

        String url = "http://172.16.153.225/myAlarm/alarm.php";
        new FetchDataTask().execute(url);

        return v;
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
                    Toast.makeText(getContext(), "Parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}