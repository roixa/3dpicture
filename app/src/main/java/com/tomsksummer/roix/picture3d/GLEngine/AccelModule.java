package com.tomsksummer.roix.picture3d.GLEngine;

import android.util.Log;

import com.tomsksummer.roix.picture3d.GLEngine.items.Point;

import java.util.logging.XMLFormatter;

/**
 * Created by root on 05.08.14.
 */
public class AccelModule {

    public  float acX,acY,acZ;



    public  Point accel=new Point(0,0,0);
    public  final Point gravity=new Point(0,0,10);
    private static final float  sensetivity=70f;

    private int weight;
    private float XAngle;
    private float YAngle;
    private float ZAngle;

    public AccelModule(){
        weight=6;
        XAngle=0;
        YAngle=0;
        ZAngle=0;
    }

    //accel values,gravity vector


    public  void setAcc(float x,float y,float z){
        acX=x;acY=y;acZ=z;
        accel=new Point(x,y,z);
    }

    public void returnSurfaceAngle(){
        XAngle=0; YAngle=0;
    }

//rotation angle around x axis in degrees
    public  float getXAngle(){
       Point project=new Point(0,accel.y,accel.z);
       float angl=(float)Math.asin(project.y / project.module())*sensetivity;
       //float angl=(float)Math.acos(accel.z/accel.module())*100f;

        return (float)(Math.rint( toFlowX(angl)/1.0) * 1.0);
    }

    public float getYAngle(){
        Point project=new Point(accel.x,0,accel.z);
        float angl=-(float)Math.asin(project.x/project.module())*sensetivity;
        //Log.d("physic base", String.valueOf(angl));
        return (float)(Math.rint( toFlowY(angl)/1.0) * 1.0);
    }

    public float getZAngle(){
        Point project=new Point(accel.x,accel.y,0);
        float angl=-(float)Math.asin(project.z/project.module())*sensetivity;
        ///Log.d("physic base", String.valueOf(angl));
        return (float)(Math.rint( toFlowZ(angl)/1.0) * 1.0);
    }

        //make flowing change of angle
    private float toFlowX(float x){
        if(XAngle==0) XAngle=x;
        float flow= (XAngle*weight+x)/((weight+1f));
        XAngle=flow;
        return flow;
    }
    private float toFlowY(float y){
        if(YAngle==0) YAngle=y;
        float flow= (YAngle*weight+y)/((weight+1f));
        YAngle=flow;
        return flow;
    }
    private float toFlowZ(float z){
        if(ZAngle==0) ZAngle=z;
        float flow= (ZAngle*weight+z)/((weight+1f));
        ZAngle=flow;
        return flow;
    }

}

