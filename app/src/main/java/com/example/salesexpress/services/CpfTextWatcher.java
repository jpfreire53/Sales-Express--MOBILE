package com.example.salesexpress.services;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CpfTextWatcher implements TextWatcher {

    private final EditText editText;

    public CpfTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString().replaceAll("[^\\d]", "");

        if (text.length() == 11) {
            text = text.substring(0, 3) + "." + text.substring(3, 6) + "." + text.substring(6, 9) + "-" + text.substring(9);
        }

        editText.removeTextChangedListener(this);
        editText.setText(text);
        editText.setSelection(text.length());
        editText.addTextChangedListener(this);
    }
}
