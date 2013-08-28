package com.abonec.AmoviesParser;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.io.IOException;
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
    public AmovieParser(URL htmlPage){
        cleaner = new HtmlCleaner();
        amoviesUrl = htmlPage;
    }

    public class SerialEpisode {
    }

    List<String> getVkLinks() throws IOException {
        List<String> vkLinks = new ArrayList<String>();
        try {
            Object[] nodes = cleaner.clean(amoviesUrl).evaluateXPath("//select[@id=\"series\"]/option");
            for(Object node : nodes) {
                vkLinks.add(((TagNode)node).getAttributes().get("value"));
            }

            String str = "";
        } catch (XPatherException e) {
        }
        return vkLinks;
    }

    List<SerialEpisode> getEpisodes() throws IOException {
        List<SerialEpisode> episodes = new ArrayList<SerialEpisode>();
        List<String> links = getVkLinks();
        for(String link : links) {
            try {
                TagNode rootNode = cleaner.clean(new URL(link));
                Object[] nodes = rootNode.evaluateXPath("//object/param[@name=\"flashvars\"]");
                for(Object node : nodes){
                    TagNode tagNode = (TagNode) node;
                    String str = "";
                }

            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (XPatherException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return episodes;
    }
}
