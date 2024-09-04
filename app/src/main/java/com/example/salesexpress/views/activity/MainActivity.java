package com.example.salesexpress.views.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.salesexpress.R;
import com.example.salesexpress.databinding.ActivityMainBinding;
import com.example.salesexpress.utils.CustomMenu;
import com.example.salesexpress.utils.MyDatabaseHelper;
import com.example.salesexpress.views.fragment.ConfirmFragment;
import com.example.salesexpress.views.fragment.RegisteredSalesFragment;
import com.example.salesexpress.views.fragment.ReprocessingFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MyDatabaseHelper myDB;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        myDB = new MyDatabaseHelper(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());
        hideNavigationBar();
        CustomMenu customMenu = new CustomMenu(this);
        View rootView = findViewById(android.R.id.content);
        View screenView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        screenView.setSystemUiVisibility(uiOptions);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        binding.mainLayout.setOnTouchListener((view, motionEvent) -> {
            hideKeyboard(view);
            return false;
        });


        Intent intent = getIntent();
        int fragmentNumber = intent.getIntExtra("fragmentNumber", 1);

        if (savedInstanceState == null) {
            switch (fragmentNumber) {
                case 1:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new RegisteredSalesFragment()).commit();
                    break;
                case 2:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ReprocessingFragment()).commit();
                    break;
                case 3:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ConfirmFragment()).commit();
                    break;
                default:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new RegisteredSalesFragment()).commit();
                    break;
            }
        }

        binding.imgArrowBack.setOnClickListener(view -> {
            Intent intent1 = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent1);
        });

        binding.imgDrawBtn.setOnClickListener(view -> customMenu.showPopup(rootView));


    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void hideNavigationBar() {
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(uiOptions);
    }

}

