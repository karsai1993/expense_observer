package observer.expense.android.expenseobserver;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class Pop extends Activity{
    SharedPreferences preferences;
    SharedPreferences.Editor newItem;
    String [] records;
    ArrayList<String> listItems;
    ListView listOfCategoriesAndPrices;
    String start = "Click on ADD button to continue...";
    MyAdapter adapter;
    TextView text;
    Button yesBtn;
    Button noBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        text = (TextView)findViewById(R.id.question);

        final SpannableStringBuilder str = new SpannableStringBuilder(text.getText().toString());
        str.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 8, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new UnderlineSpan(), 8, 21, 0);
        text.setText(str);

        getWindow().setLayout((int)(width*.6),(int)(height*.225));

        listOfCategoriesAndPrices = (ListView)findViewById(R.id.maincategorylist);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        newItem = preferences.edit();
        records = preferences.getString("records",start).split("@");

        yesBtn = (Button)findViewById(R.id.yes);
        noBtn = (Button)findViewById(R.id.no);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllRecords();
                startActivity(new Intent(Pop.this,MainActivity.class));
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Pop.this,MainActivity.class));
            }
        });
    }

    public void clearAllRecords(){
        newItem.remove("records").commit();
    }
}
