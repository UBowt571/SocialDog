package com.example.mapstest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.app.Dialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int DEFAULT_ZOOM = 16;
    private static final String TAG = "MapsActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    Bitmap bagIcon;
    Bitmap treeIcon;
    LatLng currentLocation;
    ArrayList<JSONObject> markersList;

    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        if(isServicesOK()){
            getLocationPermission();
        }

        int height = 100;
        int width = 100;

        BitmapDrawable bagBitmap=(BitmapDrawable)getResources().getDrawable(R.drawable.sacaca);
        bagIcon = Bitmap.createScaledBitmap(bagBitmap.getBitmap(), width, height, false);


        BitmapDrawable treeBitmap=(BitmapDrawable)getResources().getDrawable(R.drawable.arbre);
        treeIcon = Bitmap.createScaledBitmap(treeBitmap.getBitmap(), width, height, false);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        JSONObject list_markers = assetLoader.JSON(this,"markers.json");
        markersList = assetLoader.getJSONArray(list_markers, "markers");

        int lati;
        int longi;
        String type;
        Bitmap markerIcon;

        try{
            JSONObject obj = new JSONObject();
            obj.put("type", "bag");
            obj.put("latitude", 20);
            obj.put("longitude", 20);
            markersList.add(obj);
        } catch(Exception ex){
            ex.printStackTrace();
            return;
        }




        for(int i = 0; i<markersList.size(); i++)
        {
            Log.e(TAG, Integer.toString(i));
            try{
                type = markersList.get(i).getString("type");
                lati = markersList.get(i).getInt("latitude");
                longi = markersList.get(i).getInt("longitude");
            } catch(Exception ex){
                ex.printStackTrace();
                return;
            }
            if ("bag".equals(type)) markerIcon = bagIcon;
            else markerIcon = treeIcon;

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lati, longi))
                    .icon(BitmapDescriptorFactory.fromBitmap(markerIcon))
                    .title(getAddress(lati, longi)));
        }


        if(mLocationPermissionsGranted){
            getLocation();
            goToLocation(currentLocation.latitude, currentLocation.longitude);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);

        }
    }

    private void getLocation()
    {
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else
        {
            Location LocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (LocationGPS != null)
            {
                currentLocation = new LatLng(LocationGPS.getLatitude(), LocationGPS.getLongitude());
            }
            else if (LocationNetwork != null)
            {
                currentLocation = new LatLng(LocationNetwork.getLatitude(), LocationNetwork.getLongitude());
            } else if (LocationPassive != null)
            {
                currentLocation = new LatLng(LocationPassive.getLatitude(), LocationPassive.getLongitude());
            } else {
                Toast.makeText(MapsActivity.this, "Can't get current location !", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            String add = obj.getAddressLine(0);

            return add;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        Log.e("onLocationChanged",Double.toString(currentLocation.latitude));
        Log.e("onLocationChanged",Double.toString(currentLocation.longitude));

        Toast.makeText(MapsActivity.this, "Location changed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    private void goToLocation(double lat, double lng)
    {
        LatLng position = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
    }

    private void goToLocation(double lat, double lng, float zoom)
    {
        LatLng position = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));
    }

    public void AddMarkerAtCurrentLocation(View view)
    {
        Log.e(TAG, "Hello there");
        getLocation();
        String[] markersTypes = {"Espace vert", "Sac à déjection"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quel type de lieu voulez-vous ajouter ?");
        builder.setItems(markersTypes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on markersTypes[which]
                Bitmap markerIcon;
                String markerTitle;
                switch(which){
                    case(0):
                    default:
                        markerIcon = treeIcon;
                        markerTitle = "Espace vert";
                        break;
                    case(1):
                        markerIcon = bagIcon;
                        markerTitle = "Sac à déjection";
                        break;
                }
                MarkerOptions options = new MarkerOptions()
                        .position(new LatLng(currentLocation.latitude, currentLocation.longitude))
                        .title(getAddress(currentLocation.latitude, currentLocation.longitude))
                        .icon(BitmapDescriptorFactory.fromBitmap(markerIcon));
                mMap.addMarker(options);
            }
        });
        builder.show();
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE); //Lauches onRequestPermissionsResult
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    //Tells if the app can have access to google play services, required
    //for google maps to work
    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK()");
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS)
        {
            Log.d(TAG, "isServicesOK() : Success");
            return true;
        }
        else
        {
            if (api.isUserResolvableError(isAvailable))
            {
                Log.d(TAG, "isServicesOK() : resolvable");
                Dialog dialog = api.getErrorDialog(this, isAvailable, ERROR_DIALOG_REQUEST);
                dialog.show();
            }
            else
            {
                Log.d(TAG, "isServicesOK() : Failure");
                Toast.makeText(this, "I can't connect to Google Play services !", Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }


}
