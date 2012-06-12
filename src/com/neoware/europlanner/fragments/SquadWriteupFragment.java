package com.neoware.europlanner.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.neoware.europlanner.R;
import com.neoware.europlanner.Squad;
import com.neoware.europlanner.SquadsDefinition;

public class SquadWriteupFragment extends SherlockFragment {

    private static final String SQUAD_ID = "squadid";
    private int mSquadId;

    public SquadWriteupFragment() {
        mSquadId = 0;
    }

    public SquadWriteupFragment(int squadId) {
        mSquadId = squadId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(savedInstanceState != null) {
            mSquadId = savedInstanceState.getInt(SQUAD_ID);
        }

        SquadsDefinition defn = SquadsDefinition.getSquadsDefnInstance(getSherlockActivity());
        Squad squad = defn.getSquad(mSquadId);

        View layout = inflater.inflate(R.layout.squad_write_up, null);

        TextView writeUpTv = (TextView)layout.findViewById(R.id.writeUpTV);
        writeUpTv.setText(squad.getWriteUp(getSherlockActivity()));

        TextView predictionTv = (TextView)layout.findViewById(R.id.predictionTv);

        String prediction = getSherlockActivity().getResources().getString(
                R.string.prediction);
        String fullPrediction = prediction + " " + squad.getPredictionString(
                        getSherlockActivity());

        SpannableString spannablePred = new SpannableString(fullPrediction);
        spannablePred.setSpan(new StyleSpan(Typeface.BOLD), 0, prediction.length(), 0);
        predictionTv.setText(spannablePred);

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        savedState.putInt(SQUAD_ID, mSquadId);
    }
}
