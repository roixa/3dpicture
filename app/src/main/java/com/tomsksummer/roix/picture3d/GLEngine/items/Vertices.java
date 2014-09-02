package com.tomsksummer.roix.picture3d.GLEngine.items;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by root on 06.08.14.
 */
public class Vertices extends Object {
    private final String TAG="Vertices";
    public  float lbx, lby, lbz;
    public  float rbx, rby, rbz;
    public  float ltx, lty, ltz;
    public  float rtx, rty, rtz;

    private boolean touched;
    private FloatBuffer encoded;

    public Vertices(float plbx, float plby, float plbz,
                    float prbx, float prby, float prbz,
                    float pltx, float plty, float pltz,
                    float prtx, float prty, float prtz){
        lbx=plbx; lby=plby; lbz=plbz;
        rbx=prbx; rby=prby; rbz=prbz;
        ltx=pltx; lty=plty; ltz=pltz;
        rtx=prtx; rty=prty; rtz=prtz;
        encode();

        touched=false;
    }

    public Vertices(float x, float y, float s
                    ){
        lbx=x; lby=y; lbz=0;
        rbx=x+s; rby=y; rbz=0;
        ltx=x; lty=y+s; ltz=0;
        rtx=x+s; rty=y+s; rtz=0;
        encode();
        touched=false;

    }
    public Vertices(Vertices v
    ){
        lbx=v.lbx; lby=v.lby; lbz=v.lbz;
        rbx=v.rbx; rby=v.rby; rbz=v.rbz;
        ltx=v.ltx; lty=v.lty; ltz=v.ltz;
        rtx=v.rtx; rty=v.rty; rtz=v.rtz;
        encode();
        touched=false;

    }

    public boolean isTouched(){
        return touched;
    }

    public void setTouch(boolean touched){
        this.touched=touched;
        //encode();
    }




    /**
     * @param  z1 value of z shift of lb point
     * @param z2   etc
     * */
    public void shift(float z1,float z2,float z3, float z4){
        lbz+=z1; rbz+=z2; ltz+=z3; rtz+=z4;
        encode();
    }

    public void shift(float z){
        shift(z,z,z,z);
    }

    public boolean crossedWith(Vertices v){
        /*boolean conxi=ltx>=v.ltx;
        boolean conyi=lty>=v.lty;
        boolean conxa=ltx<=v.rbx;
        boolean conya=ltx<=v.rby;
        boolean contains=conxi&&conxa&&conya&&conyi;*/


        boolean sqlb=pointInVertices(v,lbx,lby);
        //point on a square

        boolean sqrb=pointInVertices(v,ltx,lty);


        boolean sqlt=pointInVertices(v,rbx,rby);


        boolean sqrt=pointInVertices(v,rtx,rty);


        boolean onTheSquare=sqrb||sqlb||sqlt||sqrt;

        if(onTheSquare){
            //Log.d("PictureModel","contains"+String.valueOf(v.lbx)+String.valueOf(v.lby));
        }
        //return true;
        return onTheSquare;

    }


    public FloatBuffer en(){//encode for loading in gl
        return encoded;
    }



    //distance of touching point to  vertices
    public float distanceTo(float x,float y){

            float dist;
            dist = module(lbx - x, lby - y);
            float tem=module(ltx-x,lty-y);
            dist=dist<tem?dist:tem;
            tem=module(rbx-x,rby-y);
            dist=dist<tem?dist:tem;
            tem=module(rtx-x,rty-y);
            dist=dist<tem?dist:tem;
            return dist;

    }

    private void encode(){
        float [] vertices={ // Vertices for a face
                lbx, lby, lbz,  // 0. left-bottom-front
                rbx, rby, rbz,  // 1. right-bottom-front
                ltx, lty, ltz,  // 2. left-top-front
                rtx, rty, rtz   // 3. right-top-front
        };

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        FloatBuffer vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
        vertexBuffer.put(vertices);         // Copy data into buffer
        vertexBuffer.position(0);           // Rewind
        encoded=vertexBuffer;
    }

    private float module(float x, float y){
        return (float)Math.pow((x*x+y*y),0.5);
    }



    private  boolean pointInVertices(Vertices v,float x,float y){
        boolean one=x>=v.lbx&&v.lby<=y;
        boolean two=x<=v.rtx&&v.rty>=y;
        //Log.d(TAG,String.valueOf(lty)+"  "+String.valueOf(rby));
        return one&&two;
    }
}
