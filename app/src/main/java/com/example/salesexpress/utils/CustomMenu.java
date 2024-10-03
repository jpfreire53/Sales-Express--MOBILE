package com.example.salesexpress.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.salesexpress.R;
import com.example.salesexpress.services.FragmentController;
import com.example.salesexpress.services.SharedPreferencesHelper;
import com.example.salesexpress.views.activity.LoginActivity;
import com.example.salesexpress.views.activity.MainActivity;
import com.example.salesexpress.views.fragment.RegisteredSalesFragment;
import com.example.salesexpress.views.fragment.ReprocessingFragment;

public class CustomMenu {

    private Context mContext;
    private PopupWindow mPopupWindow;

    FragmentController fragmentController;

    public CustomMenu(Context context) {
        mContext = context;
        initPopup();
    }

    private void initPopup() {
        ReprocessingFragment reprocessingFragment = new ReprocessingFragment();
        RegisteredSalesFragment registeredSalesFragment = new RegisteredSalesFragment();
        fragmentController = new FragmentController(((AppCompatActivity) mContext).getSupportFragmentManager(), R.id.frameLayout);
        View popupView = LayoutInflater.from(mContext).inflate(R.layout.my_menu, null);
        mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);

        TextView btnRegis = popupView.findViewById(R.id.btnRegis);
        TextView btnRepro = popupView.findViewById(R.id.btnRepro);
        TextView btnLogout = popupView.findViewById(R.id.btnLogout);
        ImageView btnClose = popupView.findViewById(R.id.btnClose);

        btnRegis.setOnClickListener(v -> {
            if (mContext instanceof MainActivity) {
                fragmentController.replaceFragment(registeredSalesFragment, null);
                dismissPopup();
            } else {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("fragmentNumber", 1);
                mContext.startActivity(intent);
                dismissPopup();
            }
        });


        btnRepro.setOnClickListener(j -> {
            if (mContext instanceof MainActivity) {
                fragmentController.replaceFragment(reprocessingFragment, null);
                dismissPopup();
            } else {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("fragmentNumber", 2);
                mContext.startActivity(intent);
                dismissPopup();
            }
        });

        btnClose.setOnClickListener(k -> {
            dismissPopup();
        });

        btnLogout.setOnClickListener(l -> {
            SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(mContext);
            sharedPreferencesHelper.fazerLogout();
            Toast.makeText(mContext, "Logout efetuado.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(intent);
            dismissPopup();
        });



    }

    public void showPopup(View anchorView) {
        mPopupWindow.showAtLocation(anchorView, Gravity.TOP, 0, 0);
    }

    public void dismissPopup() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
}
