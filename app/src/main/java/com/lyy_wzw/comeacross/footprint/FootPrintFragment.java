package com.lyy_wzw.comeacross.footprint;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyy_wzw.comeacross.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FootPrintFragment extends Fragment {


    public FootPrintFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_foot_print, container, false);
    }

}
