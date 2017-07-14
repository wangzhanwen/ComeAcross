package com.lyy_wzw.comeacross.footprint.cards;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.bean.FootPrint;
import com.lyy_wzw.comeacross.bean.FootPrintFile;
import com.lyy_wzw.comeacross.utils.GlideUtil;

import java.util.List;


public class SliderAdapter extends RecyclerView.Adapter<SliderCard> {

    private  List<FootPrint> mData;
    private  Context mContext;
    private  View.OnClickListener listener;

    public SliderAdapter(Context context, List<FootPrint> datas, View.OnClickListener listener) {
        mContext = context;
        mData = datas;
        this.listener = listener;
    }

    @Override
    public SliderCard onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.footprint_layout_slider_card, parent, false);

        if (listener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);
                }
            });
        }

        return new SliderCard(view);
    }

    @Override
    public void onBindViewHolder(SliderCard holder, int position) {
        FootPrintFile footPrintFile = mData.get(position).getFootPrintFiles().get(0);
        if (footPrintFile != null) {
            if (footPrintFile.getType() == 1) {
                holder.playBtn.setVisibility(View.GONE);
                String url = footPrintFile.getFilePath();
                if (url.endsWith("url")){
                    GlideUtil.loadPicAsGif(mContext, url,holder.imageView);
                }else{
                    GlideUtil.loadPic(mContext, url,holder.imageView);
                }

            }else {
                holder.playBtn.setVisibility(View.VISIBLE);
                GlideUtil.loadPic(mContext, footPrintFile.getThumbnailPath(),holder.imageView);

            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
