package com.neoware.europlanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class StageFragment extends SherlockFragment {

    @Override
    public View getView() {

        return super.getView();
    }

    private final int mStageId;

    public StageFragment() {
        mStageId = 0;
    }

    public StageFragment(int stageId) {
        mStageId = stageId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TournamentStage stage = TournamentDefinition.getTournamentDefnInstance(
                getActivity()).getStage(mStageId);

        return stage.drawView(inflater, container, savedInstanceState, this.getResources(), getActivity());
    }

}
