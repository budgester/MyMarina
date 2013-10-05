package com.budgester.mymarina;

import android.app.ListActivity;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.EditText;
import java.util.ArrayList;

public class MainActivity extends ListActivity {

    public DatabaseHandler db;
    public ListView lv;
    public ArrayAdapter<String> ad;
    public ArrayList<String> marinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayMarinas();
    }

    public void displayMarinas(){
        db = new DatabaseHandler(this);
        marinas = db.getAllMarinas();
        ad = new ArrayAdapter<String>(this, R.layout.list_marina, R.id.label, marinas);

        this.setListAdapter(ad);

        lv = getListView();

        // listening to single list item on click
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // selected item
                String marina_name = ((TextView) view).getText().toString();
                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), MarinaActivity.class);
                // sending data to new activity
                i.putExtra("marina_name", marina_name);
                startActivity(i);
            }
        });
    }

    public void newMarina(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Marina Name");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int whichButton) {
             String marina_name = input.getText().toString();
             Marina marina = new Marina(marina_name);
             db.addMarina(marina);
             displayMarinas();
             }
        });

         alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
           }
        });

        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.add_marina:
                newMarina();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
