package com.neoware.europlanner.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.neoware.europlanner.GetRemoteDrawable;
import com.neoware.europlanner.GetRemoteDrawable.DrawableRetrievedCallback;
import com.neoware.europlanner.R;
import com.neoware.europlanner.Squad;
import com.neoware.europlanner.SquadsDefinition;

public class StarPlayerFragment extends SherlockFragment {

    private static final String SQUAD_ID = "squadid";
    private int mSquadId;

    public StarPlayerFragment() {
        mSquadId = 0;
    }

    public StarPlayerFragment(int squadId) {
        mSquadId = squadId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);

        if(savedInstanceState != null) {
            mSquadId = savedInstanceState.getInt(SQUAD_ID);
        }

        SquadsDefinition defn = SquadsDefinition.getSquadsDefnInstance(
                getSherlockActivity());
        Squad squad = defn.getSquad(mSquadId);

        View layout = inflater.inflate(R.layout.squad_star_player, null);

        TextView starPlayerTv = (TextView)layout.findViewById(R.id.startPlayerTv);
        starPlayerTv.setText(squad.getStarPlayerWriteUp(getSherlockActivity()));

        String url = squad.getStarPlayerUrl(getSherlockActivity());
        if(url != null) {

            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);

            final ImageView starPlayerView = (ImageView)layout.findViewById(R.id.starPlayerIv);
            new GetRemoteDrawable(new DrawableRetrievedCallback() {

                @Override
                public void drawableRetrieved(Drawable result) {

                    if(result != null) {
                        starPlayerView.setImageDrawable(result);
                    }

                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                }
            }).execute(url);
        }

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        savedState.putInt(SQUAD_ID, mSquadId);
    }
}
