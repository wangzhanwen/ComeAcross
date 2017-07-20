package com.lyy_wzw.comeacross.footprint.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lyy_wzw.comeacross.footprint.fragment.ImageLookViewPagerFragment;

import java.util.List;

/**
 * Created by csonezp on 15-12-28.
 */
public class ImageViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final String IMAGE_URL = "image";

    List<String> mDatas;

    public ImageViewPagerAdapter(FragmentManager fm, List data) {
        super(fm);
        mDatas = data;
    }

    @Override
    public Fragment getItem(int position) {
        String url = mDatas.get(position);
        ImageLookViewPagerFragment fragment = ImageLookViewPagerFragment.newInstance(url);
        return fragment;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }
}
