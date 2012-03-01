package com.steo.europlanner;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

public class PlannerHomeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);

        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        mainLayout.getBackground().setAlpha(150);
    }
}