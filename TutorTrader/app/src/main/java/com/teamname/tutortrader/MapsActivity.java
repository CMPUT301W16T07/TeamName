package com.teamname.tutortrader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This activity brings up a map for the user to enter a location for their session.
 * A long click on the map will drop a marker and get the location at that marker.
 * This marker will be the location for the session.
 */
public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);




        /**
         * developers.google.com
         * @param point
         */
         mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {


             @Override
             public void onMapLongClick(final LatLng latLng) {

                 mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title("Session Location"));

                 //Prompt user for confirmation of the selected point
                 AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                 builder.setMessage("Use this point: \n" + latLng.toString())
                         .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int id) {
                                 // return to Create Session Activity
                                 Intent dataRet = new Intent();
                                 Bundle data = new Bundle();
                                 data.putParcelable("point", latLng);
                                 dataRet.putExtras(data);
                                 setResult(RESULT_OK, dataRet);
                                 mMap.clear();
                                 finish();
                             }
                         })
                         .setNegativeButton("No", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int id) {
                                 //remove marker
                                 mMap.clear();
                             }
                         });


                 AlertDialog alert = builder.create();
                 alert.show();

             }
         });

        Button back = (Button) findViewById(R.id.mapsBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call  once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

        }
    }


    /**
     * centers map on current location.
     * code based off of: http://stackoverflow.com/questions/18425141/android-google-maps-api-v2-zoom-to-current-location
     * @param point
     */
    public void centerOnLocation(LatLng point){
        if (point != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 11));
        }
    }
}
