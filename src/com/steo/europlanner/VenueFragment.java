package com.steo.europlanner;

import java.util.Iterator;
import java.util.Set;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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
        
        String groupString[] = res.getStringArray(R.array.groups);
        String knockoutString[] = res.getStringArray(R.array.knockout);
        String groupKnockoutString[] = new String[groupString.length + knockoutString.length];
        System.arraycopy(groupString, 0, groupKnockoutString, 0, groupString.length);
        System.arraycopy(knockoutString, 0, groupKnockoutString, groupString.length, knockoutString.length);
        Set<Integer> venueGroupKnockoutId = mVenue.getGroupKnockoutIds();
        Iterator<Integer> it = venueGroupKnockoutId.iterator();
        int ii = 0;
        while(it.hasNext())	{
        	String viewIdStr = ("venueGamesDescription" + ii++);
        	int viewId = res.getIdentifier(viewIdStr, "id", packageName);
            TextView tView = (TextView)fragView.findViewById(viewId);
            int groupknockoutId = it.next();
            // Underline text and display
            SpannableString content = new SpannableString(groupKnockoutString[groupknockoutId]);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            tView.setText(content);
            final int groupKockoutPosition = groupknockoutId;
            tView.setOnClickListener(new View.OnClickListener() {
            	@Override
				public void onClick(View v) {
					Intent gamesIntent = new Intent(VenueFragment.this.getActivity(), GamesActivity.class);
					gamesIntent.putExtra("groupKnockout", groupKockoutPosition);
					startActivity(gamesIntent);
				}
	        });
        }

        return fragView;
    }
}
