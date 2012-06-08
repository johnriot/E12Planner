package com.neoware.europlanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class StageFragment extends SherlockFragment {

    private static final String STAGE_ID = "stageid";
    private int mStageId;

    public StageFragment() {
        mStageId = 0;
    }

    public StageFragment(int stageId) {
        mStageId = stageId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(savedInstanceState != null) {
            mStageId = savedInstanceState.getInt(STAGE_ID);
        }

        TournamentStage stage = TournamentDefinition.getTournamentDefnInstance(
                getActivity()).getStage(mStageId);

        return stage.drawView(inflater, container, savedInstanceState, this.getResources(), getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        savedState.putInt(STAGE_ID, mStageId);
    }
}
