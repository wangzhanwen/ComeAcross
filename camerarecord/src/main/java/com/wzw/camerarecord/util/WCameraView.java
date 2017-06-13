package com.wzw.camerarecord.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;


import com.wzw.camerarecord.R;
import com.wzw.camerarecord.listener.CaptureLisenter;
import com.wzw.camerarecord.listener.WCameraLisenter;
import com.wzw.camerarecord.listener.ReturnLisenter;
import com.wzw.camerarecord.listener.TypeLisenter;

import java.io.File;
import java.io.IOException;



public class WCameraView extends RelativeLayout implements CameraInterface.CamOpenOverCallback, SurfaceHolder.Callback {
    private static final String TAG = "Camera";

    private static final int TYPE_PICTURE = 0x001;
    private static final int TYPE_VIDEO = 0x002;

    private WCameraLisenter mCameraLisenter;


    private Context mContext;
    private VideoView mVideoView;
    private ImageView mPhoto;
    private ImageView mSwitchCamera;
    private CaptureLayout mCaptureLayout;
    private FoucsView mFoucsView;

    private MediaPlayer mMediaPlayer;

    private int layout_width;
    private int fouce_size;
    private float screenProp;

    private Bitmap captureBitmap;
    private String videoUrl;
    private int type = -1;

    private int CAMERA_STATE = -1;
    private static final int STATE_IDLE = 0x010;
    private static final int STATE_RUNNING = 0x020;
    private static final int STATE_WAIT = 0x030;

    private boolean stopping = false;
    private boolean isBorrow = false;
    private boolean takePictureing = false;

    /**
     * switch buttom param
     */
    private int iconSize = 0;
    private int iconMargin = 0;
    private int iconSrc = 0;
    private int duration = 0;

    /**
     * constructor
     */
    public WCameraView(Context context) {
        this(context, null);
    }

    /**
     * constructor
     */
    public WCameraView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * constructor
     */
    public WCameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //get AttributeSet
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WCameraView, defStyleAttr, 0);
        iconSize = a.getDimensionPixelSize(R.styleable.WCameraView_iconSize, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 35, getResources().getDisplayMetrics()));
        iconMargin = a.getDimensionPixelSize(R.styleable.WCameraView_iconMargin, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics()));
        iconSrc = a.getResourceId(R.styleable.WCameraView_iconSrc, R.drawable.ic_sync_black_24dp);
        duration = a.getInteger(R.styleable.WCameraView_duration_max, 10 * 1000);
        a.recycle();
        initData();
        initView();
    }

    private void initData() {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        layout_width = outMetrics.widthPixels;
        fouce_size = layout_width / 4;
        CAMERA_STATE = STATE_IDLE;
    }


    private void initView() {
        setWillNotDraw(false);
        this.setBackgroundColor(0xff000000);
        //VideoView
        mVideoView = new VideoView(mContext);
        LayoutParams videoViewParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        videoViewParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        mVideoView.setLayoutParams(videoViewParam);

        //mPhoto
        mPhoto = new ImageView(mContext);
        LayoutParams photoParam = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .MATCH_PARENT);
        photoParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        mPhoto.setLayoutParams(photoParam);
        mPhoto.setBackgroundColor(0xff000000);
        mPhoto.setVisibility(INVISIBLE);
        //switchCamera
        mSwitchCamera = new ImageView(mContext);
        LayoutParams imageViewParam = new LayoutParams(iconSize, iconSize);
        imageViewParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        imageViewParam.setMargins(0, iconMargin, iconMargin, 0);
        mSwitchCamera.setLayoutParams(imageViewParam);
        mSwitchCamera.setImageResource(iconSrc);
        mSwitchCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBorrow || switching) {
                    return;
                }
                switching = true;
                new Thread() {
                    /**
                     * switch camera
                     */
                    @Override
                    public void run() {
                        CameraInterface.getInstance().switchCamera(WCameraView.this);
                    }
                }.start();
            }
        });

        //CaptureLayout
        mCaptureLayout = new CaptureLayout(mContext);
        LayoutParams layout_param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layout_param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layout_param.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layout_param.setMargins(0, 0, 0, 40);
        mCaptureLayout.setLayoutParams(layout_param);
        mCaptureLayout.setDuration(duration);

        //mFoucsView
        mFoucsView = new FoucsView(mContext, fouce_size);
        LayoutParams foucs_param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mFoucsView.setLayoutParams(foucs_param);
        mFoucsView.setVisibility(INVISIBLE);

        //add view to ParentLayout
        this.addView(mVideoView);
        this.addView(mPhoto);
        this.addView(mSwitchCamera);
        this.addView(mCaptureLayout);
        this.addView(mFoucsView);
        //START >>>>>>> captureLayout lisenter callback
        mCaptureLayout.setCaptureLisenter(new CaptureLisenter() {
            @Override
            public void takePictures() {
                if (CAMERA_STATE != STATE_IDLE || takePictureing) {
                    return;
                }
                CAMERA_STATE = STATE_RUNNING;
                takePictureing = true;
                mFoucsView.setVisibility(INVISIBLE);
                CameraInterface.getInstance().takePicture(new CameraInterface.TakePictureCallback() {
                    @Override
                    public void captureResult(Bitmap bitmap) {
                        captureBitmap = bitmap;
                        CameraInterface.getInstance().doStopCamera();
                        type = TYPE_PICTURE;
                        isBorrow = true;
                        CAMERA_STATE = STATE_WAIT;
                        mPhoto.setImageBitmap(bitmap);
                        mPhoto.setVisibility(VISIBLE);
                        mCaptureLayout.startAlphaAnimation();
                        mCaptureLayout.startTypeBtnAnimator();
                        takePictureing = false;
                        mSwitchCamera.setVisibility(INVISIBLE);
                    }
                });
            }

            @Override
            public void recordShort(long time) {
                if (CAMERA_STATE != STATE_RUNNING && stopping) {
                    return;
                }
                stopping = true;
                mCaptureLayout.setTextWithAnimation();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CameraInterface.getInstance().stopRecord(true, new
                                CameraInterface.StopRecordCallback() {
                                    @Override
                                    public void recordResult(String url) {
                                        Log.i(TAG, "stopping ...");
                                        mCaptureLayout.isRecord(false);
                                        CAMERA_STATE = STATE_IDLE;
                                        stopping = false;
                                        isBorrow = false;
                                    }
                                });
                    }
                }, 1500 - time);
            }

            @Override
            public void recordStart() {
                if (CAMERA_STATE != STATE_IDLE && stopping) {
                    return;
                }
                mCaptureLayout.isRecord(true);
                isBorrow = true;
                CAMERA_STATE = STATE_RUNNING;
                mFoucsView.setVisibility(INVISIBLE);
                CameraInterface.getInstance().startRecord(mVideoView.getHolder().getSurface());
            }

            @Override
            public void recordEnd(long time) {
                CameraInterface.getInstance().stopRecord(false, new CameraInterface.StopRecordCallback() {
                    @Override
                    public void recordResult(final String url) {
                        CAMERA_STATE = STATE_WAIT;
                        videoUrl = url;
                        type = TYPE_VIDEO;
                        new Thread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void run() {
                                try {
                                    if (mMediaPlayer == null) {
                                        mMediaPlayer = new MediaPlayer();
                                    } else {
                                        mMediaPlayer.reset();
                                    }
                                    Log.i("Camera", "URL = " + url);
                                    mMediaPlayer.setDataSource(url);
                                    mMediaPlayer.setSurface(mVideoView.getHolder().getSurface());
                                    mMediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                    mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer
                                            .OnVideoSizeChangedListener() {
                                        @Override
                                        public void
                                        onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                                            updateVideoViewSize(mMediaPlayer.getVideoWidth(), mMediaPlayer
                                                    .getVideoHeight());
                                        }
                                    });
                                    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {
                                            mMediaPlayer.start();
                                        }
                                    });
                                    mMediaPlayer.setLooping(true);
                                    mMediaPlayer.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });
            }

            @Override
            public void recordZoom(float zoom) {
                CameraInterface.getInstance().setZoom(zoom, CameraInterface.TYPE_RECORDER);
            }
        });
        mCaptureLayout.setTypeLisenter(new TypeLisenter() {
            @Override
            public void cancel() {
                if (CAMERA_STATE == STATE_WAIT) {
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                    }
                    CameraInterface.getInstance().doOpenCamera(WCameraView.this);
                    handlerPictureOrVideo(type, false);
                }
            }

            @Override
            public void confirm() {
                if (CAMERA_STATE == STATE_WAIT) {
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                    }
                    CameraInterface.getInstance().doOpenCamera(WCameraView.this);
                    handlerPictureOrVideo(type, true);
                }
            }

            @Override
            public void editor() {
                if (CAMERA_STATE == STATE_WAIT) {
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                    }
                    CameraInterface.getInstance().doOpenCamera(WCameraView.this);
                    handlerPictureOrVideo(type, false);
                }
            }
        });
        mCaptureLayout.setReturnLisenter(new ReturnLisenter() {
            @Override
            public void onReturn() {
                if (mCameraLisenter != null && !takePictureing) {
                    mCameraLisenter.quit();
                }
            }
        });
        //END >>>>>>> captureLayout lisenter callback
        mVideoView.getHolder().addCallback(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float widthSize = MeasureSpec.getSize(widthMeasureSpec);
        float heightSize = MeasureSpec.getSize(heightMeasureSpec);
        screenProp = heightSize / widthSize;
    }

    @Override
    public void cameraHasOpened() {
        CameraInterface.getInstance().doStartPreview(mVideoView.getHolder(), screenProp);
    }

    private boolean switching = false;

    @Override
    public void cameraSwitchSuccess() {
        switching = false;
    }

    /**
     * start preview
     */
    public void onResume() {
        CameraInterface.getInstance().registerSensorManager(mContext);
        if (!CameraInterface.getInstance().isPreviewing()) {
            new Thread() {
                @Override
                public void run() {
                    CameraInterface.getInstance().doOpenCamera(WCameraView.this);
                    CameraInterface.getInstance().setSwitchView(mSwitchCamera);
                }
            }.start();
        }
    }

    /**
     * stop preview
     */
    public void onPause() {
        CameraInterface.getInstance().unregisterSensorManager(mContext);
        CameraInterface.getInstance().doStopCamera();
    }

    private boolean firstTouch = true;
    private float firstTouchLength = 0;
    private int zoomScale = 0;

    /**
     * handler touch focus
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerCount() == 1) {
                    //显示对焦指示器
                    setFocusViewWidthAnimation(event.getX(), event.getY());
                }
                if (event.getPointerCount() == 2) {
                    Log.i("Camera", "ACTION_DOWN = " + 2);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    firstTouch = true;
                }
                if (event.getPointerCount() == 2) {
                    //第一个点
                    float point_1_X = event.getX(0);
                    float point_1_Y = event.getY(0);
                    //第二个点
                    float point_2_X = event.getX(1);
                    float point_2_Y = event.getY(1);

                    float result = (float) Math.sqrt(Math.pow(point_1_X - point_2_X, 2) + Math.pow(point_1_Y -
                            point_2_Y, 2));

                    if (firstTouch) {
                        firstTouchLength = result;
                        firstTouch = false;
                    }
                    if ((int) (result - firstTouchLength) / 50 != 0) {
                        firstTouch = true;
                        CameraInterface.getInstance().setZoom(result - firstTouchLength, CameraInterface.TYPE_CAPTURE);
                    }
                    Log.i("Camera", "result = " + (result - firstTouchLength));
                }
                break;
            case MotionEvent.ACTION_UP:
                firstTouch = true;
                break;
        }
        return true;
    }

    /**
     * focusview animation
     */
    private void setFocusViewWidthAnimation(float x, float y) {
        if (isBorrow) {
            return;
        }
        if (y > mCaptureLayout.getTop()) {
            return;
        }
        mFoucsView.setVisibility(VISIBLE);
        if (x < mFoucsView.getWidth() / 2) {
            x = mFoucsView.getWidth() / 2;
        }
        if (x > layout_width - mFoucsView.getWidth() / 2) {
            x = layout_width - mFoucsView.getWidth() / 2;
        }
        if (y < mFoucsView.getWidth() / 2) {
            y = mFoucsView.getWidth() / 2;
        }
        if (y > mCaptureLayout.getTop() - mFoucsView.getWidth() / 2) {
            y = mCaptureLayout.getTop() - mFoucsView.getWidth() / 2;
        }
        CameraInterface.getInstance().handleFocus(mContext, x, y, new CameraInterface.FocusCallback() {
            @Override
            public void focusSuccess() {
                mFoucsView.setVisibility(INVISIBLE);
            }
        });

        mFoucsView.setX(x - mFoucsView.getWidth() / 2);
        mFoucsView.setY(y - mFoucsView.getHeight() / 2);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mFoucsView, "scaleX", 1, 0.6f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mFoucsView, "scaleY", 1, 0.6f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mFoucsView, "alpha", 1f, 0.3f, 1f, 0.3f, 1f, 0.3f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleX).with(scaleY).before(alpha);
        animSet.setDuration(400);
        animSet.start();
    }

    public void setJCameraLisenter(WCameraLisenter wCameraLisenter) {
        this.mCameraLisenter = wCameraLisenter;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void handlerPictureOrVideo(int type, boolean confirm) {
        if (mCameraLisenter == null || type == -1) {
            return;
        }
        switch (type) {
            case TYPE_PICTURE:
                mPhoto.setVisibility(INVISIBLE);
                if (confirm && captureBitmap != null) {
                    mCameraLisenter.captureSuccess(captureBitmap);
                } else {
                    if (captureBitmap != null) {
                        captureBitmap.recycle();
                    }
                    captureBitmap = null;
                }
                break;
            case TYPE_VIDEO:
                if (confirm) {
                    //回调录像成功后的URL
                    mCameraLisenter.recordSuccess(videoUrl);
                } else {
                    //删除视频
                    File file = new File(videoUrl);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                mCaptureLayout.isRecord(false);
                LayoutParams videoViewParam = new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT);
                videoViewParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout
                        .TRUE);
                mVideoView.setLayoutParams(videoViewParam);
                break;
        }
        isBorrow = false;
        mSwitchCamera.setVisibility(VISIBLE);
        CAMERA_STATE = STATE_IDLE;
    }

    public void setSaveVideoPath(String path) {
        CameraInterface.getInstance().setSaveVideoPath(path);
    }


    /**
     * TextureView resize
     */
    public void updateVideoViewSize(float videoWidth, float videoHeight) {
        if (videoWidth > videoHeight) {
            LayoutParams videoViewParam;
            int height = (int) ((videoHeight / videoWidth) * getWidth());
            videoViewParam = new LayoutParams(LayoutParams.MATCH_PARENT,
                    height);
            videoViewParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout
                    .TRUE);
            mVideoView.setLayoutParams(videoViewParam);
        }
    }

    /**
     * forbidden audio
     */
    public void enableshutterSound(boolean enable) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("Camera", "surfaceCreated");
        CameraInterface.getInstance().doStartPreview(holder, screenProp);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("Camera", "surfaceDestroyed");
        CameraInterface.getInstance().doDestroyCamera();
    }
}
