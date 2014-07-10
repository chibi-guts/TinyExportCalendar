package com.example.tinyexportcalendar.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Maria on 07-Jul-14.
 */
public class BorderFlowerView extends View {
    Context context;
    private float curOffset=0;
    private Set<Integer> mFlowY = new HashSet<Integer>();

    Paint outlPaint = new Paint();
    Paint leafPaint = new Paint();

    int maxx = 120;
    int maxy = 240;
    int x = 170;
    int dx = -20;
    int dy = -100;

    public BorderFlowerView(Context context) {
        super(context);
        this.context = context;
        setWillNotDraw(false);
    }

    public BorderFlowerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setWillNotDraw(false);
    }

    public BorderFlowerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setWillNotDraw(false);
    }

    void setPaint(){
        outlPaint.setColor(Color.GRAY);
        outlPaint.setStrokeWidth(5.0f);
        outlPaint.setStyle(Paint.Style.STROKE);
        outlPaint.setStrokeCap(Paint.Cap.ROUND);

        leafPaint.setColor(getResources().getColor(R.color.pale_green));
        leafPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setPaint();
        float offsetFactor = Math.abs(curOffset*2 - 1);
        TypedArray ta = getResources().obtainTypedArray(R.array.simple_path);
        for (int i = 0; i<ta.length(); i++){
            canvas.drawPath(generateComplexPath(parseFloatArray(getResources().getStringArray(ta.getResourceId(i,i))), 1, 0), outlPaint);
        }
        ta = getResources().obtainTypedArray(R.array.leafs);
        for (int i = 0; i<ta.length(); i++){
            drawLeaf(canvas, parseFloatArray(getResources().getStringArray(ta.getResourceId(i,i))));
        }
        ta = getResources().obtainTypedArray(R.array.lines);
        for (int i = 0; i<ta.length(); i++){
            canvas.drawPath(generateLinesPath(parseFloatArray(getResources().getStringArray(ta.getResourceId(i,i))), 1), outlPaint);
        }
        /*for (int fY : mFlowY) {
            float fldy = (fY+0.5f)*(MyFragment.winWidth/10);
            canvas.drawPath(generateComplexPath(smallflow, offsetFactor, fldy), outlPaint);
        }*/
    }

    void drawLeaf(Canvas canvas, float[] pathVals) {
        float [] mPathVals = new float[pathVals.length-6];
        mPathVals[0] = pathVals[6] + pathVals[0];
        mPathVals[1] = pathVals[7] + pathVals[1];
        for (int i=2; i<mPathVals.length; i++){
            mPathVals[i] = pathVals[i+6];
        }
        Path leafPath = generateComplexPath(mPathVals, 1, 0);
        leafPath.setFillType(Path.FillType.WINDING);
        canvas.drawPath(leafPath,leafPaint);
        canvas.drawPath(generateComplexPath(pathVals, 1, 0), outlPaint);
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

    float [] smallflow = new float[] {0.43354171999999996f, 0.6709621707317074f, 0.03650878f, -0.0756889268292683f, 0.41985092f, -0.04674902439024391f, 0.41985092f, -0.04674902439024391f, 0.0f, 0.0f, -0.03509248f, 0.01431239024390244f, 0.16106254f, 0.03287519512195122f, 0.08649912f, 0.008185682926829268f, 0.24708738f, 0.01149941463414634f, 0.28109206f, -0.0025917317073170733f, 0.01834108f, -0.014299609756097561f, -0.07333878f, -0.011681975609756097f, -0.1782096f, -0.02002370731707317f, 0.0f, 0.0f, 0.11763222000000001f, 0.0035917804878048782f, 0.18489292f, -0.013901829268292682f, -0.015024599999999999f, -0.013516731707317072f, -0.10044560000000001f, -0.013324560975609757f, -0.22130672f, -0.012120317073170732f, 0.07894498f, -0.008618682926829269f, 0.14805502f, -0.012710634146341463f, 0.07122224f, -0.029591926829268294f, -0.13146634000000001f, 0.0007195365853658537f, -0.19176494000000002f, 0.007095317073170731f, -0.24477326000000002f, 0.019045707317073172f, -0.04125976f, 0.009301731707317074f, -0.07158594f, 0.012176268292682927f, -0.05398016f, 0.03076090243902439f};

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

    void setCurOffset(float offset) {
        curOffset = offset;
    }
    void setCoords(Set<Integer> coords) {
        this.mFlowY = coords;
    }
}
