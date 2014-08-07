package com.example.tinyexportcalendar.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Maria on 14-Jul-14.
 */
public class MyCustomBtn extends View {
    int color= Color.LTGRAY, id=1;
    public MyCustomBtn(Context context) {
        super(context);
    }
    public MyCustomBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCustomBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        DrawingHelper dh = new DrawingHelper(17, 17, 17, 0, 0);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        Path path = dh.generateComplexPath(dh.parseFloatArray(
                getResources().getStringArray((
                        getResources().obtainTypedArray(R.array.btns)).getResourceId(id, id))), 1, 0);
        path.setFillType(Path.FillType.WINDING);
        canvas.drawPath(path, paint);
    }

    void setInd(int id){
        this.id = id;
    }
    void setCol(int color) {
        this.color = color;
    }
}