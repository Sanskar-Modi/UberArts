package com.example.sanskarmodi.uberarts;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class TimeSlot extends Fragment implements ChipGroup.OnCheckedChangeListener {

    ChipGroup timeChips;

    public TimeSlot() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_slot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timeChips = view.findViewById(R.id.time_chips);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        timeChips.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(ChipGroup chipGroup, int i) {
        Toast.makeText(getContext(),chipGroup.getChildAt(i-1).getTag().toString(),Toast.LENGTH_LONG).show();
        switch (i) {
            case 0:
                Toast.makeText(getContext(),chipGroup.getTag().toString(),Toast.LENGTH_LONG).show();
                break;
        }
    }
}
