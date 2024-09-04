package com.example.salesexpress.views.fragment.popup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.salesexpress.R;

import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;

public class BlurPopUpEmailError extends SupportBlurDialogFragment {
    String email;
    public BlurPopUpEmailError(String email) {
        this.email = email;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View popUp = inflater.inflate(R.layout.fragment_blur_pop_up_email_error, container, false);
        TextView emailUser = popUp.findViewById(R.id.txtEmailPopUp);
        emailUser.setText(email);
        return popUp;
    }
}