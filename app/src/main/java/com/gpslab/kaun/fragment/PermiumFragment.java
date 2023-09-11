package com.gpslab.kaun.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gpslab.kaun.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PermiumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PermiumFragment extends Fragment {



    public PermiumFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.fragment_permium, container, false);



        return view;
    }
}