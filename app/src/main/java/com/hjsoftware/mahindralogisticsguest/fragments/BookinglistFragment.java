package com.hjsoftware.mahindralogisticsguest.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.hjsoftware.mahindralogisticsguest.R;
import com.hjsoftware.mahindralogisticsguest.SessionManager;
import com.hjsoftware.mahindralogisticsguest.activity.BookingAdapter;
import com.hjsoftware.mahindralogisticsguest.activity.BookingData;
import com.hjsoftware.mahindralogisticsguest.model.GetBookingPojo;
import com.hjsoftware.mahindralogisticsguest.webservices.API;
import com.hjsoftware.mahindralogisticsguest.webservices.RestClient;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookinglistFragment extends Fragment {
    View v;
    TextView frmdate,toodate;
    ImageButton fdate,tdate;
    private int mYear,mMonth,mDay;
    DatePickerDialog datePickerDialog;
    TextView textView,date1,date2;
    BookingAdapter bAdapter;
    RecyclerView rView;
    SessionManager session;
    ArrayList<BookingData> bookingList=new ArrayList<>();
    API REST_CLIENT;
    String profileid;
    String fromdate;
    String todate;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "SharedPref";
    ImageView ivEdit;
    Spinner spStatus;
    String stStatus="Active";
    String[] status={"Active","Cancel","Both"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        REST_CLIENT= RestClient.get();
        pref = getActivity().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        profileid=pref.getString("preparedBy",null);
        session=new SessionManager(getContext());
        System.out.println("PROFILEIDisss"+profileid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_booking_list_other, container, false);

        frmdate=(TextView) v.findViewById(R.id.edit_fromdate);
        toodate=(TextView) v.findViewById(R.id.edit_Todate);
        fdate=(ImageButton)v.findViewById(R.id.button_select);
        tdate=(ImageButton)v.findViewById(R.id.buton_select);
        textView=(TextView)v.findViewById(R.id.textview_view);
        rView=(RecyclerView)v.findViewById(R.id.recycler_view);
        date1=(TextView)v.findViewById(R.id.text_fromdate);
        date2=(TextView)v.findViewById(R.id.text_todate);
        spStatus=(Spinner)v.findViewById(R.id.spinner_status);
        rView.setVisibility(View.GONE);

        Typeface font=Typeface.createFromAsset(getActivity().getAssets(),"fonts/fontawesome-webfont.ttf");
        date2.setTypeface(font);
        date1.setTypeface(font);

        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        fromdate=(mMonth+1)+"-"+mDay+"-"+mYear;
        //fromdate=mDay+"-"+(mMonth+1)+"-"+mYear;
        todate=(mMonth+1)+"-"+mDay+"-"+mYear;
        //todate=mDay+"-"+(mMonth+1)+"-"+mYear;
        SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );
        frmdate.setText(mDay+"/"+(mMonth+1)+"/"+mYear);
        toodate.setText(mDay+"/"+(mMonth+1)+"/"+mYear);

        ArrayAdapter arrayAdapter7=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,status);
        arrayAdapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(arrayAdapter7);

        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==0)
                {
                    stStatus="Active";
                }
                else {
                    stStatus=spStatus.getItemAtPosition(position).toString();;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getBookingsData();

        fdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                frmdate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                fromdate=(monthOfYear+1)+"-"+dayOfMonth+"-"+year;
                                //fromdate=dayOfMonth+"-"+(monthOfYear+1)+"-"+year;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }


        });
        tdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                toodate.setText(dayOfMonth + "/"
                                        +(monthOfYear + 1)+ "/" + year);

                                todate=(monthOfYear+1)+"-"+dayOfMonth+"-"+year;
                                //todate=dayOfMonth+"-"+(monthOfYear+1)+"-"+year;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getBookingsData();

                /*
               Step 1: Get From date and to Date ... Call the Web service - API call
               Step 2: API call data has to be kept in arraylist
               Step 3: Arraylist has to be passed to RecyclerAdapter
                 */

            }
        });

        return v;
    }

    public void getBookingsData()
    {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();


        String custemorIds = pref.getString("customerId",null);

        //System.out.println("!"+custemorIds+"!"+fromdate+"!"+todate+"!"+profileid+"!");

        Log.i("@@@Customer id@@",custemorIds+" nnn "+fromdate+" nnn "+todate+"NNN"+stStatus);
        Call<List<GetBookingPojo>>getBookingPojo=REST_CLIENT.getbookingdetails(custemorIds,profileid,fromdate,todate,stStatus);
        getBookingPojo.enqueue(new Callback<List<GetBookingPojo>>() {

            List<GetBookingPojo>bookingstatus;
            GetBookingPojo bookingData;
            @Override
            public void onResponse(Call<List<GetBookingPojo>> call, Response<List<GetBookingPojo>> response) {
                bookingstatus=response.body();
                if(response.isSuccessful()){


                    bookingList.clear();
                    for(int i=0;i<bookingstatus.size();i++){

                        bookingData=bookingstatus.get(i);
                        String bookingid=bookingData.getBookingid();
                        String vecle=bookingData.getVehiclecategory();
                        String reprtdate=bookingData.getReportdate();
                        String reporttime=bookingData.getReporttime();
                        String guestname=bookingData.getGuestname();
                        bookingList.add(new BookingData(bookingData.getBookingid(),bookingData.getCreationdate().split(" ")[0],bookingData.getCreationtime(),bookingData.getVehiclecategory(),bookingData.getGuestname(),bookingData.getReportdate(),bookingData.getReporttime(),bookingData.getStatus(),bookingData.getDstatus(),bookingData.getVehicletype(),bookingData.getApprovalstatus()));

                    }
                    if(bookingList.size()!=0){

                        Log.i("data","OO::::"+bookingList.size());
                        rView.setVisibility(View.VISIBLE);
                        bAdapter = new BookingAdapter(getActivity(), bookingList, rView);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        rView.setLayoutManager(mLayoutManager);
                        rView.setItemAnimator(new DefaultItemAnimator());
                        rView.setAdapter(bAdapter);
                        bAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();

                    }else{

                        rView.setVisibility(View.GONE);
                        Toast.makeText(getActivity(),"No Bookings!",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }
                else {

                    rView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(),"No Bookings!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<GetBookingPojo>> call, Throwable t) {
                rView.setVisibility(View.GONE);
                progressDialog.dismiss();
                t.printStackTrace();
                System.out.println("msg"+t.getMessage());
                Toast.makeText(getActivity(),"Please check Internet connection!",Toast.LENGTH_SHORT).show();

            }
        });
    }



}
