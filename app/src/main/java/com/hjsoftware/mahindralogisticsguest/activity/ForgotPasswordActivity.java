package com.hjsoftware.mahindralogisticsguest.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hjsoftware.mahindralogisticsguest.R;
import com.hjsoftware.mahindralogisticsguest.model.MessagePojo;
import com.hjsoftware.mahindralogisticsguest.webservices.API;
import com.hjsoftware.mahindralogisticsguest.webservices.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    LinearLayout ll1,ll2;
    TextView tvUname,tvPwd;
    EditText etMobileNo,etEmailId;
    Button btGetUname,btGetPwd;
    TextView tvSms1,tvSms2;
    TextView tvLogin1,tvLogin2;
    ImageView ivBack;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "SharedPref";
    API REST_CLIENT;
    String bookernumber,bookeremail;
    ProgressDialog progressDialog;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        pref = getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        ll1=(LinearLayout)findViewById(R.id.afp_ll1);
        ll2=(LinearLayout)findViewById(R.id.afp_ll2);

        tvUname=(TextView)findViewById(R.id.afp_tv_funame);
        tvPwd=(TextView)findViewById(R.id.afp_tv_fpwd);
        etMobileNo=(EditText)findViewById(R.id.afp_et_mobile_no);
        etEmailId=(EditText)findViewById(R.id.afp_et_email);
        btGetUname=(Button)findViewById(R.id.afp_bt_get_uname);
        btGetPwd=(Button)findViewById(R.id.afp_bt_get_pwd);

        tvSms1=(TextView)findViewById(R.id.afp_tv_sms_text1);
        tvSms2=(TextView)findViewById(R.id.afp_tv_sms_text2);
        tvLogin1=(TextView)findViewById(R.id.afp_tv_login1);
        tvLogin2=(TextView)findViewById(R.id.afp_tv_login2);

        ivBack=(ImageView)findViewById(R.id.afp_iv_back);

        ll1.setVisibility(View.GONE);
        ll2.setVisibility(View.GONE);

        tvSms1.setVisibility(View.GONE);
        tvSms2.setVisibility(View.GONE);

        tvLogin1.setVisibility(View.GONE);
        tvLogin1.setPaintFlags(tvLogin1.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        tvLogin2.setVisibility(View.GONE);
        tvLogin2.setPaintFlags(tvLogin2.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        tvUname.setPaintFlags(tvUname.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        tvPwd.setPaintFlags(tvPwd.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        bookernumber=pref.getString("mobileno","");
        bookeremail=pref.getString("emailid","");

        REST_CLIENT= RestClient.get();

        tvUname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.GONE);
                tvSms2.setVisibility(View.GONE);
                tvLogin2.setVisibility(View.GONE);
                tvUname.setTextColor(getResources().getColor(R.color.colorTextBlue));
                tvPwd.setTextColor(getResources().getColor(R.color.colorBlack));
                tvPwd.setVisibility(View.GONE);
            }
        });

        tvPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll2.setVisibility(View.VISIBLE);
                ll1.setVisibility(View.GONE);
                tvSms1.setVisibility(View.GONE);
                tvLogin1.setVisibility(View.GONE);
                tvPwd.setTextColor(getResources().getColor(R.color.colorTextBlue));
                tvUname.setTextColor(getResources().getColor(R.color.colorBlack));
                tvUname.setVisibility(View.GONE);
            }
        });

        btGetUname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobileNo = etMobileNo.getText().toString().trim();

                if(mobileNo.length()!=10)
                {
                   Toast.makeText(ForgotPasswordActivity.this,"Please enter valid Mobile Number!",Toast.LENGTH_SHORT).show();
                }

                else {
                    //if (mobileNo.equals(bookernumber)) {

                        JsonObject j=new JsonObject();
                        j.addProperty("mobile",mobileNo);
                        j.addProperty("emailid","-");

                        progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Please wait ...");
                        progressDialog.show();

                        Call<MessagePojo> call=REST_CLIENT.forgotDetails(j);
                        call.enqueue(new Callback<MessagePojo>() {
                            @Override
                            public void onResponse(Call<MessagePojo> call, Response<MessagePojo> response) {

                                if(response.isSuccessful())
                                {
                                    progressDialog.dismiss();
                                    tvSms1.setVisibility(View.VISIBLE);
                                    tvLogin1.setVisibility(View.VISIBLE);

                                    etMobileNo.setEnabled(false);
                                    btGetUname.setEnabled(false);

                                }
                                else {

                                    progressDialog.dismiss();

                                    Toast.makeText(ForgotPasswordActivity.this,"Error: "+response.message(),Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<MessagePojo> call, Throwable t) {

                                progressDialog.dismiss();

                                Toast.makeText(ForgotPasswordActivity.this,"Please check Internet connection!",Toast.LENGTH_SHORT).show();

                            }
                        });


                   /* } else {

                        Toast.makeText(ForgotPasswordActivity.this, "Please enter your Registered Mobile Number!", Toast.LENGTH_SHORT).show();
                    }*/
                }

            }

        });

        btGetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailId=etEmailId.getText().toString().trim();

                if(!isValidEmail(emailId))
                {
                    Toast.makeText(ForgotPasswordActivity.this,"Please enter valid email id!",Toast.LENGTH_SHORT).show();

                }
                else {
                    //if (emailId.equals(bookeremail)) {

                        JsonObject j=new JsonObject();
                        j.addProperty("mobile","-");
                        j.addProperty("emailid",emailId);

                        progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Please wait ...");
                        progressDialog.show();

                        Call<MessagePojo> call=REST_CLIENT.forgotDetails(j);
                        call.enqueue(new Callback<MessagePojo>() {
                            @Override
                            public void onResponse(Call<MessagePojo> call, Response<MessagePojo> response) {

                                if(response.isSuccessful())
                                {
                                    progressDialog.dismiss();

                                    tvSms2.setVisibility(View.VISIBLE);
                                    tvLogin2.setVisibility(View.VISIBLE);

                                    etEmailId.setEnabled(false);
                                    btGetPwd.setEnabled(false);

                                }
                                else {
                                    progressDialog.dismiss();


                                    Toast.makeText(ForgotPasswordActivity.this,"Error: "+response.message(),Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<MessagePojo> call, Throwable t) {

                                progressDialog.dismiss();


                                Toast.makeText(ForgotPasswordActivity.this,"Please check Internet connection!",Toast.LENGTH_SHORT).show();

                            }
                        });



                    /*} else {

                        Toast.makeText(ForgotPasswordActivity.this, "Please enter your Registered Email Id!", Toast.LENGTH_SHORT).show();

                    }*/
                }

            }
        });

        tvLogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(ForgotPasswordActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        tvLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(ForgotPasswordActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(ForgotPasswordActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
