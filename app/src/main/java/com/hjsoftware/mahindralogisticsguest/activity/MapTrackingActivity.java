package com.hjsoftware.mahindralogisticsguest.activity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hjsoftware.mahindralogisticsguest.R;
import com.hjsoftware.mahindralogisticsguest.model.DutyData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapTrackingActivity extends FragmentActivity implements OnMapReadyCallback {

    int position;
    DutyData data;
    ArrayList<DutyData> myList;
    SupportMapFragment mapFragment;
    GoogleMap mMap;
    List<Address> addresses;
    Geocoder geocoder;
    TextView tvLabel;
    ImageView ivBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map_tracking);

        tvLabel=(TextView)findViewById(R.id.fmt_tv_label);
        ivBack=(ImageView)findViewById(R.id.fmt_iv_back);

        geocoder = new Geocoder(this, Locale.getDefault());


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);





        Bundle d=getIntent().getExtras();
        position = d.getInt("position");


        myList = (ArrayList<DutyData>) getIntent().getSerializableExtra("list");
        data=myList.get(position);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });




    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        System.out.println("&&&&&&&&&&&&&&&"+data.getLat()+"::"+data.getLng());

        if(data.getLat().equals("")||data.getLng().equals(""))
        {
            Toast.makeText(MapTrackingActivity.this,"No Coordinates found!",Toast.LENGTH_SHORT).show();

            tvLabel.setText("Unable to get the location details");
        }
        else {

            LatLng ll = new LatLng(Double.parseDouble(data.getLat()), Double.parseDouble(data.getLng()));
            mMap.addMarker(new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(R.drawable.cab_new)));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 16));
            mMap.getUiSettings().setMapToolbarEnabled(false);

            try {
                addresses = geocoder.getFromLocation(Double.parseDouble(data.getLat()), Double.parseDouble(data.getLng()), 1);
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
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    //complete_address=address+" "+add1+" "+add2;
                    tvLabel.setText(address);
                    //complete_address = add;
                } else {
                    tvLabel.setText("-");
                    //complete_address = "-";
                }
            } catch (IOException e) {
                e.printStackTrace();
                //complete_address = "Unable to get the location details";
                tvLabel.setText("Unable to get the location details");
            }

        }

    }
}
