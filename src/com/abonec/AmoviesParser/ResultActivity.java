package com.abonec.AmoviesParser;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.abonec.AmoviesParser.R;

/**
 * Created with IntelliJ IDEA.
 * User: abonec
 * Date: 8/28/13
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResultActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        ((TextView)findViewById(R.id.text_view)).setText(GetIntentString());
    }

    private String GetIntentString() {
        Intent intent = getIntent();
        if(intent != null) {
            ClipData data = intent.getClipData();
            if(data != null){
                return data.getItemAt(0).getText().toString();
            }
        }
        return null;
    }
}