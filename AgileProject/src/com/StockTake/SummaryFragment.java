package com.StockTake;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by todd on 03/03/14.
 */

public class SummaryFragment extends Fragment
{
    public enum Mode {ALPHABETICAL, BYVALUE};

    StockManager myStockmanager;

    /* Create Error Messages */
    TableLayout table;
    TableRow errorRow;
    TextView error1;
    TableRow.LayoutParams params;

    static protected boolean waiting = false;

    private Mode mode = Mode.ALPHABETICAL;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //if(savedInstanceState.get("mode") != null)
          //  mode = (Mode)savedInstanceState.get("mode");
        return inflater.inflate(R.layout.summary_fragment_layout, container, false);
    }
}