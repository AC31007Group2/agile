package com.StockTake;

import android.app.Activity;
import android.app.Fragment;
import android.content.*;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by todd on 03/03/14.
 */

public class SummaryFragment extends Fragment {
    public enum Mode {ALPHABETICAL, BYVALUE}

    ;
    Activity mActivity;
    StockManager myStockmanager;

    /* Create Error Messages */
    TableLayout table;
    TableRow errorRow;
    TextView error1;
    TableRow.LayoutParams params;

    static protected boolean waiting = false;

    private Mode mode = Mode.ALPHABETICAL;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            Bundle args = getArguments();
            mode = Mode.values()[args.getInt("mode", 0)];

        } catch (Exception e) {
            e.printStackTrace();
        }


        return inflater.inflate(R.layout.summary_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();
        AgileProjectActivity agileProjectActivity = (AgileProjectActivity) mActivity;
    }

}