package com.budgester.mymarina;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

/**
 * Created by budgester on 02/01/14.
 */
public class MarinaNFCRead extends Activity {
    /*
        Marina Name passed to function.
        Will need to write
        com.budgester.mymarina - in the AAR (Android Application Record)
        mn = Marina name
        gc = Gate code
        pc = Pontoon Code
        mt = Mens toilet
        ms = Mens shower
        lt = Ladies toilet
        ls = Ladies shower

        in the format

        mn="Marina Name",mc=C1234Z,pc=C4321X

        All on a single line.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readTag();
    }

    private void readTag(){
        Marina marina = new Marina();
        Intent intent = getIntent();
        DatabaseHandler db = new DatabaseHandler(this);

        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())){
            String extraNdefMessages = NfcAdapter.EXTRA_NDEF_MESSAGES;
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(extraNdefMessages);

            if (rawMsgs != null) {
                NdefMessage[] msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }

                byte[] payload = msgs[0].getRecords()[0].getPayload();
                String allcodes = new String(payload);

                Toast.makeText(getApplicationContext(),
                        allcodes,
                        Toast.LENGTH_SHORT).show();

                String[] codes = allcodes.split(",");

                for(String code : codes){
                    String[] item = code.split("=");

                    if (item[0].equals("mn") && item.length > 1){
                        marina.set_marina_name(item[1]);
                    } else if (item[0].equals("mc") && item.length > 1){
                        marina.set_main_code(item[1]);
                    } else if (item[0].equals("pc") && item.length > 1){
                        marina.set_pontoon_code(item[1]);
                    } else if (item[0].equals("mt") && item.length > 1){
                        marina.set_male_toilet(item[1]);
                    } else if (item[0].equals("ms") && item.length > 1){
                        marina.set_male_shower(item[1]);
                    } else if (item[0].equals("ft") && item.length > 1){
                        marina.set_female_toilet(item[1]);
                    } else if (item[0].equals("fs") && item.length > 1){
                        marina.set_female_shower(item[1]);
                    }
                }

                db.addMarina(marina);
                db.updateMarina(marina);
                db.close();
            }
        }

        Toast.makeText(getApplicationContext(),
                "Imported Data",
                Toast.LENGTH_SHORT).show();
        finish();
    }
}
