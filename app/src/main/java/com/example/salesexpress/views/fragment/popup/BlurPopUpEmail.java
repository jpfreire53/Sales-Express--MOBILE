package com.example.salesexpress.views.fragment.popup;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.salesexpress.R;
import com.example.salesexpress.services.FragmentController;
import com.example.salesexpress.views.fragment.RegisteredSalesFragment;

import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;

public class BlurPopUpEmail extends SupportBlurDialogFragment {
    FragmentController fragmentController;
    String email;

    public BlurPopUpEmail(String email) {
        this.email = email;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View popUp = inflater.inflate(R.layout.fragment_blur_pop_up_email, container, false);
        TextView emailUser = popUp.findViewById(R.id.txtEmailPopUp);
        emailUser.setText(email);
        return popUp;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        RegisteredSalesFragment registeredSalesFragment = new RegisteredSalesFragment();
        fragmentController = new FragmentController(getParentFragmentManager(), R.id.frameLayout);
        fragmentController.replaceFragment(registeredSalesFragment, null);
    }

}