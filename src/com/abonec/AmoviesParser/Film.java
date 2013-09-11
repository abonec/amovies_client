package com.abonec.AmoviesParser;

import android.graphics.Bitmap;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 9/8/13
 * Time: 6:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class Film {
    final String title;
    final String description;
    final String posterUrl;
    Bitmap posterBitmap;

    public Film(String title, String description, String posterUrl) {
        this.title = title;
        this.description = description;
        this.posterUrl = posterUrl;
    }
}
