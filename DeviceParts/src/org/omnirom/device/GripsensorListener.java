package org.omnirom.device;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.util.Log;

public abstract class GripsensorListener implements SensorEventListener {
    private static final int GRIP_GESTURE_LONG_SQUEEZE = 8;
    private static final int GRIP_GESTURE_SHORT_SQUEEZE = 7;
    private static final String TAG = "GripSensorListenerOmni";
    protected Handler mHandler;
    private boolean mIsRegistered = false;
    private Sensor mSensor;
    private SensorManager mSensorManager;
    private PowerManager.WakeLock mWakeLock;

    protected GripsensorListener(SensorManager sensorManager, int type, boolean wakeUp, Handler handler, Context context) {
        mSensor = null;
        mSensorManager = null;
        mHandler = null;
        mHandler = handler;
        mSensorManager = sensorManager;
        Sensor defaultSensor = sensorManager.getDefaultSensor(type);
        mSensor = defaultSensor;
        if (defaultSensor == null) {
            Log.w(TAG, "Can not get sensor: " + type + ". wakeUp: " + wakeUp);
        }
        PowerManager powerManager = null;
        powerManager = context != null ? (PowerManager) context.getSystemService("power") : powerManager;
        if (powerManager != null) {
            mWakeLock = powerManager.newWakeLock(1, "GripSensor.WakeLock");
        }
    }

    public boolean register() {
        Sensor sensor;
        if (!mIsRegistered && (sensor = mSensor) != null) {
            boolean registerListener = mSensorManager.registerListener(this, sensor, 3, mHandler);
            mIsRegistered = registerListener;
            return registerListener;
        }
        return false;
    }

    public void unregister() {
        Sensor sensor;
        if (mIsRegistered && (sensor = mSensor) != null) {
            mSensorManager.unregisterListener(this, sensor);
            mIsRegistered = false;
        }
    }

    public Sensor getSensor() {
        return mSensor;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    protected void acquireWakelock(int gestureType) {
        PowerManager.WakeLock wakeLock;
        if ((gestureType == GRIP_GESTURE_SHORT_SQUEEZE || gestureType == GRIP_GESTURE_LONG_SQUEEZE) && (wakeLock = mWakeLock) != null && !wakeLock.isHeld()) {
            mWakeLock.acquire(1000L);
        }
    }
}
