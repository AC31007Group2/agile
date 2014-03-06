package com.StockTake;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
/**
 * Agile Project Activity Class
 * <p/>
 * Description:
 * Application Starting Point
 */
public class AgileProjectActivity extends TabActivity {

    /**
     * Method that inflates the options menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    /**
     * Selected Menu Item handler Method
     *
     * @param item The menu item selected
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu:
                return startSettingsMenu();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Creates The Settings Menu and starts the activity
     *
     * @return true if menu created successfully
     */
    private boolean startSettingsMenu() {
        try {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * onCreate method, Called by the android system
     * when the application is created
     *
     * @param savedInstanceState previous parameters and state
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initialiseTabs();
    }

    /**
     * Initialise all tabs and set the current tab to the
     * first tab
     */
    private void initialiseTabs() {
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.addTab(createTab(this, SummaryActivityAlphabetical.class, "Summary", R.drawable.ic_tab_summary_alph, tabHost));
        tabHost.addTab(createTab(this, SummaryActivityRank.class, "Summary", R.drawable.ic_tab_summary_rank, tabHost));
        tabHost.addTab(createTab(this, VolumeActivity.class, "Trade Volume", R.drawable.ic_tab_volume, tabHost));
        tabHost.addTab(createTab(this, RocketActivity.class, "Alerts", R.drawable.ic_tab_alerts, tabHost));
        tabHost.setCurrentTab(0);
    }

    /**
     * Create a tab
     *
     * @param context    the activity contect
     * @param cls        the class
     * @param name       the tab name
     * @param drawableId the id of the drawable used for the tab
     * @param tabHost    the tabhosr
     * @return created tab specification
     */
    private TabHost.TabSpec createTab(Context context, Class<?> cls, String name, int drawableId, TabHost tabHost) {
        Intent intent = new Intent().setClass(context, cls);
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(name).setIndicator("", getResources().getDrawable(drawableId)).setContent(intent);
        return tabSpec;
    }
}
