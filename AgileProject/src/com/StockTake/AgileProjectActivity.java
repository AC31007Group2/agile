package com.StockTake;

//import android.app.Activity;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

public class AgileProjectActivity extends FragmentActivity {

	StockManager myStockmanager = new StockManager();
	
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
            FragmentManager fragmentManager = getFragmentManager();
            SummaryFragment fragment = new SummaryFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.main_layout);

        if(findViewById(R.id.fragment_container) != null)
        {
            if(savedInstanceState != null)
            {
                return;
            }
            FragmentManager fragmentManager = getFragmentManager();
            SummaryFragment summaryFragment = new SummaryFragment();

            SummaryFragment.Mode mode = SummaryFragment.Mode.ALPHABETICAL;
            Bundle b = new Bundle();
            b.putInt("mode",mode.ordinal());
            summaryFragment.setArguments(b);


            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, summaryFragment);
            fragmentTransaction.commit();
        }
	    Resources res = getResources(); // Resource object to get Drawables
/*
	    TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
	    
	    Intent intent = new Intent().setClass(this, SummaryActivityAlphabetical.class);
	    TabHost.TabSpec spec = tabHost.newTabSpec("Summary").setIndicator("",res.getDrawable(R.drawable.ic_tab_summary_alph)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, SummaryActivityRank.class);
	    spec = tabHost.newTabSpec("Summary").setIndicator("", res.getDrawable(R.drawable.ic_tab_summary_rank)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, VolumeActivity.class);
	    spec = tabHost.newTabSpec("Trade Volume").setIndicator("",res.getDrawable(R.drawable.ic_tab_volume)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, RocketActivity.class);
	    spec = tabHost.newTabSpec("Alerts").setIndicator("", res.getDrawable(R.drawable.ic_tab_alerts)).setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);*/
	}
}
