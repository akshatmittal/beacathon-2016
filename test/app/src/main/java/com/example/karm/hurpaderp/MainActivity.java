package com.example.karm.hurpaderp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

public class MainActivity extends AppCompatActivity implements BeaconConsumer,MonitorNotifier {
    private BeaconManager beaconManager;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
        beaconManager.bind((BeaconConsumer) this);
        Log.d("MAINSHIT", "BOUND");

        //ACCESS REQUESTING
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check 
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
            }


    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MAINSHIT", "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        beaconManager.unbind(this);
    }
    @Override
    public void onBeaconServiceConnect() {
        Log.d("MAINSHIT","Beacon Service Connect Called");
        // Set the two identifiers below to null to detect any beacon regardless of identifiers
        Identifier myBeaconNamespaceId = Identifier.parse("0x5dc33487f02e477d4058");
        Identifier myBeaconInstanceId = Identifier.parse("0x0117c55667bc");
        Region region = new Region("my-beacon-region", myBeaconNamespaceId, myBeaconInstanceId, null);
        beaconManager.addMonitorNotifier(this);
        try {
            beaconManager.startMonitoringBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void didEnterRegion(Region region) {
        Log.d("MAINSHIT", "I detected a beacon in the region with namespace id " + region.getId1() +
                " and instance id: " + region.getId2());
        beaconManager.unbind(this);
        Intent noob = new Intent(getApplicationContext(),hurdur.class);
        startActivity(noob);
        finish();

    }

    @Override
    public void didExitRegion(Region region) {
        Log.d("MAINSHIT","IM A CUNT");

    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
       // Log.d("MAINSHIT", "CUNTPUNCH I detected a beacon in the region with namespace id " + region.getId1() +
         //       " and instance id: " + region.getId2());




    }
}
