package com.hjsoftware.mahindralogisticsguest.webservices;

import com.hjsoftware.mahindralogisticsguest.model.Changepassword;
import com.hjsoftware.mahindralogisticsguest.model.DesignationPojo;
import com.hjsoftware.mahindralogisticsguest.model.DutyDataPojo;
import com.hjsoftware.mahindralogisticsguest.model.GetBookingPojo;
import com.hjsoftware.mahindralogisticsguest.model.GetFeedbackPojo;
import com.hjsoftware.mahindralogisticsguest.model.LocationPojo;
import com.hjsoftware.mahindralogisticsguest.model.LoginPojo;
import com.hjsoftware.mahindralogisticsguest.model.MessagePojo;
import com.hjsoftware.mahindralogisticsguest.model.PListPojo;
import com.hjsoftware.mahindralogisticsguest.model.SpecificBookingPojo;
import com.hjsoftware.mahindralogisticsguest.model.VehiclePojo;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by hjsoftware on 22/12/17.
 */

public interface API {
    @POST("Login/Checklogin")
    Call<List<LoginPojo>> validate(@Body JsonObject v);

    @GET("VehicleCat/GetVehicleCat")
    Call<List<VehiclePojo>> getInfo(@Query("customerid") String cid);

    @GET("Locations/GetLocations")
    Call<List<LocationPojo>> getInfoo(@Query("customerid") String cid);

    @POST("Booking/AddBooking")
    Call<MessagePojo> bookinginfo(@Body JsonObject v);

    @POST("ChangePassword/update")
    Call<Changepassword> passwordinfo(@Body JsonObject v);

    @GET("CustomerBookingsBydates/GetBookings")
    Call<List<GetBookingPojo>>getbookingdetails(@Query("customerid") String cid,
                                                @Query("profileid") String pid,
                                                @Query("fromdate") String fdate,
                                                @Query("todate") String tdate,
                                                @Query("status") String status );

    @GET("GetBookingDetails/GetDetails")
    //Call<List<SpecificBookingPojo>> getSpecificBooking(
    Call<List<GetBookingPojo>> getSpecificBooking(
            @Query("customerid") String customerId,
            @Query("bookingid") String bookingId,
            @Query("profileid") String profileid
    );

    @GET("DutySlipDetails/GetDetails")
    Call<List<DutyDataPojo>> getOngoingDSlipDetails(@Query("profileid") String profileId);

    @GET("FBQuestions/GetQuestions")
    Call<List<GetFeedbackPojo>> getFeedback();

    @POST("feedback/AddFeedBack")
    Call<MessagePojo> giveFeedback(@Body JsonObject v);

    @GET("feedback/GetDutyslips")
    Call<List<DutyDataPojo>> getFeedbackBoookings(@Query("profileid") String profileId);

    @POST("Forgotpassword/Send")
    Call<MessagePojo> forgotDetails(@Body JsonObject v);

    @GET("PtoPNames/GetNames")
    Call<List<PListPojo>> getPtoPListNames(@Query("customerid") String customerId,
                                     @Query("ServicelocId") String serviceLoc);

    @POST("Vehicles/VehNames")
    Call<List<VehiclePojo>> getVehicles(@Body JsonObject v);

    @GET("Designations/GetDetails")
    Call<List<DesignationPojo>> getDesignations(@Query("Flag") String flag);










}
