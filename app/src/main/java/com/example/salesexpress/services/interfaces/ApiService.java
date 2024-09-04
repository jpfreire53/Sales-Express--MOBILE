package com.example.salesexpress.services.interfaces;

import com.example.salesexpress.model.SalesRequestModel;
import com.example.salesexpress.model.UserModel;
import com.example.salesexpress.services.EmailResponse;
import com.example.salesexpress.services.LoginResponse;
import com.example.salesexpress.services.RegisterSalesResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {


    @POST("/login")
    Call<LoginResponse> loginUsuario(@Body UserModel userModel);
    
    @POST("/registersales")
    Call<RegisterSalesResponse> registrarVenda(@Body SalesRequestModel salesRequestModel);

    @POST("/sendemail/{id}")
    Call<EmailResponse> enviarEmailVenda(@Path("id") String saleId);


}
