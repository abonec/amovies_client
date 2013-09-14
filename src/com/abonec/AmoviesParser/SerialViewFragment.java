package com.abonec.AmoviesParser;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 9/11/13
 * Time: 4:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class SerialViewFragment extends ListFragment implements AmoviesFragment{
    public Serial serial;
    private ResultEpisodesAdapter adapter;
    private Handler populateHandler;
    public static SerialViewFragment getInstance(Serial serial){
        return new SerialViewFragment().setSerial(serial);
    }

    @Override
    public void setAmoviesEntry(AmoviesEntry entry) {
        setSerial((Serial)entry);
    }

    @Override
    public AmoviesEntry getEntry() {
        return serial;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Serial.SerialEpisode episode = serial.episodes.get(position);
        Intent intent = new Intent(getActivity(), OpenVideoDialog.class);
        intent.putExtra("episode", episode.id);
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setAdapter(serial);
        setHandlers();
    }

    private void setHandlers() {
        populateHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                updateView();
            }
        };
    }

    public SerialViewFragment setSerial(Serial serial){
        this.serial = serial;
        return this;
    }

    @Override
    public boolean compatibleWith(AmoviesEntry entry) {
        return entry.entryType == AmoviesEntry.EntryType.Serial;
    }

    public void setAdapter(Serial serial){
        ResultEpisodesAdapter adapter = new ResultEpisodesAdapter(getActivity(), R.id.episode, serial.episodes);
        this.adapter = adapter;
        setListAdapter(adapter);
    }
    public void resetView(){
        serial.clearEpisodes();
        updateView();
    }
    public void updateView(){
        if(adapter == null) return;
        adapter.notifyDataSetChanged();
    }
    public void asyncUpdateView(){
        populateHandler.sendEmptyMessage(0);
    }
}
