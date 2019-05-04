package com.hjsoftware.mahindralogisticsguest.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.hjsoftware.mahindralogisticsguest.R;
import com.hjsoftware.mahindralogisticsguest.SessionManager;
import com.hjsoftware.mahindralogisticsguest.activity.BookingListActivity;
import com.hjsoftware.mahindralogisticsguest.activity.MainActivity;
import com.hjsoftware.mahindralogisticsguest.model.DesignationPojo;
import com.hjsoftware.mahindralogisticsguest.model.LocationPojo;
import com.hjsoftware.mahindralogisticsguest.model.MessagePojo;
import com.hjsoftware.mahindralogisticsguest.model.PListPojo;
import com.hjsoftware.mahindralogisticsguest.model.VehiclePojo;
import com.hjsoftware.mahindralogisticsguest.webservices.API;
import com.hjsoftware.mahindralogisticsguest.webservices.RestClient;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjsoftware on 1/12/17.
 */

public class BookingNewFragment extends Fragment {
    View v;
    private EditText no_days,g_name,g_number,g_mailid,b_name,b_number,bmailid,flight,train,otherloctns,etSpecialInst;
    private Button r_time,submit,back;
    String[] reporting_place={"Office","Guest House","Residence","Airport","Railway Station","Other Place"};
    String[] booking_type={"-select-","Spot Rental","Point-Point"};
    String[] travel_type={"Local","Out Station"};
    private Spinner servicelocation,reportingplace,vehicletype,bookingtype,pointpoint,timeinterval;
    String vehicletypeitem, servicelocationitem1,reportingplaceitem3, bookingtypeitem4,pointpointitem5,item6;
    private TextView display,display1,op,ft,travel,r_date,timedisplay;
    private  TimePicker timePicker;
    private TextView train1,flight1,reportingDateTime;
    String format;
    Button ok,cancel,timedate;
    TimePicker tp;
    DatePicker dp;
    int hr=0,min=0,day,mnth,yr;
    String stDate,stTime;
    TextView Date,Time,bookngfor;
    LinearLayout linearLayout1,linearLayout,linearLayout_boking,lLayoutguestdetails;
    SessionManager session;
    API REST_CLIENT;
    ArrayList<String> vehiclelist= new ArrayList<String>();
    ArrayList<String> pointtopointlist = new ArrayList<String>();
    ArrayList<String> locationlist = new ArrayList<String>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "SharedPref";
    ImageView ivEdit;
    String profileId;
    RadioGroup radioGroup,radioGroupbooking;
    RadioButton radioButton1,radioButton2,radio_slf,radio_othr;
    boolean btime=false;
    TextView tv_outstation,bookingType,tvPointtPoint,tvTraveltype,tvRepdatetime,tvServLoc;
    TextView tvVehicleType,tvRepPlace;
    EditText et_outstation;
    LinearLayout ll_outstation,llSpecialInst;
    long diff=0;
    String rideCurrentTime="",rideReportingTime="",paymentType;
    EditText etCardHolderName,etCardNo,etValid1,etValid2;
    LinearLayout ll13,ll14,ll15;
    String cdName="-",cdNo="",validMnth="00",validYr="00";
    int cutOfTime=0;
    String userType="";
    List<PListPojo> pList;
    List<VehiclePojo> vList;
    String custemorIds;
    List<LocationPojo> locationstatus;
    LocationPojo loctData;
    String serviceLocId="";
    String ptopId="",vId="";
    LinearLayout llSpDesig,lletDesig;
    EditText etDesig;
    Spinner spDesig;
    String designation="",designationId="";
    ArrayList<String> dList=new ArrayList<>();
    List<DesignationPojo> dpList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.fragment_new_booking,container,false);
        REST_CLIENT= RestClient.get();
        pref = getActivity().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        session=new SessionManager(getContext());

        g_name=(EditText)v.findViewById(R.id.edit_guest);
        g_number=(EditText)v.findViewById(R.id.edit_gmobile);
        g_mailid=(EditText)v.findViewById(R.id.edit_gemail1);
        no_days=(EditText)v.findViewById(R.id.edit_nodays);
        submit=(Button) v.findViewById(R.id.btn_submit);
        servicelocation=(Spinner)v.findViewById(R.id.spinner_servicelocation);
        reportingplace=(Spinner)v.findViewById(R.id.spinner_reportingplace);
        vehicletype=(Spinner)v.findViewById(R.id.spinner_vehicle);
        bookingtype=(Spinner)v.findViewById(R.id.spinner_bokkingtype);
        pointpoint=(Spinner)v.findViewById(R.id.spinner_pointtopoint) ;
        radioGroup=(RadioGroup)v.findViewById(R.id.radiogroup);
        radioButton1=(RadioButton)v.findViewById(R.id.radio_local);
        radioButton2=(RadioButton)v.findViewById(R.id.radio_outstation);
        flight=(EditText)v.findViewById(R.id.edit_flight);
        otherloctns=(EditText)v.findViewById(R.id.edit_otherplace);
        // flight1=(TextView)v.findViewById(R.id.text_flight);
        // r_date=(TextView)v.findViewById(R.id.fnb_iv_edit);
        linearLayout=(LinearLayout)v.findViewById(R.id.linear_layout120);
        linearLayout1=(LinearLayout)v.findViewById(R.id.linear_110);
        linearLayout_boking=(LinearLayout)v.findViewById(R.id.linear_booking);
        lLayoutguestdetails=(LinearLayout)v.findViewById(R.id.ll_g_name_mail_phone);
        radioGroupbooking=(RadioGroup)v.findViewById(R.id.radiogroup_booking);
        radio_slf=(RadioButton)v.findViewById(R.id.radio_self);
        radio_othr=(RadioButton)v.findViewById(R.id.radio_other);
        reportingDateTime=(TextView)v.findViewById(R.id.edit_reportingdatetime);
        et_outstation=(EditText)v.findViewById(R.id.et_out_station);
        ll_outstation=(LinearLayout)v.findViewById(R.id.ll_out_station);

        bookingType=(TextView)v.findViewById(R.id.fnb_tv_booking_type);
        tvPointtPoint=(TextView)v.findViewById(R.id.fnb_tv_point_point);
        travel=(TextView)v.findViewById(R.id.textview_travel);
        tvRepdatetime=(TextView)v.findViewById(R.id.fnb_tv_rep_date_time);
        tvServLoc=(TextView)v.findViewById(R.id.fnb_tv_service_loction);
        tvVehicleType=(TextView)v.findViewById(R.id.fnb_tv_vehicle_type);
        tvRepPlace=(TextView)v.findViewById(R.id.fnb_tv_repoting_place);
        bookngfor=(TextView)v.findViewById(R.id.fnb_tv_bookingfor);
        op=(TextView)v.findViewById(R.id.text_otherplaces);
        flight1=(TextView)v.findViewById(R.id.text_flight);
        tv_outstation=(TextView)v.findViewById(R.id.fnb_tv_out_staion);

        etSpecialInst=(EditText) v.findViewById(R.id.fnb_et_sinst);
        llSpecialInst=(LinearLayout)v.findViewById(R.id.linear_layout12);
        //llSpecialInst.setVisibility(View.GONE);

        etCardHolderName=(EditText)v.findViewById(R.id.fnb_et_cdname);
        etCardNo=(EditText)v.findViewById(R.id.fnb_et_cdno);
        etValid1=(EditText)v.findViewById(R.id.fnb_et_valid1);
        etValid2=(EditText)v.findViewById(R.id.fnb_et_valid2);

        ll13=(LinearLayout)v.findViewById(R.id.linear_layout13);
        ll14=(LinearLayout)v.findViewById(R.id.linear_layout14);
        ll15=(LinearLayout)v.findViewById(R.id.linear_layout15);

        ll13.setVisibility(View.GONE);
        ll14.setVisibility(View.GONE);
        ll15.setVisibility(View.GONE);

        llSpDesig=(LinearLayout)v.findViewById(R.id.fnb_ll_sp_desi);
        lletDesig=(LinearLayout)v.findViewById(R.id.fnb_ll_et_desi);

        etDesig=(EditText)v.findViewById(R.id.et_designation);
        spDesig=(Spinner)v.findViewById(R.id.spinner_designation);

        llSpDesig.setVisibility(View.GONE);

        timedisplay=(TextView)v.findViewById(R.id.select_time);
        ivEdit=(ImageView)v.findViewById(R.id.fnb_iv_edit);
        ok=(Button)v.findViewById(R.id.ok);
        cancel=(Button)v.findViewById(R.id.cancel);
        tp=(TimePicker)v.findViewById(R.id.simpleTimePicker);
        dp=(DatePicker)v.findViewById(R.id.datePicker);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/fontawesome-webfont.ttf");

        bookingType.setTypeface(font);
        tvPointtPoint.setTypeface(font);
        travel.setTypeface(font);
        tvRepdatetime.setTypeface(font);
        tvServLoc.setTypeface(font);
        tvVehicleType.setTypeface(font);
        tvRepPlace.setTypeface(font);
        bookngfor.setTypeface(font);
        op.setTypeface(font);
        flight1.setTypeface(font);
        tv_outstation.setTypeface(font);

        paymentType=pref.getString("paymentType","");
        userType=pref.getString("userType","");

        final String guestname=pref.getString("bookername",null);
        final String guestnumber=pref.getString("mobileno",null);
        final String guestemail=pref.getString("emailid",null);

        if(userType.equals("Admin"))
        {
            g_name.setText(guestname);
            g_number.setText(guestnumber);
            g_mailid.setText(guestemail);

            g_name.setEnabled(false);
            g_number.setEnabled(false);
            g_mailid.setEnabled(false);
        }
        else {

            linearLayout_boking.setVisibility(View.GONE);
            g_name.setText(guestname);
            g_number.setText(guestnumber);
            g_mailid.setText(guestemail);

            g_name.setEnabled(false);
            g_number.setEnabled(false);
            g_mailid.setEnabled(false);
        }

        /*if(paymentType.equals("Credit Card"))
        {
            ll13.setVisibility(View.VISIBLE);
            ll14.setVisibility(View.VISIBLE);
            ll15.setVisibility(View.VISIBLE);
        }*/

        designation=pref.getString("designation",null);
        etDesig.setText(designation);

        designationId=pref.getString("designationid",null);



        profileId=pref.getString("preparedBy",null);
        //////System.out.println("PROFILEID isss"+profileId);

        no_days.setText("0");
        et_outstation.setText("-");
        pointpointitem5="Local";

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.radio_local:

                        pointpointitem5="Local";
                        ll_outstation.setVisibility(View.GONE);
                        tv_outstation.setVisibility(View.GONE);
                        et_outstation.setVisibility(View.GONE);
                        et_outstation.setText("-");
                        no_days.setText("0");
                        getVehicles();
                        break;
                    case R.id.radio_outstation:
                        pointpointitem5="outstation";
                        ll_outstation.setVisibility(View.VISIBLE);
                        tv_outstation.setVisibility(View.VISIBLE);
                        et_outstation.setVisibility(View.VISIBLE);
                        no_days.setText("");
                        et_outstation.setText("");
                        getVehicles();
                        break;
                }


            }
        });

        radioGroupbooking.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId==R.id.radio_self){
                    g_name.setText(guestname);
                    g_number.setText(guestnumber);
                    g_mailid.setText(guestemail);

                    g_name.setEnabled(false);
                    g_number.setEnabled(false);
                    g_mailid.setEnabled(false);

                    llSpDesig.setVisibility(View.GONE);
                    lletDesig.setVisibility(View.VISIBLE);

                    designation=pref.getString("designation",null);
                    designationId=pref.getString("designationid",null);

                    etDesig.setText(designation);

                }
                else if(checkedId==R.id.radio_other){
                    g_name.getText().clear();
                    g_mailid.getText().clear();
                    g_number.getText().clear();

                    g_name.setEnabled(true);
                    g_number.setEnabled(true);
                    g_mailid.setEnabled(true);

                    lletDesig.setVisibility(View.GONE);
                    llSpDesig.setVisibility(View.VISIBLE);

                    getDesignations();


                }
                else{

                }
            }
        });

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.alert_date_time, null);
                dialogBuilder.setView(dialogView);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();

                dp=(DatePicker)dialogView.findViewById(R.id.datePicker);
                tp=(TimePicker)dialogView.findViewById(R.id.simpleTimePicker);
                ok=(Button)dialogView.findViewById(R.id.ok);
                cancel=(Button)dialogView.findViewById(R.id.cancel);

                tp.setIs24HourView(true);

                System.out.println("hr & min"+hr+"::"+min);

                /*hr=tp.getCurrentHour();
                min=tp.getCurrentMinute();*/


                if(btime)
                {
                    tp.setCurrentHour(hr);
                    tp.setCurrentMinute(min);
                }
                else {

                    hr=tp.getCurrentHour();
                    min=tp.getCurrentMinute();

                    System.out.println("hr & min"+hr+"::"+min);


                    tp.setCurrentHour(hr);
                    tp.setCurrentMinute(min);
                }

                long now = System.currentTimeMillis() - 1000;

                setTimePickerInterval(tp);
                dp.setMinDate(now);
                day = dp.getDayOfMonth();
                mnth = dp.getMonth() + 1;
                yr = dp.getYear();


                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                dp.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        day=dayOfMonth;
                        mnth=month+1;
                        yr=year;


                    }
                });

                tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker timePicker, int i, int i1) {

                        System.out.println("time is "+i+"::"+i1);

                        i1=i1*15;
                        hr=i;
                        min=i1;
                        btime=true;

                        System.out.println("time is "+i+"::"+i1);

                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (btime)
                        {

                            Date d = makeDateGMT(yr, mnth - 1, day);
                            Date d1 = new Date();

                            ///Calendar cal = Calendar. getInstance();
                            //Date date=cal. getTime();
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            rideCurrentTime=dateFormat.format(new Date());

                            if (d.equals(d1)) {

                                Calendar datetime = Calendar.getInstance();
                                Calendar c = Calendar.getInstance();
                                datetime.set(Calendar.HOUR_OF_DAY, hr);
                                datetime.set(Calendar.MINUTE, min);

                                if (datetime.getTimeInMillis() > c.getTimeInMillis()) {

                                    stDate = mnth+ "-" + day+ "-" + yr;
                                    stTime = hr + "." + min;

                                    DecimalFormat formatter = new DecimalFormat("00");


                                    rideReportingTime=day+"/"+mnth+"/"+yr+" "+formatter.format(hr)+":"+formatter.format(min);
                                    reportingDateTime.setVisibility(View.VISIBLE);
                                    reportingDateTime.setText(day+ "/" + mnth + "/" + yr);
                                    timedisplay.setText(stTime);
                                    timedisplay.setTextColor(Color.BLACK);
                                    reportingDateTime.setTextColor(Color.BLACK);
                                    alertDialog.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Please Choose Time ahead of current time", Toast.LENGTH_SHORT).show();
                                }
                            } else {

                                stTime = hr + "." + min;
                                stDate = mnth+ "-" + day+ "-" + yr;

                                DecimalFormat formatter = new DecimalFormat("00");

                                rideReportingTime=day+"/"+mnth+"/"+yr+" "+formatter.format(hr)+":"+formatter.format(min);
                                reportingDateTime.setVisibility(View.VISIBLE);
                                reportingDateTime.setText(day + "/" + mnth + "/" + yr);
                                timedisplay.setText(stTime);
                                timedisplay.setTextColor(Color.BLACK);
                                reportingDateTime.setTextColor(Color.BLACK);
                                alertDialog.dismiss();

                            }
                        }
                        else {

                            Toast.makeText(getActivity(),"Please select time!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();
                        //getActivity().finish();

                    }
                });
            }
        });

        final String ppnames= pref.getString("ppnames",null);

        /*if(ppnames.equals("Not Required")){
            linearLayout.setVisibility(View.GONE);
        }
        else{
            pointtopointlist.add(ppnames);
        }*/


        custemorIds=pref.getString("customerId",null);

        //////System.out.println("customer ids => "+custemorIds+":::");

        retrofit2.Call<List<LocationPojo>> vehiclee=REST_CLIENT.getInfoo(custemorIds);
        vehiclee.enqueue(new Callback<List<LocationPojo>>() {


            @Override
            public void onResponse(retrofit2.Call<List<LocationPojo>> call, Response<List<LocationPojo>> response) {

                locationstatus = response.body();

                locationlist.add("-select-");

                if(response.isSuccessful()){

                    for(int i=0;i<locationstatus.size();i++) {
                        loctData= locationstatus.get(i);

                        String LocationName = loctData.getLocation();
                        locationlist.add(LocationName);

                    }

                    ArrayAdapter arrayAdapter2=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,locationlist);
                    arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    servicelocation.setAdapter(arrayAdapter2);

                    servicelocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if(position==0)
                            {

                            }
                            else {

                            servicelocationitem1 = servicelocation.getItemAtPosition(position).toString();

                            System.out.println("slocItem..." + servicelocationitem1);

                            LocationPojo l;
                            for (int i = 0; i < locationstatus.size(); i++) {
                                l = locationstatus.get(i);
                                if (servicelocationitem1.equals(l.getLocation())) {
                                    serviceLocId = l.getLocationid();
                                    break;
                                }
                            }

                            linearLayout.setVisibility(View.GONE);
                            //linearLayout1.setVisibility(View.VISIBLE);
                            radioButton1.setChecked(true);
                            //....
                            //pointpointitem5 = "Local";
                            //bookingtype.setSelection(0);
                            //bookingtypeitem4 = "Spot Rental";
                                //...


                            System.out.println("cid" + custemorIds + "sid" + serviceLocId);

                            //getVehicles();

                                if(bookingtypeitem4.equals("Spot Rental"))
                                {
                                    getVehicles();
                                }

                            pointtopointlist.clear();


                            Call<List<PListPojo>> call = REST_CLIENT.getPtoPListNames(custemorIds, serviceLocId);
                            call.enqueue(new Callback<List<PListPojo>>() {
                                @Override
                                public void onResponse(Call<List<PListPojo>> call, Response<List<PListPojo>> response) {

                                    PListPojo p;

                                    if (response.isSuccessful()) {
                                        pList = response.body();

                                        if(bookingtypeitem4.equals("Point-Point")) {
                                            linearLayout.setVisibility(View.VISIBLE);
                                        }

                                        for (int i = 0; i < pList.size(); i++) {
                                            p = pList.get(i);

                                            pointtopointlist.add(p.getPtoPName());

                                        }

                                        ArrayAdapter arrayAdapter12 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, pointtopointlist);
                                        arrayAdapter12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        pointpoint.setAdapter(arrayAdapter12);
                                        pointpoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                if (bookingtypeitem4.equals("Point-Point")) {
                                                    linearLayout.setVisibility(View.VISIBLE);
                                                    pointpointitem5 = pointpoint.getItemAtPosition(position).toString();
                                                    getVehicles();

                                                }

                                                //getVehicles();


                                                System.out.println("in on item selected.." + pointpointitem5 + "::" + bookingtypeitem4);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                    }
                                    else {

                                        System.out.println("error...."+pointtopointlist.size());

                                        pList = null;

                                        ArrayAdapter arrayAdapter12 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, pointtopointlist);
                                        arrayAdapter12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        pointpoint.setAdapter(arrayAdapter12);

                                        getVehicles();

                                        /*bookingtype.setSelection(0);
                                        Toast.makeText(getActivity(), "Cannot select Point-Point for your profile!", Toast.LENGTH_SHORT).show();
*/
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<PListPojo>> call, Throwable t) {

                                }
                            });
                        }


                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else{

                    String vehiclee=response.message();

                    Toast.makeText(getActivity(),"No locations found!",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<List<LocationPojo>> call, Throwable t) {
                Toast.makeText(getActivity(),"Please check Internet connection!",Toast.LENGTH_SHORT).show();

            }
        });




        /*Call<List<PListPojo>> call=REST_CLIENT.getPtoPListNames(custemorIds,serviceLocId);
        call.enqueue(new Callback<List<PListPojo>>() {
            @Override
            public void onResponse(Call<List<PListPojo>> call, Response<List<PListPojo>> response) {

                PListPojo p;

                if(response.isSuccessful())
                {
                    pList=response.body();

                    for(int i=0;i<pList.size();i++)
                    {
                        p=pList.get(i);

                        pointtopointlist.add(p.getPtoPName());

                    }

                    ArrayAdapter arrayAdapter12=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,pointtopointlist);
                    arrayAdapter12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    pointpoint.setAdapter(arrayAdapter12);
                    pointpoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if(bookingtypeitem4.equals("Point-Point")) {
                                pointpointitem5 = pointpoint.getItemAtPosition(position).toString();
                            }

                            getVehicles();


                            System.out.println("in on item selected.."+pointpointitem5+"::"+bookingtypeitem4);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<PListPojo>> call, Throwable t) {

            }
        });

        */



        /*ArrayAdapter arrayAdapter12=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,pointtopointlist);
        arrayAdapter12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pointpoint.setAdapter(arrayAdapter12);
        pointpoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(bookingtypeitem4.equals("Point-Point")) {
                    pointpointitem5 = pointpoint.getItemAtPosition(position).toString();
                }


                System.out.println("in on item selected.."+pointpointitm5+"::"+bookingtypeitem4);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        ArrayAdapter arrayAdapter5=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,booking_type);
        arrayAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookingtype.setAdapter(arrayAdapter5);

        bookingtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                bookingtypeitem4 = bookingtype.getItemAtPosition(position).toString();

                if(position==0)
                {
                    //getVehicles();
                }
                else if(bookingtypeitem4.equals("Spot Rental"))
                {

                }
                else {

                    if(pList!=null) {
                        if (bookingtypeitem4.equals("Point-Point") && pList.size() != 0) {
                            //if (bookingtypeitem4.equals("Point-Point") && !(ppnames.equals("Not Required"))) {
                            linearLayout.setVisibility(View.VISIBLE);

                            linearLayout1.setVisibility(View.GONE);
                            ll_outstation.setVisibility(View.GONE);
                            tv_outstation.setVisibility(View.GONE);
                            et_outstation.setVisibility(View.GONE);
                            et_outstation.setText("-");
                            no_days.setText("0");

                            System.out.println("Here selected iss.." + pointpointitem5);

                            pointpointitem5 = ppnames;

                            getVehicles();

                        } else {

                            //bookingtype.setSelection(1);
                            Toast.makeText(getActivity(), "Cannot select Point-Point! No PtoPNames found.", Toast.LENGTH_SHORT).show();
                            vehiclelist.clear();
                            ArrayAdapter arrayAdapter2=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,vehiclelist);
                            arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            vehicletype.setAdapter(arrayAdapter2);

                        }
                    }
                    else {

                        //bookingtype.setSelection(1);
                        vehiclelist.clear();
                        ArrayAdapter arrayAdapter2=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,vehiclelist);
                        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicletype.setAdapter(arrayAdapter2);
                        Toast.makeText(getActivity(), "Cannot select Point-Point! No PtoPNames found.", Toast.LENGTH_SHORT).show();
                    }
                }

                if( bookingtypeitem4.equals("Spot Rental")){
                    linearLayout.setVisibility(View.GONE);
                    linearLayout1.setVisibility(View.VISIBLE);
                    radioButton1.setChecked(true);
                    pointpointitem5="Local";




                    if(pointpointitem5.equals("outstation"))
                    {
                        ll_outstation.setVisibility(View.VISIBLE);
                        et_outstation.setVisibility(View.VISIBLE);
                        tv_outstation.setVisibility(View.VISIBLE);
                        no_days.setText("");
                        et_outstation.setText("");
                    }

                    getVehicles();
                }
                else{
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,reporting_place);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportingplace.setAdapter(arrayAdapter);


        reportingplace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reportingplaceitem3 = reportingplace.getItemAtPosition(position).toString();

                String item = parent.getItemAtPosition(position).toString();

                if(item.equals("Airport"))
                {
                    otherloctns.setVisibility(View.GONE);
                    op.setVisibility(View.GONE);
                    flight.setVisibility(View.VISIBLE);
                    flight1.setVisibility(View.VISIBLE);
                }
                else if(item.equals("Railway Station"))
                {
                    otherloctns.setVisibility(View.GONE);
                    op.setVisibility(View.GONE);
                    flight.setVisibility(View.VISIBLE);
                    flight1.setVisibility(View.VISIBLE);
                }
//                else if(item.equals("Sec-bad Rly Station"))
//                {
//                    otherloctns.setVisibility(View.GONE);
//                    op.setVisibility(View.GONE);
//                    flight.setVisibility(View.VISIBLE);
//                    flight1.setVisibility(View.VISIBLE);
//                }
//                else if(item.equals("Kacheguda Rly Station"))
//                {
//                    otherloctns.setVisibility(View.GONE);
//                    op.setVisibility(View.GONE);
//                    flight.setVisibility(View.VISIBLE);
//                    flight1.setVisibility(View.VISIBLE);
//                }
                else if(item.equals("Office"))
                {
                    //otherloctns.setText("Office Address");
                    otherloctns.setVisibility(View.VISIBLE);
                    op.setText("Office Address");
                    op.setVisibility(View.VISIBLE);
                    flight.setVisibility(View.GONE);
                    flight1.setVisibility(View.GONE);
                    //llSpecialInst.setVisibility(View.VISIBLE);
                }
                else{
                    op.setText("Other Place");
                    otherloctns.setVisibility(View.VISIBLE);
                    op.setVisibility(View.VISIBLE);
                    flight.setVisibility(View.GONE);
                    flight1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*custemorIds=pref.getString("customerId",null);

        //////System.out.println("customer ids => "+custemorIds+":::");

        retrofit2.Call<List<LocationPojo>> vehiclee=REST_CLIENT.getInfoo(custemorIds);
        vehiclee.enqueue(new Callback<List<LocationPojo>>() {


            @Override
            public void onResponse(retrofit2.Call<List<LocationPojo>> call, Response<List<LocationPojo>> response) {

                locationstatus = response.body();

                if(response.isSuccessful()){

                    for(int i=0;i<locationstatus.size();i++) {
                        loctData= locationstatus.get(i);

                        String LocationName = loctData.getLocation();
                        locationlist.add(LocationName);

                    }

                    ArrayAdapter arrayAdapter2=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,locationlist);
                    arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    servicelocation.setAdapter(arrayAdapter2);

                    servicelocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            servicelocationitem1 = servicelocation.getItemAtPosition(position).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else{

                    String vehiclee=response.message();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<List<LocationPojo>> call, Throwable t) {
                Toast.makeText(getActivity(),"Please check Internet connection!",Toast.LENGTH_SHORT).show();

            }
        });*/

        /*
        retrofit2.Call<List<VehiclePojo>> vehicle=REST_CLIENT.getInfo(custemorIds);
        vehicle.enqueue(new Callback<List<VehiclePojo>>() {

            List<VehiclePojo> vehiclestatus;
            VehiclePojo vehData;
            @Override
            public void onResponse(retrofit2.Call<List<VehiclePojo>> call, Response<List<VehiclePojo>> response) {

                vehiclestatus = response.body();

                if(response.isSuccessful()){

                    for(int i=0;i<vehiclestatus.size();i++) {
                        vehData = vehiclestatus.get(i);
                        String vehicleName = vehData.getVehcategory();
                        vehiclelist.add(vehicleName);
                    }

                    ArrayAdapter arrayAdapter2=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,vehiclelist);
                    arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    vehicletype.setAdapter(arrayAdapter2);
                    vehicletype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            vehicletypeitem =vehicletype.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else{

                    String vehicle=response.message();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<List<VehiclePojo>> call, Throwable t) {

                Toast.makeText(getActivity(),"Please check Internet connection!",Toast.LENGTH_SHORT).show();

            }
        });
        */





        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //////System.out.println("Ride start time"+rideCurrentTime);
                //////System.out.println("Ride stop time"+rideReportingTime);

                if(paymentType.equals("Credit Card"))
                {
                    cdName=etCardHolderName.getText().toString().trim();
                    cdNo=etCardNo.getText().toString().trim();
                    validMnth=etValid1.getText().toString().trim();
                    validYr=etValid2.getText().toString().trim();

                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);

                    if(year<Integer.parseInt(validYr))
                    {
                        Toast.makeText(getActivity(),"Your credit card is expired!",Toast.LENGTH_SHORT).show();
                    }
                    else if (month < Integer.parseInt(validMnth)) {

                        Toast.makeText(getActivity(),"Your credit card is expired !",Toast.LENGTH_SHORT).show();

                    }

                    else {

                        addNewBooking();
                    }
                }
                else {

                    addNewBooking();


                    /*System.out.println("Other place data.." + otherloctns.getText().toString().trim());

                    SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
                    timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    try {
                        Date date1 = timeFormat.parse(rideCurrentTime);
                        Date date2 = timeFormat.parse(rideReportingTime);
                        diff = (date2.getTime() - date1.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // movingTimeFormat= timeFormat.format(new Date(diff));

                    int Hours = (int) (diff / (1000 * 60 * 60));
                    int Mins = (int) (diff / (1000 * 60)) % 60;
                    long Secs = (int) (diff / 1000) % 60;

                    int c = pref.getInt("cutOffTime", 0);
                    int h = c / 60;
                    int m = c % 60;

                    //////System.out.println("c & Mins..&Hrs.."+c+"::"+Mins+"::"+Hours);

                    Mins = Mins + Hours * 60;

                    //////System.out.println("Mins after iss"+Mins);


                    if (c > Mins) {
                        Toast.makeText(getActivity(), "Sorry cannot do the booking..\n Min. CutOffTime is " + String.valueOf(h) + ":" + String.valueOf(m) + " hrs \n Please change the reporting Time!", Toast.LENGTH_LONG).show();
                    } else {


                        String sg_name = g_name.getText().toString();
                        String sg_number = g_number.getText().toString();
                        String sg_mailid = g_mailid.getText().toString();

                        if (reportingplaceitem3.equals("Airport") && flight.getText().toString().equals("")) {

                            flight.setError("Enter flight no");
                        } else if (reportingplaceitem3.equals("Other Place") && otherloctns.getText().toString().equals("")) {

                            otherloctns.setError("Type the destination place");
                        } else if ((reportingplaceitem3.equals("Railway Station")) && flight.getText().toString().equals("")) {

                            flight.setError("Enter train no:");
                        } else if (sg_name.equals("")) {
                            g_name.setError("Enter Guest Name");
                        } else if (sg_number.equals("") || sg_number.length() != 10) {
                            g_number.setError("Enter valid Mobile Number");
                        } else if (sg_mailid.equals("")) {
                            g_mailid.setError("Enter Mail id");
                        } else if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(g_mailid.getText().toString()).matches())) {
                            Toast.makeText(getActivity(), "Enter valid Email Address", Toast.LENGTH_SHORT).show();
                            g_mailid.setText("");
                        } else if (reportingDateTime.getText().toString().equals("select") || timedisplay.getText().toString().equals("select")) {
                            Toast.makeText(getActivity(), "Enter Reporting Date and Time", Toast.LENGTH_SHORT).show();
                            reportingDateTime.setTextColor(Color.RED);
                            timedisplay.setTextColor(Color.RED);
                        } else if (pointpointitem5.equals("outstation") && (et_outstation.getText().toString().trim().equals(""))) {
                            Toast.makeText(getActivity(), "Please enter Outstation Name!", Toast.LENGTH_SHORT).show();

                        } else if (pointpointitem5.equals("outstation") && no_days.getText().toString().trim().equals("")) {
                            Toast.makeText(getActivity(), "Please enter Outstation Days!", Toast.LENGTH_SHORT).show();

                        } else {

                            if (bookingtypeitem4.equals("Point-Point")) {
                                pointpointitem5 = "Local";
                            }

                            String custemorIds = pref.getString("customerId", null);

                            //////System.out.println("no_days" + no_days.getText().toString().trim());
                            //////System.out.println("os_name" + et_outstation.getText().toString().trim());
//                    //////System.out.println(pref.getString("bookername",null)+"::"+pref.getString("mobileno",null)+"::"+pref.getString("emailid",null));
//                    //////System.out.println("no."+no_days.getText().toString().trim());
//                    //////System.out.println("@@@@"+stDate);
//                    //////System.out.println("bbbbb"+timedisplay.getText().toString().trim());
//                    //////System.out.println("bookingtype"+bookingtypeitem4);
                            //////System.out.println("traveltype" + pointpointitem5);
//                    //////System.out.println("pointpoint"+pointpointitem5);
//                    //////System.out.println("guedst"+g_name.getText().toString().trim());
//                    //////System.out.println("prepared by "+profileId);
//                    //////System.out.println("veh_cate "+vehicletypeitem);
//                    //////System.out.println("pickupfrom "+reportingplaceitem3);
//                    //////System.out.println("flight "+flight.getText().toString().trim());
//                    //  Log.i("ids2222@@@@@",pref.getString("bookername",null));


                            JsonObject v = new JsonObject();
                            v.addProperty("customerid", custemorIds);
                            v.addProperty("vehiclecategory", vehicletypeitem);
                            v.addProperty("tariffcategory", bookingtypeitem4);
                            v.addProperty("traveltype", pointpointitem5);
                            v.addProperty("bookedby", pref.getString("bookername", null));
                            v.addProperty("bookedbymobile", pref.getString("mobileno", null));
                            v.addProperty("bbemail", pref.getString("emailid", null));
                            v.addProperty("reportdate", stDate);
                            v.addProperty("reporttime", timedisplay.getText().toString().trim());
                            v.addProperty("guestname", g_name.getText().toString().trim());
                            v.addProperty("guestmobileno", g_number.getText().toString().trim());
                            v.addProperty("guestemail", g_mailid.getText().toString().trim());
                            v.addProperty("pickupfrom", reportingplaceitem3);
                            v.addProperty("preparedby", profileId);
                            v.addProperty("location", servicelocationitem1);
                            v.addProperty("NoOfDays", no_days.getText().toString().trim());
                            v.addProperty("routesno", "0");
                            v.addProperty("OutStationName", et_outstation.getText().toString().trim());
                            v.addProperty("pickuploc", "null");
                            v.addProperty("otherplace", otherloctns.getText().toString().trim());
                            v.addProperty("Spinstruct", "-");
                            v.addProperty("CostCode", "");
                            v.addProperty("dropat", "");
                            v.addProperty("bookedbysmsstatus", "Y");
                            v.addProperty("bookedbysms", "999999999");
                            v.addProperty("bbemailstatus", "Y");
                            v.addProperty("bbemailidsend", "testt@gmail");
                            v.addProperty("guestsmsstatus", "");
                            v.addProperty("guestsms", "");
                            v.addProperty("emailsendstatus", "");
                            v.addProperty("emailidsend", "");
                            v.addProperty("empno", "");
                            v.addProperty("refno", 1);
                            v.addProperty("ftinfo", flight.getText().toString().trim());
                            v.addProperty("advrec", 0);
                            v.addProperty("sno", 1);
                            v.addProperty("remindertime", "");
                            v.addProperty("noofveh", 0);
                            v.addProperty("custrefno", "");
                            v.addProperty("tripno", "");
                            v.addProperty("confirmationno", "");
                            v.addProperty("totkms1new", 10);
                            v.addProperty("tothrs1new", 10);
                            v.addProperty("hrscrt1new", "10");
                            v.addProperty("nofdays1new", "3");
                            v.addProperty("criteria", "");
                            v.addProperty("bktype", "");
                            v.addProperty("crtype", "");
                            v.addProperty("crno", "");
                            v.addProperty("guide", "");
                            v.addProperty("instructions", "");
                            v.addProperty("sourcetype", "");
                            v.addProperty("sourcedet", "");
                            v.addProperty("docname", "");
                            v.addProperty("stime", timedisplay.getText().toString().trim());
                            v.addProperty("transfer", "");
                            v.addProperty("creditmnth", "11");
                            v.addProperty("credityear", "2014");
                            v.addProperty("garagestatus", "Not Applicable");
                            v.addProperty("bookerprefix", "+91");
                            v.addProperty("guestprefix", "+91");
                            v.addProperty("pref", "No");
                            v.addProperty("vendorallotstatus", "N");
                            v.addProperty("vendorid", "-");
                            v.addProperty("mtariffsno", 0);
                            v.addProperty("routename", "");
                            v.addProperty("status", "Active");


                            v.addProperty("cardholdername", cdName);
                            v.addProperty("cardno", cdNo);
                            v.addProperty("validmonth", validMnth);
                            v.addProperty("validyear", validYr);
                            // v.addProperty("Flag", "add");

                            Call<MessagePojo> validatee = REST_CLIENT.bookinginfo(v);
                            validatee.enqueue(new Callback<MessagePojo>() {
                                MessagePojo validatee;

                                @Override
                                public void onResponse(Call<MessagePojo> call, Response<MessagePojo> response) {

                                    if (response.isSuccessful()) {
                                        validatee = response.body();

                                        //////System.out.println("Id is " + validatee.getMessage().toString());

                                        Toast.makeText(getActivity(), "Booked successfully!", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(getActivity(), BookingListActivity.class);
                                        startActivity(i);
                                        getActivity().finish();

                                    } else {

                                        //////System.out.println("Error resposne is" + response.message());
                                        Toast.makeText(getActivity(), "Something went wrong! " + response.message(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<MessagePojo> call, Throwable t) {

                                    Toast.makeText(getActivity(), "Please check Internet connection!", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }*/
                }
            }
        });


        return v;
    }


    public void addNewBooking() {

        System.out.println("traveltype...."+pointpointitem5+"::::"+ptopId);


        SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date1 = timeFormat.parse(rideCurrentTime);
            Date date2 = timeFormat.parse(rideReportingTime);
            diff = (date2.getTime() - date1.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // movingTimeFormat= timeFormat.format(new Date(diff));

        int Hours = (int) (diff / (1000 * 60 * 60));
        int Mins = (int) (diff / (1000 * 60)) % 60;
        long Secs = (int) (diff / 1000) % 60;

        if(bookingtypeitem4.equals("Spot Rental"))
        {
            cutOfTime = pref.getInt("cutOffTime", 0);
        }
        else {

            cutOfTime = pref.getInt("pcutOffTime", 0);
        }

        //System.out.println("Booking Type issss"+bookingtypeitem4);
        //System.out.println("Cutofftime..."+cutOfTime);

        //cutOfTime = pref.getInt("cutOffTime", 0);
        int h = cutOfTime / 60;
        int m = cutOfTime % 60;

        //////System.out.println("c & Mins..&Hrs.."+c+"::"+Mins+"::"+Hours);

        Mins = Mins + Hours * 60;

        //////System.out.println("Mins after iss"+Mins);


        if (cutOfTime > Mins) {
            Toast.makeText(getActivity(), "Sorry cannot do the booking..\n Min. CutOffTime is " + String.valueOf(h) + ":" + String.valueOf(m) + " hrs \n Please change the reporting Time!", Toast.LENGTH_LONG).show();
        } else {


            String sg_name = g_name.getText().toString();
            String sg_number = g_number.getText().toString();
            String sg_mailid = g_mailid.getText().toString();

            if (reportingplaceitem3.equals("Airport") && flight.getText().toString().equals("")) {

                flight.setError("Enter flight no");
            } else if (reportingplaceitem3.equals("Other Place") && otherloctns.getText().toString().equals("")) {

                otherloctns.setError("Type the destination place");
            }
            else if (reportingplaceitem3.equals("Office") && otherloctns.getText().toString().equals("")) {

                otherloctns.setError("Enter the office address");
            }else if (reportingplaceitem3.equals("Guest House") && otherloctns.getText().toString().equals("")) {

                otherloctns.setError("Enter the location");
            }
            else if (reportingplaceitem3.equals("Residence") && otherloctns.getText().toString().equals("")) {

                otherloctns.setError("Enter the location");
            }else if ((reportingplaceitem3.equals("Railway Station")) && flight.getText().toString().equals("")) {

                flight.setError("Enter train no:");
            } else if (sg_name.equals("")) {
                g_name.setError("Enter Guest Name");
            } else if (sg_number.equals("") || sg_number.length() != 10) {
                g_number.setError("Enter valid Mobile Number");
            } else if (sg_mailid.equals("")) {
                g_mailid.setError("Enter Mail id");
            } else if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(g_mailid.getText().toString()).matches())) {
                Toast.makeText(getActivity(), "Enter valid Email Address", Toast.LENGTH_SHORT).show();
                g_mailid.setText("");
            } else if (reportingDateTime.getText().toString().equals("select") || timedisplay.getText().toString().equals("select")) {
                Toast.makeText(getActivity(), "Enter Reporting Date and Time", Toast.LENGTH_SHORT).show();
                reportingDateTime.setTextColor(Color.RED);
                timedisplay.setTextColor(Color.RED);
            } else if (pointpointitem5.equals("outstation") && (et_outstation.getText().toString().trim().equals(""))) {
                Toast.makeText(getActivity(), "Please enter Outstation Name!", Toast.LENGTH_SHORT).show();

            } else if (pointpointitem5.equals("outstation") && no_days.getText().toString().trim().equals("")) {
                Toast.makeText(getActivity(), "Please enter Outstation Days!", Toast.LENGTH_SHORT).show();

            }
            else if(vList==null)
            {
                //System.out.println("v list is null");
                Toast.makeText(getActivity(),"Cannot save the boooking..Empty details found!",Toast.LENGTH_SHORT).show();

            }
            else if(vList.size()==0)
            {
                //System.out.println("v list size is 0"+vList.size());
                Toast.makeText(getActivity(),"Cannot save the boooking..Empty details found!",Toast.LENGTH_SHORT).show();

            }else {

                /*if (bookingtypeitem4.equals("Point-Point")) {
                    pointpointitem5 = "Local";
                }*/

                String custemorIds = pref.getString("customerId", null);

                //////System.out.println("no_days" + no_days.getText().toString().trim());
                //////System.out.println("os_name" + et_outstation.getText().toString().trim());
//                    //////System.out.println(pref.getString("bookername",null)+"::"+pref.getString("mobileno",null)+"::"+pref.getString("emailid",null));
//                    //////System.out.println("no."+no_days.getText().toString().trim());
//                    //////System.out.println("@@@@"+stDate);
//                    //////System.out.println("bbbbb"+timedisplay.getText().toString().trim());
                System.out.println("bookingtype"+bookingtypeitem4);
                System.out.println("traveltype" + pointpointitem5);
//                    //////System.out.println("pointpoint"+pointpointitem5);
//                    //////System.out.println("guedst"+g_name.getText().toString().trim());
//                    //////System.out.println("prepared by "+profileId);
//                    //////System.out.println("veh_cate "+vehicletypeitem);
//                    //////System.out.println("pickupfrom "+reportingplaceitem3);
//                    //////System.out.println("flight "+flight.getText().toString().trim());
//                    //  Log.i("ids2222@@@@@",pref.getString("bookername",null));

                /*if(vList!=null) {
                    for (int i = 0; i < vList.size(); i++) {
                        VehiclePojo v;

                        if (vehicletypeitem.equals(vList.get(i).getVehcategory())) {
                            vId = vList.get(i).getVehcatgoryid();
                        }
                    }
                }
                else {

                    Toast.makeText(getActivity(),"Cannot save the boooking..Empty details found!",Toast.LENGTH_SHORT).show();
                }*/

                //System.out.println("vIddddddddd" + vId);

                System.out.println("Desig & Desig Id is..."+designation+"&"+designationId);

                JsonObject v = new JsonObject();
                v.addProperty("customerid", custemorIds);
                v.addProperty("vehiclecategory", vehicletypeitem);
                //v.addProperty("vehiclecategory", vId);
                v.addProperty("tariffcategory", bookingtypeitem4);

                if(bookingtypeitem4.equals("Point-Point")) {
                    v.addProperty("traveltype", ptopId);
                }
                else
                {
                    v.addProperty("traveltype", pointpointitem5);
                }

                v.addProperty("bookedby", pref.getString("bookername", null));
                v.addProperty("bookedbymobile", pref.getString("mobileno", null));
                v.addProperty("bbemail", pref.getString("emailid", null));
                v.addProperty("reportdate", stDate);
                v.addProperty("reporttime", timedisplay.getText().toString().trim());
                v.addProperty("guestname", g_name.getText().toString().trim());
                v.addProperty("guestmobileno", g_number.getText().toString().trim());
                v.addProperty("guestemail", g_mailid.getText().toString().trim());
                v.addProperty("pickupfrom", reportingplaceitem3);
                v.addProperty("preparedby", profileId);
                v.addProperty("location", servicelocationitem1);
                //v.addProperty("location", serviceLocId);
                v.addProperty("NoOfDays", no_days.getText().toString().trim());
                v.addProperty("routesno", "0");
                v.addProperty("OutStationName", et_outstation.getText().toString().trim());
                v.addProperty("pickuploc", otherloctns.getText().toString().trim());
                v.addProperty("otherplace", otherloctns.getText().toString().trim());
                v.addProperty("Spinstruct", etSpecialInst.getText().toString().trim());
                v.addProperty("CostCode", "-");
                v.addProperty("dropat", "-");
                v.addProperty("bookedbysmsstatus", "Y");
                //v.addProperty("bookedbysms", "999999999");//sms field
                v.addProperty("bookedbysms", pref.getString("mobileno", null));//sms field//login mobile no.
                v.addProperty("bbemailstatus", "Y");
                //v.addProperty("bbemailidsend", "testt@gmail");//email field
                v.addProperty("bbemailidsend", pref.getString("emailid", null));//email field// login email id
                v.addProperty("guestsmsstatus", "Y");
                v.addProperty("guestsms", g_number.getText().toString().trim());
                v.addProperty("emailsendstatus", "Y");
                v.addProperty("emailidsend", g_mailid.getText().toString().trim());
                v.addProperty("empno", "-");
                v.addProperty("refno", 1);
                v.addProperty("ftinfo", flight.getText().toString().trim());
                v.addProperty("advrec", 0);
                v.addProperty("sno", 1);
                v.addProperty("remindertime", 1);
                v.addProperty("noofveh", 1);
                v.addProperty("custrefno", "-");
                v.addProperty("tripno", "-");
                v.addProperty("confirmationno","-");
                v.addProperty("totkms1new", 0);
                v.addProperty("tothrs1new", 0);
                v.addProperty("hrscrt1new", 0);
                v.addProperty("nofdays1new", 0);
                v.addProperty("criteria", "-");
                v.addProperty("bktype", "Corporate");
                v.addProperty("crtype", "-");
                v.addProperty("crno", "-");
                v.addProperty("guide", "-");
                v.addProperty("instructions", "-");
                v.addProperty("sourcetype", "Email");
                v.addProperty("sourcedet", "-");
                v.addProperty("docname", "-");
                v.addProperty("stime", timedisplay.getText().toString().trim());
                v.addProperty("transfer", "-");
                v.addProperty("creditmnth", "11");
                v.addProperty("credityear", "2014");
                v.addProperty("garagestatus", "Not Applicable");
                v.addProperty("bookerprefix", "+91");
                v.addProperty("guestprefix", "+91");
                v.addProperty("pref", "No");
                v.addProperty("vendorallotstatus", "N");
                v.addProperty("vendorid", "-");
                v.addProperty("mtariffsno", 0);
                v.addProperty("routename", "-");
                v.addProperty("status", "Active");


                v.addProperty("cardholdername", cdName);
                v.addProperty("cardno", cdNo);
                v.addProperty("validmonth", validMnth);
                v.addProperty("validyear", validYr);

                v.addProperty("cancelremarks" , "-");
                v.addProperty("DesignationId",designationId);


                //v.addProperty("specialinstructions",etSpecialInst.getText().toString().trim());
                // v.addProperty("Flag", "add");

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait ...");
                progressDialog.show();

                Call<MessagePojo> validatee = REST_CLIENT.bookinginfo(v);
                validatee.enqueue(new Callback<MessagePojo>() {
                    MessagePojo validatee;

                    @Override
                    public void onResponse(Call<MessagePojo> call, Response<MessagePojo> response) {

                        if (response.isSuccessful()) {
                            validatee = response.body();

                            progressDialog.dismiss();


                            Toast.makeText(getActivity(), "Booking saved successfully, subjected to approval from MLLCRS!\nBooking Ref# "+validatee.getMessage(), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getActivity(), BookingListActivity.class);
                            startActivity(i);
                            getActivity().finish();

                        } else {

                            progressDialog.dismiss();

                            System.out.println("error/..."+response.message()+"::0");

                            Toast.makeText(getActivity(), "Something went wrong! " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessagePojo> call, Throwable t) {

                        progressDialog.dismiss();

                        Toast.makeText(getActivity(), "Please check Internet connection!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private Date makeDateGMT(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(year,month,day);
        return calendar.getTime();
    }


    public static String convert24To12System (int hour, int minute) {
        String time = "";
        String am_pm = "";
        if (hour < 12 ) {
            if (hour == 0) hour = 12;
            am_pm = "AM";
        }
        else {
            if (hour != 12)
                hour-=12;
            am_pm = "PM";
        }
        String h = hour+"", m = minute+"";
        if(h.length() == 1) h = "0"+h;
        if(m.length() == 1) m = "0"+m;
        time = h+":"+m+" "+am_pm;
        return time;
    }
    private void setTimePickerInterval(TimePicker timePicker) {
        try {
            int TIME_PICKER_INTERVAL = 15;
            NumberPicker minutePicker = (NumberPicker) timePicker.findViewById(Resources.getSystem().getIdentifier(
                    "minute", "id", "android"));
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<String>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            minutePicker.setDisplayedValues(displayedValues.toArray(new String[0]));

            //System.out.println("min value is..."+min);

            if(min<15)
            {
                minutePicker.setValue(0);
            }
            else if(min>=15&&min<30)
            {
                minutePicker.setValue(1);
            }
            else if(min>=30&&min<45)
            {
                minutePicker.setValue(2);
            }
            else {
                minutePicker.setValue(3);

            }


        } catch (Exception e) {
            Log.e("ada", "Exception: " + e);
        }
    }

    public void getVehicles()
    {

        if(bookingtypeitem4.equals("Point-Point"))
        {
            PListPojo p;

            if(pList!=null) {

                for (int i = 0; i < pList.size(); i++) {
                    p = pList.get(i);
                    if (pointpointitem5.equals(p.getPtoPName())) {
                        ptopId = p.getPtoPId();
                        break;
                    }
                }
            }
        }

        System.out.println("cid"+custemorIds);
        System.out.println("sid"+serviceLocId);
        System.out.println("btype"+bookingtypeitem4);
        System.out.println("ptopId"+ptopId);// empty
        System.out.println("ttype"+pointpointitem5);// default Local


        JsonObject v=new JsonObject();
        v.addProperty("CustomerId",custemorIds);
        v.addProperty("ServicelocId",serviceLocId);

        if(bookingtypeitem4.equals("Spot Rental")) {
            v.addProperty("BookingType", "S");
        }
        else {

            v.addProperty("BookingType", "P");


        }


        v.addProperty("PtoPId",ptopId);


        if(bookingtypeitem4.equals("Point-Point"))
        {
            v.addProperty("TravelType","L");
        }
        else {
            if(pointpointitem5.equals("Local")) {
                v.addProperty("TravelType", "L");
            }
            else {
                v.addProperty("TravelType", "O");

            }

        }

        Call<List<VehiclePojo>> call=REST_CLIENT.getVehicles(v);
        call.enqueue(new Callback<List<VehiclePojo>>() {
            @Override
            public void onResponse(Call<List<VehiclePojo>> call, Response<List<VehiclePojo>> response) {

                VehiclePojo v;

                vehiclelist.clear();

                if(vList!=null) {

                    //System.out.println("vlist size...before"+vList.size());

                    vList.clear();

                    //System.out.println("vlist size...after"+vList.size());

                }

                if(response.isSuccessful()) {
                    //if (pointpointitem5.equals("Local")) {

                    System.out.println("response is successful...");

                    /*if(bookingtypeitem4.equals("Point-Point"))
                    {
                        linearLayout.setVisibility(View.VISIBLE);
                    }*/


                    vList = response.body();

                    for (int i = 0; i < vList.size(); i++) {
                        v = vList.get(i);

                        vehiclelist.add(v.getVehcategory());

                    }
                //}

                    //Adapter Logic



                }
                else {



                        //Toast.makeText(getActivity(), "Vehicles not found!", Toast.LENGTH_SHORT).show();


                }

                ArrayAdapter arrayAdapter2=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,vehiclelist);
                arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                vehicletype.setAdapter(arrayAdapter2);
                vehicletype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        vehicletypeitem =vehicletype.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onFailure(Call<List<VehiclePojo>> call, Throwable t) {

            }
        });
    }

    public void getDesignations()
    {

        designation="";
        designationId="";
        dList.clear();

        Call<List<DesignationPojo>> call=REST_CLIENT.getDesignations("NEW");
        call.enqueue(new Callback<List<DesignationPojo>>() {
            @Override
            public void onResponse(Call<List<DesignationPojo>> call, Response<List<DesignationPojo>> response) {

                DesignationPojo dp;

                if(response.isSuccessful())
                {
                    dpList=response.body();

                    dList.add("-select-");

                    for(int i=0;i<dpList.size();i++)
                    {
                        dList.add(dpList.get(i).getDesignation());
                    }

                    ArrayAdapter arrayAdapter15=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,dList);
                    arrayAdapter15.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spDesig.setAdapter(arrayAdapter15);
                    spDesig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                            if(position==0)
                            {

                            }
                            else {

                                designation = spDesig.getItemAtPosition(position).toString();

                                for (int i = 0; i < dpList.size(); i++) {
                                    if (designation.equals(dpList.get(i).getDesignation())) {
                                        designationId = dpList.get(i).getDesignationId();
                                        break;
                                    }
                                }
                            }

                            System.out.println("Desig & DesigId isss"+designation+"&"+designationId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                }


            }

            @Override
            public void onFailure(Call<List<DesignationPojo>> call, Throwable t) {

            }
        });
    }

}
