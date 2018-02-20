package com.example.bi.voicetolist5;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;


//https://stackoverflow.com/questions/31170217/splachscreen-in-fragment
public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT =500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);

                //close activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
