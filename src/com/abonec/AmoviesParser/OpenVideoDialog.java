package com.abonec.AmoviesParser;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 8/30/13
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class OpenVideoDialog extends Activity {
    private final String[] qualities = {"720p", "480p", "360p", "280p"};
    private AmovieParser.SerialEpisode episode;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_video_dialog);
        episode = getEpisode();
        final Spinner spinner = (Spinner)findViewById(R.id.chose_resolution_spinner);
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, qualities);
        spinner.setAdapter(spinner_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean firstSelection = true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(firstSelection){
                    firstSelection = false;
                }else{
                    openWithQuality(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        Button button = (Button)findViewById(R.id.open_video_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWithQuality(spinner.getSelectedItemPosition());
            }
        });
    }

    private void openWithQuality(int positionResolution) {
        String link = null;
        switch (positionResolution){
            case 0:
                link = episode.p720.toString();
                break;
            case 1:
                link = episode.p480.toString();
                break;
            case 2:
                link = episode.p360.toString();
                break;
            case 3:
                link = episode.p240.toString();
                break;
        }
        openVideo(link);
    }

    private void openVideo(String link) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(link), "video/mp4");
        startActivity(intent);
    }


    private int getEpisodeNumber(){
        return getIntent().getIntExtra("episode", 1);
    }
    private AmovieParser.SerialEpisode getEpisode(){
        AmoviesParserApplication application = (AmoviesParserApplication)getApplication();
        return application.serial.getEpisodeById(getEpisodeNumber());
    }
}