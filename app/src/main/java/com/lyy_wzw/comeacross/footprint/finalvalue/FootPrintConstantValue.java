package com.lyy_wzw.comeacross.footprint.finalvalue;

import android.os.Environment;

import java.io.File;

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
}
