package com.teamname.tutortrader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        centerOnCurrentLocation();

        /**
         * developers.google.com
         * @param point
         */
         mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {


             @Override
             public void onMapLongClick(LatLng latLng) {
                 mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title("Session Location"));

                 //TODO: add a delay before prompt to actually see the marker.
                 //Prompt user for confirmation of the selected point
                 AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                 builder.setMessage("Use this point: \n" + latLng.toString())
                         .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int id) {
                                 // return to Create Session Activity
                                 //TODO: Add point to bundle for data return

                                 finish();
                             }
                         })
                         .setNegativeButton("No", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int id) {
                                //remove marker
                                 //TODO: remove marker if uses does not want it.
                             }
                         });

                 AlertDialog alert = builder.create();
                 alert.show();

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
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
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
     * centers map on current location
     *
     * code based off of: http://stackoverflow.com/questions/18425141/android-google-maps-api-v2-zoom-to-current-location
     */
    public void centerOnCurrentLocation(){
        Location currentLocation = mMap.getMyLocation();
        if (currentLocation != null) {
            LatLng currentPoint = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPoint, 11));
        }
    }

}
