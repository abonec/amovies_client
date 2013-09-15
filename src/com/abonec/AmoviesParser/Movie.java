package com.abonec.AmoviesParser;

import org.htmlcleaner.TagNode;

import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 9/14/13
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Movie extends AmoviesEntry {
    public class Property {
        public final String name;
        public final String value;
        public Property(String name, String value){
            this.name = name;
            this.value = value;
        }
    }
    public List<Property> properties;
    public String description;
    public String title;
    public String posterUrl;
    public Movie(URL url){
        super(EntryType.Movie);
        amoviesUrl = url;
        properties = new ArrayList<Property>();
    }
    public Movie(URL url, String htmlContent, TagNode parser){
        this(url);
        this.htmlContent = htmlContent;
        this.parser = parser;
    }
    public void pushProperty(String name, String value) {
        if(value.equals(" ")){
            return;
        }
        properties.add(new Property(name, value));
    }

}
