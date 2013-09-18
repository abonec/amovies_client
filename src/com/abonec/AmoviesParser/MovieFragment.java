package com.abonec.AmoviesParser;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 9/15/13
 * Time: 9:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class MovieFragment extends Fragment implements AmoviesFragment {
    public Movie movie;
    public View view;
    public TextView title;
    public HashMap<String, String> videoLinks;
    private Context context;
    private ImageView poster;
    private ListView properties;
    private Button openButton;
    private Spinner qualityChooser;

    @Override
    public AmoviesEntry getEntry() {
        return movie;
    }

    @Override
    public void updateView() {
        title.setText(movie.title);
        if(movie.posterBitmap == null){
            new MoviePosterLoader().execute(movie.posterUrl);
        }else{
            poster.setImageBitmap(movie.posterBitmap);
        }
        qualityChooser.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, movie.qualities()));
        setProperties();
    }

    private void setProperties() {
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(view.getContext(), R.layout.movie_property_layout, movie.properties, new String[]{"name", "value"}, new int[]{R.id.textView, R.id.textView1});
        properties.setAdapter(adapter);
    }

    @Override
    public void setAmoviesEntry(AmoviesEntry entry) {
        this.movie = (Movie) entry;
    }

    @Override
    public void resetView() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean compatibleWith(AmoviesEntry entry) {
        return entry.entryType == AmoviesEntry.EntryType.Movie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_layout, null);
        this.view = view;
        this.title = (TextView) view.findViewById(R.id.movie_title);
        this.poster = (ImageView) view.findViewById(R.id.movie_poster);
        this.properties = (ListView) view.findViewById(R.id.movies_properties);
        this.openButton = (Button) view.findViewById(R.id.open_movie_button);
        this.qualityChooser = (Spinner) view.findViewById(R.id.quality_chooser);
        this.context = view.getContext();
        setListners();
        updateView();

        return view;
    }

    private void setListners() {
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmoviesHelpers.openVideoWithUrl(context, movie.getLinkByQualityPosition(qualityChooser.getSelectedItemPosition()));
            }
        });
        qualityChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean firstTime = true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(firstTime){
                    firstTime = false;
                }else{
                    AmoviesHelpers.openVideoWithUrl(context, movie.getLinkByQualityPosition(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    public static MovieFragment newInstance(Movie movie) {
        return new MovieFragment().setMovie(movie);
    }

    private MovieFragment setMovie(Movie movie) {
        this.movie = movie;
        return this;
    }

    private class MoviePosterLoader extends AsyncTask<String, Void, Bitmap>{
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            poster.setImageBitmap(bitmap);
            movie.posterBitmap = bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                InputStream stream = url.openStream();
                Bitmap bitmapPoster = BitmapFactory.decodeStream(stream);
                return bitmapPoster;
            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return null;
        }
    }
}
