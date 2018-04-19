package com.bristech.bristech.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import com.bristech.bristech.R;
import com.bristech.bristech.entities.Event;
import com.bristech.bristech.fragments.AddEventFragment;
import com.bristech.bristech.fragments.EventsFragment;
import com.bristech.bristech.services.Geofencing;
import com.bristech.bristech.utils.EventUtils;
import com.bristech.bristech.utils.LoginUtils;
import com.bristech.bristech.utils.UserUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;


    private List<Event> mEventList;
    private FragmentManager mFragmentManager;
    private Geofencing mGeofencing;


    // TODO Use onSaveInstanceState to save the instance on device rotaion,
    // TODO so that it doesn't re-download the events again when device is rotated
    // TODO save the user in SharedPreferences and use it through there in other activities
    // TODO get the events once, not every time you change screens/rotate device
    // TODO fix options screen so it's not an activity but a fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // set up the sidebar
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Check if logged in
        boolean isLoggedIn = LoginUtils.isLoggedIn();
        if (!isLoggedIn) {
            Intent startLogin = new Intent(this, LoginActivity.class);
            startActivity(startLogin);
        } else {
            Toast.makeText(this, "You are logged in", Toast.LENGTH_LONG).show();
            UserUtils.getCurrentUser();
        }

        // Check if app has permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        } else{
            mGeofencing = new Geofencing(this);
            mGeofencing.registerGeofence();
        }

        // set up navigation functionality of the sidebar
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Gets events and set fragment manager
        mFragmentManager = getSupportFragmentManager();

        // sets upcoming events as default
        showUpcomingEvents();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_upcoming_events:
                showUpcomingEvents();
                break;
            case R.id.nav_past_events:
                showPastEvents();
                break;
            case R.id.nav_settings:
                showSettings();
                break;

            case R.id.nav_volunteer_a_speaker:
                showVolunteerSpeaker();
                break;
            case R.id.nav_attend_event:
                showAttendEvent();
                break;
            case R.id.nav_add_event:
                showAddEvent();
                break;
            default:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showUpcomingEvents() {
        EventsFragment eventsFragment = EventsFragment.getInstance();
        mFragmentManager.beginTransaction().replace(R.id.fragment_container, eventsFragment).commit();
        EventUtils.getUpcomingEvents(eventsFragment);
    }

    private void showPastEvents() {
        EventsFragment eventsFragment = EventsFragment.getInstance();
        mFragmentManager.beginTransaction().replace(R.id.fragment_container, eventsFragment).commit();
        EventUtils.getPastEvents(eventsFragment);
    }

    private void showVolunteerSpeaker() {
        Intent volunteerSpeakerActivityIntent = new Intent(this, VolunteerSpeakerActivity.class);
        startActivity(volunteerSpeakerActivityIntent);
    }

    private void showSettings() {
        Intent settingsActivityIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsActivityIntent);
    }

    private void showAttendEvent() {
        Intent feedbackActivityEvent = new Intent(this, FeedbackActivity.class);
        startActivity(feedbackActivityEvent);
    }

    private void showAddEvent() {
        AddEventFragment addEventFragment = AddEventFragment.getInstance();
        mFragmentManager.beginTransaction().replace(R.id.fragment_container, addEventFragment).commit();
    }

    private void getEvents(EventsFragment eventsFragment) {
        EventUtils.getAllEvents(eventsFragment);
    }

    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_FINE_LOCATION);
    }

}
