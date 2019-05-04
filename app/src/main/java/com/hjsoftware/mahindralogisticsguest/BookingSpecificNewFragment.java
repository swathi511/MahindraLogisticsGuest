package com.hjsoftware.mahindralogisticsguest;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.hjsoftware.mahindralogisticsguest.R;
import com.hjsoftware.mahindralogisticsguest.SessionManager;
import com.hjsoftware.mahindralogisticsguest.activity.BookingListActivity;
import com.hjsoftware.mahindralogisticsguest.model.LocationPojo;
import com.hjsoftware.mahindralogisticsguest.model.MessagePojo;
import com.hjsoftware.mahindralogisticsguest.model.PListPojo;
import com.hjsoftware.mahindralogisticsguest.model.GetBookingPojo;
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
public class BookingSpecificNewFragment extends Fragment {

    View v;
    private EditText no_days, g_name, g_number, g_mailid, b_name, b_number, bmailid, flight, train, otherloctns, etSpecialInst;
    private Button r_time, submit, back;
    //String[] reporting_place={"Office","Guest House","Residence","Airport","Hyd-bad Rly Station","Sec-bad Rly Station","Kacheguda Rly Station","Other Place"};
    String[] reporting_place = {"Office", "Guest House", "Residence", "Airport", "Railway Station", "Other Place"};
    String[] booking_type = {"Spot Rental", "Point-Point"};
    String[] travel_type = {"Local", "Out Station"};
    String[] status = {"Active", "Cancel"};
    private Spinner servicelocation, reportingplace, vehicletype, bookingtype, pointpoint, timeinterval;
    String vehicletypeitem, servicelocationitem1, reportingplaceitem3, bookingtypeitem4, pointpointitem5 = "", item6;
    private TextView display, display1, op, ft, travel, r_date, timedisplay;
    private TimePicker timePicker;
    private TextView train1, flight1, reportingDateTime, bookngfor;
    String format;
    Button ok, cancel, timedate;
    TimePicker tp;
    DatePicker dp;
    int hr, min, day, mnth, yr;
    String stDate, stTime;
    LinearLayout linearLayout1, linearLayout, linearLayout_boking;
    SessionManager session;
    API REST_CLIENT;
    ArrayList<String> mylist = new ArrayList<String>();
    ArrayList<String> mylist4 = new ArrayList<String>();
    ArrayList<String> mylist2 = new ArrayList<String>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "SharedPref";
    ImageView ivEdit;
    String profileid;
    String bookingId, custemorIds;
    Bundle b;
    RadioGroup radioGroup, radioGroupbooking;
    RadioButton radioButton1, radioButton2, radio_slf, radio_othr;
    ;
    boolean btime = false;
    ProgressDialog progressDialog;
    TextView tv_outstation;
    EditText et_outstation;
    LinearLayout ll_outstation, llSpecialInst;
    String stTravelType = "", stNoDays, stOsName;
    Spinner spStatus;
    String stStatus = "Active";
    long diff = 0;
    String rideCurrentTime = "", rideReportingTime = "";
    EditText etCardHolderName, etCardNo, etValid1, etValid2;
    LinearLayout ll13, ll14, ll15;
    String cdName = "-", cdNo = "", validMnth = "00", validYr = "00";
    String paymentType;
    int cutOfTime = 0;
    int reportTimeHr = 0, reportTimeMin = 0;
    List<LocationPojo> locationstatus;
    String serviceLocId = "";
    String ptopId = "", vId = "";
    List<PListPojo> pList;
    ArrayList<String> pointtopointlist = new ArrayList<String>();
    List<VehiclePojo> vList;
    ArrayList<String> vehiclelist = new ArrayList<String>();
    GetBookingPojo data;
    List<GetBookingPojo> datalist;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_edit_booking, container, false);
        REST_CLIENT = RestClient.get();
        pref = getActivity().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        session = new SessionManager(getContext());
        g_name = (EditText) v.findViewById(R.id.edit_guest);
        g_number = (EditText) v.findViewById(R.id.edit_gmobile);
        g_mailid = (EditText) v.findViewById(R.id.edit_gemail1);
        no_days = (EditText) v.findViewById(R.id.edit_nodays);
        submit = (Button) v.findViewById(R.id.btn_submit);
        servicelocation = (Spinner) v.findViewById(R.id.spinner_servicelocation);
        reportingplace = (Spinner) v.findViewById(R.id.spinner_reportingplace);
        vehicletype = (Spinner) v.findViewById(R.id.spinner_vehicle);
        bookingtype = (Spinner) v.findViewById(R.id.spinner_bokkingtype);
        pointpoint = (Spinner) v.findViewById(R.id.spinner_pointtopoint);
        radioGroup = (RadioGroup) v.findViewById(R.id.radiogroup);
        radioButton1 = (RadioButton) v.findViewById(R.id.radio_local);
        radioButton2 = (RadioButton) v.findViewById(R.id.radio_outstation);
        flight = (EditText) v.findViewById(R.id.edit_flight);
        otherloctns = (EditText) v.findViewById(R.id.edit_otherplace);
        op = (TextView) v.findViewById(R.id.text_otherplaces);
        flight1 = (TextView) v.findViewById(R.id.text_flight);
        flight1 = (TextView) v.findViewById(R.id.text_flight);
        travel = (TextView) v.findViewById(R.id.textview_travel);
        // r_date=(TextView)v.findViewById(R.id.feb_iv_edit);
        linearLayout = (LinearLayout) v.findViewById(R.id.linear_layout120);
        linearLayout1 = (LinearLayout) v.findViewById(R.id.linear_110);
        reportingDateTime = (TextView) v.findViewById(R.id.edit_reportingdatetime);
        timedisplay = (TextView) v.findViewById(R.id.select_time);
        ivEdit = (ImageView) v.findViewById(R.id.feb_iv_edit);
        ok = (Button) v.findViewById(R.id.ok);
        cancel = (Button) v.findViewById(R.id.cancel);
        tp = (TimePicker) v.findViewById(R.id.simpleTimePicker);
        dp = (DatePicker) v.findViewById(R.id.datePicker);

        tv_outstation = (TextView) v.findViewById(R.id.tv_out_staion);
        et_outstation = (EditText) v.findViewById(R.id.et_out_station);
        ll_outstation = (LinearLayout) v.findViewById(R.id.ll_out_station);

        reportingDateTime = (TextView) v.findViewById(R.id.edit_reportingdatetime);
        spStatus = (Spinner) v.findViewById(R.id.spinner_status);

        llSpecialInst = (LinearLayout) v.findViewById(R.id.linear_layout12);
        etSpecialInst = (EditText) v.findViewById(R.id.feb_et_sinst);
        //llSpecialInst.setVisibility(View.GONE);

        etCardHolderName = (EditText) v.findViewById(R.id.feb_et_cdname);
        etCardNo = (EditText) v.findViewById(R.id.feb_et_cdno);
        etValid1 = (EditText) v.findViewById(R.id.feb_et_valid1);
        etValid2 = (EditText) v.findViewById(R.id.feb_et_valid2);

        ll13 = (LinearLayout) v.findViewById(R.id.linear_layout13);
        ll14 = (LinearLayout) v.findViewById(R.id.linear_layout14);
        ll15 = (LinearLayout) v.findViewById(R.id.linear_layout15);

        ll13.setVisibility(View.GONE);
        ll14.setVisibility(View.GONE);
        ll15.setVisibility(View.GONE);

        b = getActivity().getIntent().getExtras();
        bookingId = pref.getString("bookingId", null);
        profileid = pref.getString("preparedBy", null);
        custemorIds = pref.getString("customerId", null);


        paymentType = pref.getString("paymentType", "");


        getServerData();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radio_local:

                        pointpointitem5 = "Local";
                        ll_outstation.setVisibility(View.GONE);
                        tv_outstation.setVisibility(View.GONE);
                        et_outstation.setVisibility(View.GONE);
                        et_outstation.setText("-");
                        no_days.setText("0");
                        getVehicles();
                        break;
                    case R.id.radio_outstation:
                        pointpointitem5 = "outstation";
                        ll_outstation.setVisibility(View.VISIBLE);
                        tv_outstation.setVisibility(View.VISIBLE);
                        et_outstation.setVisibility(View.VISIBLE);

                        if (stTravelType.equals("outstation")) {
                            no_days.setText(stNoDays);
                            et_outstation.setText(stOsName);
                        } else {

                            no_days.setText("");
                            et_outstation.setText("");
                        }

                        getVehicles();

                        break;
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

                dp = (DatePicker) dialogView.findViewById(R.id.datePicker);
                tp = (TimePicker) dialogView.findViewById(R.id.simpleTimePicker);
                ok = (Button) dialogView.findViewById(R.id.ok);
                cancel = (Button) dialogView.findViewById(R.id.cancel);

                tp.setIs24HourView(true);
                /*hr=tp.getCurrentHour();
                min=tp.getCurrentMinute();*/

                if (btime) {
                    tp.setCurrentHour(hr);
                    tp.setCurrentMinute(min);
                } else {

                    hr = reportTimeHr;
                    min = reportTimeMin;

                    //System.out.println("hr & min"+hr+"::"+min);


                    tp.setCurrentHour(hr);
                    tp.setCurrentMinute(min);
                }


/*                if(btime)
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
                }*/

                /*System.out.println("hr & min here.."+hr+"::"+min);
                System.out.println("report hr & min here.."+reportTimeHr+"::"+reportTimeMin);


                tp.setCurrentHour(reportTimeHr);
                tp.setCurrentMinute(reportTimeMin);*/

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

                        day = dayOfMonth;
                        mnth = month + 1;
                        yr = year;

                    }
                });

                tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker timePicker, int i, int i1) {

                        //System.out.println("time is "+i+"::"+i1);

                        i1 = i1 * 15;
                        hr = i;
                        min = i1;
                        btime = true;
                        //System.out.println("time is "+i+"::"+i1);

                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (btime) {

                            Date d = makeDateGMT(yr, mnth - 1, day);
                            Date d1 = new Date();

                            if (d.equals(d1)) {

                                Calendar datetime = Calendar.getInstance();
                                Calendar c = Calendar.getInstance();
                                datetime.set(Calendar.HOUR_OF_DAY, hr);
                                datetime.set(Calendar.MINUTE, min);

                                if (datetime.getTimeInMillis() > c.getTimeInMillis()) {

                                    stDate = mnth + "-" + day + "-" + yr;
                                    stTime = hr + "." + min;

                                    DecimalFormat formatter = new DecimalFormat("00");
                                    rideReportingTime = day + "/" + mnth + "/" + yr + " " + formatter.format(hr) + ":" + formatter.format(min);

                                    reportingDateTime.setVisibility(View.VISIBLE);
                                    reportingDateTime.setText(day + "/" + mnth + "/" + yr);
                                    timedisplay.setText(stTime);
                                    timedisplay.setTextColor(Color.BLACK);
                                    reportingDateTime.setTextColor(Color.BLACK);
                                    alertDialog.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Please Choose Time ahead of current time", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                stDate = mnth + "-" + day + "-" + yr;
                                stTime = hr + "." + min;

                                DecimalFormat formatter = new DecimalFormat("00");
                                rideReportingTime = day + "/" + mnth + "/" + yr + " " + formatter.format(hr) + ":" + formatter.format(min);

                                reportingDateTime.setVisibility(View.VISIBLE);
                                reportingDateTime.setText(day + "/" + mnth + "/" + yr);
                                timedisplay.setText(stTime);
                                timedisplay.setTextColor(Color.BLACK);
                                reportingDateTime.setTextColor(Color.BLACK);
                                alertDialog.dismiss();
                            }
                        } else

                        {

                            Toast.makeText(getActivity(), "Please select time!", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();

                    }
                });
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (paymentType.equals("Credit Card")) {
                    cdName = etCardHolderName.getText().toString().trim();
                    cdNo = etCardNo.getText().toString().trim();
                    validMnth = etValid1.getText().toString().trim();
                    validYr = etValid2.getText().toString().trim();

                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);

                    if (year < Integer.parseInt(validYr)) {
                        Toast.makeText(getActivity(), "Your credit card is expired!", Toast.LENGTH_SHORT).show();
                    } else if (month < Integer.parseInt(validMnth)) {

                        Toast.makeText(getActivity(), "Your credit card is expired !", Toast.LENGTH_SHORT).show();

                    } else {

                        updateBooking();
                    }
                } else {

                    updateBooking();
                }

                /*////System.out.println("status isss"+stStatus);
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                rideCurrentTime=dateFormat. format(new Date());

                //System.out.println("Ride start time"+rideCurrentTime);
                //System.out.println("Ride stop time"+rideReportingTime);

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

                int c=pref.getInt("cutOffTime",0);
                int h=c/60;
                int m=c%60;

                //System.out.println("c & Mins..&Hrs.."+c+"::"+Mins+"::"+Hours);

                Mins=Mins+Hours*60;


                //System.out.println("Mins after iss"+Mins);


                if(c>Mins)
                {
                    Toast.makeText(getActivity(),"Sorry cannot do the booking..\n Min. CutOffTime is "+String.valueOf(h)+":"+String.valueOf(m)+" hrs \n Please change the reporting Time!",Toast.LENGTH_LONG).show();
                }

                else {


                    String sg_name = g_name.getText().toString();
                    String sg_number = g_number.getText().toString();
                    String sg_mailid = g_mailid.getText().toString();

                    if (reportingplaceitem3.equals("Airport") && flight.getText().toString().equals("")) {

                        flight.setError("Enter flight no");
                    } else if (reportingplaceitem3.equals("Other Place") && otherloctns.getText().toString().equals("")) {

                        otherloctns.setError("Type the desc for place");
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
                    } else if (pointpointitem5.equals("")) {
                        Toast.makeText(getActivity(), "Please select Local/Outstation!", Toast.LENGTH_SHORT).show();

                    } else if (pointpointitem5.equals("outstation") && (et_outstation.getText().toString().trim().equals(""))) {
                        Toast.makeText(getActivity(), "Please enter Outstation Name!", Toast.LENGTH_SHORT).show();

                    } else if (pointpointitem5.equals("outstation") && no_days.getText().toString().trim().equals("")) {
                        Toast.makeText(getActivity(), "Please enter Outstation Days!", Toast.LENGTH_SHORT).show();

                    } else {

                        if (bookingtypeitem4.equals("Point-Point")) {
                            pointpointitem5 = "local";
                        }

                        String custemorIds = pref.getString("customerId", null);

//                    //System.out.println("no_days"+no_days.getText().toString().trim());
//                    //System.out.println("os_name"+et_outstation.getText().toString().trim());
//
//                    //System.out.println(pref.getString("bookername",null)+"::"+pref.getString("mobileno",null)+"::"+pref.getString("emailid",null));
//                    //System.out.println("no."+no_days.getText().toString().trim());
//                    //System.out.println("@@@@"+stDate);
//                   //System.out.println("bbbbb"+timedisplay.getText().toString().trim());
//                    //System.out.println("bookingtype"+bookingtypeitem4);
//                    //System.out.println("traveltype"+pointpointitem5);
//                    //System.out.println("pointpoint"+pointpointitem5);//System.out.println("guedst"+g_name.getText().toString().trim());
//                   //System.out.println("profileid"+profileid);
//                    //System.out.println("loc"+servicelocationitem1);
//                   //System.out.println("pickup"+reportingplaceitem3);
//                     Log.i("ids2222@@@@@",pref.getString("bookername",null));


                        JsonObject v = new JsonObject();
                        v.addProperty("bookingid", bookingId);
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
                        v.addProperty("preparedby", profileid);
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
                        v.addProperty("status", stStatus);
                        //v.addProperty("Flag", "update");


                        Call<MessagePojo> validatee = REST_CLIENT.bookinginfo(v);
                        validatee.enqueue(new Callback<MessagePojo>() {

                            MessagePojo validatee;

                            @Override
                            public void onResponse(Call<MessagePojo> call, Response<MessagePojo> response) {

                                if (response.isSuccessful()) {

                                    validatee = response.body();

                                    Toast.makeText(getActivity(), "Booking successfully updated!", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getActivity(), BookingListActivity.class);
                                    startActivity(i);
                                    getActivity().finish();


                                } else {


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
        });


        return v;
    }

    public void updateBooking() {

        System.out.println("travletype..."+pointpointitem5);
        System.out.println("veh type..."+vehicletypeitem);
        System.out.println("booking type..."+bookingtypeitem4);
        System.out.println("servicelocation..."+servicelocationitem1);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        rideCurrentTime = dateFormat.format(new Date());

        //System.out.println("Ride start time"+rideCurrentTime);
        //System.out.println("Ride stop time"+rideReportingTime);

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

        if (bookingtypeitem4.equals("Spot Rental")) {
            cutOfTime = pref.getInt("cutOffTime", 0);
        } else {

            cutOfTime = pref.getInt("pcutOffTime", 0);
        }

        //int c=pref.getInt("cutOffTime",0);
        int h = cutOfTime / 60;
        int m = cutOfTime % 60;

        //System.out.println("c & Mins..&Hrs.."+c+"::"+Mins+"::"+Hours);

        Mins = Mins + Hours * 60;


        //System.out.println("Mins after iss"+Mins);


        if (stStatus.equals("Active")) {

            if (cutOfTime > Mins) {
                Toast.makeText(getActivity(), "Sorry cannot do the booking..\n Min. CutOffTime is " + String.valueOf(h) + ":" + String.valueOf(m) + " hrs \n Please change the reporting Time!", Toast.LENGTH_LONG).show();
            } else {
                callUpdateAPI();
            }
        } else {
            callUpdateAPI();
        }
    }

    public void callUpdateAPI() {
        String sg_name = g_name.getText().toString();
        String sg_number = g_number.getText().toString();
        String sg_mailid = g_mailid.getText().toString();

        if (reportingplaceitem3.equals("Airport") && flight.getText().toString().equals("")) {

            flight.setError("Enter flight no");
        } else if (reportingplaceitem3.equals("Other Place") && otherloctns.getText().toString().equals("")) {

            otherloctns.setError("Type the desc for place");
        } else if (reportingplaceitem3.equals("Office") && otherloctns.getText().toString().equals("")) {

            otherloctns.setError("Enter the office address");
        } else if (reportingplaceitem3.equals("Guest House") && otherloctns.getText().toString().equals("")) {

            otherloctns.setError("Enter the location");
        } else if (reportingplaceitem3.equals("Residence") && otherloctns.getText().toString().equals("")) {

            otherloctns.setError("Enter the location");
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
        } else if (pointpointitem5.equals("")) {
            Toast.makeText(getActivity(), "Please select Local/Outstation!", Toast.LENGTH_SHORT).show();

        } else if (pointpointitem5.equals("outstation") && ((et_outstation.getText().toString().trim().equals("")) || (et_outstation.getText().toString().trim().equals("-")))) {
            Toast.makeText(getActivity(), "Please enter Outstation Name!", Toast.LENGTH_SHORT).show();

        } else if (pointpointitem5.equals("outstation") && (no_days.getText().toString().trim().equals(""))) {
            Toast.makeText(getActivity(), "Please enter Outstation Days!", Toast.LENGTH_SHORT).show();

        } else {

                /*if (bookingtypeitem4.equals("Point-Point")) {
                    pointpointitem5 = "local";
                }*/

            String custemorIds = pref.getString("customerId", null);

//                    //System.out.println("no_days"+no_days.getText().toString().trim());
//                    //System.out.println("os_name"+et_outstation.getText().toString().trim());
//
//                    //System.out.println(pref.getString("bookername",null)+"::"+pref.getString("mobileno",null)+"::"+pref.getString("emailid",null));
//                    //System.out.println("no."+no_days.getText().toString().trim());
//                    //System.out.println("@@@@"+stDate);
//                   //System.out.println("bbbbb"+timedisplay.getText().toString().trim());
//                    //System.out.println("bookingtype"+bookingtypeitem4);
//                    //System.out.println("traveltype"+pointpointitem5);
//                    //System.out.println("pointpoint"+pointpointitem5);//System.out.println("guedst"+g_name.getText().toString().trim());
//                   //System.out.println("profileid"+profileid);
//                    //System.out.println("loc"+servicelocationitem1);
//                   //System.out.println("pickup"+reportingplaceitem3);
//                     Log.i("ids2222@@@@@",pref.getString("bookername",null));

            //System.out.println("travletype..below."+pointpointitem5);


            JsonObject v = new JsonObject();
            v.addProperty("bookingid", bookingId);
            v.addProperty("customerid", custemorIds);
            v.addProperty("vehiclecategory", vehicletypeitem);
            v.addProperty("tariffcategory", bookingtypeitem4);
            if(bookingtypeitem4.equals("Point-Point")) {
                v.addProperty("traveltype", ptopId);
            }
            else
            {
                v.addProperty("traveltype", pointpointitem5);
            }            v.addProperty("bookedby", pref.getString("bookername", null));
            v.addProperty("bookedbymobile", pref.getString("mobileno", null));
            v.addProperty("bbemail", pref.getString("emailid", null));
            v.addProperty("reportdate", stDate);
            v.addProperty("reporttime", timedisplay.getText().toString().trim());
            v.addProperty("guestname", g_name.getText().toString().trim());
            v.addProperty("guestmobileno", g_number.getText().toString().trim());
            v.addProperty("guestemail", g_mailid.getText().toString().trim());
            v.addProperty("pickupfrom", reportingplaceitem3);
            v.addProperty("preparedby", profileid);
            v.addProperty("location", servicelocationitem1);
            v.addProperty("NoOfDays", no_days.getText().toString().trim());
            v.addProperty("routesno", "0");
            v.addProperty("OutStationName", et_outstation.getText().toString().trim());
            v.addProperty("pickuploc", "null");
            v.addProperty("otherplace", otherloctns.getText().toString().trim());
            v.addProperty("Spinstruct", etSpecialInst.getText().toString().trim());
            v.addProperty("CostCode", "");
            v.addProperty("dropat", "");
            v.addProperty("bookedbysmsstatus", "Y");
            //v.addProperty("bookedbysms", "999999999");
            v.addProperty("bookedbysms", "");
            v.addProperty("bbemailstatus", "Y");
            //v.addProperty("bbemailidsend", "testt@gmail");
            v.addProperty("bbemailidsend", "");
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
            v.addProperty("status", stStatus);

            v.addProperty("cardholdername", cdName);
            v.addProperty("cardno", cdNo);
            v.addProperty("validmonth", validMnth);

            v.addProperty("validyear", validYr);

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

                        progressDialog.dismiss();

                        validatee = response.body();

                        if (stStatus.equals("Active")) {
                            Toast.makeText(getActivity(), "Booking saved successfully, subjected to approval from MLLCRS!", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getActivity(), "Booking details updated successfully.", Toast.LENGTH_LONG).show();

                        }

                        Intent i = new Intent(getActivity(), BookingListActivity.class);
                        startActivity(i);
                        getActivity().finish();


                    } else {

                        progressDialog.dismiss();

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private Date makeDateGMT(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar();
        // calendar.setTimeZone(TimeZone.getTimeZone());
        calendar.set(year, month, day);
        return calendar.getTime();
    }


    public static String convert24To12System(int hour, int minute) {
        String time = "";
        String am_pm = "";
        if (hour < 12) {
            if (hour == 0) hour = 12;
            am_pm = "AM";
        } else {
            if (hour != 12)
                hour -= 12;
            am_pm = "PM";
        }
        String h = hour + "", m = minute + "";
        if (h.length() == 1) h = "0" + h;
        if (m.length() == 1) m = "0" + m;
        time = h + ":" + m + " " + am_pm;
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

            System.out.println("min value is..." + min);

            if (min < 15) {
                minutePicker.setValue(0);
            } else if (min >= 15 && min < 30) {
                minutePicker.setValue(1);
            } else if (min >= 30 && min < 45) {
                minutePicker.setValue(2);
            } else {
                minutePicker.setValue(3);
            }

//            System.out.println("minute picker i sset.."+minutePicker.getValue());
            //System.out.println("min value is..."+min);
        } catch (Exception e) {
            Log.e("ada", "Exception: " + e);
        }
    }


    public void getTheData() {
        /*final String ppnames= pref.getString("ppnames",null);
        if(ppnames.equals("Not Required")){
            //linearLayout.setVisibility(View.GONE);
        }
        else{
            mylist4.add(ppnames);
        }

        ArrayAdapter arrayAdapter12=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,mylist4);
        arrayAdapter12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pointpoint.setAdapter(arrayAdapter12);
        pointpoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(bookingtypeitem4.equals("Point-Point")) {
                    pointpointitem5 = pointpoint.getItemAtPosition(position).toString();

                    getVehicles();
                }
                else {

                    pointpointitem5="local";
                    pointpoint.setSelection(0);
                }


                //System.out.println("slected is spinner");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        ArrayAdapter arrayAdapter5 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, booking_type);
        arrayAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookingtype.setAdapter(arrayAdapter5);

        bookingtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                bookingtypeitem4 = bookingtype.getItemAtPosition(position).toString();

                if (position == 0) {

                } else {

                    if (pList != null) {
                        if (bookingtypeitem4 == "Point-Point" && (pList.size() != 0)) {
                            pointpoint.setVisibility(View.VISIBLE);
                            linearLayout.setVisibility(View.VISIBLE);
                            linearLayout1.setVisibility(View.GONE);
                            ll_outstation.setVisibility(View.GONE);
                            tv_outstation.setVisibility(View.GONE);
                            et_outstation.setVisibility(View.GONE);
                            no_days.setText("0");
                            et_outstation.setText("-");

                            System.out.println("Here selected iss.." + pointpointitem5);

                            getPtopNames();

                            //getVehicles();


                            //pointpointitem5 = ppnames;

                        } else {
                            bookingtype.setSelection(0);
                            Toast.makeText(getActivity(), "Cannot select Point-Point for your profile!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        bookingtype.setSelection(0);
                        Toast.makeText(getActivity(), "Cannot select Point-Point for your profile!", Toast.LENGTH_SHORT).show();
                    }
                }

                if (bookingtypeitem4 == "Spot Rental") {




                    linearLayout.setVisibility(View.GONE);
                    linearLayout1.setVisibility(View.VISIBLE);
                    ll_outstation.setVisibility(View.GONE);
                    //pointpointitem5="local";

                    if (pointpointitem5.equals("Local")) {

                    } else if (pointpointitem5.equals("outstation")) {

                    } else {

                        pointpoint.setSelection(0);
                        pointpointitem5 = "Local";
                    }

                    getVehicles();




                    if (pointpointitem5.equals("outstation")) {
                        if (stTravelType.equals("outstation")) {
                            ll_outstation.setVisibility(View.VISIBLE);
                            et_outstation.setVisibility(View.VISIBLE);
                            tv_outstation.setVisibility(View.VISIBLE);
                            no_days.setText(stNoDays);
                            et_outstation.setText(stOsName);
                        } else {
                            ll_outstation.setVisibility(View.VISIBLE);
                            et_outstation.setVisibility(View.VISIBLE);
                            tv_outstation.setVisibility(View.VISIBLE);
                            no_days.setText("");
                            et_outstation.setText("");
                        }


                    }
                } else {


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, reporting_place);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportingplace.setAdapter(arrayAdapter);


        reportingplace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reportingplaceitem3 = reportingplace.getItemAtPosition(position).toString();

                String item = parent.getItemAtPosition(position).toString();

                if (item.equals("Airport")) {
                    otherloctns.setVisibility(View.GONE);
                    op.setVisibility(View.GONE);
                    flight.setVisibility(View.VISIBLE);
                    flight1.setVisibility(View.VISIBLE);
                } else if (item.equals("Railway Station")) {
                    otherloctns.setVisibility(View.GONE);
                    op.setVisibility(View.GONE);
                    flight.setVisibility(View.VISIBLE);
                    flight1.setVisibility(View.VISIBLE);
                }
//                else if(item.equals("Hyd-bad Rly Station"))
//                {
//                    otherloctns.setVisibility(View.GONE);
//                    op.setVisibility(View.GONE);
//                    flight.setVisibility(View.VISIBLE);
//                    flight1.setVisibility(View.VISIBLE);
//                }
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
                else if (item.equals("Office")) {
                    otherloctns.setVisibility(View.VISIBLE);
                    op.setVisibility(View.VISIBLE);
                    op.setText("Office Address");
                    flight.setVisibility(View.GONE);
                    flight1.setVisibility(View.GONE);
                } else {
                    otherloctns.setVisibility(View.VISIBLE);
                    op.setVisibility(View.VISIBLE);
                    op.setText("Other Place");
                    flight.setVisibility(View.GONE);
                    flight1.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter arrayAdapter2 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, mylist);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicletype.setAdapter(arrayAdapter2);

        vehicletype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vehicletypeitem = vehicletype.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter arrayAdapter7 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, status);
        arrayAdapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(arrayAdapter7);

        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    stStatus = "Active";
                } else {
                    stStatus = "Cancel";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getServerData() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();


        ArrayAdapter arrayAdapter7 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, status);
        arrayAdapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(arrayAdapter7);

        // ************** second api call

        retrofit2.Call<List<LocationPojo>> vehiclee = REST_CLIENT.getInfoo(custemorIds);
        vehiclee.enqueue(new Callback<List<LocationPojo>>() {


            LocationPojo loctData;

            @Override
            public void onResponse(retrofit2.Call<List<LocationPojo>> call, Response<List<LocationPojo>> response) {

                locationstatus = response.body();

                // String[] items;
                if (response.isSuccessful()) {

                    for (int i = 0; i < locationstatus.size(); i++) {
                        loctData = locationstatus.get(i);
                        String LocationName = loctData.getLocation();
                        mylist2.add(LocationName);

                    }

                    System.out.println("servive list isss"+mylist2);

                    ArrayAdapter arrayAdapter8 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, mylist2);
                    arrayAdapter8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    servicelocation.setAdapter(arrayAdapter8);

                    servicelocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            servicelocationitem1 = servicelocation.getItemAtPosition(position).toString();

                            System.out.println("Location service iss"+servicelocationitem1);

                            for (int i = 0; i < locationstatus.size(); i++) {
                                if (servicelocationitem1.equals(locationstatus.get(i).getLocation())) {
                                    serviceLocId = locationstatus.get(i).getLocationid();
                                }
                            }


                            //check for issues

                            linearLayout.setVisibility(View.GONE);
                            linearLayout1.setVisibility(View.VISIBLE);
                            radioButton1.setChecked(true);
                            pointpointitem5="Local";
                            bookingtype.setSelection(0);
                            bookingtypeitem4="Spot Rental";


                            getPtopNames();
                            getTheData();

                            //getVehicles();





                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    //************************ third api call - bookings data
                    getBookingData();
                    // **************************************
                } else {

                    String vehiclee = response.message();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<List<LocationPojo>> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Please check Internet connection!", Toast.LENGTH_SHORT).show();

            }
        });
        // **********************************************
    }





    public void getBookingData()
    {

        //System.out.println("custid"+custemorIds+"bookingid"+bookingId+"profileId"+profileid);
        Call<List<GetBookingPojo>> call=REST_CLIENT.getSpecificBooking(custemorIds,bookingId,profileid);
        call.enqueue(new Callback<List<GetBookingPojo>>() {
            @Override
            public void onResponse(Call<List<GetBookingPojo>> call, Response<List<GetBookingPojo>> response) {


                if(response.isSuccessful())
                {
                    progressDialog.dismiss();
                    datalist=response.body();
                    data=datalist.get(0);

                    etSpecialInst.setText(data.getSpecialinstructions());

                    reportTimeHr=Integer.parseInt(data.getReporttime().split("\\.")[0]);
                    reportTimeMin=Integer.parseInt(data.getReporttime().split("\\.")[1]);

                    //System.out.println("&&&&&&&&&"+reportTimeHr+"::"+reportTimeMin);

                    /*hr=reportTimeHr;
                    min=reportTimeMin;*/

                    if(paymentType.equals("Credit Card"))
                    {
                        ll13.setVisibility(View.VISIBLE);
                        ll14.setVisibility(View.VISIBLE);
                        ll15.setVisibility(View.VISIBLE);

                        etCardHolderName.setText(data.getCardholdername());
                        etCardNo.setText(data.getCardno());
                        etValid1.setText(data.getValidmonth());
                        etValid2.setText(data.getValidyear());

                        cdName=data.getCardholdername();
                        cdNo=data.getCardno();
                        validMnth=data.getValidmonth();
                        validYr=data.getValidyear();
                    }


                    g_name.setText(data.getGuestname());
                    g_mailid.setText(data.getGuestemail());
                    g_number.setText(data.getGuestmobileno());


                    if(data.getTariffcategory().equals("Spot Rental"))
                    {
                        bookingtype.setSelection(0);
                        linearLayout1.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.GONE);

                        if(data.getTraveltype().equals("Local"))
                        {
                            radioButton1.setChecked(true);
                            no_days.setText(data.getNoOfDays());
                            pointpointitem5="Local";
                            stTravelType="Local";
                        }
                        else
                        {
                            stTravelType="outstation";
                            stNoDays=data.getNoOfDays();
                            stOsName=data.getOutStationName();
                            radioButton2.setChecked(true);
                            no_days.setText(data.getNoOfDays());
                            pointpointitem5="outstation";
                            ll_outstation.setVisibility(View.VISIBLE);
                            et_outstation.setText(data.getOutStationName());
                        }
                    }
                    else
                    {
                        bookingtype.setSelection(1);
                        linearLayout.setVisibility(View.VISIBLE);
                        linearLayout1.setVisibility(View.GONE);


                        if(data.getTraveltype().equals("Airport Transfer"))
                        {
                            pointpoint.setSelection(0);
                            pointpointitem5="Airport Transfer";

                        }
                        else {
                            pointpoint.setSelection(0);
                            pointpointitem5=data.getTraveltype();
                        }


                    }


                    try {
                        SimpleDateFormat newformat = new SimpleDateFormat("dd/MM/yyyy");
                        String datestring = data.getReportdate().split(" ")[0];
                        //SimpleDateFormat oldformat = new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat oldformat = new SimpleDateFormat("MM/dd/yyyy");
                        String reformattedStr = newformat.format(oldformat.parse(datestring));

                        reportingDateTime.setText(reformattedStr);
                        stDate=data.getReportdate().split(" ")[0];

                    }catch(ParseException e){e.printStackTrace();}

                    timedisplay.setText(data.getReporttime());
                    reportingDateTime.setTextColor(Color.BLACK);
                    timedisplay.setTextColor(Color.BLACK);

                    //System.out.println("@@@@0"+data.getReporttime().split("\\.")[0]);
                    //System.out.println("@@@@1"+data.getReporttime().split("\\.")[1]);

                    DecimalFormat formatter = new DecimalFormat("00");

                    rideReportingTime=reportingDateTime.getText().toString().trim()+" "+(data.getReporttime().split("\\.")[0])+":"+(data.getReporttime().split("\\.")[1]);


                    /*if(data.getLocation().equals("Hyderabad"))
                    {
                        servicelocation.setSelection(0);
                    }
                    else if(data.getLocation().equals("Vijayawada"))
                    {
                        servicelocation.setSelection(1);
                    }
                    else {
                        servicelocation.setSelection(2);
                    }*/


                    for(int i=0;i<mylist2.size();i++)
                    {
                        String v=(String) servicelocation.getItemAtPosition(i);

                        if(v.equals(data.getLocation()))
                        {
                            servicelocation.setSelection(i);
                            break;
                        }
                    }

                    LocationPojo l;

                    for(int i=0;i<locationstatus.size();i++)
                    {
                        if(data.getLocation().equals(locationstatus.get(i).getLocation()))
                        {
                            serviceLocId=locationstatus.get(i).getLocationid();
                        }
                    }

                    getVehicles();










                    /*//System.out.println("my list size... "+mylist.size()+"::");
                    //System.out.println("vehcletype selecton 0 "+vehicletype.getItemAtPosition(0));
                    //System.out.println("vehcletype selecton 1 "+vehicletype.getItemAtPosition(1));
*/
                    //loop:

                    /*for(int i=0;i<mylist.size();i++)
                    {

                        ////System.out.println("i is.."+i);

                        String v=(String) vehicletype.getItemAtPosition(i);

                        String y[]=v.split("--");
                        //System.out.println(y[0]+":"+y[1]+":"+data.getVehiclecategory()+":"+data.getVehicletype());

                        if(y[0].equals(data.getVehiclecategory())&&y[1].equals(data.getVehicletype()))
                        {
                            vehicletype.setSelection(i);
                            break;
                        }
                    }*/

                    /*for(int i=0;i<vehiclelist.size();i++)
                    {

                        ////System.out.println("i is.."+i);

                        String v1=(String) vehicletype.getItemAtPosition(i);

                        String y[]=v1.split("--");
                        //System.out.println(y[0]+":"+y[1]+":"+data.getVehiclecategory()+":"+data.getVehicletype());

                        if(y[0].equals(data.getVehiclecategory())&&y[1].equals(data.getVehicletype()))
                        {
                            vehicletype.setSelection(i);
                            break;
                        }
                    }*/

                    ////System.out.println("loop breaked!!!!");



                    if(data.getPickupfrom().equals("Office"))
                    {
                        reportingplace.setSelection(0);
                        otherloctns.setText(data.getOtherplace());
                        otherloctns.setVisibility(View.VISIBLE);
                    }
                    else if(data.getPickupfrom().equals("Guest House"))
                    {
                        reportingplace.setSelection(1);
                        otherloctns.setText(data.getOtherplace());
                        otherloctns.setVisibility(View.VISIBLE);
                    }
                    else if(data.getPickupfrom().equals("Residence"))
                    {
                        reportingplace.setSelection(2);
                        otherloctns.setText(data.getOtherplace());
                        otherloctns.setVisibility(View.VISIBLE);
                    }
                    else if(data.getPickupfrom().equals("Airport"))
                    {
                        reportingplace.setSelection(3);
                        flight.setVisibility(View.VISIBLE);
                        flight.setText(data.getFtinfo());
                    }
                    else if(data.getPickupfrom().equals("Railway Station"))
                    {
                        reportingplace.setSelection(4);
                        flight.setVisibility(View.VISIBLE);
                        flight.setText(data.getFtinfo());
                    }
                    else {

                        reportingplace.setSelection(5);
                        otherloctns.setText(data.getOtherplace());
                        otherloctns.setVisibility(View.VISIBLE);
                    }
                    /*else if(data.getPickupfers333rom().equals("Sec-bad Rly Station"))
                    {
                        reportingplace.setSelection(5);
                        flight.setVisibility(View.VISIBLE);
                        flight.setText(data.getFtInfo());
                    }
                    else if(data.getPickupfrom().equals("Kacheguda Rly Station"))
                    {
                        reportingplace.setSelection(6);
                        flight.setVisibility(View.VISIBLE);
                        flight.setText(data.getFtInfo());
                    }*/



                    if(data.getStatus().equals("Active"))
                    {
                        spStatus.setSelection(0);
                        stStatus="Active";
                    }
                    else {
                        spStatus.setSelection(1);
                        stStatus="Cancel";
                    }


                    if(data.getApprovalstatus().equals("Approved"))
                    {
                        submit.setVisibility(View.GONE);
                        bookingtype.setEnabled(false);
                        pointpoint.setEnabled(false);
                        radioGroup.setEnabled(false);
                        radioButton1.setEnabled(false);
                        radioButton2.setEnabled(false);
                        ivEdit.setEnabled(false);
                        servicelocation.setEnabled(false);
                        vehicletype.setEnabled(false);
                        reportingplace.setEnabled(false);
                        g_name.setEnabled(false);
                        g_mailid.setEnabled(false);
                        g_number.setEnabled(false);
                        flight.setEnabled(false);
                        otherloctns.setEnabled(false);
                        no_days.setEnabled(false);
                        et_outstation.setEnabled(false);
                        spStatus.setEnabled(false);
                        etSpecialInst.setEnabled(false);
                    }
                    else {
                        submit.setVisibility(View.VISIBLE);

                        //getTheData();

                    }


                }
            }

            @Override
            public void onFailure(Call<List<GetBookingPojo>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"Please check Internet connection!",Toast.LENGTH_SHORT).show();

            }
        });

    }


    public void getVehicles()
    {

        if(bookingtypeitem4.equals("Point-Point"))
        {
            PListPojo p;

            for(int i=0;i<pList.size();i++)
            {
                p=pList.get(i);
                if(pointpointitem5.equals(p.getPtoPName()))
                {
                    ptopId=p.getPtoPId();
                    break;
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

                if(response.isSuccessful())
                {
                    vList=response.body();

                    for(int i=0;i<vList.size();i++)
                    {
                        v=vList.get(i);

                        vehiclelist.add(v.getVehcategory());

                    }

                    System.out.println("v list sizeeee"+vList.size());
                    System.out.println("vehCt"+data.getVehiclecategory());
                    System.out.println("vehType"+data.getVehicletype());
                    System.out.println("vehicleListe"+vehiclelist.size());



                    //Adapter Logic

                    ArrayAdapter arrayAdapter2=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,vehiclelist);
                    arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    vehicletype.setAdapter(arrayAdapter2);
                    vehicletype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            vehicletypeitem =vehicletype.getItemAtPosition(position).toString();

                            VehiclePojo v1;





                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });



                    VehiclePojo v1;


                    for(int i=0;i<vList.size();i++)
                    {
                        v1=vList.get(i);

                        if(v1.getVehcategory().split("--")[0].equals(data.getVehiclecategory())&&
                                v1.getVehcategory().split("--")[1].equals(data.getVehicletype()))
                        {
                            vehicletype.setSelection(i);
                            System.out.println("vehicle is selected here..."+i);
                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<List<VehiclePojo>> call, Throwable t) {

            }
        });
    }

    public void getPtopNames()
    {

        System.out.println("getting ptop names...."+custemorIds+"::"+serviceLocId);

        Call<List<PListPojo>> call1=REST_CLIENT.getPtoPListNames(custemorIds,serviceLocId);
        call1.enqueue(new Callback<List<PListPojo>>() {
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
                                getVehicles();

                            }

                            //getVehicles();


                            //System.out.println("in on item selected.."+pointpointitem5+"::"+bookingtypeitem4);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else {

                    pList=null;
                }
            }

            @Override
            public void onFailure(Call<List<PListPojo>> call, Throwable t) {

            }
        });
    }

}
