package com.abonec.AmoviesParser;
import android.graphics.Bitmap;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

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
        public Serial(URL url){
            amoviesUrl = url;
            episodes = new ArrayList<SerialEpisode>();
        }
        public void pushEpisode(SerialEpisode episode){
            episodes.add(episode);
        }
    }
    public class SerialEpisode {
        public URL vkLink;
        public URL poster;
        public Bitmap posterBitmap;
        public URL p720;
        public URL p480;
        public URL p360;
        public URL p240;
        public SerialEpisode(String url, String episodePoster) {
            try {
                vkLink = new URL(url);
                poster = new URL(episodePoster);
            } catch (MalformedURLException e) {
            }
        }

        public void pushLink(String link) {
            try {
                if(link.matches(".*720\\..*$")){
                    p720 = new URL(link);
                } else if (link.matches(".*480\\..*$")){
                    p480 = new URL(link);
                } else if (link.matches(".*360\\..*$")){
                    p360 = new URL(link);
                } else if (link.matches(".*240\\..*$")){
                    p240 = new URL(link);
                }
            } catch (MalformedURLException e) {
            }
        }
    }

}
