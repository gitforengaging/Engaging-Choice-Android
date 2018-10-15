package com.aap.filmtime.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Base Activity of Sample which is extended by every activity
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    protected void launchActivity(Class<? extends BaseActivity> classType) {
        launchActivity(classType,null);
    }

    /**
     * @param classType This is used for launch class type activity
     * @param bundle    Set bundle type data in intent
     */
    protected void launchActivity(Class<? extends BaseActivity> classType, Bundle bundle) {
        Intent intent = new Intent(this, classType);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}
