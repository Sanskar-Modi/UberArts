package com.example.sanskarmodi.uberarts.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sanskarmodi.uberarts.R;
import com.example.sanskarmodi.uberarts.model.DashboardItem;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter {

    private List<DashboardItem> items;
    private ItemListener itemListener;


    public DashboardAdapter(List<DashboardItem> items, Context context) {
        this.items = items;
        this.itemListener = ((ItemListener) context );
    }

    public interface ItemListener {
        void onDashboardItemClick(View v,String title,int index);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashboard_item,viewGroup,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        DashboardItem item = items.get(i);
        ItemViewHolder holder = ((ItemViewHolder)viewHolder);
        holder.textView.setText(item.getTitle());
        holder.imageView.setImageResource(item.getImage());
        holder.itemView.setTag(item.getTitle());
        final int index = i;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onDashboardItemClick(v,v.getTag().toString(),index);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
