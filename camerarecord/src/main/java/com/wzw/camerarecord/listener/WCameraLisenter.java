package com.wzw.camerarecord.listener;

import android.graphics.Bitmap;


public interface WCameraLisenter {

    void captureSuccess(Bitmap bitmap);

    void recordSuccess(String url);

    void quit();

}
