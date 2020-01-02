package com.mpscexams.bhajaneapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();//hides the current activity action bar
        setContentView(R.layout.activity_splash_screen);

        //back press event from MainActivity should close the application
        if (getIntent().getBooleanExtra("EXIT", false))
        {
            finish();//inbuilt method to perform back pressed operation

        }else{
            //Its a delayed task to show app for 2000 time and then intent to mainActivity
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                }
            },2000);
        }



    }
}
