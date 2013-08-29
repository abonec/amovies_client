package com.abonec.AmoviesParser;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
    private Context context;
    private ListView resultListView;
    private AmovieParser.Serial serial;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        this.context = this;
        String url = getIntentString();
        TextView textView = (TextView)findViewById(R.id.text_view);
        textView.setText(url);
        resultListView = (ListView)findViewById(R.id.resultListView);
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AmovieParser.SerialEpisode episode = serial.episodes.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(episode.p720.toString()), "video/mp4");
                startActivity(intent);
            }
        });
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

    private class GetEpisodesTask extends AsyncTask<URL, Integer, AmovieParser.Serial> {
        @Override
        protected AmovieParser.Serial doInBackground(URL... params) {
            AmovieParser parser = new AmovieParser(params[0]);
            return parser.parseSerial();
        }

        @Override
        protected void onPostExecute(AmovieParser.Serial serial) {
            ResultActivity.this.serial = serial;
            ResultEpisodesAdapter adapter = new ResultEpisodesAdapter(context, R.id.episode, serial.episodes);
            resultListView.setAdapter(adapter);

        }

        /**
         * Runs on the UI thread after {@link #publishProgress} is invoked.
         * The specified values are the values passed to {@link #publishProgress}.
         *
         * @param values The values indicating progress.
         * @see #publishProgress
         * @see #doInBackground
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }
}