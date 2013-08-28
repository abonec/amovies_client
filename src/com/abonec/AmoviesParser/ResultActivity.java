package com.abonec.AmoviesParser;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 8/28/13
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResultActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        String url = getIntentString();
        TextView textView = (TextView)findViewById(R.id.text_view);
        textView.setText(url);
        try {
            new GetEpisodesTask().execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private String getIntentString() {
        Intent intent = getIntent();
        if(intent != null) {
            return intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        return null;
    }

    private class GetEpisodesTask extends AsyncTask<URL, Integer, List<AmovieParser.SerialEpisode>> {
        @Override
        protected List<AmovieParser.SerialEpisode> doInBackground(URL... params) {
            AmovieParser parser = null;
            parser = new AmovieParser(params[0]);
            try {
                parser.getEpisodes();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AmovieParser.SerialEpisode> serialEpisodes) {

            String str = "";
        }
    }
}