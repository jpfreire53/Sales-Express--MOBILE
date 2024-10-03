package com.example.salesexpress.services;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesexpress.R;
import com.example.salesexpress.model.SalesModel;

import java.util.List;

public class CustomAdapterReprocessed extends RecyclerView.Adapter<CustomAdapterReprocessed.MyViewHolder> {

    private Context context;
    private Activity activity;
    private List<SalesModel> salesModels;

    public CustomAdapterReprocessed(Context context, Activity activity, List<SalesModel> salesModels) {
        this.context = context;
        this.activity = activity;
        this.salesModels = salesModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row_reprocessed, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterReprocessed.MyViewHolder holder, final int position) {
        SalesModel salesModel = salesModels.get(position);

        holder.nameRow.setText(String.valueOf(salesModel.getName()));
        holder.valueRow.setText(MoneyTextWatcher.formatCurrencyFromString(salesModel.getValue()));


        if (salesModel.isReprocessed()) {
            Drawable reproDrawable = ContextCompat.getDrawable(context, R.drawable.bg_field_row_repro);
            holder.colorRepro.setBackground(reproDrawable);
        } else {
            Drawable defaultDrawable = ContextCompat.getDrawable(context, R.drawable.bg_field_row);
            holder.colorRepro.setBackground(defaultDrawable);
        }


    }

    @Override
    public int getItemCount() {
        return salesModels.size();
    }

    public void notifyData() {
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView valueRow, nameRow;
        LinearLayout linearLayout;
        FrameLayout colorRepro;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            valueRow = itemView.findViewById(R.id.lblValueRow);
            nameRow = itemView.findViewById(R.id.lblNameRow);
            linearLayout = itemView.findViewById(R.id.mainLayout);
            colorRepro = itemView.findViewById(R.id.colorRepro);
        }
    }



}
