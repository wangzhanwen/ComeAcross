package com.lyy_wzw.comeacross.footprint.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lyy_wzw.comeacross.R;

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

        //Glide.with(getContext()).load(imageUrl).into(image);
        if (imageUrl.endsWith(".gif")) {
            loadPicAsGif(imageUrl, image);
        }else{
            loadPic(imageUrl, image);
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

    private void loadPicAsGif(String path, ImageView imageView ){
        Glide.with(getContext())
                .load(path)
                .asGif()
                .placeholder(R.mipmap.meizhi0)
                .error(R.mipmap.meizhi7)
                .into(imageView);
    }

    private void loadPic(String path, ImageView imageView ){
        Glide.with(getContext())
                .load(path)
                .placeholder(R.mipmap.meizhi0)
                .error(R.mipmap.meizhi7)
                .into(imageView);
    }
}
