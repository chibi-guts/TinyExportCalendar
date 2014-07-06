package com.example.tinyexportcalendar.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Maria on 24-May-14.
 */
public class MonthView extends View {
    int position;
    int dayOfWeek =1, maxPrevDay=31, lastDayNum=35;
    final static String[] TAGS = {"0","1","2","sex"};
    int [] nums = {1,2,3,4,5,6,7,
                    8,9,10,11,12,13,14,
                    15,16,17,18,19,20,21,
                    22,23,24,25,26,27,28,
                    29,30,31,32,33,34,35,
                    36,37,38,39,40,41,42};
    String [] dayNames;
    float rWidth=70, rHeigth=100, left = 10, top = 10;
    int [] markingColors = new int[4];
    int [] greenColors = new int[2];

    public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        rWidth = MyFragment.winWidth/10;
        rHeigth = MyFragment.winHeight/15;
    }
    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        rWidth = MyFragment.winWidth/10;
        rHeigth = MyFragment.winHeight/15;
    }
    public MonthView(Context context) {
        super(context);
        rWidth = MyFragment.winWidth/10;
        rHeigth = MyFragment.winHeight/15;
    }

    void setNums(int [] nums) {
        this.nums = nums;
        this.invalidate();
    }

    void setPosition(int position) {
        this.position = position;
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension((int)rWidth*7+5*6, (int)rHeigth*7+5*6+10);
        Log.d("onMeasure", String.valueOf(parentWidth)+' '+String.valueOf(parentHeight)+' '+
                String.valueOf(rWidth)+' '+String.valueOf(this.getMeasuredWidth())+' '+
                String.valueOf(this.getMeasuredHeight()));
    }

    @Override
    protected void onDraw(Canvas canvas){
        getRes();
        int mark=0, filled=0;
        left = 0;
        top = 10;
        int color = getResources().getColor(R.color.pale_green);
        for(int i=0;i<7;i++){
            drawTextBox(canvas, color, mark, dayNames[i], left, top, rWidth, rHeigth);
            left += rWidth+5;
        }
        top+=rHeigth+5;

        super.onDraw(canvas);
        Set<Integer>filledSet = new HashSet<Integer>();

        top = 10+rHeigth+5;
        int flag = 0;
        int pos = 0;
        Set<String> allColored = new HashSet<String>();
        for (int i = 0; i < 6; i++) {
            left = 0;
            for (int j = 0; j < 7; j++) {
                mark=0;
                filled=0;
                for(int t=0; t<TAGS.length;t++) {
                    if (i * 7 + j < dayOfWeek) {
                        flag = -1;
                        pos = maxPrevDay - (dayOfWeek - (i * 7 + j));
                    }
                    if (i * 7 + j >= dayOfWeek && i * 7 + j <= lastDayNum) {
                        flag = 0;
                        pos = i * 7 + j - dayOfWeek;
                    }
                    if (i * 7 + j > lastDayNum) {
                        flag = 1;
                        pos = i * 7 + j - 1 - lastDayNum;
                    }
                    allColored = MainActivity.sharedPref.getStringSet(String.valueOf(position + flag) + '_' + TAGS[t], new HashSet<String>());
                    if(filled==0)
                        color = greenColors[dayOfMonthSelector(flag)];
                    for (String entry : allColored) {
                        if (Integer.decode(entry) == pos) {
                            if(t<TAGS.length-1 && filled!=1) {
                                color = markingColors[t];
                                mark = 0;
                                filled=1;
                            }
                            else {
                                mark = 1;
                            }
                        }
                    }
                    drawTextBox(canvas, color, mark, String.valueOf(nums[i * 7 + j]), left, top, rWidth, rHeigth);
                }
                left += rWidth + 5;
            }
            top += rHeigth + 5;
        }
    }

    void getRes(){
        for (int i=0; i<3; i++)
        {
            markingColors[i] = getResources().obtainTypedArray(R.array.reds).getColor(i,0);
        }
        markingColors[3] = Color.argb(0,10,10,10);
        greenColors[0] = getResources().getColor(R.color.normal_green);
        greenColors[1] = getResources().getColor(R.color.pale_green);
        dayNames = getResources().getStringArray(R.array.day_names);
    }

    void setDates(int dayOfWeek, int lastDayNum, int maxPrevDay) {
        this.dayOfWeek = dayOfWeek;
        this.lastDayNum = lastDayNum;
        this.maxPrevDay = maxPrevDay;
    }

    void drawTextBox (Canvas canvas, int color, int mark, String text, float left, float top, float w, float h) {
        int textMargin=5;
        Paint myPaint = new Paint();
        Paint myTextPaint = new Paint();
        Paint myPicPaint = new Paint();

        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setColor(color);
        myPicPaint.setColor(Color.BLACK);
        myPaint.setStrokeWidth(5f);

        myTextPaint.setColor(getResources().getColor(android.R.color.background_light));
        myTextPaint.setStrokeWidth(4f);
        myTextPaint.setTextSize(rHeigth/2);
        canvas.drawRect(left, top, left + w, top + h, myPaint);
        canvas.drawText(text,
                left + w/2 - myTextPaint.measureText(text)/2,
                top + h/2 - 17.5f + textMargin,
                myTextPaint);
        if(mark==1) {
            drawSymbol(canvas, getResources(), 0.95f*rWidth/2, left+rWidth/2, top+rHeigth/2);

        }
    }

    static void drawSymbol(Canvas canvas, Resources res, float size, float left, float top){
        Paint myPaint = new Paint();
        float[] points = new float[]{size*0.3f, size*0.7f, size*0.3f, size,
                size*0.15f, size*0.85f, size*0.45f, size*0.85f,
                size*0.75f, size*0.25f, size, 0,
                size*0.9f, 1, size, 1,
                size, 0, size, size*0.1f};
        for(int i=0; i<points.length; i++) {
            if (i%2 == 0)
                points[i] += left;
            if (i%2 == 1)
                points[i] += top;
        }
        myPaint.setStrokeWidth(4f);
        myPaint.setDither(true);                    // set the dither to true
        myPaint.setStyle(Paint.Style.STROKE);       // set to STOKE
        myPaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        myPaint.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
        myPaint.setAntiAlias(true);
        myPaint.setColor(res.getColor(android.R.color.background_light));

        canvas.drawOval(new RectF(left+0, top+size*0.1f, left+size*0.6f, top+size*0.7f), myPaint);
        canvas.drawOval(new RectF(left+size*0.3f, top+size*0.1f, left+size*0.9f, top+size*0.7f), myPaint);
        canvas.drawLines(points, myPaint);
    }

    int dayOfMonthSelector(int flag) {
        if(Math.abs(flag) == 1)    return 1;
        else return 0;
    }
}
