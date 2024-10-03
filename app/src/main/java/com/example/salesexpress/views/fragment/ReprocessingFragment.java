package com.example.salesexpress.views.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesexpress.databinding.FragmentReprocessingBinding;
import com.example.salesexpress.model.ItemModel;
import com.example.salesexpress.model.SalesModel;
import com.example.salesexpress.model.SalesRequestModel;
import com.example.salesexpress.services.CustomAdapterReprocessed;
import com.example.salesexpress.services.RegisterSalesResponse;
import com.example.salesexpress.services.RetrofitClient;
import com.example.salesexpress.services.interfaces.ApiService;
import com.example.salesexpress.utils.MyDatabaseHelper;
import com.example.salesexpress.views.fragment.popup.BlurPopUpReprocessedError;
import com.example.salesexpress.views.fragment.popup.BlurPopUpReprocessedSuccess;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReprocessingFragment extends Fragment {

    FragmentReprocessingBinding binding;
    private RecyclerView recyclerView;
    private MyDatabaseHelper myDB;
    private CustomAdapterReprocessed adapter;
    private List<SalesModel> errorSalesList;
    ApiService apiService;
    public ReprocessingFragment() {}

    public static ReprocessingFragment newInstance(String param1, String param2) {
        ReprocessingFragment fragment = new ReprocessingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReprocessingBinding.inflate(inflater, container, false);
        recyclerView = binding.recyclerRepro;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myDB = new MyDatabaseHelper(getContext());
        errorSalesList = myDB.getSales();

        adapter = new CustomAdapterReprocessed(getContext(), getActivity(), errorSalesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);



        binding.btnIrParaReprocessar.setOnClickListener(v -> {
            try {
                for (int i = 0; i < errorSalesList.size(); i++) {
                    apiService = RetrofitClient.getRetrofitInstance(this.getActivity()).create(ApiService.class);
                    SalesModel errorSalesModel = errorSalesList.get(i);
                    ArrayList<ItemModel> errorItemsModels = myDB.getLastItems(errorSalesModel.getId());
                    SalesRequestModel salesRequestModel = new SalesRequestModel(errorSalesModel, errorItemsModels);
                    Call<RegisterSalesResponse> call = apiService.registrarVenda(salesRequestModel);
                    call.enqueue(new Callback<RegisterSalesResponse>() {
                        @Override
                        public void onResponse(Call<RegisterSalesResponse> call, Response<RegisterSalesResponse> response) {
                            if (response.isSuccessful()) {
                                RegisterSalesResponse reprocessedResponse = response.body();
                                BlurPopUpReprocessedSuccess dialog = new BlurPopUpReprocessedSuccess();
                                errorSalesModel.setReprocessed(true);
                                dialog.show(getParentFragmentManager(), dialog.getClass().getSimpleName());
                                myDB.deleteLastSaleAndItems();
                                adapter.notifyData();
                                new android.os.Handler().postDelayed(() -> dialog.dismiss(), 3500);
                                Log.d("API_RESPONSE", "onResponse: " + reprocessedResponse.getMessage());

                            } else {
                                BlurPopUpReprocessedError dialog = new BlurPopUpReprocessedError();
                                dialog.show(getParentFragmentManager(), dialog.getClass().getSimpleName());
                                errorSalesModel.setReprocessed(false);
                                Log.e("API_RESPONSE", "onResponse: Erro na API");
                                new android.os.Handler().postDelayed(() -> dialog.dismiss(), 3500);
                            }
                        }

                        @Override
                        public void onFailure(Call<RegisterSalesResponse> call, Throwable t) {
                            Log.e("API_RESPONSE", "onFailure: Falha na chamada à API", t);
                            BlurPopUpReprocessedError dialog = new BlurPopUpReprocessedError();
                            new android.os.Handler().postDelayed(() -> dialog.dismiss(), 3500);
                        }
                    });
                }


            } catch (Exception e) {
                Log.i("erroRepro", ""+e.getMessage());
            }
        });

    }


}