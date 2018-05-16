package com.nickteck.cus_prawnandcrab.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.nickteck.cus_prawnandcrab.Db.Database;
import com.nickteck.cus_prawnandcrab.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

     int SPLASH_TIME_OUT = 1000;
     Database  database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        database = new Database(getApplicationContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (database.checkTables())
                {
                    Intent i = new Intent(SplashActivity.this, MenuNavigationActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);
                    finish();
                }else
                {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                    finish();
                }



            }
        }, SPLASH_TIME_OUT);
    }



}
