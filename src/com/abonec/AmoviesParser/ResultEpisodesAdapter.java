package com.abonec.AmoviesParser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 8/29/13
 * Time: 7:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResultEpisodesAdapter extends ArrayAdapter<AmovieParser.SerialEpisode> {
    private final Context context;
    private final List<AmovieParser.SerialEpisode> list;

    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AmovieParser.SerialEpisode episode = list.get(position);

        View row = inflater.inflate(R.layout.result_list_item, parent, false);

        TextView text = (TextView)row.findViewById(R.id.episode);
        ImageView poster = (ImageView)row.findViewById(R.id.poster);
        text.setText(episode.p720.toString());
        if(episode.posterBitmap != null){
            poster.setImageBitmap(episode.posterBitmap);
        }else{
            new ImageDownloadClass(poster, episode).execute();
        }



        return row;
    }

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     *                           instantiating views.
     * @param objects            The objects to represent in the ListView.
     */
    public ResultEpisodesAdapter(Context context, int textViewResourceId, List<AmovieParser.SerialEpisode> objects) {
        super(context, textViewResourceId, objects);    //To change body of overridden methods use File | Settings | File Templates.
        this.context = context;
        this.list = objects;
    }

    private class ImageDownloadClass extends AsyncTask<Void, Void, Bitmap> {
        private final ImageView poster;
        private final AmovieParser.SerialEpisode episode;
        public ImageDownloadClass(ImageView poster, AmovieParser.SerialEpisode episode) {
            this.poster = poster;
            this.episode = episode;
        }
        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param result The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            episode.posterBitmap = bitmap;
            poster.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap result = null;

            try {
                InputStream in = episode.poster.openStream();
                result = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            return result;
        }
    }
}
