package com.hjsoftware.mahindralogisticsguest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hjsoftware on 3/1/18.
 */

public class LoginPojo {

    @SerializedName("customerid")
    @Expose
    private String customerid;
    @SerializedName("profileid")
    @Expose
    private String profileid;
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("bookername")
    @Expose
    private String bookername;
    @SerializedName("emailid")
    @Expose
    private String emailid;
    @SerializedName("mobileno")
    @Expose
    private String mobileno;
    @SerializedName("cutofftime")
    @Expose
    private Integer cutofftime;
    @SerializedName("ppnames")
    @Expose
    private String ppnames;
    @SerializedName("paymenttype")
    @Expose
    private String paymenttype;
    @SerializedName("usertype")
    @Expose
    private String usertype;
    @SerializedName("ptopCutoffTime")
    @Expose
    private Integer ptopCutoffTime;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("designationid")
    @Expose
    private String designationid;

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBookername() {
        return bookername;
    }

    public void setBookername(String bookername) {
        this.bookername = bookername;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public Integer getCutofftime() {
        return cutofftime;
    }

    public void setCutofftime(Integer cutofftime) {
        this.cutofftime = cutofftime;
    }

    public String getPpnames() {
        return ppnames;
    }

    public void setPpnames(String ppnames) {
        this.ppnames = ppnames;
    }

    public String getProfileid() {
        return profileid;
    }

    public void setProfileid(String profileid) {
        this.profileid = profileid;
    }

    public String getPaymenttype() {
        return paymenttype;
    }

    public void setPaymenttype(String paymenttype) {
        this.paymenttype = paymenttype;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public Integer getPtopCutoffTime() {
        return ptopCutoffTime;
    }

    public void setPtopCutoffTime(Integer ptopCutoffTime) {
        this.ptopCutoffTime = ptopCutoffTime;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDesignationid() {
        return designationid;
    }

    public void setDesignationid(String designationid) {
        this.designationid = designationid;
    }
}