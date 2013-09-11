package com.abonec.AmoviesParser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 9/8/13
 * Time: 7:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class AmoviesHelpers {
    public static void openVideoWithUrl(Context context, String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), "video/mp4");
        context.startActivity(intent);
    }
    public static void showToastLong(Context context, String string){
        Toast.makeText(context,string, Toast.LENGTH_LONG).show();
    }
    public static void showToastShort(Context context, String string){
        Toast.makeText(context,string, Toast.LENGTH_SHORT).show();
    }
}
