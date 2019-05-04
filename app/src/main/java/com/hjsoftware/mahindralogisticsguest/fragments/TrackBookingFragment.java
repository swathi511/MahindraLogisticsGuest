package com.hjsoftware.mahindralogisticsguest.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hjsoftware.mahindralogisticsguest.R;
import com.hjsoftware.mahindralogisticsguest.RecyclerAdapter;
import com.hjsoftware.mahindralogisticsguest.SessionManager;
import com.hjsoftware.mahindralogisticsguest.model.DutyData;
import com.hjsoftware.mahindralogisticsguest.model.DutyDataPojo;
import com.hjsoftware.mahindralogisticsguest.webservices.API;
import com.hjsoftware.mahindralogisticsguest.webservices.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackBookingFragment extends Fragment {

    View v;
    RecyclerAdapter bAdapter;
    RecyclerView rView;
    SessionManager session;
    API REST_CLIENT;
    String profileid;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "SharedPref";
    TextView tvNoBookings;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fresco.initialize(getActivity().getApplicationContext());

        REST_CLIENT= RestClient.get();
        pref = getActivity().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        profileid=pref.getString("preparedBy",null);
        session=new SessionManager(getActivity());
        System.out.println("PROFILEIDisss"+profileid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_track_booking, container, false);

        rView=(RecyclerView)v.findViewById(R.id.ftb_rView);
        tvNoBookings=(TextView)v.findViewById(R.id.ftb_no_bkngs);
        tvNoBookings.setVisibility(View.GONE);


        getData();

        return v;
    }

    public void getData()
    {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();

        Call<List<DutyDataPojo>> call=REST_CLIENT.getOngoingDSlipDetails(profileid);
        call.enqueue(new Callback<List<DutyDataPojo>>() {
            @Override
            public void onResponse(Call<List<DutyDataPojo>> call, Response<List<DutyDataPojo>> response) {

                List<DutyDataPojo> dPojoList;
                DutyDataPojo dPojo;
                ArrayList<DutyData> aList=new ArrayList<>();
                DutyData dd;

                if(response.isSuccessful())
                {
                    dPojoList=response.body();

                    for(int i=0;i<dPojoList.size();i++)
                    {
                        dPojo=dPojoList.get(i);

                        aList.add(new DutyData(dPojo.getDutyslipnumber(),dPojo.getDsdate(),dPojo.getVehiclenumber(),dPojo.getDrivername(),dPojo.getDrivermobile(),dPojo.getLatitude(),dPojo.getLongitude(),dPojo.getDslipid(),
                                dPojo.getUserstatus(),dPojo.getLoginstatus(),dPojo.getAppstatus(),dPojo.getOtp(),dPojo.getDrvimagepath()));
                    }

                    if(aList.size()!=0)
                    {
                        rView.setVisibility(View.VISIBLE);
                        bAdapter = new RecyclerAdapter(getActivity(), aList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        rView.setLayoutManager(mLayoutManager);
                        rView.setItemAnimator(new DefaultItemAnimator());
                        rView.setAdapter(bAdapter);
                        bAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                    else{

                        rView.setVisibility(View.GONE);
                        tvNoBookings.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }


                }
                else {

                    rView.setVisibility(View.GONE);
                    tvNoBookings.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<DutyDataPojo>> call, Throwable t) {

                rView.setVisibility(View.GONE);
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"Please check Internet connection!",Toast.LENGTH_SHORT).show();

            }
        });

    }



}