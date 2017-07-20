package com.lyy_wzw.comeacross.footprint.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.footprint.adapter.ImageViewPagerAdapter;
import com.lyy_wzw.comeacross.utils.GlideUtil;

import uk.co.senab.photoview.PhotoView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageLookViewPagerFragment extends Fragment {

    private static final String IMAGE_URL = "image";
    private PhotoView image;
    private String imageUrl;


    public ImageLookViewPagerFragment() {

    }

    public static ImageLookViewPagerFragment newInstance(String param1) {
        ImageLookViewPagerFragment fragment = new ImageLookViewPagerFragment();
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
        View view = inflater.inflate(R.layout.fragment_image_look_view_pager, container, false);
        image = (PhotoView) view.findViewById(R.id.footprint_item_image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Log.d("ViewPagerFragment","onClick(ImageLookViewPagerFragment.java:61)-->>");
            }
        });


        if (imageUrl.endsWith(".gif")) {
            GlideUtil.loadPicAsGif(this.getContext(), imageUrl, image);

        }else{
            GlideUtil.loadPic(this.getContext(), imageUrl, image);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
