package com.bristech.bristech.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.CollapsingToolbarLayout;

import com.bristech.bristech.R;
import com.bristech.bristech.entities.Event;

import static com.bristech.bristech.fragments.EventsFragment.EVENT;

public class EventDetailActivity extends AppCompatActivity {
    public static final String TAG = "EventDetails";

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Event event = (Event) getIntent().getSerializableExtra(EVENT);
        setText(event.getName(), R.id.event_title);
        setText(event.getDateStr(), R.id.event_date);
        setText(event.getTimeStr(), R.id.event_time);
        setText(event.getLocation(), R.id.event_location);

        TextView textView = findViewById(R.id.event_description);
        textView.setText(event.getDescriptionHtml());

        ImageView testImage = findViewById(R.id.event_image);
        testImage.setImageResource(R.drawable.test_event_image_2);

        Toolbar toolbar = findViewById(R.id.event_toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setText(String key, int textViewID) {
        TextView textView = findViewById(textViewID);
        textView.setText(key);
    }

    public void registerForTalkBtnPress(View view) {
        Log.i("SettingsActivity", "Register for talk button pressed");
        new AlertDialog.Builder(this)
                .setTitle("Notice")
                .setMessage("You are successfully registered for this event.")
                .setPositiveButton("OK", null)
                .show();
    }
}
