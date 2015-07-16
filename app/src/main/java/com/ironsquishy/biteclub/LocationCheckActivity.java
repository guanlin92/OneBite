package com.ironsquishy.biteclub;import android.app.Activity;import android.content.Context;import android.content.Intent;import android.location.LocationManager;import android.provider.Settings;import android.os.Bundle;import android.app.AlertDialog;import android.content.DialogInterface;import android.widget.EditText;import android.widget.Toast;/* * Created by Guan on 6/27/2015. * Edited by Renz on 7/2/2015. */public class LocationCheckActivity extends Activity {    static final int LOCATION_SETTINGS_REQUEST = 1;    private String address;    private AlertDialog.Builder LocAlertBuilder;    @Override    protected void onActivityResult(int requestCode, int resultCode, Intent data) {        super.onActivityResult(requestCode, resultCode, data);        if (requestCode == LOCATION_SETTINGS_REQUEST) {            // user press back button from location settings            // check if location services are now enabled            boolean location_on = checkLocationStatus();            if (location_on) {                startActivity(new Intent(LocationCheckActivity.this, TransportationActivity.class));                finish();            }        }    }    @Override    protected void onCreate(final Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_splash);        boolean location_on = checkLocationStatus();        LocAlertBuilder = new AlertDialog.Builder(this);        final EditText txtInput = new EditText(this);        String str = "";        if (!location_on) {            LocAlertBuilder.setTitle("Location Services is disabled");            LocAlertBuilder.setMessage("Do you want to enable it?");            //Yes button -> transfers to the location settings            LocAlertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {                @Override                public void onClick(DialogInterface dialog, int which) {                    Toast.makeText(getApplicationContext(), "Transferring you to Location Services settings",                            Toast.LENGTH_LONG).show();                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);                }            });            //No button -> pop window to enter address then continue app.            LocAlertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {                @Override                public void onClick(DialogInterface dialog, int which) {                    Toast.makeText(getApplicationContext(), "Continuing without Location Services",                            Toast.LENGTH_LONG).show();                    startActivity(new Intent(LocationCheckActivity.this, CurrentLocationActivity.class));                        /*                        AddAlertBuilder.setTitle("Enter a starting location");                        AddAlertBuilder.setView(txtInput);                        AddAlertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {                            @Override                            public void onClick(DialogInterface dialog, int which) {                                address = txtInput.getText().toString();                                Toast.makeText(getApplicationContext(), "Address has been entered.", Toast.LENGTH_LONG).show();                                startActivity(new Intent(LocationCheckActivity.this,                                        TransportationActivity.class));                            }                        });                    }                    AlertDialog addressInput = AddAlertBuilder.create();                    addressInput.show();*/                }            });            AlertDialog alert = LocAlertBuilder.create();            alert.show();            //end renz        } else {            startActivity(new Intent(LocationCheckActivity.this, TransportationActivity.class));            finish();        }    }    /**     * Check if location if on or off     *     * @return TRUE if location is on, FALSE if location is off     */    public boolean checkLocationStatus() {        LocationManager locMan = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);        boolean location_on = false;        try {            location_on = locMan.isProviderEnabled(LocationManager.GPS_PROVIDER);        } catch (Exception e) {}        return location_on;    }}