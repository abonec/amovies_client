package com.abonec.AmoviesParser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.net.MalformedURLException;
import java.net.URL;

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
    private ResultEpisodesAdapter adapter;
    private ProgressBar resultProgressBar;

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p/>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p/>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link android.view.Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p/>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p/>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     *         if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadOrParseSeries(true, query);
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        return super.onCreateOptionsMenu(menu);    //To change body of overridden methods use File | Settings | File Templates.
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_update:
                loadOrParseSeries(true, null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.context = this;
        String url = getIntentString();
        TextView textView = (TextView)findViewById(R.id.text_view);
        textView.setText(url);
        this.resultProgressBar = (ProgressBar)findViewById(R.id.resultProgressBar);
        setListners();
        loadOrParseSeries(false, null);
    }

    private void setListners() {
        resultListView = (ListView)findViewById(R.id.resultListView);
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Serial.SerialEpisode episode = application().getSerial().episodes.get(position);
                Intent intent = new Intent(ResultActivity.this, OpenVideoDialog.class);
                intent.putExtra("episode", episode.id);
                startActivity(intent);
            }
        });

    }

    private AmoviesParserApplication application(){
        return (AmoviesParserApplication) getApplication();
    }
    private void loadOrParseSeries(boolean force, String url) {
        AmoviesParserApplication application = application();
        resultProgressBar.setVisibility(View.VISIBLE);
        resultProgressBar.setProgress(0);
        if(force || url != null){
            application.getSerial().episodes.clear();
            adapter.notifyDataSetChanged();
            application.setSerial(null);
        }
        if(url == null){
            url = getIntentString();
        }
        if((application.getSerial() == null || !application.getSerial().amoviesUrl.toString().equals(url))){
            try {
                new GetEpisodesTask().execute(new URL(url));
            } catch (MalformedURLException e) {
            }
        }else{
            populateResult(application.getSerial());
            resultProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private String getIntentString() {
        Intent intent = getIntent();
        if(intent != null) {
            return intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        return null;
    }

    private void populateResult(AmoviesEntry amoviesEntry){
        if (amoviesEntry.entryType == AmoviesEntry.EntryType.Serial){
            populateResultSerial((Serial)amoviesEntry);
        }
    }

    private void populateResultSerial(Serial serial) {
        ResultEpisodesAdapter adapter = new ResultEpisodesAdapter(context, R.id.episode, serial.episodes);
        this.adapter = adapter;
        resultListView.setAdapter(adapter);
    }

    private class GetEpisodesTask extends AsyncTask<URL, Integer, AmoviesEntry> {
        @Override
        protected AmoviesEntry doInBackground(URL... params) {
            AmovieParser parser = new AmovieParser(params[0]);
            parser.progressCallback = new AmovieParser.ProgressUpdate() {
                @Override
                public void progressUpdate(int current, int max) {
                    publishProgress(current, max);
                }
            };
            return parser.parseSerial();
//            return new MockSerial().getMock(ResultActivity.this.getAssets());
        }

        @Override
        protected void onPostExecute(AmoviesEntry amoviesEntry) {
            resultProgressBar.setVisibility(View.INVISIBLE);
            application().amoviesEntry = amoviesEntry;
            populateResult(amoviesEntry);
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
            AmoviesHelpers.showToastLong(ResultActivity.this, "2");
            AmoviesHelpers.showToastShort(ResultActivity.this, Integer.toString(max));
            resultProgressBar.setProgress((int)(current*1.0/max * 100));
        }
    }
}