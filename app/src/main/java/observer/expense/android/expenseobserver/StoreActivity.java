package observer.expense.android.expenseobserver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Edina on 2017. 02. 11..
 */
public class StoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_content);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Previous Period(s)");
    }

}
