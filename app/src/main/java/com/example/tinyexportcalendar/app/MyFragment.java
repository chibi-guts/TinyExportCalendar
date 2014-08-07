package com.example.tinyexportcalendar.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Maria on 16-Jun-14.
 */
public class MyFragment extends Fragment {

    static int winHeight, winWidth;
    static int MARK_SEX_ON = 0;
    static int[] markingColors = new int[3];
    static int curMarkingColor = 0;
    ImageButton [] colBtns = new ImageButton[4];
    static int currentPosition;
    float MIN_SCALE = 0.9f,  MIN_ALPHA = 0.7f;
    static int YEAR_MIN = 2013; //1970
    int YEAR_MAX = 2030,  curYear = 2014, curMon = 5;
    static int initialPosition;
    static String[] monthNames;
    static DummyAdapter dAdapter;
    public static GregorianCalendar fragmentsOwnCalendar = new GregorianCalendar();
    static Date curDate = new Date();
    static GregorianCalendar myCalendar = new GregorianCalendar();
    static GregorianCalendar myFragmentCalendar = new GregorianCalendar();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_content, container, false);


        curYear = myCalendar.get(Calendar.YEAR);
        curMon = myCalendar.get(Calendar.MONTH);
        initialPosition = (curYear - YEAR_MIN) * 12 + curMon;
        curDate = myCalendar.getTime();
        currentPosition = initialPosition;

        for (int i = 0; i < 3; i++)
            markingColors[i] = getResources().obtainTypedArray(R.array.reds).getColor(i, 0);
        monthNames = getResources().getStringArray(R.array.month_names);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        winHeight = displaymetrics.heightPixels;
        winWidth = displaymetrics.widthPixels;
        colBtns[0] = (ImageButton) view.findViewById(R.id.colbut1);
        colBtns[1] = (ImageButton) view.findViewById(R.id.colbut2);
        colBtns[2] = (ImageButton) view.findViewById(R.id.colbut3);
        colBtns[3] = (ImageButton) view.findViewById(R.id.colbut4);
        setButtons();

        final BorderFlowerView bFV = (BorderFlowerView) view.findViewById(R.id.fl_view);

        final VerticalViewPager verticalViewPager = (VerticalViewPager) view.findViewById(R.id.verticalviewpager);
        //dAdapter = new DummyAdapter(getChildFragmentManager());
        dAdapter = new DummyAdapter(getFragmentManager());
        verticalViewPager.setAdapter(dAdapter);
        verticalViewPager.setOffscreenPageLimit(1);
        verticalViewPager.setPageMargin(16);
        //verticalViewPager.setPageMarginDrawable(new ColorDrawable(getResources().getColor(android.R.color.holo_green_dark)));
        verticalViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();
                int pageHeight = view.getHeight();

                if (position < -1)
                    view.setAlpha(0);
                else if (position <= 1) { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                    if (position < 0) {
                        view.setTranslationY(vertMargin - horzMargin / 2);
                    } else {
                        view.setTranslationY(-vertMargin + horzMargin / 2);
                    }

                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                    view.setAlpha(MIN_ALPHA +
                            (scaleFactor - MIN_SCALE) /
                                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0);
                }
            }
        });

        verticalViewPager.setCurrentItem(initialPosition);
        verticalViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int positionOld;
            //int direction;
            //float offsetOld = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                positionOld = position;
                VelocityTracker curVel = verticalViewPager.mVelocityTracker;
                Log.d("MyFragment onPageScrolled pos offset offsetpx", String.valueOf(position)
                        +' '+String.valueOf(positionOffset)
                        +' '+String.valueOf(positionOffsetPixels));
                /*if(verticalViewPager.mVelocityTracker!=null){
                    curVel = verticalViewPager.mVelocityTracker;
                    curVel.computeCurrentVelocity(1000);
                    Log.d("MyFragment onPageScrolled offsetpx vely", String.valueOf(curVel.getYVelocity()));
                }*/
                bFV.setCurOffset(positionOffset);
                bFV.setCoords(((FragmentMonthView)dAdapter.mFragments.get(position)).monthView.getFlowY());
                bFV.invalidate();
                //offsetOld = positionOffset;
            }

            @Override
            public void onPageSelected(int position) {
                if (position - positionOld == 0) positionOld += 1;
                myCalendar.add(Calendar.MONTH, position - positionOld);
                currentPosition = position;
                Date myDate = myCalendar.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                //Log.d("mFragments size", String.valueOf(dAdapter.mFragments.size()));
                /*File myDatesFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                        +"/documents/mydatesfile.txt");
                FileWriter myWriter;
                try {
                    myWriter = new FileWriter(myDatesFile, true);
                    if(isExternalStorageWritable()) {
                        myWriter.append(String.valueOf(positionOld)
                                + ' ' + String.valueOf(position)
                                + ' ' + dateFormat.format(myDate)+'\n');
                        myWriter.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                //verticalViewPager.getAdapter().
                //fragmentManager.beginTransaction().replace(verticalViewPager.getId(), PlaceholderFragment.newInstance(position)).commit();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //FirstPdf firstPdf = new FirstPdf();
        //firstPdf.main(null);
        return view;
    }



    public class DummyAdapter extends MyFragmentStatePagerAdapter {
        public DummyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentMonthView.newInstance(position);
        }

        @Override
        public int getCount() {
            return (YEAR_MAX - YEAR_MIN)*12;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            return "PAGE" + String.valueOf(position);
        }

    }

    void setButtons(){
        for(int i=0; i < colBtns.length; i++){
            colBtns[i].setOnClickListener(new OnColorsClickListner());
            Log.d("MyFragment winWidth", String.valueOf(winWidth));

            int col = markingColors[0];
            if(i!=3)    col = markingColors[i];
            colBtns[i].setImageDrawable(drawBtn(i, col, 50f));
        }
    }

    BitmapDrawable drawBtn(int id, int color, float width){
        DrawingHelper dh = new DrawingHelper(17, 17, 17, 0, 0);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        Path path = dh.generateComplexPath(dh.parseFloatArray(
                getResources().getStringArray((
                        getResources().obtainTypedArray(R.array.btns)).getResourceId(id, id))), 1, 0);
        path.setFillType(Path.FillType.WINDING);
        Bitmap myBitmap = Bitmap.createBitmap((int)width, (int)width, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(myBitmap);
        canvas.drawPath(path, paint);
        BitmapDrawable bd = new BitmapDrawable(getResources(), myBitmap);
        return bd;
    }

    Drawable symbolDrawable(float width){
        Log.d("mainActivity drawSymbol", String.valueOf(width) + ' ' + String.valueOf((int) width));
        Bitmap myBitmap = Bitmap.createBitmap((int)width, (int)width, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(myBitmap);
        canvas.drawColor(Color.LTGRAY);
        MonthView.drawSymbol(canvas, getResources(), width*0.9f, width*0.05f, width*0.05f);
        MonthView.drawSymbol(canvas, getResources(), width*0.95f, width*0.0474f, width*0.0474f);
        BitmapDrawable bd = new BitmapDrawable(getResources(), myBitmap);
        return bd;
    }


    public class OnColorsClickListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            for (int i=0; i<colBtns.length; i++) {
                if (view == colBtns[i]) {
                    curMarkingColor = i;
                    Log.d("myFr curMarkingColor", String.valueOf(i));
                }
            }
            if(view == colBtns[3])  MARK_SEX_ON = 1;
        }
    }


}