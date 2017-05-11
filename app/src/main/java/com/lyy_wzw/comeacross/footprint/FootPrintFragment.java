package com.lyy_wzw.comeacross.footprint;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyy_wzw.comeacross.MainActivity;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.discovery.DiscoveryFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FootPrintFragment extends Fragment {


    private static MainActivity mainActivity;

    public FootPrintFragment() {
        // Required empty public constructor
    }

    public static FootPrintFragment instance(MainActivity activity){
        mainActivity = activity;
        return new FootPrintFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_foot_print, container, false);
    }

}
