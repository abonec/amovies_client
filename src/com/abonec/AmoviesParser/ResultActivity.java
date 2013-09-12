package com.abonec.AmoviesParser;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    private ListView resultListView;
    private ResultEpisodesAdapter adapter;
    public ProgressBar resultProgressBar;
    public Handler populateHandler;
    private Fragment fragment;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        this.resultProgressBar = (ProgressBar)findViewById(R.id.resultProgressBar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        loadOrParseSeries(false, null);
    }

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

    private void setHandlers() {
        populateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(adapter == null){
                    Serial serial = (Serial) msg.obj;
                    setAdapter(serial);
                }else{
                    updateAdapter();
                }
            }
        };
    }

    AmoviesParserApplication application(){
        return (AmoviesParserApplication) getApplication();
    }
    public AmoviesFragment loadAppropriateView(AmoviesEntry entry){
        if(fragment != null) return null;
        if(entry.entryType == AmoviesEntry.EntryType.Serial){
            return loadSerialFragment((Serial)entry);
        }
        return null;
    }

    public void asyncLoadAppropriateView(AmoviesEntry entry){
        final AmoviesEntry entry1 = entry;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadAppropriateView(entry1);
            }
        });
    }

    private AmoviesFragment loadSerialFragment(Serial serial) {
        SerialViewFragment fragment = new SerialViewFragment().setSerial(serial);
        this.fragment = fragment;
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction()
                .add(R.id.frame_layout, fragment)
                .commit();
        return fragment;
    }

    public void updateView(){
        if(fragment == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((AmoviesFragment)fragment).updateView();
            }
        });
    }

    private void loadOrParseSeries(boolean force, String url) {
        AmoviesParserApplication application = application();
        resultProgressBar.setVisibility(View.VISIBLE);
        resultProgressBar.setProgress(0);
        if(force || url != null){
            application.getSerial().episodes.clear();
            updateAdapter();
            application.setSerial(null);
        }
        if(url == null){
            url = getIntentString();
        }
        if((application.amoviesEntry == null || !application.amoviesEntry.amoviesUrl.toString().equals(url))){
            try {
                new GetEpisodesTask(this).execute(new URL(url));
            } catch (MalformedURLException e) {
                AmoviesHelpers.showToastLong(this, "Wrong url");
            }
        }else{
            populateResult(application.amoviesEntry);
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

    void populateResult(AmoviesEntry amoviesEntry){
        if (amoviesEntry.entryType == AmoviesEntry.EntryType.Serial){
            populateResultSerial((Serial)amoviesEntry);
        }
    }

    private void populateResultSerial(Serial serial) {
        setAdapter(serial);
    }
    private void setAdapter(Serial serial){
        ResultEpisodesAdapter adapter = new ResultEpisodesAdapter(this, R.id.episode, serial.episodes);
        this.adapter = adapter;
        resultListView.setAdapter(adapter);
    }
    private void updateAdapter(){
        adapter.notifyDataSetChanged();
    }

}