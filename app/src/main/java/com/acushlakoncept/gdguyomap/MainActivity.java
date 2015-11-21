package com.acushlakoncept.gdguyomap;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    ImageButton FAB;
    Button mBtn;
    MyLocation mLocation;
    String lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        //mBtn = (Button) findViewById(R.id.btnMap);

        mLocation = new MyLocation(this);
        locationFetcher();

       /* mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Starting new intent
                if(isOnline()){

                   *//*Toast.makeText(getActivity().getBaseContext(), "You clicked "+ keywords[position]
                           + " from " + lat + " , " + lng, Toast.LENGTH_SHORT).show();*//*

                    Intent intent = new Intent(getBaseContext(), MapsActivity.class)
                            .putExtra("lat", lat)
                            .putExtra("lng", lng);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "Network Isn't Available", Toast.LENGTH_LONG).show();
                }
            }
        });*/

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FAB = (ImageButton) findViewById(R.id.imageButton);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Starting new intent
                if(isOnline()){

                    Intent intent = new Intent(getBaseContext(), MapsActivity.class)
                            .putExtra("lat", lat)
                            .putExtra("lng", lng);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "Network Isn't Available", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        } else {
            return false;
        }
    }

    public void locationFetcher() {

        if (mLocation.canGetLocation()) {
            //mLocation.getLocation();
            double latitude = mLocation.getLatitude();
            double longitude = mLocation.getLongitude();
            lng = String.valueOf(longitude);
            lat = String.valueOf(latitude);
        } else {
            Toast.makeText(this, "LOCATION NOT ACQUIRED,TURN ON A PROVIDER", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        locationFetcher();
    }
}