package com.tomsksummer.roix.picture3d.GLEngine.items;

/**
 * Created by root on 05.08.14.
 */
public class Point {

    public float x,y,z;

    public Point (float a,float b, float c){
        x=a;    y=b;   z=c;
    }
    public static Point normal(float x,float y,float z){
        Point p=new Point(x,y,z);
        float mod=p.module();

        return new Point(x/mod,y/mod,z/mod);
    }

    public  float module(){
        return (float)Math.pow((x*x+y*y+z*z),0.5);
    }
}
