package com.hjsoftware.mahindralogisticsguest.activity;

import java.io.Serializable;

/**
 * Created by hjsoftware on 6/1/18.
 */

public class BookingData implements Serializable{

    private  String bookingId,bookingDate,bookingTime,reporting_date,reporting_time,guest_name,vehicle_name,status,dutyStatus,veh_cat,appStatus;

    public BookingData(String bookingId,String bookingDate,String bookingTime,String bookingDataVehicletype, String guestname, String reportdate, String reporttime,String status,String dutyStatus,String vehCat,String appStatus){

        this.bookingId=bookingId;
        this.bookingDate=bookingDate;
        this.bookingTime=bookingTime;
        this.reporting_date=reportdate;
        this.reporting_time=reporttime;
        this.guest_name=guestname;
        this.vehicle_name=bookingDataVehicletype;
        this.status=status;
        this.dutyStatus=dutyStatus;
        this.veh_cat=vehCat;
        this.appStatus=appStatus;
    }
    public String getReporting_date() {
        return reporting_date;
    }

    public void setReporting_date(String reporting_date) {
        this.reporting_date = reporting_date;
    }
    public String getReporting_time() {
        return reporting_time;
    }

    public void setReporting_time(String reporting_time) {
        this.reporting_time = reporting_time;
    }
    public String getGuest_name() {
        return guest_name;
    }

    public void setGuest_name(String guest_name) {
        this.guest_name = guest_name;
    }
    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getDutyStatus() {
        return dutyStatus;
    }

    public void setDutyStatus(String dutyStatus) {
        this.dutyStatus = dutyStatus;
    }

    public String getVeh_cat() {
        return veh_cat;
    }

    public void setVeh_cat(String veh_cat) {
        this.veh_cat = veh_cat;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }
}
