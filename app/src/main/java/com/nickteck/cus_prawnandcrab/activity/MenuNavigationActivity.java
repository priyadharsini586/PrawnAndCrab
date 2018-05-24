package com.nickteck.cus_prawnandcrab.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.nickteck.cus_prawnandcrab.Db.Database;
import com.nickteck.cus_prawnandcrab.R;
import com.nickteck.cus_prawnandcrab.additional_class.AdditionalClass;
import com.nickteck.cus_prawnandcrab.additional_class.Constants;
import com.nickteck.cus_prawnandcrab.fragment.ContentFragment;
import com.nickteck.cus_prawnandcrab.fragment.FavouriteFragment;
import com.nickteck.cus_prawnandcrab.fragment.HistoryFragment;
import com.nickteck.cus_prawnandcrab.fragment.MyLocationFragment;
import com.nickteck.cus_prawnandcrab.fragment.MyOrdersFragment;
import com.nickteck.cus_prawnandcrab.fragment.OffersFragment;
import com.nickteck.cus_prawnandcrab.fragment.OrderFragment;
import com.nickteck.cus_prawnandcrab.fragment.OrderTakenScreenFragment;
import com.nickteck.cus_prawnandcrab.fragment.TestimonyFragment;
import com.nickteck.cus_prawnandcrab.gcm.QuickstartPreferences;
import com.nickteck.cus_prawnandcrab.gcm.RegistrationIntentService;

public class MenuNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    TextView txtHomeToolBar;
    FrameLayout layBadge;
    ImageView imgLogOut;
    Database database ;
    LinearLayout ldtSpinner;
    String TAG = MenuNavigationActivity.class.getName();

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;
    private boolean isReceiverRegistered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ContentFragment contentFragment = new ContentFragment();
        AdditionalClass.replaceFragment(contentFragment, Constants.CONTENTFRAGMENT,MenuNavigationActivity.this);

        txtHomeToolBar = (TextView) findViewById(R.id.txtHomeToolBar);
        txtHomeToolBar.setText("Check");
        layBadge = (FrameLayout) findViewById(R.id.layBadge);
        ldtSpinner = (LinearLayout) findViewById(R.id.ldtSpinner);
        ldtSpinner.setVisibility(View.VISIBLE);
        layBadge.setOnClickListener(this);

        imgLogOut = (ImageView) findViewById(R.id.imgLogOut);
        imgLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
                settings.edit().clear().commit();
                database = new Database(getApplicationContext());
                database.deleteAll();

            }
        });


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {

                } else {

            }
            }
        };


        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            OrderTakenScreenFragment catagoryFragment = new OrderTakenScreenFragment();
            AdditionalClass.replaceFragment(catagoryFragment,Constants.ORDER_TAKEN_FRAGMENT,MenuNavigationActivity.this);

        } else if (id == R.id.nav_gallery) {
            FavouriteFragment favouriteFragment=new FavouriteFragment();
            AdditionalClass.replaceFragment(favouriteFragment,Constants.FAVOURITE_FRAGMENT,MenuNavigationActivity.this);


        } else if (id == R.id.nav_my_orders) {
            OrderFragment orderFragment = new OrderFragment();
            AdditionalClass.replaceFragment(orderFragment,Constants.ORDER_FRAGMENT,MenuNavigationActivity.this);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_offers) {
            OffersFragment orderFragment = new OffersFragment();
            AdditionalClass.replaceFragment(orderFragment,Constants.OFFERS_FRAGMENT,MenuNavigationActivity.this);
        } else if (id == R.id.nav_history) {
            HistoryFragment myOrdersFragment = new HistoryFragment();
            AdditionalClass.replaceFragment(myOrdersFragment,Constants.HISTORY_FRAGMENT,MenuNavigationActivity.this);
        }else if (id == R.id.nav_my_location)
        {
            MyLocationFragment myLocationFragment = new MyLocationFragment();
            AdditionalClass.replaceFragment(myLocationFragment,Constants.MY_LOCATION_FRAGMENT,MenuNavigationActivity.this);
        } else if (id == R.id.nav_testimony) {
            TestimonyFragment testimonyFragment = new TestimonyFragment();
            AdditionalClass.replaceFragment(testimonyFragment,Constants.TESTIMONY_FRAGMENT,MenuNavigationActivity.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.layBadge:
                MyOrdersFragment myOrdersFragment = new MyOrdersFragment();
                AdditionalClass.replaceFragment(myOrdersFragment,Constants.MY_ORDERS_FRAGMENT,MenuNavigationActivity.this);
                break;
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TestimonyFragment testimonyFragment = new TestimonyFragment();
        testimonyFragment.onActivityResult(requestCode,resultCode,data);
    }
}
