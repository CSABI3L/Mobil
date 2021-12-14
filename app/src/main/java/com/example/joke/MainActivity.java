package com.example.joke;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean isAccelerometerSensorAviable;
    private Vibrator vibrator;
    public Boolean shake=false;
    private ImageButton bellbtn;
    private ImageButton drumbtn;
    private ImageButton fartbtn;
    private ImageButton goatbtn;
    private boolean bell = false;
    private boolean drum = false;
    private boolean fart = false;
    private boolean goat = false;


    // variables for shake detection
    private static final float SHAKE_THRESHOLD = 10f; // m/S**2;3.25f
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 1000;
    private long mLastShakeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        bellbtn = findViewById(R.id.imageButtonBell);
        drumbtn = findViewById(R.id.imageButtonDrum);
        fartbtn = findViewById(R.id.imageButtonFart);
        goatbtn = findViewById(R.id.imageButtonGoat);
        bellbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Bell sound is selected", Toast.LENGTH_LONG).show();
                bell = true; fart = false; drum = false; goat = false;
            }
        });
        drumbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Drum sound is selected", Toast.LENGTH_LONG).show();
                drum = true; fart = false; bell = false; goat = false;
            }
        });
        fartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Fart sound is selected", Toast.LENGTH_LONG).show();
                fart = true; bell = false; drum = false; goat = false;
            }
        });
        goatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Goat sound is selected", Toast.LENGTH_LONG).show();
                goat = true; fart = false; bell = false; drum = false;
            }
        });

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
        {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelerometerSensorAviable = true;
        }
        else {
            isAccelerometerSensorAviable = false;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                double acceleration = Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;
                shake=false;
                if (acceleration > SHAKE_THRESHOLD) {
                    mLastShakeTime = curTime;
                    shake=true;
                    vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
                    if (drum) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.joke_drum);
                        mediaPlayer.start();
                    }
                    else if (bell) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.bell_sound);
                        mediaPlayer.start();
                    }
                    else if (fart) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.fart_sound);
                        mediaPlayer.start();
                    }
                    else if (goat) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.goat_sound);
                        mediaPlayer.start();
                    }

                }
                    //btn.callOnClick();
            }
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (isAccelerometerSensorAviable)
            sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isAccelerometerSensorAviable)
            sensorManager.unregisterListener(this);
    }

}