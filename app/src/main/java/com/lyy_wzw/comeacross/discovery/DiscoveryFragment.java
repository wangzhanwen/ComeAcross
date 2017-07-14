package com.lyy_wzw.comeacross.discovery;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lyy_wzw.comeacross.MainActivity;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.discovery.activitys.FootPrintCircleActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoveryFragment extends Fragment implements View.OnClickListener {

    private static MainActivity mainActivity;
    private View mRootView;
    private LinearLayout mFootPrintCircleBtn;

    public DiscoveryFragment() {

    }

    public static DiscoveryFragment instance(MainActivity activity){
        mainActivity = activity;
        return new DiscoveryFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_discovery, container, false);
        mFootPrintCircleBtn = (LinearLayout)mRootView.findViewById(R.id.footprint_circle_btn);
        mFootPrintCircleBtn.setOnClickListener(this);

        return mRootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.footprint_circle_btn:
//
                startActivity(new Intent(this.getContext(), FootPrintCircleActivity.class));
                break;
        }
    }
}
