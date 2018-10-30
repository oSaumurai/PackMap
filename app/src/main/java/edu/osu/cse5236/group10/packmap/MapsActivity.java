package edu.osu.cse5236.group10.packmap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String Tag= "MapsActivity";
    private static final String FINE_LOCATION=Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCAITON=Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUESTCODE=1234;
    private static final float DEFAULT_ZOOM = 15f;
    //widgets
    private EditText mSearchText;
    //vars
    private Boolean mLocationPermissionGranted=false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mSearchText=(EditText) findViewById(R.id.input_search);

        getLocationPermission();

        init();
    }

    private void init(){
        Log.d(Tag, "init: initializing");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction()==KeyEvent.ACTION_DOWN
                        || event.getAction()==KeyEvent.KEYCODE_ENTER){
                    //Searching items in map
                    geoLocate();
                }
                return false;
            }
        });
    }

    private void geoLocate(){
        Log.d(Tag, "geoLocate: geoLocating");

        String searchString=mSearchText.getText().toString();

        Geocoder geocoder=new Geocoder(MapsActivity.this);
        List<Address> list=new ArrayList<>();
        try{
            list=geocoder.getFromLocationName(searchString,1);
        }catch (IOException e){
            Log.e(Tag, "geoLocate: IOException" + e.getMessage() );
        }

        if(list.size()>0){
            Address address=list.get(0);
            Log.d(Tag, "geoLocate: found a location: " +  address.toString());
            //Toast.makeText(this, address.toString(),Toast.LENGTH_SHORT).show();
        }
    }


    private void initMap(){
        Log.d(Tag,"initMap; initializing");
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this,"Map is ready",Toast.LENGTH_SHORT).show();
        Log.d(Tag,"onMapReady: map is ready");
        mMap = googleMap;

        if(mLocationPermissionGranted){
            getDeviceLocation();
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }

    private void getLocationPermission(){
        Log.d(Tag,"getLocationPermission: getting permission");
        String[] permissions={
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COURSE_LOCAITON)
                    ==PackageManager.PERMISSION_GRANTED){
                //set
                mLocationPermissionGranted=true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUESTCODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUESTCODE);
        }

    }

    private void getDeviceLocation(){
        Log.d(Tag,"getDeviceLocation: getting the current devices location");
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionGranted){
                Task location=mFusedLocationProviderClient.getLastLocation();
                Task task = location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && task != null) {
                            Log.d(Tag, "onComplete: found locaiton");
                            Location currentLocaiton = (Location) task.getResult();
                            if (currentLocaiton != null) {
                                moveCamera(new LatLng(currentLocaiton.getLatitude(), currentLocaiton.getLongitude()),
                                        DEFAULT_ZOOM);
                            } else {
                                Log.d(Tag, "onComplete: null returned");
                            }
                        } else {
                            Log.d(Tag, "onComplete: current locaiton is null");
                            Toast.makeText(MapsActivity.this, "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch(SecurityException e){
            Log.e(Tag, "getDeviceLocation: SecurityExeption; "+e.getMessage());
        }
    }


    private void moveCamera(LatLng latLng, float zoom){
        Log.d(Tag, "moveCamera: moving the camera to: lat: " + latLng.latitude+", lng "+ latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(Tag,"onRequestPermissionResult: called");
        mLocationPermissionGranted=false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUESTCODE:{
                if(grantResults.length >0){
                    for(int i=0;i<grantResults.length;i++){
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted=false;
                            Log.d(Tag,"onRequestPermissionResult: permission failed");
                            return;
                        }
                    }
                    Log.d(Tag,"onRequestPermissionResult: permission granted");
                    mLocationPermissionGranted=true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }
}
