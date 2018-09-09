package com.artshell.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.artshell.clever.mvp.BaseTupleActivity;
import com.artshell.clever.mvp.Tuple;

@Tuple(incubator = LanguageIncubators.class, layout = R.layout.activity_main)
public class MainActivity extends BaseTupleActivity<LanguageIncubators> {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void getLanguage(View v) {
        incubator.getItems(
                items -> {
                    Log.i(TAG, "getLanguage: items size => " + items.size());
                    for (String item : items) {
                        System.out.println("item => " + item);
                    }
                },
                throwable -> {
                    Log.i(TAG, "getLanguage: throwable ");
                    throwable.printStackTrace();
                },
                subscription -> {
                    Log.i(TAG, "getLanguage: subscription ");
                    // show dialog
                },
                () -> {
                    Log.i(TAG, "getLanguage: finally");
                    // close dialog
                }
        );
    }
}
