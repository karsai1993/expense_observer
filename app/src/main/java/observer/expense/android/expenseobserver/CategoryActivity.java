package observer.expense.android.expenseobserver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class CategoryActivity extends AppCompatActivity {
    String name = "";
    String value = "";
    TextView valueName;
    TextView valueExpense;
    ListView listView;
    ArrayList<String> listItems;
    ArrayList<String> removeItemHelper = new ArrayList<>();
    MyCategoryAdapter adapter;
    SharedPreferences preferences;
    SharedPreferences.Editor newItem;
    String [] records;
    String [] results;
    String [] subResult;
    String [] subRecords;
    ArrayList<String> subResultContainer = new ArrayList<String>();
    String start = "blabla";
    int sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Category details");
        setContentView(R.layout.category_sumup);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        newItem = preferences.edit();
        records = preferences.getString("records",start).split("@");
        recordsFilteredByCategory(records);

        valueName = (TextView)findViewById(R.id.name_value);
        valueName.setText(name);
        valueExpense = (TextView)findViewById(R.id.expense_value);
        valueExpense.setText(getSumOfCategory(results));

        listView = (ListView)findViewById(R.id.categoryitems);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i=0; i<records.length; i++){
                    removeItemHelper.add(records[i]);
                }
                for (int j=0; j<removeItemHelper.size(); j++){
                    if (name.equals(removeItemHelper.get(j).split(",")[0]) &&
                            adapter.getItem(position).getComment().equals(removeItemHelper.get(j).split(",")[1]) &&
                            unSeparator(adapter.getItem(position).getExpense()).equals(removeItemHelper.get(j).split(",")[2]) &&
                            adapter.getItem(position).getDatum().equals(removeItemHelper.get(j).split(",")[3])){
                        removeItemHelper.remove(j);
                    }
                }
                subRecords = new String[removeItemHelper.size()];
                for (int k=0; k<removeItemHelper.size(); k++){
                    subRecords[k] = removeItemHelper.get(k);
                }
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < subRecords.length; j++) {
                    sb.append(subRecords[j]).append("@");
                }
                newItem.remove("records").commit();
                if (records.length > 1){
                    newItem.putString("records",sb.toString());
                    newItem.apply();
                }
                startActivity(new Intent(CategoryActivity.this,MainActivity.class));
                return false;
            }
        });

        initList(results);
    }
    public String unSeparator(String input){
        String [] subStringInput = input.split(" ");
        String subString = "";
        for (int j=0; j<subStringInput.length; j++){
            subString += subStringInput[j];
        }
        return subString;
    }

    private void recordsFilteredByCategory(String[] inputs) {
        for (int i=0; i<inputs.length; i++){
            subResult = inputs[i].split(",");
            if (subResult[0].equals(name)){
                subResultContainer.add(subResult[2]+";"+subResult[3]+";"+subResult[1]);//price-datum-comment
            }
        }
        results = new String[subResultContainer.size()];
        for (int i=0; i<subResultContainer.size(); i++){
            results[i] = subResultContainer.get(i);
        }
    }
    private String getSumOfCategory(String[] data){
        for (int i=0; i<data.length; i++){
            sum += Integer.parseInt(data[i].split(";")[0]);
        }
        return separator(String.valueOf(sum));
    }

    public void initList(String [] strings){
        listItems = new ArrayList<>(Arrays.asList(strings));
        adapter = new MyCategoryAdapter(this, generateData(listItems));
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.smoothScrollToPosition(0);
    }
    public ArrayList<Item> generateData(ArrayList<String> list){
        ArrayList<Item> elements = new ArrayList<Item>();
        for (int i=0; i<list.size(); i++){
            elements.add(0,new Item(separator(list.get(i).split(";")[0]),list.get(i).split(";")[1],list.get(i).split(";")[2]));
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
}
