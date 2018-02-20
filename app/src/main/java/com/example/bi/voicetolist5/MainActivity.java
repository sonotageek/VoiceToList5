package com.example.bi.voicetolist5;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


//Version 5- VoiceToList5


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setIcon(R.drawable.mic8); //not working

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setItemBackgroundResource(Color.blue(00000));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, new ViewFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()){

                    case R.id.action_recNote:
                        fragmentTransaction.replace(R.id.frameLayout, new MicFragment()).commit();
                        return true;
                    case R.id.action_addNote:
                        fragmentTransaction.replace(R.id.frameLayout, new AddFragment()).commit();
                        return true;
                    case R.id.action_viewNote:
                        fragmentTransaction.replace(R.id.frameLayout, new ViewFragment()).commit();
                        return true;
                    default:
                        return true;
                }
                //return false;
            }
        });

    }



}


//android:windowSoftInputMode="stateVisible|adjustPan"> stateVisible cause keybpard to show on load

//to add icon
//getSupportActionBar().setIcon(R.drawable.icon);
//android:logo="@drawable/icon" //add in manifest



//https://developer.android.com/training/system-ui/navigation.html ?
//this makes screen full . This has to be implemented
//   View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
//  int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//        | View.SYSTEM_UI_FLAG_FULLSCREEN;
//decorView.setSystemUiVisibility(uiOptions);