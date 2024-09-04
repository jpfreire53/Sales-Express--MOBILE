package com.example.salesexpress.views.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesexpress.R;
import com.example.salesexpress.databinding.FragmentConfirmBinding;
import com.example.salesexpress.model.ItemModel;
import com.example.salesexpress.model.SalesModel;
import com.example.salesexpress.model.SalesRequestModel;
import com.example.salesexpress.services.CustomAdapter;
import com.example.salesexpress.services.FragmentController;
import com.example.salesexpress.services.MoneyTextWatcher;
import com.example.salesexpress.services.RegisterSalesResponse;
import com.example.salesexpress.services.RetrofitClient;
import com.example.salesexpress.services.SharedPreferencesHelper;
import com.example.salesexpress.services.interfaces.ApiService;
import com.example.salesexpress.utils.MyDatabaseHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmFragment extends Fragment {

    private FragmentController fragmentController;
    private ConstraintLayout containerConfirm;
    CustomAdapter customAdapter;
    MyDatabaseHelper myDB;
    FragmentConfirmBinding binding;
    RecyclerView recyclerView;
    public ConfirmFragment() {}

    public static ConfirmFragment newInstance(String param1, String param2) {
        ConfirmFragment fragment = new ConfirmFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentConfirmBinding.inflate(inflater, containerConfirm, false);
        this.containerConfirm = binding.containerConfirm;
        fragmentController = new FragmentController(getParentFragmentManager(), R.id.frameLayout);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myDB = new MyDatabaseHelper(getActivity());
        recyclerView = view.findViewById(R.id.recycler_view);
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(getActivity());
        Bundle bundle = getArguments();
        Bundle bundle1 = new Bundle();

        SalesModel sale = (SalesModel) bundle.getSerializable("saleModel");
        ArrayList<ItemModel> items = (ArrayList<ItemModel>) bundle.getSerializable("itemModels");

        PaymentReceiptFragment paymentReceiptFragment = new PaymentReceiptFragment();
        FragmentManager fragmentManager = getParentFragmentManager();

        if (sale != null) {
            binding.edtEmail.setText(sale.getEmail());
            binding.edtName.setText(sale.getName());
            binding.edtCpf.setText(sale.getCpf());

            double totalAmount = Double.parseDouble(String.valueOf(sale.getMoneyChange())) + Double.parseDouble(String.valueOf(sale.getValue()));
            String formattedTotalAmount = MoneyTextWatcher.formatCurrencyFromString(Double.parseDouble(String.valueOf(totalAmount)));
            binding.edtValue.setText(formattedTotalAmount);

            binding.edtSale.setText(MoneyTextWatcher.formatCurrencyFromString(sale.getValue()));
            binding.edtMoneyChange.setText(MoneyTextWatcher.formatCurrencyFromString(sale.getMoneyChange())); //troco

            customAdapter = new CustomAdapter(getActivity(), getActivity(), items);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(customAdapter);
        }

        binding.btnIrParaAlterar.setOnClickListener(v -> {
            try {
                fragmentController.popBackStack();
                if (items.size() > 0){
                    items.clear();
                }
            } catch (Exception e) {
                Log.i("erroBack", "onViewCreated: " + e.getMessage());
            }
        });

        binding.btnIrParaFinalizar.setOnClickListener(j -> {
            try {
                if (bundle != null){
                    String isCredit = bundle.getString("isCredit", "");
                    if(isCredit.equals("DINHEIRO")){
                        SalesRequestModel salesRequestModel = new SalesRequestModel(sale, items);
                        Log.i("items", "registrarVenda: " + salesRequestModel.getItems());
                        ApiService apiService = RetrofitClient.getRetrofitInstance(getActivity()).create(ApiService.class);
                        Call<RegisterSalesResponse> call = apiService.registrarVenda(salesRequestModel);
                        call.enqueue(new Callback<RegisterSalesResponse>() {
                            @Override
                            public void onResponse(Call<RegisterSalesResponse> call, Response<RegisterSalesResponse> response) {
                                    RegisterSalesResponse registerResponse = response.body();
                                    sale.setSend(true);
                                    bundle1.putBoolean("isSend", sale.isSend());
                                    paymentReceiptFragment.setArguments(bundle1);
                                    bundle1.putSerializable("saleModel", sale);
                                    bundle1.putSerializable("itemModels", items);
                                    paymentReceiptFragment.setArguments(bundle1);
                                    fragmentController.replaceFragment(paymentReceiptFragment, null);
                                    Toast.makeText(getActivity(), "Venda registrada com sucesso no Portal.", Toast.LENGTH_SHORT).show();
                                    Log.d("API_RESPONSE", "onResponse: " + registerResponse.getMessage());

                            }
                            @Override
                            public void onFailure(Call<RegisterSalesResponse> call, Throwable t) {
                                Log.e("API_RESPONSE", "onFailure: Falha na chamada à API", t);
                                myDB.insertSale(sale);
                                myDB.insertItems(items);
                                bundle1.putSerializable("saleModel", sale);
                                bundle1.putSerializable("itemModels", items);
                                paymentReceiptFragment.setArguments(bundle1);
                                fragmentController.replaceFragment(paymentReceiptFragment, null);
                                Toast.makeText(getActivity(), "Erro ao enviar a venda para o portal. A venda foi salva para reprocessamento.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        SalesRequestModel salesRequestModel = new SalesRequestModel(sale, items);
                        Log.i("items", "registrarVenda: " + salesRequestModel.getItems());
                        ApiService apiService = RetrofitClient.getRetrofitInstance(getActivity()).create(ApiService.class);
                        Call<RegisterSalesResponse> call = apiService.registrarVenda(salesRequestModel);
                        call.enqueue(new Callback<RegisterSalesResponse>() {
                            @Override
                            public void onResponse(Call<RegisterSalesResponse> call, Response<RegisterSalesResponse> response) {
                                RegisterSalesResponse registerResponse = response.body();
                                sale.setSend(true);
                                bundle1.putBoolean("isSend", sale.isSend());
                                paymentReceiptFragment.setArguments(bundle1);
                                bundle1.putSerializable("saleModel", sale);
                                bundle1.putSerializable("itemModels", items);
                                paymentReceiptFragment.setArguments(bundle1);
                                fragmentController.replaceFragment(paymentReceiptFragment, null);
                                Toast.makeText(getActivity(), "Venda registrada com sucesso no Portal.", Toast.LENGTH_SHORT).show();
                                Log.d("API_RESPONSE", "onResponse: " + registerResponse.getMessage());

                            }
                            @Override
                            public void onFailure(Call<RegisterSalesResponse> call, Throwable t) {
                                Log.e("API_RESPONSE", "onFailure: Falha na chamada à API", t);
                                myDB.insertSale(sale);
                                myDB.insertItems(items);
                                bundle1.putSerializable("saleModel", sale);
                                bundle1.putSerializable("itemModels", items);
                                paymentReceiptFragment.setArguments(bundle1);
                                fragmentController.replaceFragment(paymentReceiptFragment, null);
                                Toast.makeText(getActivity(), "Erro ao enviar a venda para o portal. A venda foi salva para reprocessamento.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}



