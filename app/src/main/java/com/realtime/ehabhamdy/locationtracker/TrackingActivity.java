package com.realtime.ehabhamdy.locationtracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by ehabhamdy on 1/3/17.
 */

public class TrackingActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener  {

    public static final String TAG = "LocationTestApp";

    private TextView mLong;
    private TextView mLat;
    TextView tf;
    int count =0;


    private GoogleApiClient mGoogleClientApi;
    private LocationRequest mLocationRequest;

    private static final String[] LOCATION_PERMS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int LOCATION_REQUEST = 50;

    public static final long ONE_MIN = 1000 * 60;
    public static final long FIVE_MIN = ONE_MIN * 5;
    public static final float MIN_LAST_READ_ACCURACY = 500.0F;
    private static final float MIN_ACCURACY = 25.0f;


    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mLocReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        Intent intent = this.getIntent();
        String trackingNumber = intent.getStringExtra("tNumber");

        mGoogleClientApi = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mLocReference = mFirebaseDatabase.getReference().child(trackingNumber);

        mLat = (TextView) findViewById(R.id.lat_tv);
        mLong = (TextView) findViewById(R.id.long_tv);

        tf = (TextView) findViewById(R.id.tf);



    }


    private boolean hasPermission(String perm) {
        return (ContextCompat.checkSelfPermission(this, perm) ==
                PackageManager.PERMISSION_GRANTED);
    }


    private boolean canGetLocation() {
        return hasPermission(ACCESS_FINE_LOCATION);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleClientApi.connect();
    }

    @Override
    protected void onStop() {
        mGoogleClientApi.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(8000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                             int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        LOCATION_PERMS,
                        LOCATION_REQUEST);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClientApi, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection Failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, location.toString());


        LocationCoord loc = new LocationCoord(location.getLatitude(), location.getLongitude());

        mLocReference.setValue(loc);

        //Toast.makeText(this, "Location Updated", Toast.LENGTH_SHORT).show();
        int n = count++;
        tf.setText(String.valueOf(n));

        mLat.setText(String.valueOf(location.getLatitude()));
        mLong.setText(String.valueOf(location.getLongitude()));

    }

}
