package com.tomsksummer.roix.picture3d;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;

import com.tomsksummer.roix.picture3d.GLEngine.PictureSurfaceView;
import com.tomsksummer.roix.picture3d.GLEngine.mSurfaceView;
import com.tomsksummer.roix.picture3d.R;

public class PictureActivity extends Activity {

    private PictureSurfaceView mGLSurfaceView;
    private SensorManager sensorManager;
    private Sensor sensorAccel;//accelerator sensor
    private ImageButton handButton;
    private ImageButton bendButton;
    private ImageButton undoButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        setContentView(R.layout.activity_picture);
        mGLSurfaceView = ( PictureSurfaceView)findViewById(R.id.surfaceView);
        handButton=(ImageButton)findViewById(R.id.handButton);
        bendButton=(ImageButton)findViewById(R.id.bendButton);
        undoButton=(ImageButton)findViewById(R.id.undoButton);

        View.OnClickListener buttonsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.handButton:
                        //handButton.setSelected(!mGLSurfaceView.changeMode());
                        mGLSurfaceView.setMoveMode();
                        break;
                    case R.id.bendButton:
                        mGLSurfaceView.setDrawMode();
                        break;
                    case R.id.undoButton:
                        mGLSurfaceView.buttonPushedBack();
                        break;
                }

            }
        };

        handButton.setOnClickListener(buttonsListener);
        bendButton.setOnClickListener(buttonsListener);
        undoButton.setOnClickListener(buttonsListener);

    }


    @Override
    protected void onResume()
    {
        // Вызывается GL surface view's onResume() когда активити переходит onResume().
        super.onResume();
        sensorManager.registerListener(listener, sensorAccel,
                SensorManager.SENSOR_DELAY_NORMAL);

        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause()
    {
        // Вызывается GL surface view's onPause() когда наше активити onPause().
        super.onPause();
        mGLSurfaceView.onPause();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){

        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }



    class Gesture extends GestureDetector.SimpleOnGestureListener{
        public boolean onSingleTapUp(MotionEvent ev) {
            return true;
        }
        public void onLongPress(MotionEvent ev) {

        }
        private float dist=0;
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                float distanceY) {
            if(e1.getPointerCount()>1||e2.getPointerCount()>1){
                return false;
            }
            dist+=distanceX;
            Log.d("scale", "on scroll" + String.valueOf(dist));
            return true;
        }
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return true;
        }
    }




    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = detector.getScaleFactor();

            Log.d("scale ", String.valueOf(scale));
            return true;
        }
    }


    float[] valuesAccel = new float[3];


    SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            int type=event.sensor.getType();
            int pref=Sensor.TYPE_ACCELEROMETER;
            if(type==pref){

                for (int i = 0; i < 3; i++)    valuesAccel[i] = event.values[i];

                mGLSurfaceView.renderer.base.setAcc(valuesAccel[0],valuesAccel[1],valuesAccel[2]);
                mGLSurfaceView.requestRender();
            }
        }

    };
}
