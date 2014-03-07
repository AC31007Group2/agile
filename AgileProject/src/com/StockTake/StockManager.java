package com.StockTake;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Stock Manager Class
 * As this class extends Application it should be treated as a singleton
 */
public class StockManager extends Application {
    public FeedParser newParse;
    private String mState;
    private List<Finance> portfolioList = new LinkedList<Finance>();
    private HashMap<String, String> stockNamesLong = new HashMap<String, String>();

    /**
     * onCreate Method, Called by the Android System
     */
    @Override
    public void onCreate() {
        super.onCreate();
        this.newParse = new FeedParser(getApplicationContext());
    }

    /**
     * Enum used for sorting mode
     */
    public enum SortParameter {
        NAME, VALUE
    }

    /**
     * get current state method
     *
     * @return state
     */
    public String getState() {
        return mState;
    }

    /**
     * Set current state method
     *
     * @param state the state to set
     */
    public void setState(String state) {
        mState = state;
    }

    /**
     * clear the portfolio hashmap - doesn't delete but keep overall structure
     * without content
     */
    public void clearPortfolio() {
        portfolioList.clear();
        stockNamesLong.clear();
    }

    /**
     * Create a new Finance object from the given stock code. Retrieves the detials from the internet API
     *
     * @param stockCode - the code for the stock to retrieve.
     * @return Finance object describing the named stock.
     * @throws IOException
     * @throws JSONException
     */
    public Finance createFinanceObject(String stockCode) throws IOException,
            JSONException {
        Finance newStock = new Finance(StockManager.this);
        this.newParse.parseJSON(newStock, stockCode);
        this.newParse.getHistoric(newStock, stockCode);
        newStock.calcRun();
        newStock.calcRocketPlummet();
        return newStock;
    }

    /**
     * Adds an entry into the stock portfolio for the given stock code, (human-readable) name and quantity.
     * Retrieves data from the internet, and so might be quite slow.
     *
     * @param stockCode      the short code for retrieving the stock
     * @param stockNameLong  the long name for retrieving the stock
     * @param numberOfShares the number of shares to add
     * @return Returns true if adding succeeds, false if it already exists.
     * @throws IOException
     * @throws JSONException
     */
    public boolean addPortfolioEntry(String stockCode, String stockNameLong,
                                     int numberOfShares) throws IOException, JSONException {
        Finance stockObj = createFinanceObject(stockCode);
        stockObj.setNumberOfShares(numberOfShares);
        stockObj.calculateTotalValue();
        if (this.portfolioList.contains(stockObj)) {
            return false;
        }
        portfolioList.add(stockObj);
        stockNamesLong.put(stockCode.substring(stockCode.indexOf(":") + 1),
                stockNameLong);
        return true;
    }

    /**
     * Returns total number of pounds all of the stocks together are worth.
     *
     * @return
     */
    public float getPortfolioTotal() {
        float value = 0;
        for (Finance stockObj : portfolioList) {
            value += stockObj.getTotalValue();
        }
        return value;
    }

    /**
     * set the text view identified by its id to visible
     * @param contextActivity the activity or application context
     * @param id the resource id
     */
    public void setTextViewToVisible(Activity contextActivity, int id) {
        TextView text = (TextView) contextActivity
                .findViewById(id);
        text.setVisibility(View.VISIBLE);
    }

    /**
     * Builds a table in the GUI
     * @param contextActivity the Application or activity context
     * @param sortBy the SortParameter enum
     */
    public void summaryTable(Activity contextActivity, SortParameter sortBy) {
        TableLayout table = (TableLayout) contextActivity
                .findViewById(R.id.tableLayout2);

        DisplayMetrics screenSize = ScreenSize(contextActivity);
        setTextViewToVisible(contextActivity, R.id.summaryNameHeader);
        if (screenSize.widthPixels > 480) {
            setTextViewToVisible(contextActivity, R.id.summaryValueHeader);
            setTextViewToVisible(contextActivity, R.id.summarySharesHeader);
        }
        setTextViewToVisible(contextActivity, R.id.summaryTotalHeader);
        table.setStretchAllColumns(true);
        table.setShrinkAllColumns(true);

        int stockCount = portfolioList.size();
        int stockCounter = 0;

        TableRow[] rowStock = new TableRow[stockCount];
        TextView[] stockName = new TextView[stockCount];
        TextView[] stockShares = new TextView[stockCount];
        TextView[] stockValue = new TextView[stockCount];
        TextView[] stockTotal = new TextView[stockCount];

        TableRow rowTotal = new TableRow(contextActivity);
        TextView portfolioTotal = new TextView(contextActivity);

        sortPortfolioList(sortBy);

        for (Finance stockObj : portfolioList) {
            stockCounter = generateUiRow(contextActivity, table, screenSize, stockCounter, rowStock, stockName, stockShares, stockValue, stockTotal, stockObj);
        }

        float potfolioTotal = getPortfolioTotal();
        BigDecimal potfolioTotalRounded = stockRound(potfolioTotal, 0);

        String totalVal = "Total Portfolio Value:     £"
                + String.format("%,.0f", potfolioTotalRounded);
        portfolioTotal.setText(totalVal);
        portfolioTotal.setTextSize(20f);
        portfolioTotal.setHeight(100);
        portfolioTotal.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 4;

        rowTotal.addView(portfolioTotal, params);
        table.addView(rowTotal);
    }

    /**
     * sort the portfolio list member
     * @param sortBy the SortParameter enum
     */
    private void sortPortfolioList(SortParameter sortBy) {
        switch (sortBy) {
            case NAME:
                Collections.sort(portfolioList, new NameComparator());
                break;

            case VALUE:
                Collections.sort(portfolioList, new TotalComparator());
                break;

            default:
                Collections.sort(portfolioList, new NameComparator());
                break;
        }
    }

    /**
     * this method rounds the stock value float down
     * and returns a big decimal with the precision specified
     * @param stockvalue the stock value float
     * @param precision the number of digits after the decimal place
     * @return the generated decimal
     */
    private BigDecimal stockRound(float stockvalue, int precision) {
        BigDecimal stockValueRounded = new BigDecimal(
                Double.toString(stockvalue));
        stockValueRounded = stockValueRounded.setScale(precision,
                BigDecimal.ROUND_DOWN);

        return stockValueRounded;
    }

    /**
     * this method generates a row and inserts it into the UI
     * @param contextActivity the current context
     * @param table the table to insert the row into
     * @param screenSize the current screen size
     * @param stockCounter the current stock indice
     * @param rowStock the stock row TableRow[]
     * @param stockName the stock name TextView[]
     * @param stockShares the stock shares TextView[]
     * @param stockValue the stock value TextView[]
     * @param stockTotal the stock total TextView[]
     * @param stockObj the stock Finance Object
     * @return next stock indice
     */
    private int generateUiRow(Activity contextActivity, TableLayout table, DisplayMetrics screenSize, int stockCounter, TableRow[] rowStock, TextView[] stockName, TextView[] stockShares, TextView[] stockValue, TextView[] stockTotal, Finance stockObj) {
        rowStock[stockCounter] = new TableRow(contextActivity);
        stockName[stockCounter] = new TextView(contextActivity);
        stockShares[stockCounter] = new TextView(contextActivity);
        stockValue[stockCounter] = new TextView(contextActivity);
        stockTotal[stockCounter] = new TextView(contextActivity);

        float thisStockValue = stockObj.getLastValue();
        // half up rounding mode - so reduces errors to +/- £1
        BigDecimal stockValueRounded = stockRound(thisStockValue, 2);

        float thisStockTotal = stockObj.getTotalValue();

        // rounding down the stock totalValue.
        BigDecimal stockTotalRounded = stockRound(thisStockTotal, 0);
        String longName = stockNamesLong.get(stockObj.getStockSymbol());

        stockName[stockCounter].setText(longName);
        stockName[stockCounter].setTypeface(Typeface.DEFAULT);
        stockName[stockCounter].setTextColor(Color.rgb(58, 128, 255));
        stockName[stockCounter].setTextSize(20f);
        stockName[stockCounter].setHeight(80);
        stockName[stockCounter].setGravity(Gravity.LEFT
                | Gravity.CENTER_VERTICAL);

        if (screenSize.widthPixels > 480) {
            createMiddleColumns(stockShares[stockCounter], stockValue[stockCounter], stockObj, stockValueRounded);
        }

        stockTotal[stockCounter].setText("£"
                + String.format("%,3.0f", stockTotalRounded));
        setColumnParamaters(stockTotal[stockCounter]);

        rowStock[stockCounter].addView(stockName[stockCounter]);
        if (screenSize.widthPixels >= 480) {
            rowStock[stockCounter].addView(stockShares[stockCounter]);
            rowStock[stockCounter].addView(stockValue[stockCounter]);
        }
        rowStock[stockCounter].addView(stockTotal[stockCounter]);

        table.addView(rowStock[stockCounter]);

        stockCounter++;
        return stockCounter;
    }

    /**
     * Generates the optional center columns
     * @param stockShare the stock share TextView
     * @param value the value TextView
     * @param stockObj the Finance stock object
     * @param stockValueRounded the stock value rounded down
     */
    private void createMiddleColumns(TextView stockShare, TextView value, Finance stockObj, BigDecimal stockValueRounded) {
        stockShare.setText(String.format("%,3.0f",
                (float) stockObj.getNumberOfShares()));
        setColumnParamaters(stockShare);
        value.setText("£"
                + String.format("%.2f", stockValueRounded));
        setColumnParamaters(value);
    }

    /**
     * Sets standard column parameters for a TextView
     * @param textView the TextView to set the parameters for
     */
    private void setColumnParamaters(TextView textView) {
        textView.setGravity(Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        textView.setTextSize(20f);
        textView.setSingleLine(true);
    }

    /**
     *
     * @param contextActivity
     * @return
     */
    public int volumeTable(Activity contextActivity) {
        TableLayout table = (TableLayout) contextActivity
                .findViewById(R.id.tableLayout2);

        table.setStretchAllColumns(true);
        table.setShrinkAllColumns(true);

        int stockCount = portfolioList.size();
        int stockCounter = 0;
        int runs = 0;

        TableRow[] rowRun = new TableRow[stockCount];
        TextView[] runStock = new TextView[stockCount];
        TextView[] runLabel = new TextView[stockCount];

        Collections.sort(portfolioList, new NameComparator());

        for (Finance stockObj : portfolioList) {
            rowRun[stockCounter] = new TableRow(contextActivity);
            runStock[stockCounter] = new TextView(contextActivity);
            runLabel[stockCounter] = new TextView(contextActivity);
            if (stockObj.isRun()) {
                GenerateRowRun(rowRun[stockCounter], runStock[stockCounter], runLabel[stockCounter], stockObj);
                runs++;
            }
            table.addView(rowRun[stockCounter]);
            stockCounter++;
        }

        return runs;
    }

    /**
     * Generate a UI row for a stock run
     * @param tableRow the table row to insert values to
     * @param textViewStockName the Stock name TextView
     * @param textViewRun the Run TextView
     * @param stockObj the Finance Stock Object
     */
    private void GenerateRowRun(TableRow tableRow, TextView textViewStockName, TextView textViewRun, Finance stockObj) {
        textViewStockName.setText(stockNamesLong.get(stockObj.getStockSymbol()));
        textViewStockName.setTextSize(20f);
        textViewStockName.setHeight(100);
        textViewStockName.setGravity(Gravity.CENTER_VERTICAL);
        textViewRun.setText("Run");
        textViewRun.setTextSize(20f);
        textViewRun.setHeight(100);
        textViewRun.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        tableRow.addView(textViewStockName);
        tableRow.addView(textViewRun);
    }

    /**
     * Generates the Table for rockets
     * @param contextActivity the application or activity context
     * @return rocket plummet value
     */
    public int rocketTable(Activity contextActivity) {
        // Find TableLayout defined in main.xml
        TableLayout table = (TableLayout) contextActivity
                .findViewById(R.id.tableLayout3);

        table.setStretchAllColumns(true);
        table.setShrinkAllColumns(true);

        int stockCount = portfolioList.size();
        int stockCounter = 0;
        int rocketplummet = 0;

        TableRow[] rowRocket = new TableRow[stockCount];
        TextView[] rocketStock = new TextView[stockCount];
        TextView[] rocketState = new TextView[stockCount];

        Collections.sort(portfolioList, new NameComparator());

        for (Finance stockObj : portfolioList) {
            createRocketUIElements(contextActivity, stockCounter, rowRocket, rocketStock, rocketState, stockObj);
            if (stockObj.isRocket()) {
                addRowRocketView(rowRocket[stockCounter], rocketStock[stockCounter], rocketState[stockCounter], "<font color='green'>Rocket</font>");
                rocketplummet++;
            } else if (stockObj.isPlummet()) {
                addRowRocketView(rowRocket[stockCounter], rocketStock[stockCounter], rocketState[stockCounter], "<font color='red'>Plummet</font>");
                rocketplummet++;
            }
            table.addView(rowRocket[stockCounter]);
            stockCounter++;
        }
        return rocketplummet;
    }

    /**
     * creates the UI elements for a rocket
     * @param contextActivity the application or activity context
     * @param stockCounter the stock indice
     * @param rowRocket the rocket TableRow
     * @param rocketStock the stock TextView
     * @param rocketState the state StockView
     * @param stockObj the Finance Stock Object
     */
    private void createRocketUIElements(Activity contextActivity, int stockCounter, TableRow[] rowRocket, TextView[] rocketStock, TextView[] rocketState, Finance stockObj) {
        rowRocket[stockCounter] = new TableRow(contextActivity);
        rocketStock[stockCounter] = new TextView(contextActivity);
        rocketState[stockCounter] = new TextView(contextActivity);
        rocketState[stockCounter].setGravity(Gravity.RIGHT);
        rocketState[stockCounter].setTextSize(20f);
        rocketStock[stockCounter].setTextSize(20f);
        rocketStock[stockCounter].setHeight(100);
        rocketStock[stockCounter].setGravity(Gravity.CENTER_VERTICAL);
        rocketStock[stockCounter].setText(stockNamesLong.get(stockObj.getStockSymbol()));
    }

    /**
     * adds the rocket row to the UI
     * @param tableRow the TableRow to add
     * @param child the child TextView
     * @param title the title TextView
     * @param HTML the text in HTML  e.g. <font color='green'>Rocket</font>
     */
    private void addRowRocketView(TableRow tableRow, TextView child, TextView title, String HTML) {
        title.setText(Html.fromHtml(HTML));
        tableRow.addView(child);
        tableRow.addView(title);
    }

    /**
     * gets the display metrics for an activity
     * @param context
     * @return
     */
    private DisplayMetrics ScreenSize(Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }
}
