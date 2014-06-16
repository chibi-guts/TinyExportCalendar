package com.example.tinyexportcalendar.app;

import android.graphics.Color;

/**
 * Created by Maria on 18-May-14.
 */
public class ColoredDates {
    int year;
    int month;
    int dayOfMonth;
    int color;
    ColoredDates (int _year, int _month, int _dayOfMonth) {
        year = _year;
        month = _month;
        dayOfMonth = _dayOfMonth;
        color = Color.WHITE;
    }
    ColoredDates (int _year, int _month, int _dayOfMonth, int _color) {
        year = _year;
        month = _month;
        dayOfMonth = _dayOfMonth;
        color = _color;
    }
    void changeColor (int _color) {
        color = _color;
    }
}

