package themedicall.com;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import themedicall.com.Globel.Glob;
import themedicall.com.HelperClasses.MapHelper;

import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MapActivityForSelectingHospital extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    TextView tv_pickuplocation;
    RelativeLayout rl_search_for_place_auto_complete;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int REQUEST_FINE_LOCATION = 11;
    private LatLng latlngPickup;

    private GoogleMap mMap;
    MapHelper mapHelper;
    private int timer = 3;
    Handler mHandler;
    JSONObject jsonObject;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    SupportMapFragment mapFragment;
    String mPickupAddress, mDropOffLocation;
    double latitude; // latitude
    double longitude; // longitude

    LatLng latLng;
    String mCity;
    LatLng mLatLng;
    String mAddress;

    Location myCurrentLocation;
    Location myStaticCurrentLocation;
    Marker mCurrLocationMarker;
    String cancel_req_tag = "MapActivity";

    Button bt_submit_new_hospital;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_for_selecting_hospital);

        init();
        turnOnGPS();
        getPickupLocation();

        createNetErrorDialog();
        checkPermission();

        mapFragment.getMapAsync(this);

        mapHelper = new MapHelper();

        loadingHospitalHonServer();




    }//end of onCreate

    private void init()
    {

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        tv_pickuplocation = (TextView) findViewById(R.id.tv_pickuplocation);
        rl_search_for_place_auto_complete = (RelativeLayout) findViewById(R.id.rl_search_for_place_auto_complete);

        bt_submit_new_hospital = (Button) findViewById(R.id.bt_submit_new_hospital);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

    }

    public void getPickupLocation()
    {

        rl_search_for_place_auto_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                callingLocationDialog();

            }
        });
    }//end of getting pickup location

    public void callingLocationDialog(){


        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setBoundsBias(new LatLngBounds(new LatLng(23.695,  68.149), new LatLng(35.88250, 76.51333)))//south and north latlong bourdy for pakistan
                            .build(MapActivityForSelectingHospital.this);




            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e("TAg", "the code is result: " + resultCode);
        Log.e("TAg", "the code is resquest: " + requestCode);
        Log.e("TAg", "the code is Intent: " + data);

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                String plceName = place.getName().toString();
                String plceAddress = place.getAddress().toString();

                LatLng latlng = place.getLatLng();
                Log.i("TAG", "Place: 123" + place.getName());
                Log.i("TAG", "Place: " + place.getAddress());
                mAddress = place.getAddress().toString();
                getAddressApi(latlng.latitude, latlng.longitude);


                Log.i("TAG", "Place Coordinates: " + place.getLatLng());


                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latlng);
                markerOptions.title(plceAddress);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_myplaces));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                mMap.addMarker(markerOptions).showInfoWindow();

                tv_pickuplocation.setText(plceAddress);
                tv_pickuplocation.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.whiteColor));
                latlngPickup = latlng;
                mLatLng = latlng;


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("TAG", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }//end of onActivity Result


    public void turnOnGPS()
    {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(MapActivityForSelectingHospital.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mGoogleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            //**************************
            builder.setAlwaysShow(true); //this is the key ingredient
            //**************************

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        MapActivityForSelectingHospital.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }


    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    protected void createNetErrorDialog() {

        if (isNetworkAvailable()==false){


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You need a network connection to use this application. Please turn on mobile network or Wi-Fi in Settings.")
                    .setTitle("Unable to connect")
                    .setCancelable(false)
                    .setPositiveButton("Settings",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                    startActivity(i);
                                }
                            }
                    )
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    MapActivityForSelectingHospital.this.finish();
                                }
                            }
                    );
            AlertDialog alert = builder.create();
            alert.show();
        }else {
            //remainging
        }
    }




    //market for Pickup Location
    public void addingMarketForPickLocation(LatLng pickup, String pickupTitle){
        mMap.addMarker(new MarkerOptions()
                .position(pickup)
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup_icon))
                .title(pickupTitle));

    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d("MapActivity", "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }


    public void setMyLocationEnable(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        }else {
            mMap.setMyLocationEnabled(true);


        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setMyLocationEnable();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        buildGoogleApiClient();
        mGoogleApiClient.connect();

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                double cLat = mMap.getCameraPosition().target.latitude;
                double cLng = mMap.getCameraPosition().target.longitude;
                Log.e("TAG", "the current postion on marker lat is: " + cLat);
                Log.e("TAG", "the current postion on marker lng is: " + cLng);
                getAddressApi(cLat, cLng);
                mLatLng = new LatLng(cLat, cLng);


            }
        });

    }


    ///

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

            myCurrentLocation = mLastLocation;
            myStaticCurrentLocation = mLastLocation;

            mapHelper.setLatitude(latitude);
            mapHelper.setLongitude(longitude);

            Log.e("latlang" , "latitudeCustomer "+latitude);
            Log.e("latlang" , "longitudeCustomer "+longitude);
            latLng = new LatLng(latitude , longitude);
            mLatLng = latLng;
   /*         MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Location");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_add));
            markerOptions.draggable(true);*/

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            //mMap.addMarker(markerOptions);


          /*  CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(mLatlngPickup).zoom(12.f).build();*/

     /*       mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));*/


        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000); //1 seconds
        mLocationRequest.setFastestInterval(1000); //1 seconds
        mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public void getAddressApi(double lat, double lng)
    {


        // Tag used to cancel the request
        //progressDialog.setMessage("Adding you ...");
        // showDialog();

        //String addressUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lang + "&key=" + getResources().getString(R.string.google_maps_key);

        StringRequest strReq = new StringRequest(Request.Method.GET, "http://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lng+"&sensor=true", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "lat lang Response: " + response.toString());
                //hideDialog();
                String city = "";
                try {

                    jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONArray("results").getJSONObject(0);
                    Log.e("TAG", "the result from address: " + data);
                    JSONArray cityName = data.getJSONArray("address_components");

                    String formatedAddress = data.getString("formatted_address");
                    Log.e("TAG", "address_components formated Address " + formatedAddress);
                    mAddress = formatedAddress;
                    for (int i = 0; i < cityName.length(); i++) {
                        JSONObject zero2 = cityName.getJSONObject(i);
                        String long_name = zero2.getString("long_name");
                        JSONArray mtypes = zero2.getJSONArray("types");
                        String Type = mtypes.getString(0);
                        if (Type.equalsIgnoreCase("locality")) {
                            city = long_name;

                        }
                    }

//                        String logn_name = cityName.getJSONObject(2).get("long_name").toString();
                    Log.e("TAG", "address_components city " + city);
                    mCity = city;




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);

    }

    public void loadingHospitalHonServer()
    {
        bt_submit_new_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog customeDialogTakingWorkplaceName = new Dialog(MapActivityForSelectingHospital.this);
                customeDialogTakingWorkplaceName.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customeDialogTakingWorkplaceName.setContentView(R.layout.custom_layout_for_getting_workplace_name);

                final EditText et_dialog_workplacename = (EditText)  customeDialogTakingWorkplaceName.findViewById(R.id.et_dialog_workplacename);
                Button bt_dialog_submit = (Button)  customeDialogTakingWorkplaceName.findViewById(R.id.bt_dialog_submit);
                //setting onClick listner for dialog button
                bt_dialog_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String workPlaceName = et_dialog_workplacename.getText().toString();
                        if (workPlaceName.length()==0){
                            et_dialog_workplacename.setError("Field should not be empty");
                        }
                        else {

                            double lat = mLatLng.latitude;
                            double lng = mLatLng.longitude;
                            Log.e("TAG", "Selected Hospital City is: " + mCity);
                            Log.e("TAG", "Selected Hospital address is: " + mAddress);
                            Log.e("TAG", "Selected Hospital LatLng is: " + mLatLng);
                            Log.e("TAG", "Selected Hospital Lat is: " + lat);
                            Log.e("TAG", "Selected Hospital Lng is: " + lng);
                            Log.e("TAG", "Selected Hospital workplace name is: " + workPlaceName);
                            String placeName = workPlaceName;
                            String placeAddress = mAddress;
                            String placeCity = mCity;
                            String placeLat = String.valueOf(lat);
                            String placeLng = String.valueOf(lng);
                            if (placeCity!=null) {
                                addingNewHospitalService(placeName, placeAddress, placeCity, placeLat, placeLng);



                            }
                            else {
                                Toast.makeText(MapActivityForSelectingHospital.this, "Error! Please Specify your location again", Toast.LENGTH_SHORT).show();
                            }

                            customeDialogTakingWorkplaceName.dismiss();
                        }
                    }
                });

                customeDialogTakingWorkplaceName.getWindow().getAttributes().windowAnimations = R.style.DialogAnimatinoLeftRight;
                customeDialogTakingWorkplaceName.show();
                customeDialogTakingWorkplaceName.setCancelable(false);


            }
        });
    }

    //server for sending new hospital on server
    public void addingNewHospitalService(final String placeName, final String placeAddress, final String placeCity, final String placeLat, final String placeLng)
    {

        // Tag used to cancel the request
        //progressDialog.setMessage("Adding you ...");
        progressDialog.setTitle("Please Wait...");
        showDialog();

        //String addressUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lang + "&key=" + getResources().getString(R.string.google_maps_key);

        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.ADDNEWHOSPITAL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "lat lang Response: " + response.toString());
                hideDialog();
                try {

                    jsonObject = new JSONObject(response);

                    View child = getLayoutInflater().inflate(R.layout.custom_toast_layout, null);
                    TextView textView = (TextView)child.findViewById(R.id.tv_toast_message);
                    String message = jsonObject.getString("error_message");
                    textView.setText(message);
                    Toast toast = Toast.makeText(getApplicationContext() , message, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setView(child);
                    toast.show();
                    finish();



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url


                String myid = "-1";
                SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
                if (sharedPreferences != null) {

                    myid = sharedPreferences.getString("myid", null);

                }

                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("place_name", placeName);
                params.put("place_address",placeAddress);
                params.put("place_lat", placeLat);
                params.put("place_lng", placeLng);
                params.put("place_city", placeCity);
                params.put("doctor_id", myid);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


}
