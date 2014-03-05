package com.StockTake;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by todd on 04/03/14.
 */
public class AsyncStockManager
{
    SharedPreferences sharedPreferences;
    private HashMap<String, String> stockNamesLong = new HashMap<String, String>();


    public AsyncStockManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public ArrayList<StockRow> getStockRows()
    {
        ArrayList<StockRow> stockRows = new ArrayList<StockRow>();

        return  stockRows;
    }
}
