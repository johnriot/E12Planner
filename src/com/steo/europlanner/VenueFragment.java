package com.steo.europlanner;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        
        String venueNamesShort[] = res.getStringArray(R.array.venues_short);
        for(String venueName: venueNamesShort)
        	venueName = venueName.toLowerCase();
        String imageViewIdStr = "venueImage";
        int imageViewId = res.getIdentifier(imageViewIdStr, "id", packageName);
        ImageView venueImageView = (ImageView)fragView.findViewById(imageViewId);
        venueImageView.setImageResource(res.getIdentifier(venueNamesShort[mVenue.getVenueId()],
                "drawable", packageName));

        String venueNamesLong[] = res.getStringArray(R.array.venues);
        String venueNameViewIdStr = "venueName";
        int venueNameViewId = res.getIdentifier(venueNameViewIdStr, "id", packageName);
        TextView venueNameView = (TextView)fragView.findViewById(venueNameViewId);
        venueNameView.setText(venueNamesLong[mVenue.getVenueId()]);
        
        String venueCapacities[] = res.getStringArray(R.array.venue_capacities);
        String venueCapacityViewIdStr = "venueCapacity";
        int venueCapacityViewId = res.getIdentifier(venueCapacityViewIdStr, "id", packageName);
        TextView venueCapacityView = (TextView)fragView.findViewById(venueCapacityViewId);
        venueCapacityView.setText(venueCapacities[mVenue.getVenueId()]);
        
        String venueGamesDescription[] = res.getStringArray(R.array.venue_games_description);
        String venueGamesDescriptionViewIdStr = "venueGamesDescription";
        int venueGamesDescriptionViewId = res.getIdentifier(venueGamesDescriptionViewIdStr, "id", packageName);
        TextView venueGamesDescriptionView = (TextView)fragView.findViewById(venueGamesDescriptionViewId);
        venueGamesDescriptionView.setText(venueGamesDescription[mVenue.getVenueId()]);

        return fragView;
    }
}
