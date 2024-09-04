package com.example.salesexpress.views.fragment.popup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.salesexpress.R;

import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;

public class BlurPopUpReprocessedError extends SupportBlurDialogFragment {
    public BlurPopUpReprocessedError() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blur_pop_up_error, container, false);
    }
}