package com.abonec.AmoviesParser;

import android.database.MatrixCursor;
import android.graphics.Bitmap;
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
    public String vkLink;
    public String description;
    public String title;
    public String posterUrl;
    public Bitmap posterBitmap;
    public MatrixCursor properties;
    private int key = 0;
    public Movie(URL url){
        super(EntryType.Movie);
        amoviesUrl = url;
        properties = new MatrixCursor(new String[]{"_id", "name", "value"});
        initializeUrls();
    }
    public Movie(URL url, String htmlContent, TagNode parser){
        this(url);
        this.htmlContent = htmlContent;
        this.parser = parser;
    }
    public void pushProperty(String name, String value) {
        if(value.trim().equals("")){
            return;
        }
        properties.addRow(new Object[]{key++, name, value});
    }

}


//09-17 11:18:32.591: ERROR/dalvikvm(7883): Could not find class 'android.os.UserManager', referenced from method vq.b
//        09-17 11:18:32.591: ERROR/dalvikvm(7883): Could not find class 'android.os.UserManager', referenced from method vq.c
//        09-17 11:18:32.591: ERROR/SPPClientService(3666): [PushClientService] <<--- PushClientService.deInitPushClientService -------END  --->>
//        09-17 11:18:33.702: ERROR/MtpService(6605): In MTPAPP onReceive:android.intent.action.BATTERY_CHANGED
//        09-17 11:18:33.702: ERROR/MtpService(6605): battPlugged Type : 2
//        09-17 11:18:43.752: ERROR/MtpService(6605): In MTPAPP onReceive:android.intent.action.BATTERY_CHANGED
//        09-17 11:18:43.752: ERROR/MtpService(6605): battPlugged Type : 2
//        09-17 11:18:46.384: ERROR/Watchdog(2024): !@Sync 18
//        09-17 11:18:46.664: ERROR/Parcel(2024): Class not found when unmarshalling: com.rootuninstaller.batrsaver.model.DeepSleep, e: java.lang.ClassNotFoundException: com.rootuninstaller.batrsaver.model.DeepSleep
//        09-17 11:18:50.318: ERROR/dalvikvm(7972): Could not find class 'android.os.UserManager', referenced from method vq.b
//        09-17 11:18:50.328: ERROR/dalvikvm(7972): Could not find class 'android.os.UserManager', referenced from method vq.c
//        09-17 11:18:50.878: ERROR/dalvikvm(7984): Could not find class 'android.os.UserManager', referenced from method vq.b
//        09-17 11:18:50.888: ERROR/dalvikvm(7984): Could not find class 'android.os.UserManager', referenced from method vq.c
//        09-17 11:18:54.712: ERROR/MtpService(6605): In MTPAPP onReceive:android.intent.action.BATTERY_CHANGED
//        09-17 11:18:54.712: ERROR/MtpService(6605): battPlugged Type : 2
//        09-17 11:19:05.074: ERROR/MtpService(6605): In MTPAPP onReceive:android.intent.action.BATTERY_CHANGED
//        09-17 11:19:05.074: ERROR/MtpService(6605): battPlugged Type : 2
//        09-17 11:19:15.124: ERROR/MtpService(6605): In MTPAPP onReceive:android.intent.action.BATTERY_CHANGED
//        09-17 11:19:15.124: ERROR/MtpService(6605): battPlugged Type : 2
//        09-17 11:19:16.385: ERROR/Watchdog(2024): !@Sync 19
//        09-17 11:19:25.324: ERROR/MtpService(6605): In MTPAPP onReceive:android.intent.action.BATTERY_CHANGED
//        09-17 11:19:25.324: ERROR/MtpService(6605): battPlugged Type : 2
//        09-17 11:19:31.470: ERROR/Gsm/SmsMessage(2270): hasUserDataHeader : false
//        09-17 11:19:31.490: ERROR/Gsm/SmsMessage(2525): hasUserDataHeader : false
//        09-17 11:19:35.533: ERROR/MtpService(6605): In MTPAPP onReceive:android.intent.action.BATTERY_CHANGED
//        09-17 11:19:35.533: ERROR/MtpService(6605): battPlugged Type : 2
//        09-17 11:19:45.733: ERROR/MtpService(6605): In MTPAPP onReceive:android.intent.action.BATTERY_CHANGED
//        09-17 11:19:45.733: ERROR/MtpService(6605): battPlugged Type : 2
//        09-17 11:19:46.384: ERROR/Watchdog(2024): !@Sync 20
//        09-17 11:19:53.271: ERROR/Gsm/SmsMessage(2270): hasUserDataHeader : false
//        09-17 11:19:53.311: ERROR/Gsm/SmsMessage(2525): hasUserDataHeader : false
//        09-17 11:20:00.820: ERROR/WifiP2pStateTracker(2024): getNetworkInfo : NetworkInfo: type: WIFI_P2P[], state: DISCONNECTED/DISCONNECTED, reason: (unspecified), extra: (none), roaming: false, failover: false, isAvailable: false
//        09-17 11:20:01.390: ERROR/WifiP2pStateTracker(2024): getNetworkInfo : NetworkInfo: type: WIFI_P2P[], state: DISCONNECTED/DISCONNECTED, reason: (unspecified), extra: (none), roaming: false, failover: false, isAvailable: false
//        09-17 11:20:01.390: ERROR/WifiP2pStateTracker(2024): getNetworkInfo : NetworkInfo: type: WIFI_P2P[], state: DISCONNECTED/DISCONNECTED, reason: (unspecified), extra: (none), roaming: false, failover: false, isAvailable: false
//        09-17 11:20:01.410: ERROR/WifiP2pStateTracker(2024): getNetworkInfo : NetworkInfo: type: WIFI_P2P[], state: DISCONNECTED/DISCONNECTED, reason: (unspecified), extra: (none), roaming: false, failover: false, isAvailable: false
//        09-17 11:20:01.430: ERROR/WifiP2pStateTracker(2024): getNetworkInfo : NetworkInfo: type: WIFI_P2P[], state: DISCONNECTED/DISCONNECTED, reason: (unspecified), extra: (none), roaming: false, failover: false, isAvailable: false
//        09-17 11:20:01.450: ERROR/WifiP2pStateTracker(2024): getNetworkInfo : NetworkInfo: type: WIFI_P2P[], state: DISCONNECTED/DISCONNECTED, reason: (unspecified), extra: (none), roaming: false, failover: false, isAvailable: false
//        09-17 11:20:01.520: ERROR/WifiP2pStateTracker(2024): getNetworkInfo : NetworkInfo: type: WIFI_P2P[], state: DISCONNECTED/DISCONNECTED, reason: (unspecified), extra: (none), roaming: false, failover: false, isAvailable: false
//        09-17 11:20:06.145: ERROR/MtpService(6605): In MTPAPP onReceive:android.intent.action.BATTERY_CHANGED
//        09-17 11:20:06.145: ERROR/MtpService(6605): battPlugged Type : 2
//        09-17 11:20:16.205: ERROR/MtpService(6605): In MTPAPP onReceive:android.intent.action.BATTERY_CHANGED
//        09-17 11:20:16.205: ERROR/MtpService(6605): battPlugged Type : 2
//        09-17 11:20:16.395: ERROR/Watchdog(2024): !@Sync 21