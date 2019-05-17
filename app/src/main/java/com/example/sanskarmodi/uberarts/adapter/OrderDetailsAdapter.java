package com.example.sanskarmodi.uberarts.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.sanskarmodi.uberarts.OrdersFragment;
import com.example.sanskarmodi.uberarts.R;
import com.example.sanskarmodi.uberarts.model.Order;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Book;
import io.paperdb.Paper;

public class OrderDetailsAdapter extends RecyclerView.Adapter {
    List<Order> orderList= new ArrayList<>();
    Book orders;
    OrderDetailsListener listener;

    public OrderDetailsAdapter(Context context, OrdersFragment mContext) {
        super();
        Paper.init(context);
        listener = ((OrderDetailsListener)mContext);
        orders = Paper.book("orders");
        populateOrders();

    }

    public interface OrderDetailsListener {
        void onItemChecked(Order order,int index);
    }

    private void populateOrders() {
        for(String key : orders.getAllKeys()) {
            orderList.add((Order) orders.read(key));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_item_view,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final Order order = orderList.get(i);
        ItemViewHolder itemViewHolder = ((ItemViewHolder)viewHolder);
        itemViewHolder.textView.setText(order.getClassType());
        itemViewHolder.textView2.setText(order.getTime());
        itemViewHolder.textView3.setText(String.valueOf(order.getPrice()));
        final int index = i;
        itemViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    listener.onItemChecked(order,index);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public  class ItemViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        TextView textView2;
        TextView textView3;
        CheckBox checkBox;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.category);
            textView2 = itemView.findViewById(R.id.time);
            textView3 = itemView.findViewById(R.id.price);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
