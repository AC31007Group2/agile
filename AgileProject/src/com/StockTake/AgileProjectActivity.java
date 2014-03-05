package com.StockTake;

//import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.Intent;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class AgileProjectActivity extends FragmentActivity {


    SummaryFragment summaryFragment;



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		case R.id.settings_menu:
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        case R.id.summary_fragment_alphabetical:
            return CreateSummaryFragment();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

    private boolean CreateSummaryFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        summaryFragment = new SummaryFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, summaryFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unbind from the service

    }



    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.main_layout);
        CreateSummaryFragment();
	}
}
