package com.example.tinyexportcalendar.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Maria on 07-Jul-14.
 */
public class BorderFlowerView extends View {
    public BorderFlowerView(Context context) {
        super(context);
    }

    public BorderFlowerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BorderFlowerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        outlPaint.setStrokeWidth(3.0f);
        outlPaint.setStyle(Paint.Style.STROKE);
        outlPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawPath(generateCaulisPath(), outlPaint);
    }

    Path generateCaulisPath(){
        Path mPath = new Path();
        mPath.moveTo(pathVals[0]*70,pathVals[1]*430*70/70);
        for(int i = 2; i < pathVals.length-3; i=i+4) {
            mPath.rQuadTo(pathVals[i]*70,pathVals[i+1]*430*70/70,pathVals[i+2]*70,pathVals[i+3]*430*70/70);
        }
        return mPath;
    }
    float [] pathVals = new float[] {0.97791356f,  0.07032632f,  0.0f,  0.0f,  0.043024f,        -0.07289865f, -0.41724307f, -0.04275032f, -0.78600884f,  0.05148501f,        -0.37606426f,  0.21664843f,  0.06519421f,  0.24795188f,  0.28942986f,        0.02053253f,  0.599787f,  0.11115082f,  0.169505f,  0.19023892f,       -0.43028199f,  0.0790881f, -0.22895329f,  0.15747084f,  0.01303886f,        0.20306403f,  0.13553957f,  0.0255367f,  0.36590257f,  0.06105105f,        0.29989357f,  0.13038852f, -0.12957771f,  0.13611171f, -0.57370933f,        0.13038845f, -0.62586473f,  0.22230164f};
    float [] pathVals0 = new float[] {0.97791356f,  0.07032632f,  0.97791356f,  0.07032632f,  1.02093756f,  -0.0025723299999999977f,  0.6036944899999999f,  -0.04532265f,  -0.18231435000000018f,  0.006162359999999999f,  -0.5583786100000001f,  0.22281079f,  -0.4931844000000001f,  0.47076267000000005f,  -0.2037545400000001f,  0.49129520000000004f,  0.39603245999999986f,  0.60244602f,  0.5655374599999998f,  0.79268494f,  0.13525546999999982f,  0.87177304f,  -0.09369782000000018f,  1.02924388f,  -0.08065896000000018f,  1.23230791f,  0.054880609999999816f,  1.25784461f,  0.4207831799999998f,  1.3188956600000001f,  0.7206767499999998f,  1.4492841800000003f,  0.5910990399999998f,  1.5853958900000003f,  0.01738970999999978f,  1.7157843400000004f,  -0.6084750200000002f,  1.9380859800000003f};
}
