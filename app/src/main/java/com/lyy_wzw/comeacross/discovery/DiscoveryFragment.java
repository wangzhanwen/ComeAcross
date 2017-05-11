package com.lyy_wzw.comeacross.discovery;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyy_wzw.comeacross.MainActivity;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.chat.ChatFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoveryFragment extends Fragment {

    private static MainActivity mainActivity;

    public DiscoveryFragment() {
        // Required empty public constructor
    }

    public static DiscoveryFragment instance(MainActivity activity){
        mainActivity = activity;
        return new DiscoveryFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discovery, container, false);
    }

}
