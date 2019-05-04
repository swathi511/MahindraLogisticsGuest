package com.hjsoftware.mahindralogisticsguest.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjsoftware.mahindralogisticsguest.DrawerItemCustomAdapter;
import com.hjsoftware.mahindralogisticsguest.NavigationData;
import com.hjsoftware.mahindralogisticsguest.R;
import com.hjsoftware.mahindralogisticsguest.fragments.BookingNewFragment;
import com.hjsoftware.mahindralogisticsguest.SessionManager;
import com.hjsoftware.mahindralogisticsguest.webservices.API;
import com.hjsoftware.mahindralogisticsguest.webservices.RestClient;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    DrawerItemCustomAdapter adapter;
    final static int REQUEST_LOCATION = 199;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "SharedPref";
    TextView tvName,tvMobile;
    RelativeLayout rLayout;
    SessionManager session;
    String stTime,stProfileid;
    API REST_CLIENT;
    HashMap<String, String> user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mTitle = mDrawerTitle = getTitle();
        //mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_view_drawer);
        tvName=(TextView)findViewById(R.id.ah_tv_name);
        // tvMobile=(TextView)findViewById(R.id.ah_tv_mobile);
        rLayout=(RelativeLayout)findViewById(R.id.left_drawer);

        REST_CLIENT= RestClient.get();

        session=new SessionManager(getApplicationContext());
        user=session.getUserDetails();

        pref = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        String myString=pref.getString("emailid","xxx");

        String upperString = myString.substring(0,1).toUpperCase() + myString.substring(1);

        tvName.setText(upperString);
        // tvMobile.setText(pref.getString("mobile","91xxxxxxxx"));

        setupToolbar();

        NavigationData[] drawerItem = new NavigationData[6];

        drawerItem[0] = new NavigationData(R.drawable.arrow, "New Booking");
        drawerItem[1] = new NavigationData(R.drawable.arrow, "All Bookings");
        drawerItem[2] = new NavigationData(R.drawable.arrow, "Track your Booking");
        drawerItem[3] = new NavigationData(R.drawable.arrow, "Feedback");
        drawerItem[4] = new NavigationData(R.drawable.arrow,"Change Password");
        drawerItem[5] = new NavigationData(R.drawable.arrow,"Logout");



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        setupDrawerToggle();

        Fragment fragment=new BookingNewFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_frame, fragment,"New Booking").commit();
        setTitle("New Booking");
        adapter.setSelectedItem(0);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {


                if (getSupportFragmentManager().getBackStackEntryCount() >0) {

                    mDrawerToggle.setDrawerIndicatorEnabled(false);//showing back button
                    setTitle("New Booking");

                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // onBackPressed();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.remove(getSupportFragmentManager().findFragmentByTag("All_Bookings"));
                            fragmentManager.popBackStackImmediate();
                        }
                    });
                }
                else
                {

                    mDrawerToggle.setDrawerIndicatorEnabled(true);
                    setTitle("LR Booking");

                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //mDrawerLayout.openDrawer(mDrawerList);
                            mDrawerLayout.openDrawer(rLayout);
                        }
                    });
                }
            }
        });
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        Fragment fragment = null;
        adapter.setSelectedItem(position);

        switch (position) {
            case 0:
                Intent k=new Intent(HomeActivity.this,HomeActivity.class);
                k.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                k.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(k);
                finish();

                break;
            case 1:
                Intent i=new Intent(HomeActivity.this,BookingListActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();

                break;
            case 2:
                Intent m=new Intent(HomeActivity.this,TrackBookingActivity.class);
                m.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                m.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(m);
                finish();

                break;
            case 3:
                Intent o=new Intent(HomeActivity.this,FeedbackListActivity.class);
                o.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                o.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(o);
                finish();

                break;
            case 4:
                Intent j=new Intent(HomeActivity.this,ChangepasswordActivity.class);
                j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                j.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(j);
                finish();

                break;
            case 5:

                SessionManager s=new SessionManager(getApplicationContext());
                s.logoutUser();
                Intent l=new Intent(this,MainActivity.class);
                l.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    l.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(l);
                finish();
                break;

            case 6:

                break;



            default:
                break;
        }

        if (fragment != null) {

            openFragment(fragment,position);

        } else {
            // Log.e("MainActivity", "Error in creating fragment");
        }
    }

    private void openFragment(Fragment fragment, int position){

        Fragment containerFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);

        if (containerFragment.getClass().getName().equalsIgnoreCase(fragment.getClass().getName())) {
            mDrawerLayout.closeDrawer(mDrawerList);
            return;
        }

        else{
            /*
           FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            */
            mDrawerLayout.closeDrawer(mDrawerList);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        /*
        int titleId = getResources().getIdentifier("toolbar", "id", "android");
        TextView abTitle = (TextView) findViewById(titleId);
        abTitle.setTextColor(Color.parseColor("#000000"));*/
    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

//sendLogoutStatus();
        super.onBackPressed();

    }





}
