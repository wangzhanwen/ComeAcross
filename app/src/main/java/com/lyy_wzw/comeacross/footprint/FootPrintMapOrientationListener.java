package com.lyy_wzw.comeacross.footprint;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by yidong9 on 17/5/15.
 */

public class FootPrintMapOrientationListener implements SensorEventListener{
    private SensorManager mySensorManager;
    private Sensor mySensor;
    private Context myContext;
    private float lastX;
    private onOrientationListener myOrientationListener;
    public void start(){
        mySensorManager=(SensorManager) myContext.getSystemService(Context.SENSOR_SERVICE);
        if (mySensorManager!=null) {

            mySensor=mySensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        if (mySensor!=null) {
            mySensorManager.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_UI);
        }
    }
    public void stop(){
        mySensorManager.unregisterListener(this);
    }
    public FootPrintMapOrientationListener(Context myContext) {
        this.myContext = myContext;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ORIENTATION) {
            float x=event.values[SensorManager.DATA_X];


            if (Math.abs(x-lastX)>1.0) {
                if (myOrientationListener!=null) {

                    myOrientationListener.onOrientationChanged(lastX);
                }
            }
            lastX=x;
        }
    }

    public void setMyOrientationListener(onOrientationListener myOrientationListener) {
        this.myOrientationListener = myOrientationListener;
    }

    public interface onOrientationListener{
        void onOrientationChanged(float x);
    }
}