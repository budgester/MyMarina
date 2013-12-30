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
import android.util.Log;

public class MainActivity extends ListActivity {

    public DatabaseHandler db;
    public ListView lv;
    public ArrayAdapter<String> ad;
    public ArrayList<String> marinaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MyMarina","Starting");
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(this);
        marinaList = db.getAllMarinas();
        ad = new ArrayAdapter<String>(this, R.layout.list_marina, R.id.label, marinaList);
        displayMarinas();
    }

    @Override
    public void onResume(){
        Log.d("MyMarina","Resuming");

        super.onResume();
        db = new DatabaseHandler(this);
        marinaList = db.getAllMarinas();
        ad = new ArrayAdapter<String>(this, R.layout.list_marina, R.id.label, marinaList);
        ad.notifyDataSetChanged();
        displayMarinas();
    }

    public void displayMarinas(){
        Log.d("MyMarina","Displaying");
        //Create new marina object
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
                startActivity(i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
     }


    public void newMarina(){
        db = new DatabaseHandler(this);
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
                db.close();
                //marina.create();
                Intent i = new Intent(getApplicationContext(), MarinaActivity.class);
                // sending data to new activity
                i.putExtra("marina_name", marina_name);
                startActivity(i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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