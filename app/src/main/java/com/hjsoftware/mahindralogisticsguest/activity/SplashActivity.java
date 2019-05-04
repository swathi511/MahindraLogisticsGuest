package com.hjsoftware.mahindralogisticsguest.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hjsoftware.mahindralogisticsguest.BuildConfig;
import com.hjsoftware.mahindralogisticsguest.R;
import com.hjsoftware.mahindralogisticsguest.SessionManager;
import com.hjsoftware.mahindralogisticsguest.model.LoginPojo;
import com.hjsoftware.mahindralogisticsguest.webservices.API;
import com.hjsoftware.mahindralogisticsguest.webservices.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    Button btLogin;
    EditText etUname,etPwd,etCode;
    String stUname,stPwd,stCode;
    CoordinatorLayout cLayout;
    ProgressDialog progressDialog;
    API REST_CLIENT;
    SessionManager session;
    TextView tvFpwd;
    HashMap<String, String> user;
    ArrayList<String> mylist3 = new ArrayList<String>();
    String uname,pwd;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "SharedPref";
    String version= BuildConfig.VERSION;
    TextView tvLabel,tvLabel1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        session=new SessionManager(getApplicationContext());
        pref = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        user = session.getUserDetails();
        uname=user.get(SessionManager.KEY_NAME);
        pwd=user.get(SessionManager.KEY_PWD);
        REST_CLIENT= RestClient.get();

        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(intent.getAction())) {
                Log.w("data", "Main Activity is not the root.  Finishing Main Activity instead of launching.");
                finish();
                return;

            }
        }

        if(session.isLoggedIn())
        {


            //System.out.println("details$$$$$$$$ are"+uname+pwd+version);

            JsonObject v=new JsonObject();
            v.addProperty("login",uname);
            v.addProperty("pwd",pwd);
            v.addProperty("GuestVersion",version);

            Call<List<LoginPojo>> login=REST_CLIENT.validate(v);
            login.enqueue(new Callback<List<LoginPojo>>() {

                List <LoginPojo> loginstatus;
                LoginPojo LoginData;
                @Override
                public void onResponse(Call<List<LoginPojo>> call, Response<List<LoginPojo>> response) {


                    if(response.isSuccessful())
                    {
                        loginstatus=response.body();
                        //progressDialog.dismiss();


                        for(int i=0;i<loginstatus.size();i++){

                            LoginData=loginstatus.get(i);
                            String loginNames=LoginData.getEmailid();
                            mylist3.add(loginNames);
                            String custemorIds=LoginData.getCustomerid();
                            session.createLoginSession(uname,pwd);
                            editor.putString("bookername",LoginData.getBookername());
                            editor.putString("emailid",LoginData.getEmailid());
                            editor.putString("mobileno",LoginData.getMobileno());
                            editor.putString("ppnames",LoginData.getPpnames());
                            editor.putString("preparedBy",LoginData.getProfileid());
                            editor.putString("customerId",custemorIds);
                            editor.putInt("cutOffTime", LoginData.getCutofftime());
                            editor.putString("paymentType",LoginData.getPaymenttype());
                            editor.putString("userType",LoginData.getUsertype());
                            editor.putInt("pcutOffTime",LoginData.getPtopCutoffTime());
                            editor.commit();
                            //progressDialog.dismiss();
                            Intent k=new Intent(SplashActivity.this,HomeActivity.class);
                            startActivity(k);
                            finish();
                        }


                    }
                    else {
                        //progressDialog.dismiss();
                        String msg = response.message();
                        if(msg.equals("Not Found"))
                        {
                            /*Snackbar snackbar = Snackbar.make(cLayout, "Invalid details entered", Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor("#ffffff"));
                            snackbar.getView().setBackgroundColor(ContextCompat.getColor(SplashActivity.this, R.color.colorPrimaryDark));
                            snackbar.show();
                            etUname.setText("");
                            etPwd.setText("");
                            etCode.setText("");*/

                            Toast.makeText(SplashActivity.this,"Invalid credentials!"+" : "+msg,Toast.LENGTH_SHORT).show();


                        }
                        else {

                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SplashActivity.this);

                            LayoutInflater inflater = getLayoutInflater();
                            final View dialogView = inflater.inflate(R.layout.alert_version, null);
                            dialogBuilder.setView(dialogView);

                            Button ok = (Button) dialogView.findViewById(R.id.av_bt_ok);

                            final AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);

                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    alertDialog.dismiss();
                                    openAppRating(getApplicationContext());

                                    finish();
                                }
                            });

                        }
                    }
                }
                @Override
                public void onFailure(Call<List<LoginPojo>> call, Throwable t) {

                    //progressDialog.dismiss();
                    Toast.makeText(SplashActivity.this,"Please check Internet connection!",Toast.LENGTH_LONG).show();
                    finish();

                }
            });
        }
        else {

            Intent k=new Intent(SplashActivity.this,MainActivity.class);
            startActivity(k);
            finish();
            
        }
    }

    public static void openAppRating(Context context) {
        // you can also use BuildConfig.APPLICATION_ID
        String appId = context.getPackageName();
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                // make sure it does NOT open in the stack of your activity
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // task reparenting if needed
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }
    }
}
