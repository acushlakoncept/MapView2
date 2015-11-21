package com.acushlakoncept.gdguyomap;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    private static final int GPS_ERRORDIALOG_REQUEST = 9001;
    private static final float DEFAULT_ZOOM = 15;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    String placeName;
    Double lat, lng, urLat, urLng;
    Marker marker;
    Circle shape;
    private ShareActionProvider mShareActionProvider;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        //setUpMapIfNeeded();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lat = Double.valueOf(extras.getString("lat").toString());
            lng = Double.valueOf(extras.getString("lng").toString());
        } else {
            // set default value for now

        }

        if (servicesOK()) {
            //setContentView(R.layout.activity_maps);

            if (initMap()) {
                //Toast.makeText(this, "Ready to map!", Toast.LENGTH_SHORT).show();
                gotoLocation(lat, lng, DEFAULT_ZOOM);

                /*LatLng urLatLng = new LatLng(lat, lng);
                MarkerOptions options = new MarkerOptions()
                        .title("You are here!")
                        .position(urLatLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_BLUE
                        ));
                mMap.addMarker(options).showInfoWindow();*/
            }
            else {
                Toast.makeText(this, "Map not available!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
           // setContentView(R.layout.activity_main);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mapTypeNone:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.shareMapLocation:
                createShareLocationIntent();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean servicesOK() {
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        }
        else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this, "Can't connect to Google Play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean initMap() {
        if (mMap == null) {
            SupportMapFragment mapFrag =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = mapFrag.getMap();
        }
        return (mMap != null);
    }

    private void gotoLocation(double lat, double lng, float zoom){
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        CameraUpdate zoomUpdate =CameraUpdateFactory.zoomTo(zoom);
        mMap.moveCamera(update);
        mMap.animateCamera(zoomUpdate, 5000, null);
        //mMap.animateCamera(zoomUpdate);

        LatLng latLng = new LatLng(lat, lng);

        MarkerOptions options = new MarkerOptions()
                .title("You are Here")
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker());
        mMap.addMarker(options).showInfoWindow();
        shape = drawCircle(latLng);
    }

    private Circle drawCircle(LatLng ll) {

        CircleOptions options = new CircleOptions()
                .center(ll)
                .radius(35)
                .fillColor(0x300000FF)
                .strokeColor(Color.BLUE)
                .strokeWidth(3);

        return mMap.addCircle(options);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MapStateManager mgr = new MapStateManager(this);
        mgr.saveMapState(mMap);
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        MapStateManager mgr = new MapStateManager(this);
        CameraPosition position = mgr.getSavedCameraPosition();
        if (position != null){
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            mMap.moveCamera(update);
           // mMap.setMapType(mgr.getMapType());
        }
    }*/

    @SuppressWarnings("deprecation")
    private Intent createShareLocationIntent() {
        final String url = "http://maps.google.com/maps?q=";
        final String shareMsg = "Here is the direction to " +placeName + ": "
                + url + lat + "," + lng;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMsg);
        startActivity(Intent.createChooser(shareIntent, "Share using: "));
        return shareIntent;
    }
}
