package com.example.tinyexportcalendar.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceFragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MainActivity extends ActionBarActivity {
    FragmentManager mainFragmentManager;
    PrefsFragment pf;
    MyFragment fr;
    static int winHeight, winWidth;
    static int MARK_SEX_ON = 0;
    static int[] markingColors = new int[3];
    static int curMarkingColor = 0;
    Button[] colBtns = new Button[4];
    static int currentPosition;
    float MIN_SCALE = 0.9f,  MIN_ALPHA = 0.7f;
    static int YEAR_MIN = 2013; //1970
    int YEAR_MAX = 2030,  curYear = 2014, curMon = 5;
    static int initialPosition;
    static String[] monthNames;
    static SharedPreferences sharedPref;
    static DummyAdapter dAdapter;
    public static GregorianCalendar fragmentsOwnCalendar = new GregorianCalendar();
    static Date curDate = new Date();
    static GregorianCalendar myCalendar = new GregorianCalendar();
    static GregorianCalendar myFragmentCalendar = new GregorianCalendar();

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fr = new MyFragment();

        //mainFragmentManager.beginTransaction().attach(fr).commit();

        setContentView(R.layout.activity_main);

        mainFragmentManager = getFragmentManager();
        fr = (MyFragment) getFragmentManager().findFragmentById(R.id.main_content_frag);


        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
                /*R.drawable.ic_drawer,*/ R.string.drawer_open, R.string.drawer_close) {

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        curYear = myCalendar.get(Calendar.YEAR);
        curMon = myCalendar.get(Calendar.MONTH);
        initialPosition = (curYear - YEAR_MIN) * 12 + curMon;
        curDate = myCalendar.getTime();
        currentPosition = initialPosition;

        for (int i = 0; i < 3; i++)
            markingColors[i] = getResources().obtainTypedArray(R.array.reds).getColor(i, 0);
        monthNames = getResources().getStringArray(R.array.month_names);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        winHeight = displaymetrics.heightPixels;
        winWidth = displaymetrics.widthPixels;
        //setButtons();


        sharedPref = getSharedPreferences(getString(R.string.dates_data), Context.MODE_PRIVATE);
        emptyUnusedPrefs();

        /*final VerticalViewPager verticalViewPager = (VerticalViewPager) findViewById(R.id.verticalviewpager);
        dAdapter = new DummyAdapter(getFragmentManager());
        verticalViewPager.setAdapter(dAdapter);
        verticalViewPager.setOffscreenPageLimit(1);
        verticalViewPager.setPageMargin(16);
        verticalViewPager.setPageMarginDrawable(new ColorDrawable(getResources().getColor(android.R.color.holo_green_dark)));
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

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                positionOld = position;
            }

            @Override
            public void onPageSelected(int position) {
                if (position - positionOld == 0) positionOld += 1;
                myCalendar.add(Calendar.MONTH, position - positionOld);
                currentPosition = position;
                Date myDate = myCalendar.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
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
          //  }
        /*
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
        //FirstPdf firstPdf = new FirstPdf();
        //firstPdf.main(null);

    }

    void emptyUnusedPrefs() {
        Map<String,?> keys = sharedPref.getAll();
        SharedPreferences.Editor editor = MainActivity.sharedPref.edit();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            if(entry.getKey().contains("_dates")) {
                editor.remove(entry.getKey());
            }
            if ( ((Set<String>) entry.getValue()).size()==0) {
                editor.remove(entry.getKey());
            }
        }
        editor.commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }


    void setButtons(){
        colBtns[0] = (Button) findViewById(R.id.colbut1);
        colBtns[1] = (Button) findViewById(R.id.colbut2);
        colBtns[2] = (Button) findViewById(R.id.colbut3);
        colBtns[3] = (Button) findViewById(R.id.colbut4);
        for(int i=0; i < colBtns.length; i++){
            colBtns[i].setOnClickListener(new OnColorsClickListner());
            if(i<colBtns.length-1)  colBtns[i].setBackgroundColor(markingColors[i]);
            colBtns[i].setWidth((int)winWidth/10);
            colBtns[i].setHeight((int) winWidth/10);
        }
        colBtns[3].setBackground(symbolDrawable(winWidth/10));
    }

    Drawable symbolDrawable(float width){
        Log.d("mainActivity drawSymbol", String.valueOf(width)+' '+String.valueOf((int)width));
        Bitmap myBitmap = Bitmap.createBitmap((int)width, (int)width, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(myBitmap);
        MonthView.drawSymbol(canvas, getResources(), width*0.9f, width*0.05f, width*0.05f);
        BitmapDrawable bd = new BitmapDrawable(getResources(), myBitmap);
        return bd;
    }


    public class OnColorsClickListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            for (int i=0; i<colBtns.length; i++) {
                if (view == colBtns[i]) {
                    curMarkingColor = i;
                    Log.d("mainActivity curMarkingColor", String.valueOf(i));
                }
            }
            if(view == colBtns[3])  MARK_SEX_ON = 1;
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            if(mPlanetTitles[position].equals(getString(R.string.settings_title))){
                Log.d("mainActivity", "SettingsPressed "+mPlanetTitles[position]);
                getFragmentManager().beginTransaction().hide(fr).replace(android.R.id.content, new PrefsFragment()).commit();

            }
            //selectItem(position);
        }
    }

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }

}
