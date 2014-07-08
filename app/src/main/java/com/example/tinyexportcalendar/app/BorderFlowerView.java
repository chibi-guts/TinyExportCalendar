package com.example.tinyexportcalendar.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by Maria on 07-Jul-14.
 */
public class BorderFlowerView extends View {
    private final Interpolator mInterpolator;
    // Frame of transition between current and next frames.
    private int mFrame = 0;
    private int mCurrent = 0;
    private int mNext = 1;
    float[] rads = new float[]{15.0f,30.0f,45.0f,60.0f,70.0f,80.0f,90.0f,100.0f,105.0f,110.0f};
    float curOffset=0;

    public BorderFlowerView(Context context) {
        super(context);
        setWillNotDraw(false);
        mInterpolator = new AccelerateDecelerateInterpolator();
    }

    public BorderFlowerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        mInterpolator = new AccelerateDecelerateInterpolator();
    }

    public BorderFlowerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        mInterpolator = new AccelerateDecelerateInterpolator();
    }

    /*@Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(70, 70 * 7 + 5 * 6 + 10);
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint outlPaint = new Paint();
        outlPaint.setColor(Color.BLACK);
        outlPaint.setStrokeWidth(5.0f);
        outlPaint.setStyle(Paint.Style.STROKE);
        outlPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawPath(generateCaulisPath(), outlPaint);
        canvas.drawCircle(50f, 150f, rads[4]*(1-curOffset), outlPaint);
//        if(elem_x)

        // Frames 0, 1 is the first pause.
        // Frames 9, 10 is the last pause.
        // Constrain current frame to be between 0 and 6.
        /*final int currentFrame;
        if (mFrame < 2) {
            currentFrame = 0;
        } else if (mFrame > 8) {
            currentFrame = 6;
        } else {
            currentFrame = mFrame - 2;
        }
        */
        // A factor of the difference between current
        // and next frame based on interpolation.
        // Only 6 frames are used between the transition.
        /*final float factor = mInterpolator.getInterpolation(currentFrame / 6.0f);
        final float current = rads[mCurrent];
        final float next = rads[mNext];
        canvas.drawCircle(60f, 150f, current + (next-current)*factor, outlPaint);
        */
        // Next frame.
        //mFrame++;

        // Each number change has 10 frames. Reset.
        /*if (mFrame == 10) {
            mFrame = 0;
            mCurrent = mNext;
            mNext++;
            if (mNext == 10) {
                mNext = 0;
            }
        }

        // Callback for the next frame.
        postInvalidateDelayed(100);*/
    }

    Path generateCaulisPath(){
        Path mPath = new Path();
        int x=70;
        int dx = 10;
        int maxx = 50;
        int maxy = 453-43;
        float fact = maxy/maxx;
        mPath.moveTo(pathVals[0]*x+dx,pathVals[1] * x * fact);
        for(int i = 2; i < pathVals.length-5; i=i+6) {
            mPath.cubicTo(pathVals[i] * x + dx, pathVals[i + 1] * x * fact, pathVals[i + 2] * x + dx, pathVals[i + 3] * x * fact,
                    pathVals[i + 4] * x + dx, pathVals[i + 5] * x * fact);
        }
        return mPath;
    }

    void drawInterElem(float x, float y){
        //elem_x = x;
        //elem_y = y;
    }

    float [] pathVals00 = new float[] {0.97791356f,  0.07032632f,  0.0f,  0.0f,  0.043024f,        -0.07289865f, -0.41724307f, -0.04275032f, -0.78600884f,  0.05148501f,        -0.37606426f,  0.21664843f,  0.06519421f,  0.24795188f,  0.28942986f,        0.02053253f,  0.599787f,  0.11115082f,  0.169505f,  0.19023892f,       -0.43028199f,  0.0790881f, -0.22895329f,  0.15747084f,  0.01303886f,        0.20306403f,  0.13553957f,  0.0255367f,  0.36590257f,  0.06105105f,        0.29989357f,  0.13038852f, -0.12957771f,  0.13611171f, -0.57370933f,        0.13038845f, -0.62586473f,  0.22230164f};
    float [] pathVals0 = new float[] {0.97791356f,  0.07032632f,  0.97791356f,  0.07032632f,  1.02093756f,  -0.0025723299999999977f,  0.6036944899999999f,  -0.04532265f,  -0.18231435000000018f,  0.006162359999999999f,  -0.5583786100000001f,  0.22281079f,  -0.4931844000000001f,  0.47076267000000005f,  -0.2037545400000001f,  0.49129520000000004f,  0.39603245999999986f,  0.60244602f,  0.5655374599999998f,  0.79268494f,  0.13525546999999982f,  0.87177304f,  -0.09369782000000018f,  1.02924388f,  -0.08065896000000018f,  1.23230791f,  0.054880609999999816f,  1.25784461f,  0.4207831799999998f,  1.3188956600000001f,  0.7206767499999998f,  1.4492841800000003f,  0.5910990399999998f,  1.5853958900000003f,  0.01738970999999978f,  1.7157843400000004f,  -0.6084750200000002f,  1.9380859800000003f};
    float [] pathVals= new float[] {0.9797398026359226f, 0.1849867959752283f, 0.9924906962418972f, 0.11971526582829232f, 0.6034084548005291f, 0.13134774631361432f, 0.29991223462432715f, 0.17527041317068495f, 0.04004556974475781f, 0.2128789080531839f, 0.29977073711431496f, 0.32091813872272107f, 0.4453362354830447f, 0.3549374301128919f, 0.6560916697245986f, 0.4041921677548889f, 0.8758279346625456f, 0.4442704644718597f, 0.5344034533043067f, 0.5532131759081949f, 0.22683914718709078f, 0.6513517024004771f, 0.3479661226791642f, 0.713729395112361f, 0.6234707199178137f, 0.7602043204755974f, 0.7777805855003999f, 0.7862348802678566f, 1.040045569744758f, 0.8224360590776593f, 0.964895266332379f, 0.893114468055637f, 0.8173728899283438f, 1.031858483047898f, 0.3117354087511517f, 1.026024544019332f, 0.2523572310088138f, 1.1197152658282923f};

    void setCurOffset(float offset) {
        curOffset = offset;
    }
}
