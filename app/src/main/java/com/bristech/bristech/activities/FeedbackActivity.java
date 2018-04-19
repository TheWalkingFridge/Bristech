package com.bristech.bristech.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bristech.bristech.R;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Toolbar toolbar = findViewById(R.id.feedback_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView textView = findViewById(R.id.txt_poll_link);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "Would you mind providing us some feedback through this <a href='https://docs.google.com/forms/d/e/1FAIpQLScleeUHXIv1y4sz7sud0MaY_NUJ7OUPT81zRc3Y_s0xYFyQzg/viewform?usp=sf_link'>Google Poll</a> link, in order for us to improve future events?";
        textView.setText(Html.fromHtml(text));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void submitBtnPress(View view) {
        Log.i("FeedbackActivity", "Submit button pressed");
        new AlertDialog.Builder(this)
                .setTitle("Notice")
                .setMessage("Thank you for the feedback")
                .setPositiveButton("GO BACK", null)
                .show();
        TextView textView = findViewById(R.id.txt_poll_link);
        textView.setText(R.string.thank_for_feedback);
    }

    public void returnToHomepageBtnPress(View view) {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }


}
