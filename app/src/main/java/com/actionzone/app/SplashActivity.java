package com.actionzone.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        // Handler permet d'exécuter du code après un délai
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent = intention de navigation entre deux écrans
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                // finish() ferme SplashActivity pour qu'on ne puisse pas y revenir
                finish();
            }
        }, 2000); // 2000 millisecondes = 2 secondes
    }
}