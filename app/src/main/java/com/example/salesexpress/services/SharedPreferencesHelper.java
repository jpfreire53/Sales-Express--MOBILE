package com.example.salesexpress.services;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static final String PREF_NAME = "MinhaPreferencia";
    private static final String KEY_USER = "key_user";
    private static final String KEY_PASSWORD = "key_password";
    private static final String KEY_ID = "key_id";
    private static final String KEY_PAID = "key_paid";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;


    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void salvarUsuario(String user, String password, int id) {
        editor.putString(KEY_USER, user);
        editor.putString(KEY_PASSWORD, password);
        editor.putInt(KEY_ID, id);
        editor.apply();
    }


    public void salvarIsPaid(boolean isPaid){
        editor.putBoolean(KEY_PAID, isPaid);
        editor.apply();
    }

    public String getId() {
        return sharedPreferences.getString(KEY_ID, "");
    }

    public boolean isPaid() {return sharedPreferences.getBoolean(KEY_PAID, false);}

    public boolean isUserLogged() {
        return sharedPreferences.contains(KEY_USER) && sharedPreferences.contains(KEY_PASSWORD);
    }

    public void fazerLogout() {
        editor.remove(KEY_USER);
        editor.remove(KEY_PASSWORD);
        editor.apply();
    }

}
