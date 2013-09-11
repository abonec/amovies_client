package com.abonec.AmoviesParser;

import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 9/8/13
 * Time: 6:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class AmoviesEntry {
    enum EntryType { Serial, Film };
    public final EntryType entryType;
    public URL amoviesUrl;
    public AmoviesEntry(EntryType type) {
        this.entryType = type;
    }
}
