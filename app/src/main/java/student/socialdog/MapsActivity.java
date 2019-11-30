package student.socialdog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.app.Dialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends Fragment implements OnMapReadyCallback, LocationListener {

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
    DatabaseReference markersDB;
    LocationManager locationManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps2, container, false);
        super.onCreate(savedInstanceState);
        if(isServicesOK()){
            getLocationPermission();
        }

        int height = 100;
        int width = 100;

        BitmapDrawable bagBitmap=(BitmapDrawable)getResources().getDrawable(R.drawable.sacaca);
        bagIcon = Bitmap.createScaledBitmap(bagBitmap.getBitmap(), width, height, false);


        BitmapDrawable treeBitmap=(BitmapDrawable)getResources().getDrawable(R.drawable.arbre);
        treeIcon = Bitmap.createScaledBitmap(treeBitmap.getBitmap(), width, height, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        markersDB = database.getReference("markers");

        Button addmarker = rootView.findViewById(R.id.addmarker);

        addmarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMarkerAtCurrentLocation(v);
            }
        });



        return rootView;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Read from the database
        markersDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<HashMap> markers =  (ArrayList<HashMap>)dataSnapshot.getValue();

                Bitmap markerIcon;
                Object lati;
                Object longi;
                Object type;

                for(int i=0; i<markers.size(); i++)
                {
                    try{
                        type = markers.get(i).get("type");
                        lati = markers.get(i).get("latitude");
                        longi = markers.get(i).get("longitude");
                    } catch(Exception ex){
                        ex.printStackTrace();
                        return;
                    }
                    if ("bag".equals(type)){
                        markerIcon = bagIcon;
                    }
                    else{
                        markerIcon = treeIcon;
                    }

                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng((Double)lati, (Double) longi))
                            .icon(BitmapDescriptorFactory.fromBitmap(markerIcon))
                            .title(getAddress((Double)lati, (Double) longi)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        if(mLocationPermissionsGranted){
            getLocation();
            goToLocation(currentLocation.latitude, currentLocation.longitude);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);

        }
    }

    private void getLocation()
    {
        locationManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
                Toast.makeText(getContext(), "Can't get current location !", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            String add = obj.getAddressLine(0);

            return add;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        Log.e("onLocationChanged",Double.toString(currentLocation.latitude));
        Log.e("onLocationChanged",Double.toString(currentLocation.longitude));

        Toast.makeText(getContext(), "Location changed", Toast.LENGTH_SHORT).show();
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
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);

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

    public void addMarkerAtCurrentLocation(View view)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("message");
        //myRef.setValue("Hello, World!");

        getLocation();
        String[] markersTypes = {"Espace vert", "Sac à déjection"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

        if(ContextCompat.checkSelfPermission(this.getContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(getActivity(),
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
        int isAvailable = api.isGooglePlayServicesAvailable(getContext());
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
                Dialog dialog = api.getErrorDialog(getActivity(), isAvailable, ERROR_DIALOG_REQUEST);
                dialog.show();
            }
            else
            {
                Log.d(TAG, "isServicesOK() : Failure");
                Toast.makeText(getContext(), "I can't connect to Google Play services !", Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }


}