package com.lyy_wzw.comeacross.footprint.presenter;

import android.content.Context;

import com.lyy_wzw.comeacross.footprint.FootPrintContract;
import com.lyy_wzw.comeacross.footprint.contract.FootPrintPopupWinContract;
import com.lyy_wzw.comeacross.footprint.finalvalue.FPPopupWinValue;
import com.lyy_wzw.comeacross.utils.PixelUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzw on 17/5/18.
 */

public class FootPrintPopupWinPresenter implements FootPrintPopupWinContract.Presenter{
    private FootPrintPopupWinContract.View mView;
    private Context mContext;

    public FootPrintPopupWinPresenter(Context context, FootPrintPopupWinContract.View view){
        mContext = context;
        mView = view;
        //将presenter 传递给view
        this.mView.setPresenter(this);
    }

    @Override
    public List<String> getImageUrls() {
        List<String> imageUrls = new ArrayList<>();

        imageUrls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-14-17881962_1329090457138411_8289893708619317248_n.jpg");
        imageUrls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-10-18382517_1955528334668679_3605707761767153664_n.jpg");
        imageUrls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-09-18443931_429618670743803_5734501112254300160_n.jpg");
        //imageUrls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-08-18252341_289400908178710_9137908350942445568_n.jpg");
        imageUrls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-05-18251898_1013302395468665_8734429858911748096_n.jpg");
        imageUrls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-04-18299181_1306649979420798_1108869403736276992_n.jpg");
        imageUrls.add("http://ww1.sinaimg.cn/large/61e74233ly1feuogwvg27j20p00zkqe7.jpg");
        imageUrls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-02-926821_1453024764952889_775781470_n.jpg");
        imageUrls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-28-18094719_120129648541065_8356500748640452608_n.jpg");

        //根据图片的个数动态设置足迹详情窗口里GridView高度
        int height = 0;
        for (int i = 0; i < imageUrls.size(); i=i+3) {
            height = height + PixelUtil.dip2px(mContext, FPPopupWinValue.GRIDE_VIEW_ITEM_HEIGHT);
        }
        mView.setGridViewHeight(height);

        return imageUrls;
    }
}
