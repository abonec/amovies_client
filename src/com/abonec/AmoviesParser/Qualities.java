package com.abonec.AmoviesParser;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 9/15/13
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Qualities {
    protected final Pattern urlPattern = Pattern.compile(".*(720|480|360|240)\\..*$");
    protected Map<String, String> urls;
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
    public void initializeUrls(){
        this.urls = new HashMap<String, String>();
    }
}
