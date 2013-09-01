package com.abonec.AmoviesParser;
import android.graphics.Bitmap;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 8/28/13
 * Time: 7:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class AmovieParser {

    HtmlCleaner cleaner;
    URL amoviesUrl;
    ProgressUpdate progressCallback;
    int episodesLength;
    public AmovieParser(URL htmlPage){
        cleaner = new HtmlCleaner();
        amoviesUrl = htmlPage;
    }

    public interface ProgressUpdate {
        void progressUpdate(int current, int max);
    }
    List<String> getVkLinks(){
        List<String> vkLinks = new ArrayList<String>();
        try {
            Object[] nodes = getByXpath(amoviesUrl, "//select[@id=\"series\"]/option");
            for(Object node : nodes) {
                vkLinks.add(((TagNode)node).getAttributeByName("value"));
            }

            String str = "";
        } catch (XPatherException e) {
        } catch (IOException e) {
        }
        return vkLinks;
    }

    Serial parseSerial() {
        Serial serial = new Serial(amoviesUrl);
        List<String> links = getVkLinks();
        episodesLength = links.size();
        for(String link : links) {
            TagNode cleaner = getCleaner(link);
            if (cleaner == null) {
                continue;
            }
            Object[] nodes = getResolutionsNodes(cleaner);
            SerialEpisode episode = new SerialEpisode(link, getPoster(cleaner));
            for(Object node : nodes){
                TagNode tagNode = (TagNode) node;
                episode.pushLink(tagNode.getAttributeByName("src"));
            }
            serial.pushEpisode(episode);
            progressCallback.progressUpdate(serial.episodes.size(), episodesLength);

        }
        return serial;
    }

    private Object[] getResolutionsNodes(TagNode cleaner) {
        try {
            return cleaner.evaluateXPath("//video/source");
        } catch (XPatherException e) {
            return new Object[0];
        }
    }

    private Object[] getByXpath(URL url, String xpath) throws IOException, XPatherException {

        return cleaner.clean(url).evaluateXPath(xpath);

    }

    private TagNode getCleaner(String link){
        try {
            return cleaner.clean(new URL(link));
        } catch (IOException e) {
            return null;
        }
    }
    private String getPoster(TagNode cleaner){
        TagNode videoTag = null;
        try {
            videoTag = (TagNode) cleaner.evaluateXPath("//video")[0];
        } catch (XPatherException e) {
            return null;
        }
        return videoTag.getAttributeByName("poster");
    }
    public class Serial {
        public URL amoviesUrl;
        public List<SerialEpisode> episodes;
        private int lastId = 1;
        private Map<Integer, SerialEpisode> episodesMap;

        public Serial(URL url){
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
    }
    public class SerialEpisode {
        public String vkLink;
        public String poster;
        public Bitmap posterBitmap;
        public int id;
        private Map<String, String> urls;
        private Pattern urlPattern = Pattern.compile(".*(720|480|360|240)\\..*$");
        public SerialEpisode(String url, String episodePoster) {
            this.vkLink = url;
            this.poster = episodePoster;
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

        public void pushLink(String link) {
            Matcher matcher = urlPattern.matcher(link);
            if(matcher.matches()){
                String quality = matcher.group(1) + "p";
                urls.put(quality, link);
            }
        }
    }

}
