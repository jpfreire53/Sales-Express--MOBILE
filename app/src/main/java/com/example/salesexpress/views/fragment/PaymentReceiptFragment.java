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
 import androidx.recyclerview.widget.LinearLayoutManager;
 import androidx.recyclerview.widget.RecyclerView;

 import com.example.salesexpress.R;
 import com.example.salesexpress.databinding.FragmentPaymentReceiptBinding;
 import com.example.salesexpress.model.ItemModel;
 import com.example.salesexpress.model.SalesModel;
 import com.example.salesexpress.services.CustomAdapterReceipt;
 import com.example.salesexpress.services.EmailResponse;
 import com.example.salesexpress.services.FragmentController;
 import com.example.salesexpress.services.MoneyTextWatcher;
 import com.example.salesexpress.services.RetrofitClient;
 import com.example.salesexpress.services.SharedPreferencesHelper;
 import com.example.salesexpress.services.interfaces.ApiService;
 import com.example.salesexpress.views.fragment.popup.BlurPopUpEmail;
 import com.example.salesexpress.views.fragment.popup.BlurPopUpEmailError;

 import java.util.List;

 import retrofit2.Call;
 import retrofit2.Callback;
 import retrofit2.Response;

 public class PaymentReceiptFragment extends Fragment {

    FragmentPaymentReceiptBinding binding;
    private ConstraintLayout containerReceipt;
    RecyclerView recyclerView;
    CustomAdapterReceipt customAdapter;
    private FragmentController fragmentController;
    public PaymentReceiptFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPaymentReceiptBinding.inflate(inflater, containerReceipt, false);
        this.containerReceipt = binding.containerReceipt;

        fragmentController = new FragmentController(getParentFragmentManager(), R.id.frameLayout);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_viewR);
        Bundle bundle = getArguments();
        RegisteredSalesFragment registeredSalesFragment = new RegisteredSalesFragment();

        assert bundle != null;
        SalesModel sale = (SalesModel) bundle.getSerializable("saleModel");
        if (sale != null) {
            binding.edtEmail.setText(sale.getEmail());
            binding.edtName.setText(sale.getName());
            binding.edtCpf.setText(sale.getCpf());

            double totalAmount = Double.parseDouble(String.valueOf(sale.getMoneyChange())) + Double.parseDouble(String.valueOf(sale.getValue()));
            String formattedTotalAmount = MoneyTextWatcher.formatCurrencyFromString(Double.parseDouble(String.valueOf(totalAmount)));
            binding.edtValue.setText(formattedTotalAmount);

            binding.edtSale.setText(MoneyTextWatcher.formatCurrencyFromString(sale.getValue()));
            binding.edtMoneyChange.setText(MoneyTextWatcher.formatCurrencyFromString(sale.getMoneyChange()));

            List<ItemModel> items = (List<ItemModel>) bundle.getSerializable("itemModels");
            Log.d("LISTITEMS", "onViewCreated: " + items.size());

            customAdapter = new CustomAdapterReceipt(getActivity(), getActivity(), items);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(customAdapter);

        }

        binding.btnNo.setOnClickListener(v -> {
            fragmentController.replaceFragment(registeredSalesFragment, null);
        });

        binding.btnYes.setOnClickListener(j -> {
            try {
                enviarEmail(sale);
            } catch (Exception e) {
                Log.i("erroEmail", "" + e.getMessage());
            }
        });

     }

     public void enviarEmail(SalesModel salesModel) {
         try {
             ApiService apiService;
             apiService = RetrofitClient.getRetrofitInstance(getActivity()).create(ApiService.class);
             SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(getActivity());
             Call<EmailResponse> call = apiService.enviarEmailVenda(sharedPreferencesHelper.getId());
             call.enqueue(new Callback<EmailResponse>() {
                 @Override
                 public void onResponse(Call<EmailResponse> call, Response<EmailResponse> response) {
                     if (response.isSuccessful()) {
                         EmailResponse emailResponse = response.body();
                         BlurPopUpEmail dialog = new BlurPopUpEmail(salesModel.getEmail());
                         dialog.show(getParentFragmentManager(), dialog.getClass().getSimpleName());
                         new android.os.Handler().postDelayed(() -> dialog.dismiss(), 3500);
                         Log.d("API_RESPONSE", "onResponse: " + emailResponse.getMessage());
                     } else {
                         BlurPopUpEmailError dialog1 = new BlurPopUpEmailError(salesModel.getEmail());
                         dialog1.show(getParentFragmentManager(), dialog1.getClass().getSimpleName());
                         new android.os.Handler().postDelayed(() -> dialog1.dismiss(), 3500);
                         Log.e("API_RESPONSE", "onResponse: Erro na API");
                     }
                 }

                 @Override
                 public void onFailure(Call<EmailResponse> call, Throwable t) {
                     BlurPopUpEmailError dialog2 = new BlurPopUpEmailError(salesModel.getEmail());
                     dialog2.show(getParentFragmentManager(), dialog2.getClass().getSimpleName());
                     new android.os.Handler().postDelayed(() -> dialog2.dismiss(), 3500);
                     Log.e("API_RESPONSE", "onFailure: Falha na chamada à API", t);
                     Toast.makeText(getActivity(), "Erro ao efetuar o envio do e-mail.", Toast.LENGTH_SHORT).show();
                 }
             });

         } catch (Exception e) {
             Log.i("registrarVenda", "registrarVenda: " + e.getMessage());
         }
     }
 }




