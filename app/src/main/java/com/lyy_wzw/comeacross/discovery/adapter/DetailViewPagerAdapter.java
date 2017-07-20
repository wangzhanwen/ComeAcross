package com.lyy_wzw.comeacross.discovery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lyy_wzw.comeacross.discovery.fragment.ImageViewPagerFragment;
import com.lyy_wzw.comeacross.footprint.fragment.ImageLookViewPagerFragment;

import java.util.List;

/**
 * Created by yidong9 on 17/7/20.
 */

public class DetailViewPagerAdapter  extends FragmentStatePagerAdapter {
    private static final String IMAGE_URL = "image";

    List<String> mDatas;


    public DetailViewPagerAdapter(FragmentManager fm, List data) {
        super(fm);
        mDatas = data;
    }

    @Override
    public Fragment getItem(int position) {
        String url = mDatas.get(position);
        ImageViewPagerFragment fragment = ImageViewPagerFragment.newInstance(url);
        return fragment;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }
}
