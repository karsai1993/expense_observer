package observer.expense.android.expenseobserver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    Button plusBtn;
    Button clearBtn;
    Button startBtn;
    ListView listOfCategoriesAndPrices;
    ArrayList<String> listItems;
    TextView allExpense;
    TextView startDate;
    MyAdapter adapter;
    String [] records;
    String [] storedData;
    String [] dataStore;
    String [] singleRecordData;
    String [] subTransformedRecordsForStartingTransformation;
    String [] subTransformedRecordsForMaintainingTransformation;
    String [] subTransformedRecordsForResult;
    String [] categoryContainerHelper;
    String[] modifiedRecords;
    ArrayList<String> categoryContainer = new ArrayList<String>();
    ArrayList<String> storedRecordData = new ArrayList<String>();
    ArrayList<String> transformedResultsContainer = new ArrayList<String>();
    String [] recordTransformHelper;
    String [] recordTransformSubHelper;
    String [] transformedRecords;
    SharedPreferences preferences;
    SharedPreferences.Editor newItem;
    String start = "Click on ADD button to continue...";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Boolean alreadyListed = false;
    Boolean alreadyCounted = false;
    Boolean needOnlyStoredIconInActionBar = false;
    int sum;
    int amount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        newItem = preferences.edit();
        storedData = preferences.getString("storage","Empty").split("%");
        if (!storedData[0].equals("Empty")){
            for (int j=0; j<storedData.length; j++){
                storedRecordData.add(storedData[j]);
            }
        }
        records = preferences.getString("records",start).split("@");
        if(records[0].equals(start)){
            getSupportActionBar().show();
            needOnlyStoredIconInActionBar = true;
            setContentView(R.layout.activity_main_default);
            startBtn = (Button)findViewById(R.id.start);
            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,AddNewItem.class));
                    newItem.putString("startTime",sdf.format(Calendar.getInstance().getTime()));
                    newItem.apply();
                }
            });
        }
        else {
            getSupportActionBar().setTitle("Actual Period");
            getSupportActionBar().show();
            needOnlyStoredIconInActionBar = false;
            setContentView(R.layout.activity_main);
            listOfCategoriesAndPrices = (ListView)findViewById(R.id.maincategorylist);
            listOfCategoriesAndPrices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(!adapter.getItem(position).getTitle().equals(start)) {
                        Intent i = new Intent(MainActivity.this,CategoryActivity.class);
                        i.putExtra("name",adapter.getItem(position).getTitle());
                        startActivity(i);
                    }
                }
            });

            modifiedRecords = mainCategoryAndPricesListCreator(records);
            initList(modifiedRecords);

            plusBtn = (Button)findViewById(R.id.plus);
            plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this,AddNewItem.class);
                    startActivity(i);
                }
            });
            clearBtn = (Button)findViewById(R.id.clearmain);
            clearBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,Pop.class));
                }
            });
            allExpense = (TextView)findViewById(R.id.all_expense);
            allExpense.setText(getSumOfCategory(modifiedRecords)+" HUF");

            startDate = (TextView)findViewById(R.id.startTimeValue);
            startDate.setText(preferences.getString("startTime","ERROR"));
        }

    }
    private String getSumOfCategory(String[] data){
        for (int i=0; i<data.length; i++){
            amount += Integer.parseInt(data[i].split("-")[1]);
        }
        return separator(String.valueOf(amount));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!needOnlyStoredIconInActionBar){
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        else{
            getMenuInflater().inflate(R.menu.main_only_stored_icon, menu);
            return true;
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.showcharticon:
                startActivity(new Intent(MainActivity.this,AChartEnginePieChartActivity.class));
                return true;
            case R.id.saveicon:
                storedRecordData.add(preferences.getString("startTime","ERROR")+","+sdf.format(Calendar.getInstance().getTime())+","+preferences.getString("records",start));
                dataStore = new String[storedRecordData.size()];
                for (int i=0; i<storedRecordData.size(); i++){
                    dataStore[i]=storedRecordData.get(i);
                }
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < dataStore.length; j++) {
                    sb.append(dataStore[j]).append("%");
                }
                newItem.putString("storage",sb.toString());
                newItem.apply();
                preferences.edit().remove("records").commit();
                finish();
                startActivity(getIntent());
                return true;
            case R.id.storeicon:
                startActivity(new Intent(MainActivity.this,StoreActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String [] mainCategoryAndPricesListCreator(String [] inputRecords){
        if(inputRecords[0].equals(start)){
            return inputRecords;
        }
        else{
            subTransformedRecordsForStartingTransformation = new String[inputRecords.length];
            subTransformedRecordsForMaintainingTransformation = new String[inputRecords.length];
            subTransformedRecordsForResult = new String[inputRecords.length];
            for (int i=0; i<inputRecords.length; i++){
                singleRecordData = inputRecords[i].split(",");
                subTransformedRecordsForStartingTransformation[i] = singleRecordData[0]+"-"+singleRecordData[2];
            }
            subTransformedRecordsForMaintainingTransformation = subTransformedRecordsForStartingTransformation;
            for (int p=0; p<subTransformedRecordsForStartingTransformation.length; p++){
                recordTransformHelper = subTransformedRecordsForStartingTransformation[p].split("-");
                sum = 0;
                for (int z=0; z<subTransformedRecordsForMaintainingTransformation.length; z++){
                    recordTransformSubHelper = subTransformedRecordsForMaintainingTransformation[z].split("-");
                    if (recordTransformHelper[0].equals(recordTransformSubHelper[0])){
                        sum += Integer.parseInt(recordTransformSubHelper[1]);
                        recordTransformHelper[1] = String.valueOf(sum);
                    }
                }
                subTransformedRecordsForResult[p] = recordTransformHelper[0]+"-"+recordTransformHelper[1];
            }
            for (int k=0; k<subTransformedRecordsForStartingTransformation.length; k++) {
                categoryContainerHelper = subTransformedRecordsForStartingTransformation[k].split("-");
                alreadyCounted = false;
                if (categoryContainer.contains(categoryContainerHelper[0])){
                    alreadyCounted = true;
                }
                if (!alreadyCounted){
                    categoryContainer.add(categoryContainerHelper[0]);
                }
            }
            transformedRecords = new String[categoryContainer.size()];
            for (int r=0; r<subTransformedRecordsForResult.length; r++){
                alreadyListed = false;
                if (transformedResultsContainer.contains(subTransformedRecordsForResult[r])){
                    alreadyListed = true;
                }
                if (!alreadyListed){
                    transformedResultsContainer.add(subTransformedRecordsForResult[r]);
                }
            }
            for (int i=0; i<transformedRecords.length; i++){
                transformedRecords[i] = transformedResultsContainer.get(i);
            }
            return transformedRecords;
        }
    }

    public void initList(String [] strings){
        listItems = new ArrayList<>(Arrays.asList(strings));
        if (strings[0].equals(start)){
            adapter = new MyAdapter(this, generateDefaultData(listItems));
        }
        else{
            adapter = new MyAdapter(this, generateData(listItems));
        }
        listOfCategoriesAndPrices.setAdapter(adapter);
    }

    public ArrayList<Item> generateData(ArrayList<String> list){
        ArrayList<Item> elements = new ArrayList<Item>();
        for (int i=0; i<list.size(); i++){
            elements.add(new Item(list.get(i).split("-")[0],separator(list.get(i).split("-")[1])));
        }
        return elements;
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

    public ArrayList<Item> generateDefaultData(ArrayList<String> list){
        ArrayList<Item> elements = new ArrayList<Item>();
        for (int i=0; i<list.size(); i++){
            elements.add(new Item(list.get(i),""));
        }
        return elements;
    }
}
