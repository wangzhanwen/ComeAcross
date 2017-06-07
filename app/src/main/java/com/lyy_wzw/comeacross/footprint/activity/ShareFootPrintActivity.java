package com.lyy_wzw.comeacross.footprint.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.lyy_wzw.comeacross.R;
import com.lyy_wzw.comeacross.footprint.adapter.FPShareWinGridViewAdapter;
import com.lyy_wzw.comeacross.footprint.finalvalue.FootPrintConstantValue;

import java.util.ArrayList;
import java.util.List;



public class ShareFootPrintActivity extends AppCompatActivity {

    private GridView mImagesGridView;
    private List<String> mImageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_foot_print);

        mImagesGridView = (GridView)findViewById(R.id.share_footprint_grid_view);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(FootPrintConstantValue.SHARE_FOOTPRINT_IMAGE_URLS_BUNDLE_KEY);
        mImageUrls = bundle.getStringArrayList(FootPrintConstantValue.SHARE_FOOTPRINT_IMAGE_URLS_KEY);


//        mImageUrls = new ArrayList<>();
//
//        mImageUrls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-14-17881962_1329090457138411_8289893708619317248_n.jpg");
//        mImageUrls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-10-18382517_1955528334668679_3605707761767153664_n.jpg");
//        mImageUrls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-09-18443931_429618670743803_5734501112254300160_n.jpg");
//        //imageUrls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-08-18252341_289400908178710_9137908350942445568_n.jpg");
//        //imageUrls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-05-18251898_1013302395468665_8734429858911748096_n.jpg");
//        //imageUrls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-04-18299181_1306649979420798_1108869403736276992_n.jpg");
//        mImageUrls.add("http://ww1.sinaimg.cn/large/61e74233ly1feuogwvg27j20p00zkqe7.jpg");
//        mImageUrls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-02-926821_1453024764952889_775781470_n.jpg");
//        mImageUrls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-28-18094719_120129648541065_8356500748640452608_n.jpg");

        mImagesGridView.setAdapter(new FPShareWinGridViewAdapter(this, R.layout.footprint_sharewin_gridview_item, mImageUrls));
    }

    public static Handler mShareHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FootPrintConstantValue.SHARE_IMAGEURLS_HANDLE_KEY:

                    break;
            }
        }
    };
}
