package com.lyy_wzw.comeacross.footprint;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.MapView;
import com.lyy_wzw.comeacross.MainActivity;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.discovery.DiscoveryFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FootPrintFragment extends Fragment {


    private static MainActivity mainActivity;
    MapView mMapView = null;

    public FootPrintFragment() {

    }

    public static FootPrintFragment instance(MainActivity activity){
        mainActivity = activity;
        return new FootPrintFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foot_print, container, false);
        mMapView =  (MapView) view.findViewById(R.id.footprint_mapView);

        return view;
    }

}
