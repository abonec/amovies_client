package com.abonec.AmoviesParser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

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
public class ResultEpisodesAdapter extends ArrayAdapter<Serial.SerialEpisode> {
    static class ViewHolder{
        ImageView poster;
        TextView title;
        Spinner qualities;
        Button open_button;
    }
    private final Context context;
    private final List<Serial.SerialEpisode> list;

    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        Serial.SerialEpisode episode = list.get(position);
        if(row == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.result_list_item, parent, false);
            holder.title = (TextView)row.findViewById(R.id.episode);
            holder.poster = (ImageView)row.findViewById(R.id.poster);
            holder.qualities = (Spinner)row.findViewById(R.id.chose_resolution_spinner);
            holder.open_button = (Button)row.findViewById(R.id.open_video_button);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String title = episode.title;
        if(episode.deprecated){
            title += " " + context.getString(R.string.has_been_deprecated);
        }
        holder.title.setText(title);
        if(episode.posterBitmap != null){
            holder.poster.setImageBitmap(episode.posterBitmap);
        }else{
            new ImageDownloadClass(holder.poster, episode).execute();
        }
        fillQualities(holder.qualities, episode);
        setListners(holder, episode, row);
        return row;
    }

    private void setListners(ViewHolder holder, Serial.SerialEpisode episode, View view) {
        final View row = view;
        final Serial.SerialEpisode serialEpisode = episode;
        final ViewHolder viewHolder = holder;
        holder.qualities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean onInitialize = true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(onInitialize){
                    onInitialize = false;
                }else{
                    String url = serialEpisode.getLinkByQualityPosition(position);
                    AmoviesHelpers.openVideoWithUrl(context, url);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        holder.open_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = serialEpisode.getLinkByQualityPosition(viewHolder.qualities.getSelectedItemPosition());
                AmoviesHelpers.openVideoWithUrl(context, url);
            }
        });
    }

    private void fillQualities(Spinner qualities, Serial.SerialEpisode episode) {
        if(qualities == null) return;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(qualities.getContext(), android.R.layout.simple_list_item_1, episode.qualities());
        qualities.setAdapter(adapter);
    }

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     *                           instantiating views.
     * @param objects            The objects to represent in the ListView.
     */
    public ResultEpisodesAdapter(Context context, int textViewResourceId, List<Serial.SerialEpisode> objects) {
        super(context, textViewResourceId, objects);    //To change body of overridden methods use File | Settings | File Templates.
        this.context = context;
        this.list = objects;
    }

    private class ImageDownloadClass extends AsyncTask<Void, Void, Bitmap> {
        private final ImageView poster;
        private final Serial.SerialEpisode episode;
        public ImageDownloadClass(ImageView poster, Serial.SerialEpisode episode) {
            this.poster = poster;
            this.episode = episode;
        }
        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param bitmap The result of the operation computed by {@link #doInBackground}.
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
                InputStream in = (new URL(episode.poster).openStream());
                result = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
            }

            return result;
        }
    }
}
