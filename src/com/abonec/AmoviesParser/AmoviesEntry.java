package com.abonec.AmoviesParser;

import org.htmlcleaner.TagNode;

import java.net.URL;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 9/8/13
 * Time: 6:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class AmoviesEntry {
    Date time;
    enum EntryType { Serial, Movie };
    public final EntryType entryType;
    public URL amoviesUrl;
    public String htmlContent;
    public TagNode parser;
    public AmoviesEntry(EntryType type) {
        this.entryType = type;
    }
    public void setTimeNow(){
        time = new Date();
    }
}
