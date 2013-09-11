package com.abonec.AmoviesParser;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 9/3/13
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockSerial {
    public Serial getMock(AssetManager assets) {

        URL url = null;
        try {
            url = new URL("http://mock.vk.com/episode");
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Serial serial = new Serial(url);
        for(int i=0; i<=10; i++){
            Serial.SerialEpisode episode = serial.initializeEpisode();
            episode.title = "Первая серия";
            episode.pushLink("http://vk.com/me/720.mp4");
            episode.posterBitmap = mockPoster(assets);
        }
        return serial;
    }

    private Bitmap mockPoster(AssetManager assets){
        InputStream stream = null;
        try {
            stream = assets.open("mock_poster.png");
        } catch (IOException e) {
        }
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        return bitmap;
    }
}
