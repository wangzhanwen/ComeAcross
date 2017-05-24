package com.lyy_wzw.comeacross.footprint;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.footprint.adapter.FPPopupWinGridViewAdapter;
import com.lyy_wzw.comeacross.footprint.contract.FootPrintPopupWinContract;
import com.lyy_wzw.comeacross.footprint.presenter.FootPrintPopupWinPresenter;
import com.lyy_wzw.comeacross.homecommon.BasePopupWindow;
import com.lyy_wzw.comeacross.utils.PixelUtil;

/**
 * Created by yidong9 on 17/5/18.
 */

public class FootPrintPopupWin extends BasePopupWindow implements FootPrintPopupWinContract.View{
    private static final String TAG = "FootPrintPopupWin";
    private View mContainerView = null ;
    private GridView mFootPrintImageViews = null;

    private FootPrintPopupWinContract.Presenter mPresenter;

    public FootPrintPopupWin(Context context) {
        super(context);
        //设置presenter
        new FootPrintPopupWinPresenter(getContext(), this);
        mContainerView = LayoutInflater.from(context).inflate(R.layout.footprint_map_popup_win, null);
        this.setWidth(PixelUtil.dip2px(getContext(), 320));
        this.setHeight(PixelUtil.dip2px(getContext(), 220));
        this.setContentView(mContainerView);
        initViews(mContainerView);

    }




    @Override
    public void setPresenter(FootPrintPopupWinContract.Presenter presenter) {
        if (presenter != null) {
            this.mPresenter = presenter;
        }
    }


    @Override
    public void initViews(View rootView) {
        mFootPrintImageViews = (GridView)rootView.findViewById(R.id.footprint_popupwin_gridview);
        Log.d(TAG, mPresenter.getImageUrls().toString());
        mFootPrintImageViews.setAdapter(new FPPopupWinGridViewAdapter(getContext(), R.layout.footprint_popupwin_gridview_item, mPresenter.getImageUrls()));
    }

    @Override
    public void setGridViewHeight(int height) {
        ViewGroup.LayoutParams params = mFootPrintImageViews.getLayoutParams();
        params.height = height;
    }
}
