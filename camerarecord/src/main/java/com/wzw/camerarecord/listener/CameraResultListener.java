package com.wzw.camerarecord.listener;

import android.graphics.Bitmap;

/**
 * Created by yidong9 on 17/6/8.
 */

public interface CameraResultListener {
    void captureResult(Bitmap bitmap);

    void recordResult(String url);

    String getCaptureSavePath();

    String getRecordSavePath();
}
