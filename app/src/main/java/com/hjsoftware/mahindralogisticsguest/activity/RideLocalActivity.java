package com.hjsoftware.mahindralogisticsguest.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hjsoftware.mahindralogisticsguest.GPSTracker;
import com.hjsoftware.mahindralogisticsguest.R;
import com.hjsoftware.mahindralogisticsguest.RideStartOverlayService;
import com.hjsoftware.mahindralogisticsguest.SessionManager;
import com.hjsoftware.mahindralogisticsguest.webservices.API;
import com.hjsoftware.mahindralogisticsguest.webservices.RestClient;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by hjsoft on 1/12/16.
 */
public class RideLocalActivity extends AppCompatActivity implements OnMapReadyCallback
{
    SupportMapFragment mapFragment;
    GoogleMap mMap;
    protected boolean mRequestingLocationUpdates;
    final static int REQUEST_LOCATION = 199;
    double latitude1,longitude1,current_lat=0.0,current_long=0.0;
    Geocoder geocoder;
    List<Address> addresses;
    LatLng lastLoc,curntloc;
    String complete_address="";
    boolean entered=false;
    TextView tvCurrentLoc;
  /* ArrayList<AllBookingsData> cabData;
   AllBookingsData data;*/
   View vwBottomSheet;
    API REST_CLIENT;
    String stProfileId;
    HashMap<String, String> user;
    SessionManager session;
    SimpleDateFormat dateFormat;
    Marker cab,gPickup,gDrop,gCurrent;
    String c_lat,c_long;
    Handler h,hC,g;
    Runnable r,rC,gR;
    LatLng lastLocDist;
    ImageButton btGetDirections,btStop;
    boolean gettingDirections=false;
    BottomSheetDialogFragment myBottomSheet;
    LatLng startPosition,finalPosition;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "SharedPref";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String deviceId;
    String pickuplocation;
    String pickLong,pickLot,dropLot,dropLong;
    Double picklong,picklot,droplong,droplot;
    ImageView iv_back;
    GPSTracker gps;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_local);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /*androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(handler1);*/

        tvCurrentLoc=(TextView)findViewById(R.id.ars_tv_cloc);
        iv_back=(ImageView)findViewById(R.id.am_iv_imageicon);
        btGetDirections=(ImageButton)findViewById(R.id.ars_bt_get_directions);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        // getGPSLocationUpdates();
        pref = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        geocoder = new Geocoder(getApplication(), Locale.getDefault());


        //gps=new GPSTracker(getApplication());

        // mRequestingLocationUpdates=false;
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        pickuplocation=pref.getString("pickuploc",null);

        pickLong=pref.getString("pickuplongitude",null);
        pickLot=pref.getString("pickuplatitude",null);
        dropLong=pref.getString("droplongitude",null);
        dropLot=pref.getString("droplatitude",null);



        picklot=Double.valueOf(pickLot).doubleValue();
        picklong=Double.valueOf(pickLong).doubleValue();
        droplot=Double.valueOf(dropLot).doubleValue();
        droplong=Double.valueOf(dropLong).doubleValue();

        System.out.println("PickupLong////"+picklot);
        System.out.println("PickupLot///"+picklong);
        System.out.println("DropLong///"+droplot);
        System.out.println("DropLot/////"+droplong);


        if(Build.VERSION.SDK_INT<23)
        {
            //System.out.println("Sdk_int is"+Build.VERSION.SDK_INT);
            //System.out.println("the enetred values is "+entered);
            establishConnection();
        }
        else
        {
            if(RideLocalActivity.this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                establishConnection();
            }
            else
            {
                if(shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    Toast.makeText(RideLocalActivity.this,"Location Permission is required for this app to run!",Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            }
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geocoder = new Geocoder(this, Locale.getDefault());
        gps=new GPSTracker(RideLocalActivity.this);

        pref = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();


        REST_CLIENT= RestClient.get();

        session = new SessionManager(getApplicationContext());
        user = session.getUserDetails();
       // stProfileId=user.get(SessionManager.KEY_PROFILE_ID);

        mRequestingLocationUpdates=false;

        if(Build.VERSION.SDK_INT<23)
        {
            establishConnection();
        }
        else
        {
            if(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                establishConnection();
            }
            else
            {
                if(shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    Toast.makeText(RideLocalActivity.this,"Location Permission is required for this app to run!", Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            }
        }

//        final BottomSheetBehavior behavior = BottomSheetBehavior.from(vwBottomSheet);
//        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//
//                switch (newState) {
//                    case BottomSheetBehavior.STATE_DRAGGING:
//                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_DRAGGING");
//                        break;
//                    case BottomSheetBehavior.STATE_SETTLING:
//                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_SETTLING");
//                        break;
//                    case BottomSheetBehavior.STATE_EXPANDED:
//                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_EXPANDED");
//                        break;
//                    case BottomSheetBehavior.STATE_COLLAPSED:
//                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_COLLAPSED");
//                        break;
//                    case BottomSheetBehavior.STATE_HIDDEN:
//                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_HIDDEN");
//                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                        break;
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                Log.i("BottomSheetCallback", "slideOffset: " + slideOffset);
//
//            }
//        });


        btGetDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!(picklot.equals("-"))&&(!(picklong.equals("-")))&&!(picklot.equals(""))&&!(picklong.equals(""))) {


                    if (Build.VERSION.SDK_INT >= 23) {

                        if (isSystemAlertPermissionGranted(RideLocalActivity.this)) {

                            // Toast.makeText(RideStartActivity.this, "permission granted", Toast.LENGTH_LONG).show();
                            stopService(new Intent(getApplicationContext(), RideStartOverlayService.class));
                            startService(new Intent(getApplicationContext(), RideStartOverlayService.class));
                            // startService(new Intent(getApplicationContext(), HUD.class));

                            gettingDirections = true;

                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + picklot + "," + picklong);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            mapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            //mapIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");


                            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(mapIntent);
                            }

                        } else {
                            requestSystemAlertPermission(RideLocalActivity.this, 1);
                        }
                    } else {

                        stopService(new Intent(getApplicationContext(), RideStartOverlayService.class));
                        startService(new Intent(getApplicationContext(), RideStartOverlayService.class));
                        // startService(new Intent(getApplicationContext(), HUD.class));

                        gettingDirections = true;

                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + picklot + "," +picklong);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        //mapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        // mapIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");


                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }

                    }
                }
                else {

                    btGetDirections.setEnabled(false);
                    btGetDirections.setClickable(false);
                    Toast.makeText(RideLocalActivity.this,"Drop Location not known!", Toast.LENGTH_SHORT).show();
                }


            }
        });




    }

    public static void requestSystemAlertPermission(Activity context, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;
        final String packageName = context == null ? context.getPackageName() : context.getPackageName();
        final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName));
        if (context != null)
            context.startActivityForResult(intent, requestCode);
        else
            context.startActivityForResult(intent, requestCode);
    }

    @TargetApi(23)
    public static boolean isSystemAlertPermissionGranted(Context context) {
        final boolean result = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || Settings.canDrawOverlays(context);
        return result;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Toast.makeText(RideLocalActivity.this,"called",Toast.LENGTH_SHORT).show();

                establishConnection();

            } else {
                Toast.makeText(RideLocalActivity.this, "Permission not granted", Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void establishConnection(){

        entered=true;
        getGPSLocationUpdates();


    }

    public void sendLocationUpdatesToServer()
    {

        h=new Handler();
        r=new Runnable() {
            @Override
            public void run() {

                h.postDelayed(r,20000);

                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    try {
                        addresses = geocoder.getFromLocation(current_lat, current_long, 1);

                        if (addresses.size() != 0) {
                            int l = addresses.get(0).getMaxAddressLineIndex();
                            String add = "", add1 = "", add2 = "";

                            for (int k = 0; k < l; k++) {
                                add = add + addresses.get(0).getAddressLine(k);
                                add = add + " ";

                                if (k == 1) {
                                    add1 = addresses.get(0).getAddressLine(k);
                                }
                                if (k == 2) {
                                    add2 = addresses.get(0).getAddressLine(k);
                                }
                            }
                            String address = addresses.get(0).getAddressLine(0);
                            String add_1 = addresses.get(0).getAddressLine(1);//current place name eg:Nagendra nagar,Hyderabad
                            String add_2 = addresses.get(0).getAddressLine(2);
                            //city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            //complete_address = address + " " + add_1 + " " + add_2;
                            if(add_1!=null||add_2!=null)
                            {
                                complete_address = address + " " + add_1 + " " + add_2;
                            }
                            else {
                                complete_address=address;
                            }

                            tvCurrentLoc.setText(complete_address);
                        } else {
                            tvCurrentLoc.setText("-");
                            complete_address = "-";
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        complete_address = "Unable to get the location details";
                        tvCurrentLoc.setText(complete_address);
                    }

                    if (lastLocDist != null && current_lat != 0.0 && current_long != 0.0) {

                        if(cab!=null){

                        }
                        else {

                            cab = mMap.addMarker(new MarkerOptions().position(lastLocDist)
                                    .title("Current Location")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                        }

                        curntloc=new LatLng(current_lat,current_long);

                        startPosition = cab.getPosition();
                        finalPosition = new LatLng(current_lat, current_long);

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curntloc, 16));
                        mMap.getUiSettings().setMapToolbarEnabled(false);

                    } else {

                        //dbAdapter.insertEntry(0.0, 0.0, complete_address, resDist, timeUpdated);

                    }
                   // c_lat = String.valueOf(current_lat);
                    //c_long = String.valueOf(current_long);



                    lastLocDist = new LatLng(current_lat, current_long);
                }
                else {

                    Toast.makeText(RideLocalActivity.this, "GPS is not enabled..Please Check!", Toast.LENGTH_SHORT).show();

                   // dbAdapter.insertNetworkIssueData(requestId,"GPS "+getCurrentDateTime());

                }


            }
        };

        h.post(r);
    }



    @Override
    protected void onStart() {

        super.onStart();

        if(Build.VERSION.SDK_INT>=23)
        {
            if(!entered)
            {

            }
            else
            {
                if(gettingDirections) {

                }
                else
                {
                    //mGoogleApiClient.connect();
                }
            }
        }
        else
        {
            if(gettingDirections) {

            }
            else
            {
                //mGoogleApiClient.connect();
            }

        }
    }

    @Override
    protected void onStop() {

        // if(mGoogleApiClient!=null) {

        if(gettingDirections)
        {

        }
        else {
            //mGoogleApiClient.disconnect();
        }
        //}
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(gettingDirections)
        {

        }
        else {


        }
    }

    @Override
    protected void onResume() {
        super.onResume();



        if(gettingDirections)
        {
            gettingDirections=false;
        }
        else {


        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        //stopService(new Intent(getApplicationContext(), RideStartOverlayService.class));

        if(h!=null)
        {
            h.removeCallbacks(r);
        }

        if(hC!=null)
        {
            hC.removeCallbacks(rC);
        }

        if(g!=null)
        {
            g.removeCallbacks(gR);
        }

        gps.stopUsingGPS();



    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK: {

                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        // Toast.makeText(MapsActivity.this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//      c_lat=gps.getLastKnownLatitude();
//      c_long=gps.getLastKnownLongitude();

       current_lat=gps.getLatitude();
        current_long=gps.getLongitude();

        if (gCurrent!= null) {

        } else {
            if(!(current_lat==0)&&!(current_long==0)) {
                LatLng pickLatLng = new LatLng(current_lat,current_long);
                gPickup = mMap.addMarker(new MarkerOptions().position(pickLatLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue)));
            }
        }

        if (gPickup != null) {

        } else {
            if (!(picklot.equals("-")) && (!(picklong.equals("-")))&&!(picklot.equals(""))&&!(picklong.equals(""))) {
                LatLng pickLatLng = new LatLng(picklot,picklong);
                gDrop = mMap.addMarker(new MarkerOptions().position(pickLatLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pink)));
            }
        }

        if(latitude1!=0.0&&longitude1!=0.0) {

            lastLoc = new LatLng(latitude1, longitude1);
            lastLocDist = lastLoc;

            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(-34, 151);
            if (cab != null) {

            }
            else {
                cab = mMap.addMarker(new MarkerOptions().position(lastLoc)
                        .title("Current Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
            }
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                    .target(lastLoc)
                    .zoom(16)
                    //.bearing(30).tilt(45)
                    .build()));
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            try {
                addresses = geocoder.getFromLocation(latitude1, longitude1, 1);
                if (addresses.size() != 0) {
                    int l = addresses.get(0).getMaxAddressLineIndex();
                    String add = "", add1 = "", add2 = "";

                    for (int k = 0; k < l; k++) {
                        add = add + addresses.get(0).getAddressLine(k);
                        add = add + " ";

                        if (k == 1) {
                            add1 = addresses.get(0).getAddressLine(k);
                        }
                        if (k == 2) {
                            add2 = addresses.get(0).getAddressLine(k);
                        }
                    }
                    String address = addresses.get(0).getAddressLine(0);
                    String add_1 = addresses.get(0).getAddressLine(1);//current place name eg:Nagendra nagar,Hyderabad
                    String add_2 = addresses.get(0).getAddressLine(2);
                    //tvCurrentLoc.setText(address+" "+add_1+" "+add_2);
                    if (add_1 != null || add_2 != null) {
                        complete_address = address + " " + add_1 + " " + add_2;
                    } else {
                        complete_address = address;
                    }

                    tvCurrentLoc.setText(complete_address);
                } else {
                    tvCurrentLoc.setText("-");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {

    }





    public void getGPSLocationUpdates()
    {

        g=new Handler();
        gR=new Runnable() {
            @Override
            public void run() {

                g.postDelayed(this,2000);

                current_lat = gps.getLatitude();
                current_long = gps.getLongitude();

                if (current_lat != 0.0 && current_long != 0.0) {
                    sendLocationUpdatesToServer();
//                    if (first) {
//
//                        if(!pref.getBoolean("saved",false)) {
//
//                            String rst=pref.getString("rideStartingTime","nodata");
//                            String pLat=pref.getString("pickup_lat","nodata");
//                            String pLng=pref.getString("pickup_long","nodata");
//
//                            if(rst.equals("nodata")&&pLat.equals("nodata")&&pLng.equals("nodata"))
//                            {
//                                /*pickupLat = String.valueOf(current_lat);
//                                pickupLong = String.valueOf(current_long);
//                                rideStartingTime=getCurrentDateTime();*/
//
//
////                                if(data.getRideStartTime().equals(""))
////                                {
////                                    rideStartingTime=getCurrentDateTime();
////                                }
////                                else {
////                                   // rideStartingTime = data.getRideStartTime();
////                                }
//
////                                editor.putString("pickup_lat", pickupLat);
////                                editor.putString("pickup_long", pickupLong);
////                                editor.putString("rideStartingTime", rideStartingTime);
////                                editor.commit();
//
//                            }
//
//                           /* pickupLat = String.valueOf(current_lat);
//                            pickupLong = String.valueOf(current_long);
//
//                            //rideStartingTime = getCurrentTime();
//                            rideStartingTime=getCurrentDateTime();
//
//                            editor.putString("pickup_lat", pickupLat);
//                            editor.putString("pickup_long", pickupLong);
//                            editor.putString("rideStartingTime", rideStartingTime);*/
////                            editor.putBoolean("saved", true);
////                            editor.commit();
//
//                        }
//
//
//
//
//                        first = false;
//                    }
                }

            }
        };

        g.post(gR);
    }















}
