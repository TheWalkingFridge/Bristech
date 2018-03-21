package com.bristech.bristech.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bristech.bristech.R;
import com.bristech.bristech.activities.EventDetailActivity;
import com.bristech.bristech.adaptors.EventsAdaptor;
import com.bristech.bristech.entities.Event;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment implements EventsAdaptor.EventOnClick {

    private static final String EVENT = "events";

    private List<Event> mEvents;
    private EventsAdaptor mEventsAdaptor;

    //Views
    private RecyclerView mRecyclerView;

    public EventsFragment() {
    }

    public static EventsFragment getInstance(List<Event> events){
        EventsFragment fragment = new EventsFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(EVENT, (ArrayList<Event>) events);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEvents = (List<Event>) getArguments().getSerializable(EVENT);
        mEventsAdaptor = new EventsAdaptor(mEvents, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_events, container, false);

        //Setting up the events recycler view
        mRecyclerView = root.findViewById(R.id.rv_events);
        mRecyclerView.setAdapter(mEventsAdaptor);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    public void setEvents(List<Event> events){
        this.mEvents = events;
        updateAdaptor(events);
    }

    private void updateAdaptor(List<Event> events){
        if(events != null)
            mEventsAdaptor.updateEvents(events);
    }

    @Override
    public void onClick() {
        // create new intent
        Intent eventDetailActivityIntent = new Intent(getContext(), EventDetailActivity.class);

        // send details of event to individual view
        Bundle mBundle = new Bundle();
        mBundle.putString("event_title", "testTitle");
        mBundle.putString("event_topic", "testTopic");
        mBundle.putString("event_date", "testDate");
        mBundle.putString("event_time", "testTime");
        mBundle.putString("event_duration", "testDuration");
        mBundle.putString("event_location", "testLocation");
        mBundle.putString("event_description", "Lorem ipsum dolor sit amet, tollit noster nam ut. Sea id eruditi platonem, summo everti in eam. Inermis tibique perfecto ea vim, choro phaedrum in his. Eam alia omittam forensibus ut, nisl purto inciderint in pri. Cum diam alia nominavi no, te nisl illum sea.");
        eventDetailActivityIntent.putExtras(mBundle);

        // start the activity
        startActivity(eventDetailActivityIntent);
    }
}
