package com.lyy_wzw.comeacross.discovery.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.ImageClickEvent;
import com.lyy_wzw.comeacross.footprint.fragment.ImageLookViewPagerFragment;
import com.lyy_wzw.comeacross.utils.GlideUtil;

import org.greenrobot.eventbus.EventBus;

import uk.co.senab.photoview.PhotoView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageViewPagerFragment extends Fragment {
    private static final String IMAGE_URL = "image";
    private ImageView imageView;
    private String imageUrl;

    public ImageViewPagerFragment() {

    }

    public static ImageViewPagerFragment newInstance(String param1) {
        ImageViewPagerFragment fragment = new ImageViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IMAGE_URL, param1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUrl = getArguments().getString(IMAGE_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image_view_pager, container, false);
        imageView = (ImageView) view.findViewById(R.id.circle_detail_footprint_item_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ViewPagerFragment","onClick(ImageLookViewPagerFragment.java:61)-->>");
                EventBus.getDefault().post(new ImageClickEvent(0));
            }
        });


        if (imageUrl.endsWith(".gif")) {
            GlideUtil.loadPicAsGif(this.getContext(), imageUrl, imageView);

        }else{
            GlideUtil.loadPic(this.getContext(), imageUrl, imageView);
        }

        return view;
    }

}
