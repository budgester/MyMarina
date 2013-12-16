package com.budgester.mymarina;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;

/**
 * Created by budgester on 18/09/13.
 * Updates and Display Marina Information
 */

public class MarinaActivity extends Activity {

    private Marina marina;
    private DatabaseHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.marina);

        Intent i = getIntent();

        // getting attached intent data
        String marina_name = i.getStringExtra("marina_name");
        setTitle(marina_name);

        this.db = new DatabaseHandler(this);
        this.marina = new Marina(marina_name);
        this.marina = this.db.getMarina(marina_name);

        TextView txtMainCode = (TextView) findViewById(R.id.main_code);
        txtMainCode.setText(this.marina.get_main_code());

        TextView txtPontoonCode = (TextView) findViewById(R.id.pontoon_code);
        txtPontoonCode.setText(this.marina.get_pontoon_code());

        TextView txtMaleToilet = (TextView) findViewById(R.id.male_toilet);
        txtMaleToilet.setText(this.marina.get_male_toilet());

        TextView txtMaleShower = (TextView) findViewById(R.id.male_shower);
        txtMaleShower.setText(this.marina.get_male_shower());

        TextView txtFemaleToilet = (TextView) findViewById(R.id.female_toilet);
        txtFemaleToilet.setText(this.marina.get_female_toilet());

        TextView txtFemaleShower = (TextView) findViewById(R.id.female_shower);
        txtFemaleShower.setText(this.marina.get_female_shower());
    }

    public void onClick(View view){
        showEntryDialog(view.getId());
     }

    private void showEntryDialog(Integer code){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Enter Code");
        //final int code;
        final EditText input = new EditText(this);
        final Integer myID = code;
        b.setView(input);
        b.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int whichButton)
            {

                if (myID == R.id.main_code ){
                    marina.set_main_code(input.getText().toString());
                }
                if (myID == R.id.pontoon_code){
                    marina.set_pontoon_code(input.getText().toString());
                }

                if (myID == R.id.male_toilet){
                    marina.set_male_toilet(input.getText().toString());
                }
                if (myID == R.id.male_shower){
                    marina.set_male_shower(input.getText().toString());
                }
                if (myID == R.id.female_toilet){
                    marina.set_female_toilet(input.getText().toString());
                }
                if (myID == R.id.female_shower){
                    marina.set_female_shower(input.getText().toString());
                }

                db.updateMarina(marina);
                finish();
                Intent intent = getIntent();
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        b.setNegativeButton("CANCEL", null);
        b.create().show();

    }

    public void deleteMarina(){
        this.db.deleteMarina(this.marina);
        //Reload Main Activity List View
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.marina, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.delete_marina:
                deleteMarina();
                return true;
            case R.id.write_nfc:
                this.setContentView(R.layout.nfc_write);
                Intent i = new Intent(getApplicationContext(), MarinaNFC.class);
                i.putExtra("marina_name", this.marina.get_marina_name());
                // sending data to new activity
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
