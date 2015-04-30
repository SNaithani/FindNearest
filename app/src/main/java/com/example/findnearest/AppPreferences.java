package com.example.findnearest;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by shivanshu on 4/30/2015.
 */
public class AppPreferences extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);


    }


}


