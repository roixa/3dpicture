package com.tomsksummer.roix.picture3d;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.tomsksummer.roix.picture3d.GLEngine.PictureSurfaceView;
import com.tomsksummer.roix.picture3d.GLEngine.mSurfaceView;
import com.tomsksummer.roix.picture3d.R;

public class PictureActivity extends FragmentActivity implements View.OnTouchListener{

    private PictureSurfaceView mGLSurfaceView;
    private SensorManager sensorManager;
    private Sensor sensorAccel;//accelerator sensor
    private Button menuButton;
    private ImageButton handButton;
    private ImageButton bendButton;
    private Button undoButton;
    private SeekBar seek;
    private ScaleGestureDetector SGD;
    private GestureDetector myG;//gestures


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        PreferenceHelper.init(getApplicationContext());
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        setContentView(R.layout.activity_picture);
        mGLSurfaceView = ( PictureSurfaceView)findViewById(R.id.surfaceView);
        menuButton=(Button) findViewById(R.id.menubutton);
        handButton=(ImageButton)findViewById(R.id.handButton);
        bendButton=(ImageButton)findViewById(R.id.bendButton);
        undoButton=(Button)findViewById(R.id.undoButton);
        seek=(SeekBar)findViewById(R.id.seekBar);
        seek.setVisibility(View.GONE);
        SGD = new ScaleGestureDetector(this,new ScaleListener());
        myG = new GestureDetector(this,new Gesture());
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
                    case R.id.menubutton:
                        showMenuDialog();
                        break;
                }

            }
        };
        menuButton.setOnClickListener(buttonsListener);
        handButton.setOnClickListener(buttonsListener);
        bendButton.setOnClickListener(buttonsListener);
        undoButton.setOnClickListener(buttonsListener);
        bendButton.setOnTouchListener(this);
        final int seekSz=100;
        seek.setProgress(seekSz/2+seekSz*(int)PreferenceHelper.getShift());

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                PreferenceHelper.saveShift((float)(seekBar.getProgress()-seekSz/2)/(float)seekSz);
                seek.setVisibility(View.GONE);
            }
        });



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
    public boolean onTouchEvent(MotionEvent e){
        //mGLSurfaceView.requestRender();
        // Be sure to call the superclass implementation
        //SGD.onTouchEvent(e);
        //myG.onTouchEvent(e);
        return super.onTouchEvent(e);
    }


    private void showMenuDialog() {
        FragmentManager fm = getSupportFragmentManager();
        MenuDialog dialog = new MenuDialog();

        dialog.show(fm, "fragment_edit_name");
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        SGD.onTouchEvent(e);
        myG.onTouchEvent(e);
        return true;
    }


    class Gesture extends GestureDetector.SimpleOnGestureListener{
        public boolean onSingleTapUp(MotionEvent ev) {
            mGLSurfaceView.setDrawMode();
            return true;
        }

        public void onLongPress(MotionEvent ev) {
            seek.setVisibility(View.VISIBLE);

        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                float distanceY) {
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
