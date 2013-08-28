package com.abonec.AmoviesParser;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class GetActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onButtonClick(View v){
        EditText ed = (EditText)findViewById(R.id.editText);

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, ed.getText().toString());
        startActivity(intent);

    }
}
