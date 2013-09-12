package com.abonec.AmoviesParser;

import android.os.AsyncTask;
import android.view.View;

import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 9/11/13
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetEpisodesTask extends AsyncTask<URL, Integer, AmoviesEntry> {
    private final ResultActivity activity;
    private AmoviesFragment fragment;

    /**
     * Creates a new asynchronous task. This constructor must be invoked on the UI thread.
     */
    public GetEpisodesTask(ResultActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    protected AmoviesEntry doInBackground(URL... params) {
        AmoviesParser parser = new AmoviesParser(params[0], activity);
        parser.serialProgressCallback = new AmoviesParser.ProgressUpdate() {
            @Override
            public void progressUpdate(int current, int max, AmoviesEntry entry) {
                activity.asyncLoadAppropriateView(entry);
                activity.updateView();
                publishProgress(current, max);
            }
        };
        return parser.parseSerial();
//            return new MockSerial().getMock(ResultActivity.this.getAssets());
    }

    @Override
    protected void onPostExecute(AmoviesEntry amoviesEntry) {
        activity.loadAppropriateView(amoviesEntry);
        activity.resultProgressBar.setVisibility(View.INVISIBLE);
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
        int current = values[0];
        int max = values[1];
        activity.resultProgressBar.setProgress((int)(current*1.0/max * 100));
    }
}

