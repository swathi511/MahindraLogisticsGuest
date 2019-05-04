package com.hjsoftware.mahindralogisticsguest.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hjsoftware.mahindralogisticsguest.R;
import com.hjsoftware.mahindralogisticsguest.model.GetFeedbackPojo;
import com.hjsoftware.mahindralogisticsguest.model.MessagePojo;
import com.hjsoftware.mahindralogisticsguest.webservices.API;
import com.hjsoftware.mahindralogisticsguest.webservices.RestClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {

    View v;
    API REST_CLIENT;
    TextView tvQ1,tvQ2,tvQ3,tvQ4;
    RadioButton rbY1,rbN1;
    RadioButton rbY2,rbN2;
    RadioButton rbY3,rbN3;
    RadioButton rbY4,rbN4;
    EditText etComments;
    RadioGroup rg1,rg2,rg3,rg4;
    String rb1Value="-",rb2Value="-",rb3Value="-",rb4Value="-";
    TextView tvSubmit;
    RatingBar ratingBar;
    float ratingValue=0;
    String stQid1,stQid2,stQid3,stQid4;
    TextView tvLabel;
    String stDSlipId,stDsno;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "SharedPref";
    ImageView ivLeft;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feedback);
        pref = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();


        tvQ1=(TextView)findViewById(R.id.ffd_tv_q1);
        tvQ2=(TextView)findViewById(R.id.ffd_tv_q2);
        tvQ3=(TextView)findViewById(R.id.ffd_tv_q3);
        tvQ4=(TextView)findViewById(R.id.ffd_tv_q4);
        rbY1=(RadioButton)findViewById(R.id.radio_yes1);
        rbY2=(RadioButton)findViewById(R.id.radio_yes2);
        rbY3=(RadioButton)findViewById(R.id.radio_yes3);
        rbY4=(RadioButton)findViewById(R.id.radio_yes4);
        rbN1=(RadioButton)findViewById(R.id.radio_no1);
        rbN2=(RadioButton)findViewById(R.id.radio_no2);
        rbN3=(RadioButton)findViewById(R.id.radio_no3);
        rbN4=(RadioButton)findViewById(R.id.radio_no4);

        rg1=(RadioGroup)findViewById(R.id.ffd_radiogroup1);
        rg2=(RadioGroup)findViewById(R.id.ffd_radiogroup2);
        rg3=(RadioGroup)findViewById(R.id.ffd_radiogroup3);
        rg4=(RadioGroup)findViewById(R.id.ffd_radiogroup4);

        ratingBar=(RatingBar)findViewById(R.id.ffb_rbar);

        etComments=(EditText)findViewById(R.id.ffd_et_note);
        tvSubmit=(TextView)findViewById(R.id.ffd_submit);

        tvLabel=(TextView)findViewById(R.id.ffbd_tv_label);
        ivLeft=(ImageView)findViewById(R.id.ffd_iv_back);

        REST_CLIENT= RestClient.get();

        Bundle d=getIntent().getExtras();

        stDSlipId=d.getString("dslipid","");
        stDsno=d.getString("dslipno","");


        tvLabel.setText("Give Feedback for # "+stDsno);

        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.radio_yes1:
                        rb1Value="Yes";
                        break;

                    case R.id.radio_no1:
                        rb1Value="No";
                        break;

                }
            }
        });

        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.radio_yes2:
                        rb2Value="Yes";
                        break;

                    case R.id.radio_no2:
                        rb2Value="No";
                        break;

                }
            }
        });

        rg3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.radio_yes3:
                        rb3Value="Yes";
                        break;

                    case R.id.radio_no3:
                        rb3Value="No";
                        break;

                }
            }
        });

        rg4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.radio_yes4:
                        rb4Value="Yes";
                        break;


                    case R.id.radio_no4:
                        rb4Value="No";
                        break;

                }
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ratingValue=ratingBar.getRating();

                if(rb1Value.equals("-")||rb2Value.equals("-")||rb3Value.equals("-")||rb4Value.equals("-"))
                {
                    Toast.makeText(FeedbackActivity.this,"Please answer all the questions!",Toast.LENGTH_SHORT).show();
                }
                else if(ratingValue==0){

                    Toast.makeText(FeedbackActivity.this,"Please give the rating!",Toast.LENGTH_SHORT).show();
                }
                else {

                    giveFeedback();

                }
            }
        });

        Call<List<GetFeedbackPojo>> call=REST_CLIENT.getFeedback();
        call.enqueue(new Callback<List<GetFeedbackPojo>>() {
            @Override
            public void onResponse(Call<List<GetFeedbackPojo>> call, Response<List<GetFeedbackPojo>> response) {

                GetFeedbackPojo p;
                List<GetFeedbackPojo> pList;

                if(response.isSuccessful())
                {

                    pList=response.body();

                    for(int i=0;i<pList.size();i++)
                    {
                        p=pList.get(i);

                        if(i==0) {
                            tvQ1.setText(p.getQuestion());
                            stQid1=p.getQid();
                        }
                        if(i==1) {
                            tvQ2.setText(p.getQuestion());
                            stQid2=p.getQid();
                        }

                        if(i==2) {
                            tvQ3.setText(p.getQuestion());
                            stQid3=p.getQid();
                        }
                        if(i==3) {
                            tvQ4.setText(p.getQuestion());
                            stQid4=p.getQid();
                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<List<GetFeedbackPojo>> call, Throwable t) {

            }
        });
    }


    public void giveFeedback()
    {

        System.out.println("*********************");
        System.out.println(stDSlipId);
        System.out.println(stQid1);
        System.out.println(stQid2);
        System.out.println(stQid3);
        System.out.println(stQid4);
        System.out.println(rb1Value);
        System.out.println(rb2Value);
        System.out.println(rb3Value);
        System.out.println(rb4Value);
        System.out.println(ratingValue);
        System.out.println(etComments.getText().toString().trim());
        System.out.println("*********************");



        JsonObject v=new JsonObject();
        v.addProperty("dslipid",stDSlipId);
        v.addProperty("fbquestion1",stQid1);
        v.addProperty("fbquestion2",stQid2);
        v.addProperty("fbquestion3",stQid3);
        v.addProperty("fbquestion4",stQid4);
        v.addProperty("fbanswer1",rb1Value);
        v.addProperty("fbanswer2",rb2Value);
        v.addProperty("fbanswer3",rb3Value);
        v.addProperty("fbanswer4",rb4Value);
        v.addProperty("rating",String.valueOf(ratingValue));
        v.addProperty("comments",etComments.getText().toString().trim());

        Call<MessagePojo> call=REST_CLIENT.giveFeedback(v);
        call.enqueue(new Callback<MessagePojo>() {
            @Override
            public void onResponse(Call<MessagePojo> call, Response<MessagePojo> response) {

                System.out.println("is response successful"+response.isSuccessful());

                if(response.isSuccessful())
                {
                    Toast.makeText(FeedbackActivity.this,"Thanks for the feedback!",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(FeedbackActivity.this,FeedbackListActivity.class);
                    startActivity(i);
                    finish();

                }
                else {

                    System.out.println("****"+response.message());
                }
            }

            @Override
            public void onFailure(Call<MessagePojo> call, Throwable t) {

                t.printStackTrace();

            }
        });
    }
}
