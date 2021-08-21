package com.wearos.skatelaps;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LapTimes extends AppCompatActivity {

    private TextView textLaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lap_times);

        Bundle bundle = getIntent().getExtras();
        final String[] laps = bundle.getStringArray("laps");
        String allLaps = "";
        for (int i = 0; i < laps.length; i++) {
            String space = i < 10 ? "          " : "            ";
            allLaps += i + 1 + space + laps[i] + "\n";
        }
        allLaps += "\n";
        textLaps = (TextView) findViewById(R.id.scrollLaps);
        textLaps.setText(allLaps);
    }
}