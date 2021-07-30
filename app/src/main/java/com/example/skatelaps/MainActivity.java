package com.example.skatelaps;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
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
    private TextView textLaps;
    private TextView textLapTime;
    private TextView textTotalTime;
    private TextView textCompareToPreviousLap;
    private TextView textFastestLapTime;
    private Boolean isFirstRequest = true;
    SensorManager mSensorManager;
    HRListener hrEventListener;
    LinkedBlockingQueue readingsQueue = new LinkedBlockingQueue();

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

        hrEventListener = new HRListener((TextView) findViewById(R.id.hr), (TextView) findViewById(R.id.hr_max), readingsQueue);
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
                        String[] laps = result[12].split("#");
                        int totalLaps = laps.length;

                        if(totalLaps > Integer.valueOf(textLaps.getText().toString()) || isFirstRequest) {
                            // Get instance of Vibrator from current Context an vibrate 0.5s
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(500);


                            float lastLap = Float.valueOf(laps[totalLaps - 1]);
                            float secondLastLap = Float.valueOf(laps[totalLaps - 2]);
                            String fastestLap = result[12];
                            textLaps.setText(String.valueOf(totalLaps));
                            textLapTime.setText(String.valueOf(lastLap));
                            textTotalTime.setText(secondsToMinutes(result[4]));
                            textFastestLapTime.setText(String.valueOf(result[3]));

                            if(lastLap < secondLastLap) {
                                textCompareToPreviousLap.setText("▼");
                                textCompareToPreviousLap.setTextColor(Color.GREEN);
                            } else {
                                textCompareToPreviousLap.setText("▲");
                                textCompareToPreviousLap.setTextColor(Color.RED);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    textLaps.setText("Error!");
                }
        });

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                queue.add(stringRequest);
                System.out.println("callie");
            }
        }, 0, 50000);

        // Enables Always-on
        setAmbientEnabled();
    }

    private String secondsToMinutes(String seconds) {
        float t = Float.valueOf(seconds);
        int minutes = (int) (t / 60);
        float s = t % 60;
        if (s < 10) {
            seconds = "0" + seconds;
        }
        return minutes + ":" + round(s, 2);
    }

    private static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    protected void onStop() {
        mSensorManager.unregisterListener(hrEventListener);
        super.onStop();
    }

    private void getStepCount() {
        mSensorManager = ((SensorManager)getSystemService(SENSOR_SERVICE));
        Sensor mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mSensorManager.registerListener(hrEventListener, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

}