package com.wearos.skatelaps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class MainActivity extends WearableActivity {

    private static final String TAG = "MainActivity";
    SensorManager mSensorManager;
    HRListener hrEventListener;
    LinkedBlockingQueue readingsQueue = new LinkedBlockingQueue();
    private TextView textLaps;
    private TextView textLapTime;
    private TextView textTotalTime;
    private TextView textCompareToPreviousLap;
    private TextView textFastestLapTime;
    private Boolean isFirstRequest = true;
    private String[] laps;

    private static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        final String transponder = bundle.getString("transponder");

        textLaps = (TextView) findViewById(R.id.laps_placeholder);
        textLapTime = (TextView) findViewById(R.id.lap_time_placeholder);
        textTotalTime = (TextView) findViewById(R.id.total_time_placeholder);
        textCompareToPreviousLap = (TextView) findViewById(R.id.lap_time_compare);
        textFastestLapTime = (TextView) findViewById(R.id.fastest_lap_time_placeholder);

        permissionRequest();
        hrEventListener = new HRListener((TextView) findViewById(R.id.hr), (TextView) findViewById(R.id.hr_max), (TextView) findViewById(R.id.hr_avg), readingsQueue);
        getStepCount();

        final String url = "http://vinksite.com/LapsSubs/Cmon.php?uid=" + transponder;
        final RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        String[] result = response.split(";");
                        laps = result[12].split("#");
                        int totalLaps = laps.length;

                        if (totalLaps > Integer.parseInt(textLaps.getText().toString()) || isFirstRequest) {
                            // Wake screen if it's in standby
                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

                            // Get instance of Vibrator from current Context an vibrate 0.5s
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(500);

                            try {
                                float lastLap = Float.parseFloat(laps[totalLaps - 1]);
                                String fastestLap = result[3];
                                textLaps.setText(String.valueOf(totalLaps));
                                textLapTime.setText(String.valueOf(round(lastLap, 3)));
                                textTotalTime.setText(secondsToMinutes(result[4]));
                                textFastestLapTime.setText(String.valueOf(round(Float.parseFloat(fastestLap), 3)));

                                if (laps.length == 1 || lastLap < Float.parseFloat(laps[totalLaps - 2])) {
                                    textCompareToPreviousLap.setText("▼");
                                    textCompareToPreviousLap.setTextColor(Color.GREEN);
                                } else {
                                    textCompareToPreviousLap.setText("▲");
                                    textCompareToPreviousLap.setTextColor(Color.RED);
                                }
                            } catch (Exception e) {
                                finish();
                            }
                            isFirstRequest = false;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textLaps.setText("Server down!");
            }
        });

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                queue.add(stringRequest);
            }
        }, 0, 5000);

        // Configure a gesture detector
        textLaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LapTimes.class);
                Bundle bundle = new Bundle();
                bundle.putStringArray("laps", laps);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        // Enables Always-on
        setAmbientEnabled();
    }

    private String secondsToMinutes(String seconds) {
        float t = Float.valueOf(seconds);
        int hours = (int) (t / 3600);
        int remainder = (int) t - hours * 3600;
        int minutes = remainder / 60;
        remainder = remainder - minutes * 60;
        int s = remainder;

        String m = String.valueOf(minutes);
        seconds = s < 10 ? "0" + s : String.valueOf(s);
        if (minutes < 10) {
            m = "0" + m;
        }
        return hours < 1 ? m + ":" + seconds : hours + ":" + m + ":" + seconds;
    }

    protected void onStop() {
        mSensorManager.unregisterListener(hrEventListener);
        super.onStop();
    }

    private void getStepCount() {
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        Sensor mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mSensorManager.registerListener(hrEventListener, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void permissionRequest() {
        if (checkSelfPermission(Manifest.permission.BODY_SENSORS)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.BODY_SENSORS}, 100);
        } else {
            Log.d(TAG, "ALREADY GRANTED");
        }
    }
}