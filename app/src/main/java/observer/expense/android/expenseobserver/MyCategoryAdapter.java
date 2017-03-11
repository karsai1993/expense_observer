package observer.expense.android.expenseobserver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyCategoryAdapter extends ArrayAdapter<Item> {

    private final Context context;
    private final ArrayList<Item> itemsArrayList;

    public MyCategoryAdapter(Context context, ArrayList<Item> itemsArrayList) {

        super(context, R.layout.category_list_item, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.category_list_item, parent, false);

        // 3. Get the two text view from the rowView
        TextView priceView = (TextView) rowView.findViewById(R.id.price_value);
        TextView datumView = (TextView) rowView.findViewById(R.id.datum);
        TextView commentView = (TextView) rowView.findViewById(R.id.comment_value);

        // 4. Set the text for textView
        priceView.setText(itemsArrayList.get(position).getExpense()+" HUF");
        datumView.setText(itemsArrayList.get(position).getDatum());
        commentView.setText(itemsArrayList.get(position).getComment());

        // 5. retrn rowView
        return rowView;
    }
}
