package com.abonec.AmoviesParser;

import android.app.Application;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 8/29/13
 * Time: 9:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class AmoviesParserApplication extends Application {
    public AmovieParser.Serial serial;
    /**
     * Called when the application is starting, before any other application
     * objects have been created.  Implementations should be as quick as
     * possible (for example using lazy initialization of state) since the time
     * spent in this function directly impacts the performance of starting the
     * first activity, service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    @Override
    public void onCreate() {
        super.onCreate();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
