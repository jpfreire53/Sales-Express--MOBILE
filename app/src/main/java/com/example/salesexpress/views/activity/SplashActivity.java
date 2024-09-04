package com.example.salesexpress.views.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.salesexpress.R;
import com.example.salesexpress.services.SharedPreferencesHelper;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideNavigationBar();
        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        TextView versionText = findViewById(R.id.versionTxT);
        versionText.setText("2.5");

        new Handler().postDelayed(() -> {
            if (sharedPreferencesHelper.isUserLogged()) {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            finish();
        }, 3000);
    }

    private void hideNavigationBar() {
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        decorView.setSystemUiVisibility(uiOptions);
    }

}

