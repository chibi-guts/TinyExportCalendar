package com.example.tinyexportcalendar.app;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Maria on 29-May-14.
 */
public class FragmentMonthView extends Fragment {
    int dayOfWeek =1, maxPrevDay=31, lastDayNum=35;
    MonthView monthView;
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentMonthView newInstance(int sectionNumber) {
        FragmentMonthView fragment = new FragmentMonthView();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentMonthView() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);
        LinearLayout layout = (LinearLayout)rootView.findViewById(R.layout.fragment_layout);
        TextView textView = (TextView) rootView.findViewById(R.id.textview);
        monthView = (MonthView) rootView.findViewById(R.id.month_view);
        int deltaMon = getArguments().getInt(ARG_SECTION_NUMBER) -
                ((MainActivity.fragmentsOwnCalendar.get(Calendar.YEAR)-MainActivity.YEAR_MIN)*12+MainActivity.fragmentsOwnCalendar.get(Calendar.MONTH));
        MainActivity.fragmentsOwnCalendar.add(Calendar.MONTH, deltaMon);
        Date myDate = MainActivity.fragmentsOwnCalendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMMM");
        textView.setText(dateFormat.format(myDate));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 60);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(MainActivity.winWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);

        monthView.setPosition(getArguments().getInt(ARG_SECTION_NUMBER));
        monthView.setNums(fillNums(MainActivity.fragmentsOwnCalendar.getTime()));
        monthView.setDates(dayOfWeek, lastDayNum, maxPrevDay);

        rootView.setOnTouchListener(new OnDayTouchListener(
                monthView, textView.getMeasuredHeight(), getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }



    int [] fillNums(Date date){
        int [] nums = {1,2,3,4,5,6,7,
                8,9,10,11,12,13,14,
                15,16,17,18,19,20,21,
                22,23,24,25,26,27,28,
                29,30,31,32,33,34,35,
                36,37,38,39,40,41,42};

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        GregorianCalendar referenceCalendar = new GregorianCalendar();

        referenceCalendar.setTime(MainActivity.fragmentsOwnCalendar.getTime());
        referenceCalendar.set(Calendar.DAY_OF_MONTH, 1);
        dayOfWeek = referenceCalendar.get(Calendar.DAY_OF_WEEK);
        dayOfWeek = (dayOfWeek>1)? dayOfWeek-1 : 7;
        dayOfWeek-=1;
        referenceCalendar.add(Calendar.MONTH, -1);
        maxPrevDay = referenceCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        referenceCalendar.add(Calendar.MONTH, 1);
        referenceCalendar.setTime(date);
        lastDayNum = dayOfWeek+referenceCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)-1;

        for (int i = dayOfWeek-1; i>=0; i--){
            nums[dayOfWeek-1-i] = maxPrevDay-i;
        }
        for (int i = dayOfWeek; i <=lastDayNum; i++ ){
            nums[i] = i - (dayOfWeek-1);
        }
        for (int i = lastDayNum+1; i<42; i++){
            nums[i] = i + 1 - lastDayNum-1;
        }
        return nums;
    }

    public class OnDayTouchListener implements View.OnTouchListener {
        final String[] TAGS = {"0","1","2","sex"};
        MonthView monthView;
        int [] loc;
        int dt, position;
        long startClickTime;
        OnDayTouchListener (MonthView monthView, int dt, int position){
            this.monthView = monthView;
            this.dt = dt;
            this.position = position;
        }
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            float x, y, rWidth = monthView.rWidth, rHeigth = monthView.rHeigth, top, left;
            final int MAX_CLICK_DURATION = 200;
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    startClickTime = Calendar.getInstance().getTimeInMillis();
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                    if (clickDuration < MAX_CLICK_DURATION) {
                        x = motionEvent.getX();
                        y = motionEvent.getY();
                        //Log.d("ONTOUCH", "click event:" + ' ' + String.valueOf(x) + ' ' + String.valueOf(y));
                        int[] loc = new int[4];
                        monthView.getLocationInWindow(loc);
                        top = 10+dt+monthView.rHeigth+5;
                        for (int i = 0; i < 6; i++) {
                            left = loc[0];
                            for (int j = 0; j < 7; j++) {
                                if (x >= left && x <= left + rWidth && y >= top && y <= top + rHeigth) {
                                    Log.d("ONTOUCH"+' '+ String.valueOf(loc[0]) + ' ' + String.valueOf(loc[1])
                                                    +' '+ String.valueOf(left) + ' ' + String.valueOf(top),
                                            "click event:" + ' ' + String.valueOf(x) + ' ' +
                                          String.valueOf(y) + ' ' + String.valueOf(i * 7 + j));
                                    int key = position;
                                    int pos = i * 7 + j - dayOfWeek;
                                    if (i * 7 + j < dayOfWeek) {
                                        key = position-1;
                                        pos = maxPrevDay-(dayOfWeek - (i*7+j));
                                    }
                                    else if (i * 7 + j > lastDayNum){
                                        key = position+1;
                                        pos = i * 7 + j - lastDayNum - 1;
                                    }

                                    for (int t = 0; t < 4; t++) {
                                        int oldT = -1;
                                        Set<String> allColored = MainActivity.sharedPref.getStringSet(
                                                String.valueOf(key) + '_' + TAGS[t], new HashSet<String>());
                                        Log.d("StringSet in ONTOUCH before", " "+allColored);
                                        if (allColored.contains(String.valueOf(pos))) {
                                            if (MainActivity.curMarkingColor!=3 && t!=3)
                                                allColored.remove(String.valueOf(pos));
                                            if(MainActivity.curMarkingColor==3 && t==3)
                                                allColored.remove(String.valueOf(pos));
                                            oldT = t;
                                        }
                                        if (!allColored.contains(String.valueOf(pos)) && t == MainActivity.curMarkingColor && t != oldT) {
                                            allColored.add(String.valueOf(pos));
                                        }
                                        Log.d("StringSet in ONTOUCH after", " " + allColored);
                                        SharedPreferences.Editor editor = MainActivity.sharedPref.edit();
                                        editor.putStringSet(String.valueOf(key) + '_' + TAGS[t], allColored);
                                        editor.commit();
                                    }
                                    monthView.invalidate();
                                    invalidateNeighbours(position);
                                }
                                left += rWidth + 5;
                            }
                            top += rHeigth + 5;
                        }
                    }
                }
            }
            return true;
        }

        void invalidateNeighbours(int position){
            int[] positions = new int[]{position-1, position+1};
            for (int p:positions) {
                FragmentMonthView myFrag = (FragmentMonthView) MainActivity.dAdapter.mFragments.get(p);
                myFrag.monthView.invalidate();
                //Log.d("ONTOUCH invalidating", String.valueOf(position)+' '+String.valueOf(key)+' '
                //+String.valueOf(myFrag.getArguments().getInt(ARG_SECTION_NUMBER)));
                MainActivity.dAdapter.mFragments.set(p, myFrag);
            }
        }
    }
}