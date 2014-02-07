package com.StockTake;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class AgileProjectActivity extends TabActivity {
	
	StockManager myStockmanager = new StockManager();
	
	public void onCreate(Bundle savedInstanceState) {
		// Seems that this is the main entry point in this project.
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    System.out.println("AgileProjectActivity - oncreate");
	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, SummaryActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Summary (Alphabetically)").setIndicator("Summary (Alphabetically)",
	                      res.getDrawable(R.drawable.ic_tab_summary_alph))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	 // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, VolumeActivity.class);

	 // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Summary (Rank)").setIndicator("Summary (Rank)",
	                      res.getDrawable(R.drawable.ic_tab_summary_rank))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	 // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, VolumeActivity.class);

	    
	    
	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Trade Volume").setIndicator("Trade Volume",
	                      res.getDrawable(R.drawable.ic_tab_volume))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
		 // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, RocketActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Alerts").setIndicator("Alerts",
	                      res.getDrawable(R.drawable.ic_tab_alerts))
	                  .setContent(intent);
	    tabHost.addTab(spec);


	    tabHost.setCurrentTab(0);
	}
	
	
}


