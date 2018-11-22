package edu.osu.cse5236.group10.packmap;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import edu.osu.cse5236.group10.packmap.data.model.LocationInfo;
import edu.osu.cse5236.group10.packmap.data.store.LocationInfoStore;


public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String Tag= "MapsFragment";
    private static final String FINE_LOCATION= Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCAITON=Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUESTCODE=1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40,168), new LatLng(71,137));
    //widgets
    private AutoCompleteTextView mSearchText;
    private Activity mMapActivity;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView bottom_heading;
    private ImageView locateButton;
    private Button selectButton;
    //vars
    private Boolean mLocationPermissionGranted=false;
    private Boolean mGpsWorked=false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap;
    private PlaceAutoCompleteAdapter mPlaceAutoCompleteAdapter;
    private GeoDataClient mGeoDataClient;
    private Address currentSelectedAddress;
    private LocationInfoStore mLocationStore;
    private LocationInfo mLocationInfo;
    private String mActivityId;
    private LocationManager locationManager;
    private AlertDialog.Builder dialog;
    Context context;

    private View v;
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        dialog.setMessage("Google map connection failed");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapActivity=this.getActivity();
        context=this.getActivity();
        locationManager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        mActivityId=getActivity().getIntent().getStringExtra("activityId");
        getLocationPermission();
    }

    private void checkGpsAndWifi(){
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            dialog.setMessage("need to enable the location servcices");
            dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    //get gps
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                }
            });
            dialog.setNegativeButton("discard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                }
            });
            dialog.show();
        }else{
            mGpsWorked=true;
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_map, container, false);
        //firebase
        mLocationStore=mLocationStore.getInstance();
        //TODO might need change
        mLocationInfo=new LocationInfo();
        //register bottom sheet
        bottomSheetBehavior=BottomSheetBehavior.from(v.findViewById(R.id.bottom_Sheet_Layout));
        bottom_heading=v.findViewById(R.id.bottom_Sheet_Heading);
        selectButton=v.findViewById(R.id.select_button);
        locateButton=v.findViewById(R.id.ic_my_location);
        //searchText UI
        mSearchText=(AutoCompleteTextView) v.findViewById(R.id.input_search);
        mGeoDataClient=Places.getGeoDataClient(context);
        mPlaceAutoCompleteAdapter=new PlaceAutoCompleteAdapter(context, mGeoDataClient, LAT_LNG_BOUNDS,null);
        dialog=new AlertDialog.Builder(context);
        mSearchText.setAdapter(mPlaceAutoCompleteAdapter);
        if(mSearchText==null){
            Log.d(Tag, "onCreateView: null returned");
        }
        initMap();

        return v;
    }
    //Search area UI
    private void initSearchText(){
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
        hideSoftKeyboard();
    }
    //Search area UI
    private void geoLocate(){
        Log.d(Tag, "geoLocate: geoLocating");

        String searchString=mSearchText.getText().toString();

        Geocoder geocoder=new Geocoder(this.getActivity());
        List<Address> list=new ArrayList<>();
        try{
            list=geocoder.getFromLocationName(searchString,1);
        }catch (IOException e){
            Log.e(Tag, "geoLocate: IOException" + e.getMessage() );
        }

        if(list.size()>0){
            currentSelectedAddress = list.get(0);
            Log.d(Tag, "geoLocate: found a location: " +  list.toString());
            moveCamera(new LatLng(currentSelectedAddress.getLatitude(),currentSelectedAddress.getLongitude()),DEFAULT_ZOOM,currentSelectedAddress.getAddressLine(0));
        }
    }

    private void initMap(){
        Log.d(Tag,"initMap; initializing");
        SupportMapFragment mapFragment=(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment!=null) {
            mapFragment.getMapAsync(this);
        }else{
            Log.d(Tag, "initMap: mapFragment is null");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this.getActivity(),"Map is ready",Toast.LENGTH_SHORT).show();
        Log.d(Tag,"onMapReady: map is ready");
        checkGpsAndWifi();
        if(mLocationPermissionGranted&&mGpsWorked){
            mMap = googleMap;
            getDeviceLocation();
            if(ActivityCompat.checkSelfPermission(mMapActivity,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mMapActivity,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    bottom_heading.setText("Address Information");
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            });
            //get current device location
            locateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDeviceLocation();
                }
            });

            mMap.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
                @Override
                public void onPoiClick(PointOfInterest poi) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    String info=poi.name;
                    bottom_heading.setText(info);
                    mLocationInfo.setName(poi.name);
                    mLocationInfo.setCoordinates(new GeoPoint(poi.latLng.latitude,poi.latLng.longitude));
                }
            });
            //add current selected location
            selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mActivityId==null){
                        Toast.makeText(mMapActivity, "Need add activity to put location", Toast.LENGTH_SHORT).show();
                    }else {
                        mLocationStore.addNewLocation(mLocationInfo,mActivityId);
                        Toast.makeText(mMapActivity, "location added", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            initSearchText();
        }
    }

    private void getLocationPermission(){
        Log.d(Tag,"getLocationPermission: getting permission");
        String[] permissions={
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        if(ContextCompat.checkSelfPermission(mMapActivity.getApplicationContext(),FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(mMapActivity.getApplicationContext(),COURSE_LOCAITON)
                    ==PackageManager.PERMISSION_GRANTED){
                //set
                mLocationPermissionGranted=true;
            }else{
                requestPermissions(permissions,LOCATION_PERMISSION_REQUESTCODE);
            }
        }else{
            requestPermissions(permissions,LOCATION_PERMISSION_REQUESTCODE);
        }

    }

    private void getDeviceLocation(){
        Log.d(Tag,"getDeviceLocation: getting the current devices location");
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(mMapActivity);

        try{
            if(mLocationPermissionGranted){
                Task location=mFusedLocationProviderClient.getLastLocation();
                Task task = location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(Tag, "onComplete: found locaiton");
                            Location currentLocaiton = (Location) task.getResult();
                            if (currentLocaiton != null) {
                                moveCamera(new LatLng(currentLocaiton.getLatitude(), currentLocaiton.getLongitude()),
                                        DEFAULT_ZOOM,"My Locaiton");
                            } else {
                                Log.d(Tag, "onComplete: null returned");
                            }
                        } else {
                            Log.d(Tag, "onComplete: current locaiton is null");
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch(SecurityException e){
            Log.e(Tag, "getDeviceLocation: SecurityExeption; "+e.getMessage());
        }
    }


    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(Tag, "moveCamera: moving the camera to: lat: " + latLng.latitude+", lng "+ latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        if(!title.equals("My Locaiton")){
            MarkerOptions options= new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(options);
        }
        hideSoftKeyboard();
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

    private void hideSoftKeyboard(){
        // view = this.getActivity().getCurrentFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}