package com.abonec.AmoviesParser;

import android.app.Activity;
import android.content.Context;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.io.IOException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 8/28/13
 * Time: 7:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class AmoviesParser {
    public ProgressUpdate serialProgressCallback;

    public interface ProgressUpdate {
        void progressUpdate(int current, int max, AmoviesEntry entry);
    }

    HtmlCleaner cleaner;
    URL amoviesUrl;
    int episodesLength;
    private Context context;
    public AmoviesParser(URL htmlPage){
        cleaner = new HtmlCleaner();
        amoviesUrl = htmlPage;
    }

    public AmoviesParser(URL htmlPage, Context context) {
        this(htmlPage);
        this.context = context;
    }

    void initializeEpisdes(Serial serial){
        try {
            Object[] nodes = getByXpath(amoviesUrl, "//select[@id=\"series\"]/option");
            for(Object node : nodes) {
                Serial.SerialEpisode episode = serial.initializeEpisode();
                TagNode tag = (TagNode) node;
                episode.vkLink = tag.getAttributeByName("value");
                episode.title = tag.getText().toString();
            }
        } catch (IOException e) {
//            TODO: Здесь вылетает эксепшен при недоступности хоста, надо обработать
            e.printStackTrace();
        } catch (XPatherException e) {
        }
    }

    Serial parseSerial() {
        Serial serial = new Serial(amoviesUrl);
        initializeEpisdes(serial);
        setSerialToApplication(serial);
        episodesLength = serial.episodes.size();
        for(Serial.SerialEpisode episode : serial.episodes) {
            TagNode cleaner = getCleaner(episode.vkLink);
            if (cleaner == null) {
                continue;
            }
            episode.poster = getPoster(cleaner);
            if(episode.poster == null){
                episode.deprecated = true;
            }
            Object[] nodes = getResolutionsNodes(cleaner);
            for(Object node : nodes){
                TagNode tagNode = (TagNode) node;
                episode.pushLink(tagNode.getAttributeByName("src"));
            }
            int currentEpisode = serial.episodes.indexOf(episode);
            serialProgressCallback.progressUpdate(currentEpisode, episodesLength, serial);

        }
        return serial;
    }

    private void setSerialToApplication(Serial serial) {
        if(context == null) return;
        AmoviesParserApplication application = (AmoviesParserApplication) ((Activity)context).getApplication();
        application.setSerial(serial);
    }

    private Object[] getResolutionsNodes(TagNode cleaner) {
        try {
            return cleaner.evaluateXPath("//video/source");
        } catch (XPatherException e) {
            return new Object[0];
        }
    }

    private Object[] getByXpath(URL url, String xpath) throws IOException, XPatherException {

        return cleaner.clean(url, "cp1251").evaluateXPath(xpath);

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
            Object[] tags = cleaner.evaluateXPath("//video");
            if(tags.length > 0){
                videoTag = (TagNode) tags[0];
            } else {
                return null;
            }
        } catch (XPatherException e) {
            return null;
        }
        return videoTag.getAttributeByName("poster");
    }

}
