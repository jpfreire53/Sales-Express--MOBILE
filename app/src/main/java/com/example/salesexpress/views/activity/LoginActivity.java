package com.example.salesexpress.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salesexpress.databinding.ActivityLoginBinding;
import com.example.salesexpress.model.UserModel;
import com.example.salesexpress.services.LoginResponse;
import com.example.salesexpress.services.RetrofitClient;
import com.example.salesexpress.services.SharedPreferencesHelper;
import com.example.salesexpress.services.interfaces.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ApiService apiService;
    ActivityLoginBinding binding;
    UserModel userModel;
    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        sharedPreferencesHelper = new SharedPreferencesHelper(LoginActivity.this);
        setContentView(binding.getRoot());
        View screenView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        screenView.setSystemUiVisibility(uiOptions);

        apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        if(sharedPreferencesHelper.isUserLogged()){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }

        binding.btnLogin.setOnClickListener(view -> realizarLogin());

    }

    private void realizarLogin() {
        Log.d("LoginActivity", "Antes da criação da solicitação Retrofit");
        String user = binding.edtUser.getText().toString();
        String password = binding.edtPassword.getText().toString();

        userModel = new UserModel();
        userModel.setUser(user);
        userModel.setPassword(password);

        Call<LoginResponse> call = apiService.loginUsuario(userModel);
        Log.d("LoginActivity", "Antes da criação da solicitação Retrofit2");
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                Log.d("LoginActivity", "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    sharedPreferencesHelper.salvarUsuario(userModel.getUser(), userModel.getPassword());
                    Toast.makeText(LoginActivity.this, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Usuário ou senha inválidos!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Log.e("LoginActivity", "onFailure: " + t.getMessage(), t);
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

