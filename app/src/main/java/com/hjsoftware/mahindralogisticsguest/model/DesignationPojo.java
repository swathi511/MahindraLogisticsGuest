package com.hjsoftware.mahindralogisticsguest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DesignationPojo {

    @SerializedName("DesignationId")
    @Expose
    private String designationId;
    @SerializedName("Designation")
    @Expose
    private String designation;

    public String getDesignationId() {
        return designationId;
    }

    public void setDesignationId(String designationId) {
        this.designationId = designationId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
