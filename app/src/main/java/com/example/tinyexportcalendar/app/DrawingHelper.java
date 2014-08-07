package com.example.tinyexportcalendar.app;

import android.graphics.Path;

/**
 * Created by Maria on 14-Jul-14.
 */
public class DrawingHelper {
    int maxx, maxy , x , dx , dy;
    DrawingHelper (int maxx, int maxy, int x, int dx, int dy) {
        this.maxx = maxx;
        this.maxy = maxy;
        this.x = x;
        this.dx = dx;
        this.dy = dy;
    }
    Path generateComplexPath(float[] pathVals, float factor, float fldy){
        Path mPath = new Path();
        float scaleFact = maxy/maxx;

        mPath.moveTo(pathVals[0]*x+dx,pathVals[1]*x*scaleFact+fldy+dy);
        for(int i = 2; i < pathVals.length-5; i=i+6) {
            mPath.rCubicTo(pathVals[i]*x*factor, pathVals[i + 1]*x*scaleFact*factor,
                    pathVals[i + 2]*x*factor, pathVals[i + 3]*x*scaleFact*factor,
                    pathVals[i + 4]*x*factor, pathVals[i + 5]*x*scaleFact*factor);
        }
        return mPath;
    }

    Path generateLinesPath(float[] pathVals, float factor){
        Path mPath = new Path();
        float scaleFact = maxy/maxx;

        mPath.moveTo(pathVals[0]*x+dx, pathVals[1]*x*scaleFact+dy);
        mPath.rLineTo((pathVals[2]-pathVals[0])*x, (pathVals[3]-pathVals[1])*x*scaleFact);
        return mPath;
    }
    float[] parseFloatArray(String[] strPath) {
        Path mPath = new Path();
        float [] pathVals = new float[strPath.length];

        float scaleFact = maxy/maxx;
        for (int i=0; i<strPath.length-1; i+=2) {
            pathVals[i] = Float.parseFloat(strPath[i])/maxx;
            pathVals[i+1] = Float.parseFloat(strPath[i+1])/maxy;
        }
        return pathVals;
    }
}
