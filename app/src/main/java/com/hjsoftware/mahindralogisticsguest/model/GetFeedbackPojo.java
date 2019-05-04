package com.hjsoftware.mahindralogisticsguest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFeedbackPojo {

    @SerializedName("qid")
    @Expose
    private String qid;
    @SerializedName("question")
    @Expose
    private String question;

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
