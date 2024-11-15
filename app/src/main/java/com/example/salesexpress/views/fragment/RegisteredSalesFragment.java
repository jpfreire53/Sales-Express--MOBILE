package com.example.salesexpress.views.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.salesexpress.R;
import com.example.salesexpress.databinding.FragmentRegisteredSalesBinding;
import com.example.salesexpress.model.ItemModel;
import com.example.salesexpress.model.SalesModel;
import com.example.salesexpress.services.CpfTextWatcher;
import com.example.salesexpress.services.DateTextWatcher;
import com.example.salesexpress.services.FragmentController;
import com.example.salesexpress.services.MoneyTextWatcher;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisteredSalesFragment extends Fragment {
    private FragmentController fragmentController;
    FragmentRegisteredSalesBinding binding;
    private LinearLayout container;
    private final List<View> dynamicViews = new ArrayList<>();
    SalesModel salesModel;
    private final List<ItemModel> itemModels = new ArrayList<>();
    private EditText currentEditText;

    public RegisteredSalesFragment() {}
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisteredSalesBinding.inflate(inflater, container, false);
        fragmentController = new FragmentController(getParentFragmentManager(), R.id.frameLayout);
        List<String> isCreditList = new ArrayList<>(Arrays.asList("CRÉDITO", "DÉBITO", "DINHEIRO"));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, isCreditList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.isCredit.setAdapter(dataAdapter);

        List<String> parcelList = new ArrayList<>(Arrays.asList("À VISTA", "2X PARC.", "3X PARC.", "4X PARC.", "5X PARC.", "6X PARC.", "7X PARC.", "8X PARC.", "9X PARC.", "10X PARC.", "11X PARC.", "12X PARC."));
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, parcelList);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnrParcel.setAdapter(dataAdapter1);

        binding.isCredit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String valorSelectionado = isCreditList.get(i);
                switch (valorSelectionado) {
                    case "CRÉDITO":
                        binding.edtMoneyChangeC.setVisibility(View.GONE);
                        binding.spnrParcel.setVisibility(View.VISIBLE);
                        binding.spnrParcel.setSelection(0);
                        binding.spnrParcel.setEnabled(true);
                        break;
                    case "DÉBITO":
                        binding.edtMoneyChangeC.setVisibility(View.GONE);
                        binding.spnrParcel.setVisibility(View.VISIBLE);
                        binding.spnrParcel.setSelection(0);
                        binding.spnrParcel.setEnabled(false);
                        break;
                    case "DINHEIRO":
                        binding.edtMoneyChangeC.setVisibility(View.VISIBLE);
                        binding.spnrParcel.setVisibility(View.GONE);
                        break;
                    default:
                        binding.edtMoneyChangeC.setVisibility(View.GONE);
                        binding.spnrParcel.setVisibility(View.VISIBLE);
                        binding.spnrParcel.setSelection(0);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        this.container = binding.container;

        LinearLayout horizontalContainer = binding.horizontalContainer;
        dynamicViews.add(horizontalContainer);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.edtCPF.addTextChangedListener(new CpfTextWatcher(binding.edtCPF));
        binding.edtValueC.addTextChangedListener(new MoneyTextWatcher(binding.edtValueC));
        binding.edtMoneyChangeC.addTextChangedListener(new MoneyTextWatcher(binding.edtMoneyChangeC));
        binding.edtDate.addTextChangedListener(new DateTextWatcher(binding.edtDate));
        ConfirmFragment confirmFragment = new ConfirmFragment();
        Bundle bundle = new Bundle();


        if (itemModels.size() > 0) {
            itemModels.clear();
        }

        binding.btnIrParaResumo.setOnClickListener(v -> {
            try {
                String name = binding.edtNameC.getText().toString();
                String cpf = binding.edtCPF.getText().toString();
                String email = binding.edtEmailC.getText().toString();
                String value = binding.edtValueC.getText().toString(); //valor venda
                String moneyChange = binding.edtMoneyChangeC.getText().toString();  //valor recebido
                String date = binding.edtDate.getText().toString();
                String isCredit = binding.isCredit.getSelectedItem().toString();
                String spnrParcel = binding.spnrParcel.getSelectedItem().toString();

                value = value.replaceAll("\\D", "");
                moneyChange = moneyChange.replaceAll("\\D", "");

                if (name.equals("") || cpf.equals("") || email.equals("") || value.equals("") || dynamicViews.size() == 0 || verifyEditText() || date.equals("")) {
                    Toast.makeText(getActivity(), "Preecha os campos para inserir a venda.", Toast.LENGTH_SHORT).show();
                } else if (!email.contains("@") || email.lastIndexOf(".") <= email.lastIndexOf("@")) {
                    Toast.makeText(getActivity(), "E-mail digitado de maneira incorreta.", Toast.LENGTH_SHORT).show();
                } else if (cpf.length() < 11) {
                    Toast.makeText(getActivity(), "CPF digitado de maneira incorreta.", Toast.LENGTH_SHORT).show();
                } else if (moneyChange.equals("")) {
                    binding.edtMoneyChangeC.setText("0");
                } else if (date.length() < 10) {
                    Toast.makeText(getActivity(), "Preencha a data de maneira correta.", Toast.LENGTH_SHORT).show();
                } else {
                    double valueDouble = Double.parseDouble(value);
                    double moneyChangeDouble = Double.parseDouble(moneyChange);
                    double finaltotal;

                    if (moneyChangeDouble == 0) {
                        finaltotal = 0;
                    } else {
                        if (moneyChangeDouble < valueDouble) {
                            finaltotal = ((moneyChangeDouble - valueDouble) * -1);
                        } else {
                            finaltotal = (moneyChangeDouble - valueDouble);
                        }
                    }

                    salesModel = new SalesModel("", name, cpf, email, valueDouble, finaltotal, date, "1");
                    bundle.putSerializable("saleModel", salesModel);
                    if (dynamicViews.size() > 0) {
                        Log.i("dynamicViews", "onViewCreated: SIZE: " + dynamicViews.size());
                        for (int i = 0; i < dynamicViews.size(); i++) {
                            boolean isExist = false;
                            View dynamicView = dynamicViews.get(i);
                            Log.i("dynamicViews", "onViewCreated: itemViewChild: " + dynamicView);
                            EditText newEditText = dynamicView.findViewById(R.id.newEditText);
                            String newItemText = newEditText.getText().toString();
                            if (!newItemText.equals("")) {
                                ItemModel newItemModel = new ItemModel("", newItemText, salesModel.getId(), "1");
                                Log.i("dynamicViews", "onViewCreated: SIZE: " + dynamicViews.size());
                                for (int j = 0; j < itemModels.size(); j++) {
                                    if (itemModels.get(j).description.equals(newItemText)) {
                                        isExist = true;
                                    }
                                }
                                if (!isExist) {
                                    itemModels.add(newItemModel);
                                }
                            }
                        }
                    }
                    bundle.putSerializable("itemModels", (Serializable) itemModels);
                    bundle.putString("parcel", spnrParcel);
                    bundle.putString("isCredit", isCredit);
                    confirmFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getParentFragmentManager();
                    Fragment fragmentAtual = fragmentManager.findFragmentById(R.id.frameLayout);
                    fragmentController.addFragment(fragmentAtual, confirmFragment, null);
                }
            } catch (Exception e) {
                Log.i("ErroJP", "" + e.getMessage());
            }
        });

        binding.btnPlus.setOnClickListener(v -> {
            try {
                if (dynamicViews.isEmpty()) {
                    addDynamicViews();
                } else {
                    int existingPosition = dynamicViews.size();
                    Log.i("existingPosition", "onViewCreated: " + existingPosition);
                    addDynamicViews();
                }
                binding.btnPlus.setVisibility(View.GONE);
                binding.btnMinus.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                Log.i("erro btnPlus", "" + e.getMessage());
            }
        });

        binding.btnPlus.setOnLongClickListener(v -> {
            launchQRCodeScanner(binding.newEditText);
            return true;
        });

        binding.btnMinus.setOnClickListener(j -> {
            int existingPosition = dynamicViews.size();
            if (existingPosition > 0) {
                dynamicViews.remove(binding.horizontalContainer);
                container.removeView(binding.horizontalContainer);
                itemModels.clear();
                updateButtonVisibility();
                updateHints();
            }
        });
    }
    private void addDynamicViews() {
        View dynamicView = getLayoutInflater().inflate(R.layout.dynamic_layout, container, false);

        ImageButton btnAdd = dynamicView.findViewById(R.id.btnAdd);
        ImageButton btnMinus = dynamicView.findViewById(R.id.btnMinus);

        btnAdd.setOnClickListener(v -> {
            btnAdd.setVisibility(View.GONE);
            btnMinus.setVisibility(View.VISIBLE);
            addDynamicViews();
        });

        btnMinus.setOnClickListener(v -> {
            itemModels.clear();
            removeDynamicView(dynamicView);
        });

        int existingPosition = container.getChildCount();
        EditText newEditText = dynamicView.findViewById(R.id.newEditText);

        btnAdd.setOnLongClickListener(v -> {
            launchQRCodeScanner(newEditText);
            return true;
        });

        container.addView(dynamicView, container.getChildCount());
        dynamicViews.add(dynamicView);

        if (existingPosition >= 10) {
            newEditText.setHint("ITEM " + (existingPosition));
        } else {
            newEditText.setHint("ITEM 0" + (existingPosition + 1));
        }
    }
    private void removeDynamicView(View dynamicView) {
        container.removeView(dynamicView);
        dynamicViews.remove(dynamicView);
        updateButtonVisibility();
        updateHints();
    }
    private void updateButtonVisibility() {
        int numDynamicViews = dynamicViews.size();
        for (int i = 0; i < numDynamicViews; i++) {
            View dynamicView = dynamicViews.get(i);
            ImageButton btnAdd = dynamicView.findViewById(R.id.btnAdd);
            ImageButton btnMinus = dynamicView.findViewById(R.id.btnMinus);

            if (i == numDynamicViews - 1) {
                if (btnAdd != null) {
                    btnAdd.setVisibility(View.VISIBLE);
                }
                if (btnMinus != null) {
                    btnMinus.setVisibility(View.GONE);
                }
            } else if (i == 1) {
                if (btnAdd != null) {
                    btnAdd.setVisibility(View.GONE);
                }
                if (btnMinus != null) {
                    btnMinus.setVisibility(View.VISIBLE);
                }
            } else {
                if (btnAdd != null) {
                    btnAdd.setVisibility(View.GONE);
                }
                if (btnMinus != null) {
                    btnMinus.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    private void updateHints() {
        int numDynamicViews = dynamicViews.size();
        for (int i = 0; i < numDynamicViews; i++) {
            View dynamicView = dynamicViews.get(i);
            EditText newEditText = dynamicView.findViewById(R.id.newEditText);

            if (i >= 10) {
                newEditText.setHint("ITEM " + (i));
            } else {
                newEditText.setHint("ITEM 0" + (i + 1));
            }
        }
    }

    public boolean verifyEditText () {
        boolean isEmpty = true;
        for (int i = 0 ; i < dynamicViews.size() ; i++) {
            View dynamicView = dynamicViews.get(i);
            EditText newEditText = dynamicView.findViewById(R.id.newEditText);
            if(!newEditText.getText().toString().equals("")){
                isEmpty = false;

            }
        }
        return isEmpty;
    }

    private final ActivityResultLauncher<ScanOptions> qrCodeScannerLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    try {
                        // Converte o conteúdo do QR code para um objeto JSON
                        JSONObject jsonObject = new JSONObject(result.getContents());

                        // Extrai o campo "product" do JSON
                        String product = jsonObject.optString("produto", "");

                        // Coloca o valor do produto no currentEditText
                        if (currentEditText != null && !product.isEmpty()) {
                            currentEditText.setText(product);
                        } else {
                            Toast.makeText(getContext(), "QR Code não contém dados válidos.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Erro ao ler QR Code: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("QRCodeError", "Erro ao processar JSON: " + e.getMessage());
                    }
                }
            });



    private void launchQRCodeScanner(EditText newEditText) {
        currentEditText = newEditText; // Armazena o EditText atual

        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Escaneie o QR Code");
        options.setBeepEnabled(true);
        options.setCameraId(0); // 0 para usar a câmera traseira
        options.setOrientationLocked(true);

        qrCodeScannerLauncher.launch(options); // Apenas chama o launcher
    }


}
