package com.example.salesexpress.services;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class DateTextWatcher implements TextWatcher {
    private final EditText editText;
    private boolean isUpdating;
    private String previousText = "";
    private final String mask = "##/##/####";

    public DateTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Armazena o texto anterior para comparações
        previousText = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Não faz nada aqui, apenas em afterTextChanged
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (isUpdating) return;

        String cleanText = s.toString().replaceAll("[^\\d]", ""); // Remove caracteres não numéricos

        if (cleanText.equals(previousText.replaceAll("[^\\d]", ""))) {
            return; // Não formata se o conteúdo não mudou
        }

        isUpdating = true;

        StringBuilder formattedText = new StringBuilder();
        int index = 0;
        for (char m : mask.toCharArray()) {
            if (m == '#') {
                if (index < cleanText.length()) {
                    formattedText.append(cleanText.charAt(index));
                    index++;
                } else {
                    break;
                }
            } else {
                formattedText.append(m);
            }
        }

        // Atualiza o texto e coloca o cursor no final do texto formatado
        String newText = formattedText.toString();
        editText.setText(newText);
        editText.setSelection(Math.min(newText.length(), mask.length()));

        isUpdating = false;
    }
}
