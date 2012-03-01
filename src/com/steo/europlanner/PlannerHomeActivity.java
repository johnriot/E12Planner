package com.steo.europlanner;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class PlannerHomeActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        mainLayout.getBackground().setAlpha(150);
    }
}