package com.neoware.europlanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class GroupFragment extends SherlockFragment {

    private final TournamentStage mStage;

    public GroupFragment(TournamentStage stage) {
        mStage = stage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return mStage.drawView(inflater, container, savedInstanceState, this.getResources());
    }

}
