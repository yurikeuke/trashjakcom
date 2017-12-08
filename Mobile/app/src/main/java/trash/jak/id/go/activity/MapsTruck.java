package trash.jak.id.go.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import trash.jak.id.go.R;
import trash.jak.id.go.utils.FetchAddressIntentService;
import trash.jak.id.go.utils.Utils;

import static android.content.ContentValues.TAG;

/**
 * Created by itp on 07/12/17.
 */

public class MapsTruck  extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener  {

    public GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private boolean fabExpanded = false;
    private Toolbar _toolbar;
    private FloatingActionButton fabMain, fabGreen;
    private AddressResultReceiver mResultReceiver;
    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput, latitude, longitude;
    private LatLng mCenterLatLong;
    private FloatingActionButton fabSettings;
    private LinearLayout layoutWisata,layoutMall,layoutKuliner,layoutShowAll;
    Marker marker1,marker2,marker3,marker4,marker5,marker6,
            marker7,marker8,marker9,marker10,marker11,marker12,
            marker13,marker14,marker15,marker16,marker17,marker18,
            marker19,marker20,marker21,marker22,marker23,marker24,marker25,marker26,marker27,marker28, marker29,marker30, marker31;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapstruck);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maptruck);
        mapFragment.getMapAsync(this);
        _toolbar    = (Toolbar) findViewById(R.id.toolbarMaps);
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Tracking Truck");
        fabGreen = (FloatingActionButton) findViewById(R.id.fabGreen);
        fabGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsTruck.this);
                builder.setMessage("Yakin akan menon-aktifkan tracking?")
                        .setCancelable(false)
                        .setIcon(R.drawable.map)
                        .setTitle("Tracking Truck")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                                    @Override
                                    public void onCameraChange(CameraPosition cameraPosition) {
                                        mCenterLatLong = cameraPosition.target;
                                        mMap.clear();

                                    }
                                });
                                Toast.makeText(MapsTruck.this, "Aktif" , Toast.LENGTH_SHORT).show();
                                fabGreen.setVisibility(View.GONE);
                                fabMain.setVisibility(View.VISIBLE);
                            }
                        })
                        .setNegativeButton("Tidak",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to invoke NO event
                                        dialog.cancel();
                                    }
                                });
                builder.show();


            }
        });

        fabMain = (FloatingActionButton) findViewById(R.id.fabSetting);
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsTruck.this);
                builder.setMessage("Yakin akan mengaktifkan tracking?")
                        .setCancelable(false)
                        .setIcon(R.drawable.map)
                        .setTitle("Tracking Truck")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                                    @Override
                                    public void onCameraChange(CameraPosition cameraPosition) {
                                        mCenterLatLong = cameraPosition.target;
                                        mMap.clear();
                                        try {

                                            Location mLocation = new Location("");
                                            mLocation.setLatitude(mCenterLatLong.latitude);
                                            mLocation.setLongitude(mCenterLatLong.longitude);

                                            startIntentService(mLocation);
                                            //mLocationMarkerText.setText("Lat : " + mCenterLatLong.latitude + "," + "Long : " + mCenterLatLong.longitude);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                Toast.makeText(MapsTruck.this, "Aktif" , Toast.LENGTH_SHORT).show();
                                fabGreen.setVisibility(View.VISIBLE);
                                fabMain.setVisibility(View.GONE);
                            }
                        })
                        .setNegativeButton("Tidak",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke NO event
                                dialog.cancel();
                            }
                        });
                builder.show();


            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        BitmapDescriptor icontruck = BitmapDescriptorFactory.fromResource(R.drawable.map);
        BitmapDescriptor iconmall = BitmapDescriptorFactory.fromResource(R.drawable.map);
        BitmapDescriptor iconmosque = BitmapDescriptorFactory.fromResource(R.drawable.map);


        // Add a marker in Jakarta, and move the camera.
        LatLng jakarta = new LatLng(-6.21462, 106.84513);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(jakarta)      // Sets the center of the map to Mountain View
                .zoom(4)                   // Sets the zoom
                // .bearing(90)                // Sets the orientation of the camera to east
                .tilt(20)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder


        //Wisata
        marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.170171, 106.831421)).title("B 1234 JKT")
                .draggable(true).icon(icontruck));
        marker2 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.135192, 106.813291)).title("B 1235 JKT")
                .draggable(true).icon(icontruck));
        marker3 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.175400, 106.827166)).title("B 1236 JKT")
                .draggable(true).icon(icontruck));
        marker4 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.125778, 106.842843)).title("B 1237 JKT")
                .draggable(true).icon(icontruck));
        marker5 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.2006518, 106.840763)).title("B 1238 JKT")
                .draggable(true).icon(icontruck));
        marker6 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.302531, 106.895177)).title("B 1239 JKT")
                .draggable(true).icon(icontruck));

        //location rakornas
        marker7 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.122100, 106.836316)).title("B 1240 JKT")
                .draggable(true).icon(icontruck));
        //marker7.showInfoWindow();

        //Restauran
        marker8 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.200564, 106.852938)).title("B 1241 JKT")
                .draggable(true).icon(icontruck));
        marker9 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.223703, 106.842091)).title("B 1242 JKT")
                .draggable(true).icon(icontruck));
        marker10 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.248900, 106.798030)).title("B 1243 JKT")
                .draggable(true).icon(icontruck));


        //mMap.moveCamera(CameraUpdateFactory.newLatLng(jakarta));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(jakarta, 10.8f));
        enableMyLocation();
    }
    private void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(MapsTruck.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void startIntentService(Location mLocation) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(MapsTruck.this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Utils.LocationConstants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Utils.LocationConstants.LOCATION_DATA_EXTRA, mLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        MapsTruck.this.startService(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null)
                changeMap(location);
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Utils.LocationConstants.RESULT_DATA_KEY);

            mAreaOutput = resultData.getString(Utils.LocationConstants.LOCATION_DATA_AREA);

            mCityOutput = resultData.getString(Utils.LocationConstants.LOCATION_DATA_CITY);
            mStateOutput = resultData.getString(Utils.LocationConstants.LOCATION_DATA_STREET);

            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Utils.LocationConstants.SUCCESS_RESULT) {
                //  showToast(getString(R.string.address_found));


            }


        }

    }
    /**
     * Updates the address in the UI.
     */
    protected void displayAddressOutput() {
        //  mLocationAddressTextView.setText(mAddressOutput);
        try {
            /*if (mAreaOutput != null)
                // mLocationText.setText(mAreaOutput+ "");

                mLocationAddress.setText(mAddressOutput);*/
            // mLocationMarkerText.setText(mAddressOutput);
            //mLocationText.setText(mAreaOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeMap(Location location) {

        Log.d(TAG, "Reaching map" + mMap);

        if (ActivityCompat.checkSelfPermission(MapsTruck.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MapsTruck.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // check if map is created successfully or not
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(false);
            LatLng latLong;


            latLong = new LatLng(location.getLatitude(), location.getLongitude());

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(19f).build();

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            //mLocationMarkerText.setText("Lat : " + location.getLatitude() + "," + "Long : " + location.getLongitude());
            startIntentService(location);


        } else {
            Toast.makeText(MapsTruck.this,
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }

    }

}
