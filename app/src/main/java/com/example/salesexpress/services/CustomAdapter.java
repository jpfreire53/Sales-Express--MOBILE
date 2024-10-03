package com.example.salesexpress.services;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesexpress.R;
import com.example.salesexpress.model.ItemModel;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    Activity activity;
    private List<ItemModel> itemModels;

    public CustomAdapter(Context context, Activity activity, List<ItemModel> itemModels) {
        this.context = context;
        this.activity = activity;
        this.itemModels = itemModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, final int position) {
        ItemModel itemModel = itemModels.get(position);

        holder.id.setText(String.valueOf((position + 1)));
        holder.description.setText(String.valueOf(itemModel.getDescription()));
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, description;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.txtItm);
            description = itemView.findViewById(R.id.txtProduct);
            linearLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
