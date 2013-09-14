package com.abonec.AmoviesParser;

import android.graphics.Bitmap;

import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 9/3/13
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class Serial extends AmoviesEntry {
    public List<SerialEpisode> episodes;
    private int lastId = 1;
    private Map<Integer, SerialEpisode> episodesMap;

    public Serial(URL url){
        super(EntryType.Serial);
        amoviesUrl = url;
        episodes = new ArrayList<SerialEpisode>();
        this.episodesMap = new HashMap<Integer, SerialEpisode>();
    }
    public void pushEpisode(SerialEpisode episode){
        episode.id = ++lastId;
        episodesMap.put(episode.id, episode);
        episodes.add(episode);
    }
    public SerialEpisode getEpisodeById(int id){
        return episodesMap.get(id);
    }

    public SerialEpisode initializeEpisode() {
        SerialEpisode episode = new SerialEpisode();
        pushEpisode(episode);
        return episode;
    }

    public void clearEpisodes() {
        episodes.clear();
    }

    public class SerialEpisode {
        public String vkLink;
        public String poster;
        public String title;
        public Bitmap posterBitmap;
        public int id;
        private Map<String, String> urls;
        private Pattern urlPattern = Pattern.compile(".*(720|480|360|240)\\..*$");
        public boolean deprecated;

        public SerialEpisode(){
            this.urls = new HashMap<String, String>();

        }
        public String[] qualities(){
            String[] keys = urls.keySet().toArray(new String[0]);
            Arrays.sort(keys, Collections.reverseOrder());
            return keys;
        }

        public String getLinkByQuality(String quality){
            return urls.get(quality);
        }
        public String getLinkByQualityPosition(int position){
            return getLinkByQuality(qualities()[position]);
        }

        public void pushLink(String link) {
            Matcher matcher = urlPattern.matcher(link);
            if(matcher.matches()){
                String quality = matcher.group(1) + "p";
                urls.put(quality, link);
            }
        }
    }
}

