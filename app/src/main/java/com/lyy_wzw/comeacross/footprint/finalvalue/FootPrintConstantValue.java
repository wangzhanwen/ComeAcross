package com.lyy_wzw.comeacross.footprint.finalvalue;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yidong9 on 17/6/1.
 */

public class FootPrintConstantValue {
    public  static final int SHARE_IMAGE_MAX_COUNT = 9;

    public  static final String SHARE_FOOTPRINT_FILE_TYPE_KEY = "share_footprint_file_type_toShareFootPrintActivity_key";
    public  static final String SHARE_FOOTPRINT_IMAGE_URLS_KEY = "share_footprint_imageurls_toShareFootPrintActivity_key";
    public  static final String SHARE_FOOTPRINT_VIDEO_URLS_KEY = "share_footprint_video_url_toShareFootPrintActivity_key";
    public  static final String SHARE_FOOTPRINT_IMAGE_URLS_BUNDLE_KEY = "share_footprint_imageurls_bundle_toShareFootPrintActivity_key";
    public  static final int SHARE_IMAGEURLS_HANDLE_KEY = 0x000001;
    public  static final String  FILE_SAVE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "ComeAcross";

    public  static final String[] FOOTPRINT_MARK_LABEL = new String[]{"家居","美食","旅行"};
    public  static final String[] FOOTPRINT_MARK_LABEL_COLOR = new String[]{"#ff0000","#00ff00","#0000ff"};

    public static final String BAIDU_APPKEY = "mT08b9sP4x40kfxDjjTBPKusLDpaeQNG";
    public static final String BAIDU_SAFE_CODE = "E7:9C:58:A2:85:3D:ED:62:ED:17:3D:C5:E7:4C:72:A8:C1:74:D1:8A;com.lyy_wzw.comeacross";

    public static final String CIRCLE_DETAIL_BUNDLE_FOOTPRINT_KEY = "circle_detail_bundle_footprint_key";
    public static final String CIRCLE_DETAIL_BUNDLE_KEY = "circle_detail_bundle_key";

}
