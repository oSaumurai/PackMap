package edu.osu.cse5236.group10.packmap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import com.annimon.stream.Stream;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.osu.cse5236.group10.packmap.data.DataUtils;
import edu.osu.cse5236.group10.packmap.data.model.ActivityInfo;
import edu.osu.cse5236.group10.packmap.data.model.Group;
import edu.osu.cse5236.group10.packmap.data.model.LocationInfo;
import edu.osu.cse5236.group10.packmap.data.store.ActivityStore;
import edu.osu.cse5236.group10.packmap.data.store.GroupStore;


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
    private Button upVoteButtom,downVoteButtom;
    //vars
    private Boolean mLocationPermissionGranted=false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap;
    private PlaceAutoCompleteAdapter mPlaceAutoCompleteAdapter;
    private GeoDataClient mGeoDataClient;
    private Address currentSelectedAddress;
    private ActivityStore mActivityStore;
    private ActivityInfo mActivityInfo;
    private LocationInfo mLocationInfo;
    private List<LocationInfo> mLocationInfoList;
    private String mGroupId;
    private String mActivityId;
    private String userId;

    Context context;

    private View v;
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapActivity=this.getActivity();

        mGroupId=getActivity().getIntent().getStringExtra("groupId");
        mActivityId=getActivity().getIntent().getStringExtra("activityId");
        userId=getActivity().getIntent().getStringExtra("userId");
        getLocationPermission();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_map, container, false);
        context = this.getActivity();
        //firebase
        mActivityStore=ActivityStore.getInstance();
        //TODO might need change
        mActivityInfo=new ActivityInfo();
        mLocationInfo=new LocationInfo();
        mLocationInfoList=new ArrayList<>();
        //register bottom sheet
        bottomSheetBehavior=BottomSheetBehavior.from(v.findViewById(R.id.bottom_Sheet_Layout));
        bottom_heading=v.findViewById(R.id.bottom_Sheet_Heading);
        upVoteButtom=v.findViewById(R.id.up_vote);
        downVoteButtom=v.findViewById(R.id.down_vote);
        //searchText UI
        mSearchText=(AutoCompleteTextView) v.findViewById(R.id.input_search);
        mGeoDataClient=Places.getGeoDataClient(context);
        mPlaceAutoCompleteAdapter=new PlaceAutoCompleteAdapter(context, mGeoDataClient, LAT_LNG_BOUNDS,null);
        mSearchText.setAdapter(mPlaceAutoCompleteAdapter);
        if(mSearchText==null){
            Log.d(Tag, "onCreateView: null returned");
        }
        initMap();

        return v;
    }
    //Search area UI
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
        mMap = googleMap;

        if(mLocationPermissionGranted){
            getDeviceLocation();
            if(ActivityCompat.checkSelfPermission(mMapActivity,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mMapActivity,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            });

            mMap.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
                @Override
                public void onPoiClick(PointOfInterest poi) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    String info=poi.name+"\nLatitude"+poi.latLng.latitude+"\nLongtitude"+poi.latLng.longitude;
                    bottom_heading.setText(info);
                    mLocationInfo.setName(poi.name);
                    mLocationInfo.setCoordinates(new GeoPoint(poi.latLng.latitude,poi.latLng.longitude));
                    mActivityInfo.setName("233");
                    mActivityInfo.setInfo("sadwa");

                    mActivityStore.addLocation(mActivityId,mLocationInfo);
                    //addActivity(mActivityInfo);
                    //mActivityStore.addNewActivity(mActivityInfo.getName(),mLocationInfo,"233");
                }
            });

            //init and send data to BottomView
            upVoteButtom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAllLocations();
                }

            });

            downVoteButtom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivityStore.removeFromlist(0);
                }
            });

            //marker listener
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    //TODO set info
                    String temp=currentSelectedAddress.getAddressLine(0);
                    bottom_heading.setText(temp);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    return true;
                }
            });


            init();
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
    ////////// Down here is for database test/////////////


    private void addPositionToActivity(String activityName, LocationInfo newLocation){
        ActivityStore.getInstance().getActivityById("ccc", new GetActivityOnCompleteListener());
        List<LocationInfo> mLocaitonInfo=mActivityInfo.getSelectedLocations();
        Log.d(Tag, "addPositionToActivity: "+ mLocaitonInfo);
        mLocaitonInfo.add(newLocation);
    }

    public class GetActivityOnCompleteListener implements OnCompleteListener<QuerySnapshot> {
         @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(Tag, document.getId() + " => " + document.getData());
                    mActivityInfo = DataUtils.getObject(document, ActivityInfo.class);
                }
            }
        }
    }

    private void showAllLocations(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference dr = db.collection("activities").document("cEsA1ro73MXwX7osMWTu");
        //mActivityInfo=mActivityStore.getActivityById("cEsA1ro73MXwX7osMWTu",);
        mLocationInfoList=mActivityInfo.getSelectedLocations();
        Log.d(Tag, "showAllLocations: " + mLocationInfoList);



        //MarkerOptions options= new MarkerOptions().position(latLng).title(title);
        //mMap.addMarker(options);


    }

}