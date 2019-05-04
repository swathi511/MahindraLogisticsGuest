package com.hjsoftware.mahindralogisticsguest.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PListPojo {

    @SerializedName("PtoPName")
    @Expose
    private String ptoPName;
    @SerializedName("PtoPId")
    @Expose
    private String ptoPId;

    public String getPtoPName() {
        return ptoPName;
    }

    public void setPtoPName(String ptoPName) {
        this.ptoPName = ptoPName;
    }

    public String getPtoPId() {
        return ptoPId;
    }

    public void setPtoPId(String ptoPId) {
        this.ptoPId = ptoPId;
    }

}