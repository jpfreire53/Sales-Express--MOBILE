package com.example.salesexpress.services;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class MoneyTextWatcher implements TextWatcher {
    private final WeakReference<EditText> editTextWeakReference;
    private static final Locale LOCALE_PT_BR = new Locale("pt", "BR");

    private final Locale locale = new Locale("pt", "BR");

    public MoneyTextWatcher(EditText editText) {
        this.editTextWeakReference = new WeakReference<>(editText);
    }

    public static String formatPrice(String price) {
        DecimalFormat df = new DecimalFormat("0.00");
        return String.valueOf(df.format(Double.valueOf(price)));

    }

    public static String formatTextPrice(String price) {
        Locale ptBr = new Locale("pt", "BR");
        BigDecimal bD = new BigDecimal(formatPriceSave(formatPrice(price)));
        String newFormat = String.valueOf(NumberFormat.getCurrencyInstance(ptBr).format(bD));
        String replaceable = String.format("[%s]", getCurrencySymbol());
        return newFormat.replaceAll(replaceable, "");

    }

    static String formatPriceSave(String price) {
        String replaceable = String.format("[%s,.\\s]", getCurrencySymbol());
        String cleanString = price.replaceAll(replaceable, "");
        StringBuilder stringBuilder = new StringBuilder(cleanString.replaceAll(" ", ""));

        return String.valueOf(stringBuilder.insert(cleanString.length() - 2, '.'));

    }

    public static String getCurrencySymbol() {
        Locale ptBr = new Locale("pt", "BR");
        return Objects.requireNonNull(NumberFormat.getCurrencyInstance(ptBr).getCurrency()).getSymbol();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        EditText editText = editTextWeakReference.get();
        if (editText == null) return;
        editText.removeTextChangedListener(this);

        BigDecimal parsed = parseToBigDecimal(editable.toString());
        String formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);
        String replaceable = String.format("[\\s]", getCurrencySymbol());
        String cleanString = formatted.replaceAll(replaceable, "");

        editText.setText(cleanString);
        editText.setSelection(cleanString.length());
        editText.addTextChangedListener(this);
    }

    private BigDecimal parseToBigDecimal(String value) {
        String replaceable = String.format("[%s,.\\s]", getCurrencySymbol());

        String cleanString = value.replaceAll(replaceable, "");

        try {
            return new BigDecimal(cleanString).setScale(
                    2, RoundingMode.FLOOR).divide(new BigDecimal(100), RoundingMode.FLOOR);
        } catch (NumberFormatException e) {
            return new BigDecimal(0);

        }
    }

    public static String formatCurrency(double value) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(LOCALE_PT_BR);
        return currencyFormatter.format(value);
    }
    public static String formatCurrencyFromString(double value) {
        double adjustedValue = value / 100.0;
        return formatCurrency(adjustedValue);
    }
}