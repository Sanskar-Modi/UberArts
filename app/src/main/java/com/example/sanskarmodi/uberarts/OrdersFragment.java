package com.example.sanskarmodi.uberarts;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.sanskarmodi.uberarts.adapter.OrderDetailsAdapter;
import com.example.sanskarmodi.uberarts.model.Order;

public class OrdersFragment extends Fragment implements OrderDetailsAdapter.OrderDetailsListener{

    RecyclerView ordersList;
    CheckBox checkBox;

    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ordersList = view.findViewById(R.id.ordersList);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        ordersList.setLayoutManager(linearLayoutManager);
        ordersList.setAdapter(new OrderDetailsAdapter(getActivity(),this));
    }

    @Override
    public void onItemChecked(Order order, int index) {
        Toast.makeText(getActivity(),String.valueOf(order.getPrice()),Toast.LENGTH_LONG).show();
    }
}
