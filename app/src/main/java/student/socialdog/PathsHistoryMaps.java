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
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map;


public class PathsHistoryMaps extends Fragment implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int DEFAULT_ZOOM = 14;
    private static final String TAG = "PathsHistoryMaps";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    Bitmap bagIcon;
    Bitmap treeIcon;
    LatLng currentLocation;
    ArrayList<JSONObject> markersList;
    ArrayList<LatLng> pathList;
    Polyline pathPolyline;
    ArrayList<Path> allPaths;
    Marker startMarker;
    int currentPathId = 0;
    TextView dateText;
    TextView durationText;
    TextView dogsText;
    boolean init = true;
    boolean emptyHistory = false; //true if no path in history


    DatabaseReference pathsDB;
    DatabaseReference markersDB, markers_unapprovedDB, allMarkersDB;
    LocationManager locationManager;

    private class Path implements Comparable<Path>
    {
        int pathId;
        String key;
        String date;
        String duration;
        ArrayList<LatLng> points;
        ArrayList<String> dogs;

        @Override
        public int compareTo(Path p)
        {
            return(p.pathId - pathId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_paths_history_maps, container, false);
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

        Button nexWalkButton = rootView.findViewById(R.id.nextWalk);
        Button previousWalkButton = rootView.findViewById(R.id.previousWalk);
        Button deleteWalkButton = rootView.findViewById(R.id.deleteWalk);
        dateText = rootView.findViewById(R.id.dateText);
        durationText = rootView.findViewById(R.id.durationText);
        dogsText = rootView.findViewById(R.id.dogsTest);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        markersDB = database.getReference("markers");
        pathsDB = database.getReference("paths");


        nexWalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextWalk();
            }
        });

        previousWalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousWalk();
            }
        });

        deleteWalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWalk();
            }
        });
        return rootView;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        allPaths = new ArrayList<Path>();
        pathList = new ArrayList<>();
        // Lecture des marqueurs depuis database FireBase
        markersDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Map> markers =  (HashMap<String,Map>)dataSnapshot.getValue();
                Bitmap markerIcon;
                Object lati;
                Object longi;
                Object type;
                for(Map.Entry<String, Map> current : markers.entrySet())
                {
                    try{
                        type = current.getValue().get("type");
                        lati = current.getValue().get("latitude");
                        longi = current.getValue().get("longitude");
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
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        pathsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Map> paths =  (HashMap<String,Map>)dataSnapshot.getValue();
                Object lat;
                Object lng;
                allPaths.clear();
                for(Map.Entry<String, Map> current : paths.entrySet())
                {
                    if(current.getValue().get("userkey").toString().equals(MainActivity.userKey)){
                        int i = 0;
                        int j = 0;
                        while (current.getValue().get("latitude" + i) != null)
                        {
                            lat = current.getValue().get("latitude" + i);
                            lng = current.getValue().get("longitude" + i);
                            pathList.add(new LatLng((Double)lat,(Double) lng));
                            //Log.e(TAG,"lat " + i + " : " + Double.toString(pathList.get(i).latitude));
                            //Log.e(TAG,"lng " + i + " : " + Double.toString(pathList.get(i).longitude));
                            i++;
                        }


                        Path newPath = new Path();
                        newPath.pathId = ((Long) current.getValue().get("id")).intValue();
                        newPath.points = pathList;
                        newPath.date = current.getValue().get("date").toString();
                        newPath.duration = current.getValue().get("duration").toString();
                        newPath.key = current.getKey();
                        newPath.dogs = new ArrayList<>();

                        while (current.getValue().get("dog" + j) != null)
                        {
                            newPath.dogs.add(current.getValue().get("dog" + j).toString());
                            j++;
                        }

                        allPaths.add(newPath); //TO SORT;
                        pathList = new ArrayList<>();
                    }
                }
                emptyHistory = (allPaths.size() == 0);
                Collections.sort(allPaths);
                if(init && !emptyHistory)
                {
                    showPathInfos(currentPathId);
                    init = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        if(mLocationPermissionsGranted){
            getLocation();
            goToLocation(currentLocation.latitude, currentLocation.longitude);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
        }
    }

    void nextWalk()
    {
        if(!emptyHistory)
        {
            if(currentPathId < allPaths.size()-1)
            {
                showPathInfos(++currentPathId);
            }
        } else Toast.makeText(getContext(), "Aucune balade dans l'historique", Toast.LENGTH_SHORT).show();

    }

    void previousWalk()
    {
        if(!emptyHistory)
        {
            if(currentPathId > 0)
                showPathInfos(--currentPathId);
        } else Toast.makeText(getContext(), "Aucune balade dans l'historique", Toast.LENGTH_SHORT).show();
    }

    void deleteWalk()
    {
        if(!emptyHistory)
        {
            Toast.makeText(getContext(), "Balade supprimée", Toast.LENGTH_SHORT).show();
            removePath(currentPathId);
            if(allPaths.size() > 1)
            {
                if(currentPathId == allPaths.size()-1) showPathInfos(--currentPathId);
                else showPathInfos(++currentPathId);
            }
            else
            {
                if (pathPolyline != null) pathPolyline.remove(); //Reset the polyline
                if (startMarker != null) startMarker.remove();
                dogsText.setText("");
                dateText.setText("");
                durationText.setText("");
            }

        } else Toast.makeText(getContext(), "Aucune balade dans l'historique", Toast.LENGTH_SHORT).show();

    }

    public void removePath(int cId)
    {
        if(!emptyHistory)
        {
            Log.e("removePath", "I REMOVED THE PATH " + cId);
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            pathsDB = database.getReference("paths");
            pathsDB.child(allPaths.get(cId).key).setValue(null); //deletes a path in FB
        }

    }



    void showPathInfos(int cId)
    {
        drawPathId(cId);
        dateText.setText("Date : " + allPaths.get(cId).date);
        durationText.setText("Durée : " + allPaths.get(cId).duration);
        if(allPaths.get(cId).dogs.size() > 1)
        {
            String allDogs = "Chiens : ";
            for(String dog : allPaths.get(cId).dogs)
            {
                allDogs += dog + " ";
            }
            dogsText.setText(allDogs);
        } else
        {
            if (allPaths.get(cId).dogs.size() == 0)  dogsText.setText("");
            else dogsText.setText("Chien : " + allPaths.get(cId).dogs.get(0));
        }

    }

    //Move Camera to the center of a walk
    void placeCameraOnPath(ArrayList<LatLng> pathFocus)
    {
        double medLat = 0;
        double medLng = 0;
        for (int i = 0; i < pathFocus.size(); i++)
        {
            medLat += pathFocus.get(i).latitude;
            medLng += pathFocus.get(i).longitude;
        }
        medLat = medLat/pathFocus.size();
        medLng = medLng/pathFocus.size();
        goToLocation(medLat,medLng,DEFAULT_ZOOM);
    }


    //Draw a path from an array of latlng
    private void drawPath(ArrayList<LatLng> pointList) {
        if (pathPolyline != null) pathPolyline.remove(); //Reset the polyline
        if (startMarker != null) startMarker.remove();

        PolylineOptions polylineOptions = new PolylineOptions();
        for (int i = 0; i < pointList.size(); i = i + 1) {
            polylineOptions.add(new LatLng(pointList.get(i).latitude,
                    pointList.get(i).longitude));
        }

        LatLng startLatLng = new LatLng(pointList.get(0).latitude, pointList.get(0).longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(startLatLng);
        markerOptions.title("Start Position");
        startMarker = mMap.addMarker(markerOptions);

        polylineOptions.color(Color.BLUE);
        polylineOptions.width(10);
        polylineOptions.geodesic(false);
        pathPolyline = mMap.addPolyline(polylineOptions);
        placeCameraOnPath(pointList);
    }

    void drawPathId(int id)
    {
        if(!emptyHistory)
        {
            drawPath(allPaths.get(id).points);
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
        mMap.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
    }

    private void goToLocation(double lat, double lng, float zoom)
    {
        LatLng position = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
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
