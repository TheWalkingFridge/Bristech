package com.bristech.bristech.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bristech.bristech.R;
import com.bristech.bristech.entities.Event;
import com.bristech.bristech.entities.User;
import com.bristech.bristech.utils.UserUtils;

import java.util.List;

import static com.bristech.bristech.fragments.EventsFragment.EVENT;
import static com.bristech.bristech.utils.UserUtils.attendEvent;
import static com.bristech.bristech.utils.UserUtils.user;

public class EventDetailActivity extends AppCompatActivity {
    public static final String TAG = "EventDetails";

    Event mEvent;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        mEvent = (Event) getIntent().getSerializableExtra(EVENT);
        setText(mEvent.getName(), R.id.event_title);
        setText(mEvent.getDateStr(), R.id.event_date);
        setText(mEvent.getTimeStr(), R.id.event_time);
//        setText(mEvent.getLocation(), R.id.event_location);

        FrameLayout myFrame = findViewById(R.id.event_colour_frame);
        myFrame.setBackgroundColor(mEvent.getTileColour());

        TextView textView = findViewById(R.id.event_description);
        textView.setText(mEvent.getDescriptionHtml());

        Toolbar toolbar = findViewById(R.id.event_toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final Button registerButton = findViewById(R.id.btn_register_for_event);

        if(mEvent != null && mEvent.getStatus().equals("past")) {
            registerButton.setVisibility(View.GONE);
        }

        if(User.currentUser == null || User.currentUser.getEmail() == null) {
            registerButton.setVisibility(View.GONE);
        }

//        else {
//            if(User.currentUser.getEvents().contains(mEvent.getId())) {

                UserUtils.registerEvent(mEvent.getId(), User.currentUser.getEmail(), new UserUtils.UserCallback<Boolean>() {
                    @Override
                    public void onComplete(Boolean object) {
                        Log.i(TAG, "before click button");

                        if (object == true){
                            registerButton.setBackgroundResource(R.drawable.clr_pressed);
                        }
                        else{
                            registerButton.setBackgroundResource(R.drawable.clr_normal);
                        }
                    }
                });
//            }
//        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerForTalkBtnPress(v);
            }
        });
    }

    private void setText(String key, int textViewID) {
        TextView textView = findViewById(textViewID);
        textView.setText(key);
    }

    public void registerForTalkBtnPress(View view) {
        register();
    }

    void register(){
//        Log.i(TAG, "lkfgghfgkhjcghghb");
        attendEvent(mEvent.getId(), User.currentUser.getEmail(), new UserUtils.UserCallback<Boolean>() {
            @Override
            public void onComplete(final Boolean isGoing) {
                UserUtils.getUser(new UserUtils.UserCallback<User>() {
                    @Override
                    public void onComplete(User uobject) {
                        User.currentUser = uobject;
                        if( isGoing ) {
                            Snackbar.make(findViewById(R.id.activity_event_coordinator),
                                    "Registered"
                                    , Snackbar.LENGTH_LONG).show();

                            // USER IS GOING
                            isGoing();
                            Log.i(TAG, "is going");
                        }
                        else {
                            Snackbar.make(findViewById(R.id.activity_event_coordinator),
                                    "Unregistered"
                                    , Snackbar.LENGTH_LONG).show();

                            // USER IS NOT GOING
                            isNotGoing();
                            Log.i(TAG, "not going");
                        }
                    }
                });
            }
        });
    }

    // user is going to the event(register) indicating with color grey
    private void isGoing(){
        UserUtils.registerEvent(mEvent.getId(),User.currentUser.getEmail(), new UserUtils.UserCallback<Boolean>(){
            @Override
            public void onComplete(final Boolean isUserRegistered){
                UserUtils.getUser(new UserUtils.UserCallback<User>() {
                    @Override
                    public void onComplete(User object) {
                        User.currentUser = object;
                        final Button registerButton = findViewById(R.id.btn_register_for_event);
                        registerButton.setBackgroundResource(R.drawable.clr_pressed);
                    }
                });
            }
        });
    }

    // user is not going to the event(unregister) indicating with color red
    private void isNotGoing(){
        UserUtils.registerEvent(mEvent.getId(),User.currentUser.getEmail(), new UserUtils.UserCallback<Boolean>(){
            @Override
            public void onComplete(final Boolean isUserRegistered){
                UserUtils.getUser(new UserUtils.UserCallback<User>() {
                    @Override
                    public void onComplete(User object) {
                        User.currentUser = object;
                        final Button registerButton = findViewById(R.id.btn_register_for_event);
                        registerButton.setBackgroundResource(R.drawable.clr_normal);
                    }
                });
            }
        });
    }

}