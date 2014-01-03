package com.budgester.mymarina;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;

/**
 * Created by budgester on 05/10/13.
 */
public class MarinaNFCWrite extends Activity{

    boolean mWriteMode = false;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;
    private Marina marina;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        String marina_name = i.getStringExtra("marina");
        this.db = new DatabaseHandler(this);
        this.marina = new Marina(marina_name);
        this.marina = this.db.getMarina(marina_name);

        String NFCString = "mn=" + this.marina.get_marina_name() +
                ",mc=" + this.marina.get_main_code() +
                ",pc=" + this.marina.get_pontoon_code() +
                ",mt=" + this.marina.get_male_toilet() +
                ",ms=" + this.marina.get_male_shower() +
                ",ft=" + this.marina.get_female_toilet() +
                ",fs=" + this.marina.get_female_shower();

        this.setContentView(R.layout.nfc_write);

        TextView  nfcStringValue  = (TextView) findViewById(R.id.value);
        nfcStringValue.setText(NFCString);

        Toast.makeText(this, "Marina is : " + marina_name, Toast.LENGTH_LONG).show();

        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mNfcAdapter = NfcAdapter.getDefaultAdapter(MarinaNFCWrite.this);
                mNfcPendingIntent = PendingIntent.getActivity(MarinaNFCWrite.this, 0,
                        new Intent(MarinaNFCWrite.this, MarinaNFCWrite.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

                enableTagWriteMode();

                new AlertDialog.Builder(MarinaNFCWrite.this).setTitle("Touch tag to write")
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                disableTagWriteMode();
                            }
                        }).create().show();
            }
        });
    }

    private void enableTagWriteMode() {
        mWriteMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] mWriteTagFilters = new IntentFilter[] { tagDetected };
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
    }

    private void disableTagWriteMode() {
        mWriteMode = false;
        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Tag writing mode
        if (mWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefRecord aar = NdefRecord.createApplicationRecord("com.budgester.mymarina");
            NdefRecord record = NdefRecord.createMime("text/plain", ((TextView) findViewById(R.id.value)).getText().toString().getBytes());
            NdefMessage message = new NdefMessage(new NdefRecord[] {record, aar});
            if (writeTag(message, detectedTag)) {
                Toast.makeText(this, "Success: Wrote placeid to nfc tag", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    /*
    * Writes an NdefMessage to a NFC tag
    */

    public boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Toast.makeText(getApplicationContext(),
                            "Error: tag not writable",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(getApplicationContext(),
                            "Error: tag too small",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                ndef.writeNdefMessage(message);
                return true;
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        return true;
                    } catch (IOException e) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }
}
