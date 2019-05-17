package com.example.sanskarmodi.uberarts.fragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanskarmodi.uberarts.HomeActivity;
import com.example.sanskarmodi.uberarts.R;
import com.example.sanskarmodi.uberarts.model.Order;

import java.util.Objects;

import io.paperdb.Book;
import io.paperdb.Paper;

public class DashboardItemViewFragment extends Fragment implements View.OnClickListener, ChipGroup.OnCheckedChangeListener {
    Button btnBookSlot;
    TextView txtDetail,txtTitle;
    ChipGroup chipTimes;

    Bookinglistener bookinglistener;

    private static final String TAG = "ItemViewFragment";
    private Book orders;
    private String classType;
    private String classTime;

    public DashboardItemViewFragment() {
    }

    /*public static DashboardItemViewFragment getInstance(Bundle bundle) {
        DashboardItemViewFragment dashboardItemViewFragment = new DashboardItemViewFragment();
        dashboardItemViewFragment.setArguments(bundle);
        return dashboardItemViewFragment;
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: " + this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Paper.init(Objects.requireNonNull(getActivity()));
        }
        orders = Paper.book("orders");
//        Log.i(TAG, "onCreate: " + getArguments().getString("title") + " " + getArguments().getString("desc"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard_item_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtTitle = view.findViewById(R.id.txtTitle);
        txtDetail = view.findViewById(R.id.txtDetail);
        btnBookSlot = view.findViewById(R.id.btnBookSlot);
        chipTimes = view.findViewById(R.id.time_chips);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        txtTitle.setText(getArguments().getString("title"));
        txtDetail.setText(getArguments().getString("desc"));
        chipTimes.setOnCheckedChangeListener(this);
        btnBookSlot.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof HomeActivity)
            bookinglistener = ((Bookinglistener)context);
    }

    @Override
    public void onClick(View v) {
        classType = txtTitle.getText().toString();
        int price = 600;
        orders.write(classType,new Order(classType,classTime,price));
        String details = (classTime + " " + classType);
        bookinglistener.onDescItemClick();
    }

    @Override
    public void onCheckedChanged(ChipGroup chipGroup, int i) {
        Chip chip = chipGroup.findViewById(chipGroup.getCheckedChipId());
        classTime = chip.getText().toString();
    }

    public interface Bookinglistener {
        void onDescItemClick();
    }
}


