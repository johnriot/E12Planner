package com.steo.europlanner;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class VenueFragment extends SherlockFragment {

    private final Venue mVenue;

    public VenueFragment(Venue venue) {
        mVenue = venue;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Resources res = this.getResources();

        View fragView = inflater.inflate(R.layout.venue_fragment, container, false);

        String packageName = getClass().getPackage().getName();

        String venueNames[] = res.getStringArray(R.array.venues);
        
        String venueNameViewIdStr = "venue0Venue";
        int venueNameViewId = res.getIdentifier(venueNameViewIdStr, "id", packageName);
        TextView venueNameView = (TextView)fragView.findViewById(venueNameViewId);
        venueNameView.setText(venueNames[mVenue.getVenueId()]);
        

        LinearLayout venueFragmentLayout = (LinearLayout)fragView.findViewById(
                R.id.venueFragmentLayout); 

        return fragView;
    }
}
