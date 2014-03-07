package com.StockTake;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/*
 * Creates a seetings-activity handling the settings screen
 * Extends preferenceActivity
 */
public class SettingsActivity extends PreferenceActivity {
	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
