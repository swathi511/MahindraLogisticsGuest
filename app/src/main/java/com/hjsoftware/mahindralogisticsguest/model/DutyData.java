package com.hjsoftware.mahindralogisticsguest.model;

import java.io.Serializable;

public class DutyData implements Serializable {

    String dsno,dsdate,vehno,dname,dmobile,lat,lng,dslipid,userstatus,loginstatus,appstatus,otp,img;

    public DutyData(String dsno,String dsdate,String vehno,String dname,String dmobile,String lat,String lng,String dslipid,
                    String userstatus,String loginstatus,String appstatus,String otp,String img)
    {
        this.dsno=dsno;
        this.dsdate=dsdate;
        this.vehno=vehno;
        this.dname=dname;
        this.dmobile=dmobile;
        this.lat=lat;
        this.lng=lng;
        this.dslipid=dslipid;
        this.userstatus=userstatus;
        this.loginstatus=loginstatus;
        this.appstatus=appstatus;
        this.otp=otp;
        this.img=img;
    }

    public String getDsno() {
        return dsno;
    }

    public void setDsno(String dsno) {
        this.dsno = dsno;
    }

    public String getDsdate() {
        return dsdate;
    }

    public void setDsdate(String dsdate) {
        this.dsdate = dsdate;
    }

    public String getVehno() {
        return vehno;
    }

    public void setVehno(String vehno) {
        this.vehno = vehno;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDmobile() {
        return dmobile;
    }

    public void setDmobile(String dmobile) {
        this.dmobile = dmobile;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDslipid() {
        return dslipid;
    }

    public void setDslipid(String dslipid) {
        this.dslipid = dslipid;
    }

    public String getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(String userstatus) {
        this.userstatus = userstatus;
    }

    public String getLoginstatus() {
        return loginstatus;
    }

    public void setLoginstatus(String loginstatus) {
        this.loginstatus = loginstatus;
    }

    public String getAppstatus() {
        return appstatus;
    }

    public void setAppstatus(String appstatus) {
        this.appstatus = appstatus;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
