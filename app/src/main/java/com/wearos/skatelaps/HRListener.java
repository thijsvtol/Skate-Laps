package com.wearos.skatelaps;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.LinkedBlockingQueue;

public class HRListener implements SensorEventListener {

    private static final String TAG = "HRReadingsActivity";
    private TextView mTextHR;
    private TextView mTextHRMax;
    private Integer maxHR = 0;
    private LinkedBlockingQueue readingsQueue;

    public HRListener(TextView textHR, TextView textHRMax, LinkedBlockingQueue queue) {
        mTextHR = textHR;
        mTextHRMax = textHRMax;
        readingsQueue = queue;
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            int hr_value = (int)event.values[0];
            readingsQueue.offer(hr_value);
            if (hr_value > maxHR) {
                maxHR = hr_value;
                mTextHRMax.setText("max. " + maxHR);
            }
            mTextHR.setText(hr_value + "♥");
        }
        else {
            Log.d(TAG, "Unknown sensor type");
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged - accuracy: " + accuracy);
    }
}
