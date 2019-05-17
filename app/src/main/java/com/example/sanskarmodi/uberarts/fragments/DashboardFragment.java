package com.example.sanskarmodi.uberarts.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sanskarmodi.uberarts.HomeActivity;
import com.example.sanskarmodi.uberarts.R;
import com.example.sanskarmodi.uberarts.adapter.DashboardAdapter;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "DashboardFragment";

    private Listener listener;
    private RecyclerView dashboardRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DashboardAdapter dashboardAdapter;

    public DashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dashboardRecyclerView = view.findViewById(R.id.dashboardRecyclerView);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        dashboardAdapter = new DashboardAdapter(HomeActivity.dashboardItemList,getActivity());
        dashboardRecyclerView.setLayoutManager(linearLayoutManager);
        dashboardRecyclerView.setAdapter(dashboardAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Listener)
            listener = (Listener) context;
    }

    @Override
    public void onClick(View v) {
       listener.onDashboardInteraction(v);
    }

    public interface Listener {
        void onDashboardInteraction(View v);
    }
}
