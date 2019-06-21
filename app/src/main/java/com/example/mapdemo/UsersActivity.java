package com.example.mapdemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.renderscript.Double3;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.database.core.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by delaroy on 3/27/17.
 */
public class UsersActivity extends AppCompatActivity
        implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    // INIT For Map

    private GoogleMap mMap;
    private AutoCompleteTextView editText;
    //    private SearchView mSearchView;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    View mapView;
    ArrayAdapter<String> Countryadapter;
    private final LatLng mDefaultLocation = new LatLng(10.762622, 106.660172);

    ArrayList<String> PLACES;
    ArrayList<Double> LATITUDE;
    ArrayList<Double> LONGTITUDE;
    ArrayList<String> POPPULATION;
    Map<String, String> markers;
    ArrayList<String> Loc_IDs;
    FirebaseAuth auth;


    private static final String TAG = "MapActivity";
    //private boolean mLocationPermissionGranted = fals


    private Integer userID;
    private TextView textViewName;
    String[] listviewTitle = new String[]{
            "Hồ Chí Minh", "Hà Nội", "Đã Nẵng", "Nha Trang",
            "Cần Thơ", "Phan Thiết", "Hội An", "Đà Lạt",
    };


    int[] listviewImage = new int[]{
            R.drawable.hochiminh, R.drawable.hanoi, R.drawable.danang, R.drawable.nhatrang,
            R.drawable.cantho, R.drawable.phanthiet, R.drawable.hoian, R.drawable.dalat,
    };

    String[] listviewShortDescription = new String[]{
            "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
            "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        PLACES = new ArrayList<String>();
        LATITUDE = new ArrayList<Double>();
        LONGTITUDE = new ArrayList<Double>();
        POPPULATION = new ArrayList<String>();
        markers = new HashMap<String, String>();
        Loc_IDs = new ArrayList<String>();

        // init Authenticator
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null){
            Intent intent= new Intent(UsersActivity.this, LoginActivity.class);
            startActivity(intent);
//            Toast.makeText(this, "user no null", Toast.LENGTH_LONG).show();
            finish();
        }
        setContentView(R.layout.activity_users);

        editText = findViewById(R.id.actv);

        getData();
        editText.setOnItemClickListener(mAutocompleteClickListener);
        editText.setAdapter(Countryadapter);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFrag != null;
        mapView = mapFrag.getView();
        mapFrag.getMapAsync(this);
        checkLocationPermission();

        mapFrag.getMapAsync(this);

    }

    @Override
    public void onResume(){
        super.onResume();

        if(mMap != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            mMap.clear();
            getAllMarker();
            // add markers from database to the map
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.action_menu:
                Intent i = new Intent(this, Home.class);
                startActivity(i);
//                finish();
                return true;

    }
        return super.onOptionsItemSelected(item);
    }
    private void getAllMarker(){
        for (int i = 0; i < PLACES.size(); i++) {

            LatLng latLng = new LatLng(LATITUDE.get(i), LONGTITUDE.get(i));

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(PLACES.get(i))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon))
                    .snippet("Population: ")
            );
            markers.put(marker.getId(), Loc_IDs.get(i));
            marker.hideInfoWindow();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
            mMap.setOnInfoWindowClickListener(this);

        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }

    private void getData() {


        PLACES.add("Hà Nội");
        Countryadapter = new ArrayAdapter(UsersActivity.this,
                android.R.layout.simple_list_item_1, PLACES);
//        Loc_ID.add
        LATITUDE.add(21.0);
        LONGTITUDE.add(105.75);
        POPPULATION.add("6.844.100");
        Loc_IDs.add("1");
        // Load Data (Places)
        Query query = FirebaseDatabase.getInstance().getReference().child("places");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getDataFromDB(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void getDataFromDB(DataSnapshot data){
        if (data.exists()) {
            for (DataSnapshot issue : data.getChildren()) {
                PLACES.add(issue.child("name").getValue(String.class));
                String temp = issue.child("latt").getValue(String.class);
                LATITUDE.add(Double.parseDouble(temp));
                temp = issue.child("longtt").getValue(String.class);
                LONGTITUDE.add(Double.parseDouble(temp));
                Loc_IDs.add(issue.child("id").getValue(String.class));
            }
            getAllMarker();
        }
    }

    //info marker click
    @Override
    public void onInfoWindowClick(Marker marker) {
        if (markers.size() <= 0){
            Toast.makeText(this, "markers empty",
            Toast.LENGTH_SHORT).show();
            return;
        }
        String locationID = markers.get(marker.getId());
        Intent i = new Intent(this, ReviewActivity.class);
        i.putExtra("LOCATION_ID", locationID);
        startActivity(i);
//        Toast.makeText(this, locationID,
//                Toast.LENGTH_SHORT).show();
//        //do something
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    //click on autocomplete textview
    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();
            String input = Countryadapter.getItem(i);

            if (input != null || !input.equals("")) {
                for (int k = 0; k < PLACES.size(); k++) {
                    if (input == PLACES.get(k)) {
                        Toast.makeText(UsersActivity.this, "Đã chọn "+PLACES.get(k), Toast.LENGTH_LONG).show();

                        LatLng latLng = new LatLng(LATITUDE.get(k), LONGTITUDE.get(k));
                        //infowindow
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(input)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon))
                        );

                        marker.hideInfoWindow();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    }

                }

            }
        }
    };

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setOnInfoWindowClickListener(this);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 5));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }
        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 180, 180, 0);
        }
        getAllMarker();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


                //move map camera
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    };

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.dlg_location_permission_needed_title)
                        .setMessage(R.string.dlg_msg_location_permission_needed)
                        .setPositiveButton(R.string.agree_btn_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(UsersActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, R.string.msg_permission_denied, Toast.LENGTH_LONG).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
