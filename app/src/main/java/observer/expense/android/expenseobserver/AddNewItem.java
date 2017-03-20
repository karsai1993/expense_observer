package observer.expense.android.expenseobserver;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class AddNewItem extends AppCompatActivity {
    String [] items;
    String [] splittedPrice;
    String welcome = "For example: Food, Clothes, Entertainment...";
    String start = "blabla";
    String separatedPriceValue = "";
    String builtPriceValue = "";
    String errorLog = "";
    int coeff = 0;
    int length;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ArrayList<String> listedCategories = new ArrayList<String>();
    ListView listView;
    EditText editText;
    EditText editPrice;
    EditText editComment;
    Button doneBtn;
    Button clearCat;
    Boolean isNewCategory;
    Boolean isNeedToStopInfinityLoop = false;
    Boolean canGo;
    SharedPreferences preferences;
    SharedPreferences.Editor newItem;

    String resultCategory;
    String resultComment;
    String resultPriceInString;
    int resultPrice;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String [] collectionOfRecords;
    String record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Define your purchase!");
        getSupportActionBar().show();
        setContentView(R.layout.add_new_item);
        listView = (ListView)findViewById(R.id.listview);
        editText = (EditText)findViewById(R.id.txtsearch);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        newItem = preferences.edit();
        items = preferences.getString("categories",welcome).split(";");
        initList();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!adapter.getItem(position).equals(welcome)) {
                    editText.setText(adapter.getItem(position));
                    editText.setSelection(adapter.getItem(position).length());
                }
            }
        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    initList();
                }
                else{
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editPrice = (EditText)findViewById(R.id.price);
        editPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                length = s.length();
                separatedPriceValue = "";
                builtPriceValue = "";
                coeff = 0;
                splittedPrice = String.valueOf(s).split(" ");
                for (int j=0; j<splittedPrice.length; j++){
                    builtPriceValue += splittedPrice[j];
                }
                coeff = builtPriceValue.length()/3;
                if (coeff > 0){
                    for (int i=0; i<builtPriceValue.length(); i++){
                        separatedPriceValue += String.valueOf(builtPriceValue.charAt(i));
                        for (int k=1; k< coeff+1; k++){
                            if(i == builtPriceValue.length()-k*4+(k-1)){
                                separatedPriceValue += String.valueOf(" ");
                            }
                        }
                    }
                    if(isNeedToStopInfinityLoop){
                        isNeedToStopInfinityLoop = false;
                        return;
                    }
                    isNeedToStopInfinityLoop = true;
                    editPrice.setText(separatedPriceValue);
                    editPrice.setSelection(length);
                }
            }
        });
        clearCat = (Button)findViewById(R.id.clearcategoryies);
        clearCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newItem.remove("categories").commit();
                items = preferences.getString("categories",welcome).split(";");
                initList();
            }
        });
        editComment = (EditText)findViewById(R.id.comment);
        doneBtn = (Button)findViewById(R.id.done);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String item:items){
                    listedCategories.add(item);
                }
                isNewCategory = false;
                if (!listedCategories.contains(editText.getText().toString())){
                    isNewCategory = true;
                }
                canGo = true;
                resultCategory = editText.getText().toString();
                resultComment = editComment.getText().toString();
                resultPriceInString = unSeparator(editPrice.getText().toString());
                errorLog = "";
                if(resultCategory.equals("")){
                    errorLog += "define a category";
                    canGo = false;
                    isNewCategory = false;
                }
                if (resultComment.equals("")){
                    resultComment = "No Comment";
                }
                if (canGo && isNumber(resultCategory)){
                    errorLog += "do not use number(s) in category name";
                    isNewCategory = false;
                    canGo = false;
                }
                try{
                    resultPrice = Integer.parseInt(resultPriceInString);
                } catch (NumberFormatException e){
                    if (!errorLog.equals("")){
                        errorLog += " and ";
                    }
                    if (resultCategory.equals("")){
                        errorLog += "a price";
                    }
                    else{
                        errorLog += "define a price";
                    }
                    isNewCategory = false;
                    canGo = false;
                }
                if(isNewCategory){
                    items = append(items,editText.getText().toString());
                    if(items[0].equals(welcome)){
                        items = Arrays.copyOfRange(items,1,items.length);
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < items.length; i++) {
                        sb.append(items[i]).append(";");
                    }
                    newItem.putString("categories",sb.toString());
                }
                if(canGo){
                    Intent i = new Intent(AddNewItem.this,MainActivity.class);
                    record = resultCategory+","+resultComment+","+String.valueOf(resultPrice)+","+sdf.format(Calendar.getInstance().getTime());
                    collectionOfRecords = preferences.getString("records",start).split("@");
                    collectionOfRecords = append(collectionOfRecords,record);
                    if (collectionOfRecords[0].equals(start)){
                        collectionOfRecords = Arrays.copyOfRange(collectionOfRecords,1,collectionOfRecords.length);
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < collectionOfRecords.length; j++) {
                        sb.append(collectionOfRecords[j]).append("@");
                    }
                    newItem.putString("records",sb.toString());
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please, "+errorLog+"!",Toast.LENGTH_LONG).show();
                }
                newItem.apply();
            }
        });
    }

    public String unSeparator(String input){
        String [] subStringInput = input.split(" ");
        String subString = "";
        for (int j=0; j<subStringInput.length; j++){
            subString += subStringInput[j];
        }
        return subString;
    }

    public Boolean isNumber(String string){
        try{
            int num = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public void searchItem(String textToSearch){
        for(String item:items){
            if(!item.contains(textToSearch)){
                listItems.remove(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void initList(){
        listItems = new ArrayList<>(Arrays.asList(items));
        adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.txtitem,listItems);
        listView.setAdapter(adapter);
    }

    static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }
}

