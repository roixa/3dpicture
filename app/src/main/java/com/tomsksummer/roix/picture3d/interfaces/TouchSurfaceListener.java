package com.tomsksummer.roix.picture3d.interfaces;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by root on 06.08.14.
 */
public interface TouchSurfaceListener {
    void OnTouch(float x, float y);
    void OnStartTouch();
    void OnEndTouch();
}
