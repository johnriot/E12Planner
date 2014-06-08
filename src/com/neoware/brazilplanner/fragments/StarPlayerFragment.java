package com.neoware.brazilplanner.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.neoware.brazilplanner.GetRemoteDrawable;
import com.neoware.brazilplanner.GetRemoteDrawable.DrawableRetrievedCallback;
import com.neoware.brazilplanner.R;
import com.neoware.brazilplanner.Squad;
import com.neoware.brazilplanner.SquadsDefinition;

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

        TextView starPlayerNameTv = (TextView)layout.findViewById(R.id.starPlayerName);
        starPlayerNameTv.setText(squad.getStarPlayerName(getSherlockActivity()));

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
