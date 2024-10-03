package com.example.salesexpress.views.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.salesexpress.utils.CustomMenu;
import com.example.salesexpress.utils.MyDatabaseHelper;
import com.example.salesexpress.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    MyDatabaseHelper myDB;
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDB = new MyDatabaseHelper(this);
        criarBancoDeDados();
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());
        hideNavigationBar();
        CustomMenu customMenu = new CustomMenu(this);
        View rootView = findViewById(android.R.id.content);
        View screenView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        screenView.setSystemUiVisibility(uiOptions);

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("fragmentNumber", 1);
                startActivity(intent);
            }
        });

        binding.btnReprocessed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("fragmentNumber", 2);
                startActivity(intent);
            }
        });

        binding.btnImageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customMenu.showPopup(rootView);
            }
        });

    }

    private void hideNavigationBar() {
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        decorView.setSystemUiVisibility(uiOptions);
    }

    public void criarBancoDeDados() {
        SQLiteDatabase db = myDB.getWritableDatabase();
        myDB.onCreate(db);
        db.close();
    }
}