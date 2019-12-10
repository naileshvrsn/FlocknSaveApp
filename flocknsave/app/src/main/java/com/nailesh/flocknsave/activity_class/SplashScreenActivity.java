package com.nailesh.flocknsave.activity_class;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.nailesh.flocknsave.R;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(SplashScreenActivity.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(2000)
                .withFooterText("Copyright 2019")
                .withBackgroundResource(R.drawable.background)
                .withLogo(R.drawable.logo_flocknsave);

        config.getLogo().setMaxWidth(1200);
        config.getFooterTextView().setTextSize(20);
        View view = config.create();
        setContentView(view);
    }
}
