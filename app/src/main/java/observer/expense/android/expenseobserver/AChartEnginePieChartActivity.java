package observer.expense.android.expenseobserver;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class AChartEnginePieChartActivity extends AppCompatActivity {

    SharedPreferences preferences;
    String [] records;
    String start = "Click on ADD button to continue...";
    String [] singleRecordData;
    String [] subTransformedRecords;
    String [] subTransformedRecords2;
    String [] subTransformedRecords3;
    String [] recordTransformHelper;
    String [] recordTransformSubHelper;
    String [] transformedRecords;
    int sum;
    String [] categoryContainerHelper;
    ArrayList<String> categoryContainer = new ArrayList<String>();
    ArrayList<String> transformedResultsContainer = new ArrayList<String>();
    Boolean alreadyListed = false;
    Boolean alreadyCounted = false;
    ArrayList<String> listItems;
    int allPaidMoney = 0;
    Random rnd;

    /*private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE,Color.MAGENTA, Color.CYAN};

    private static double[] VALUES = new double[] { 10, 11, 12, 13 };

    private static String[] NAME_LIST = new String[] { "A", "B", "C", "D" };*/
    private static int[] COLORS;
    private static int[] VALUES;
    private static String[] NAME_LIST;
    private CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private GraphicalView mChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Pie Chart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.pie_layout);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        records = preferences.getString("records",start).split("@");
        listItems = new ArrayList<>(Arrays.asList(mainCategoryAndPricesListCreator(records)));
        COLORS = new int[listItems.size()];
        VALUES = new int[listItems.size()];
        NAME_LIST = new String[listItems.size()];
        generateData(listItems);

        //mRenderer.setApplyBackgroundColor(true);
        //mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setShowLabels(false);
        //mRenderer.setLabelsTextSize(15);
        mRenderer.setLegendTextSize(15);
        mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setStartAngle(90);

        for (int i = 0; i < VALUES.length; i++) {
            mSeries.add(NAME_LIST[i] + ": " + separator(String.valueOf(VALUES[i])) +"("+(float)Math.round(((double)VALUES[i]/allPaidMoney*100) * 100) / 100+"%)", VALUES[i]);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
            mRenderer.addSeriesRenderer(renderer);
        }

        if (mChartView != null) {
            mChartView.repaint();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
            mRenderer.setClickEnabled(true);
            mRenderer.setSelectableBuffer(10);

            /*mChartView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();

                    if (seriesSelection != null) {
                        Toast.makeText(AChartEnginePieChartActivity.this,"Chart element data point index "+ (seriesSelection.getXValue()) + " was clicked" + " point value="+ seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
                    }
                }
            });*/

            /*mChartView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
                    if (seriesSelection == null) {
                        Toast.makeText(AChartEnginePieChartActivity.this,"No chart element was long pressed", Toast.LENGTH_SHORT);
                        return false; 
                    }
                    else {
                        Toast.makeText(AChartEnginePieChartActivity.this,"Chart element data point index "+ seriesSelection.getPointIndex()+ " was long pressed",Toast.LENGTH_SHORT);
                        return true;       
                    }
                }
            });*/
            layout.addView(mChartView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        }
        else {
        mChartView.repaint();
        }
    }
    public void generateData(ArrayList<String> list){
        for (int i=0; i<list.size(); i++){
            //elements.add(new Item(list.get(i).split("-")[0],separator(list.get(i).split("-")[1])));
            allPaidMoney += Integer.parseInt(list.get(i).split("-")[1]);
        }
        for (int i=0; i<list.size(); i++){
            /*COLORS = new int[listItems.size()];
            VALUES = new double[listItems.size()];
            NAME_LIST = new String[listItems.size()];*/
            //elements.add(new Item(list.get(i).split("-")[0],separator(list.get(i).split("-")[1])));
            NAME_LIST[i] = list.get(i).split("-")[0];
            VALUES[i] = Integer.parseInt(list.get(i).split("-")[1]);
            rnd = new Random();
            //int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            COLORS[i] = Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
        }
    }

    public String separator(String text){
        String result = "";
        int coeff = 0;
        coeff = text.length()/3;
        if (coeff > 0){
            for (int i=0; i<text.length(); i++){
                result += String.valueOf(text.charAt(i));
                for (int k=1; k< coeff+1; k++){
                    if(i == text.length()-k*4+(k-1)){
                        result += String.valueOf(" ");
                    }
                }
            }
        }
        else {
            result = text;
        }
        return result;
    }
    public String [] mainCategoryAndPricesListCreator(String [] inputRecords){
        if(inputRecords[0].equals(start)){
            return inputRecords;
        }
        else{
            subTransformedRecords = new String[inputRecords.length];
            subTransformedRecords2 = new String[inputRecords.length];
            subTransformedRecords3 = new String[inputRecords.length];
            for (int i=0; i<inputRecords.length; i++){
                singleRecordData = inputRecords[i].split(",");
                subTransformedRecords[i] = singleRecordData[0]+"-"+singleRecordData[2];
            }
            subTransformedRecords2 = subTransformedRecords;
            for (int p=0; p<subTransformedRecords.length; p++){
                recordTransformHelper = subTransformedRecords[p].split("-");
                sum = 0;
                for (int z=0; z<subTransformedRecords2.length; z++){
                    recordTransformSubHelper = subTransformedRecords2[z].split("-");
                    if (recordTransformHelper[0].equals(recordTransformSubHelper[0])){
                        sum += Integer.parseInt(recordTransformSubHelper[1]);
                        recordTransformHelper[1] = String.valueOf(sum);
                    }
                }
                subTransformedRecords3[p] = recordTransformHelper[0]+"-"+recordTransformHelper[1];
            }
            for (int k=0; k<subTransformedRecords.length; k++) {
                categoryContainerHelper = subTransformedRecords[k].split("-");
                alreadyCounted = false;
                if (categoryContainer.contains(categoryContainerHelper[0])){
                    alreadyCounted = true;
                }
                if (!alreadyCounted){
                    categoryContainer.add(categoryContainerHelper[0]);
                }
            }
            transformedRecords = new String[categoryContainer.size()];
            for (int r=0; r<subTransformedRecords3.length; r++){
                alreadyListed = false;
                if (transformedResultsContainer.contains(subTransformedRecords3[r])){
                    alreadyListed = true;
                }
                if (!alreadyListed){
                    transformedResultsContainer.add(subTransformedRecords3[r]);
                }
            }
            for (int i=0; i<transformedRecords.length; i++){
                transformedRecords[i] = transformedResultsContainer.get(i);
            }
            return transformedRecords;
        }
    }
}