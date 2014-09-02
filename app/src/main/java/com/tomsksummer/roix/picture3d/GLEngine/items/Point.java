package com.tomsksummer.roix.picture3d.GLEngine.items;

/**
 * Created by root on 05.08.14.
 */
public class Point {

    public float x,y,z;

    public Point (float a,float b, float c){
        x=a;    y=b;   z=c;
    }

    public  float module(){
        return (float)Math.pow((x*x+y*y+z*z),0.5);
    }
}
