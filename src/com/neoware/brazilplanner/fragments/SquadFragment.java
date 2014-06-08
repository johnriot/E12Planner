package com.neoware.brazilplanner.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.neoware.brazilplanner.Player;
import com.neoware.brazilplanner.Player.PlayerPositionException;
import com.neoware.brazilplanner.R;
import com.neoware.brazilplanner.Squad;
import com.neoware.brazilplanner.SquadsDefinition;

public class SquadFragment extends SherlockFragment {

    private static final String SQUAD_ID = "squadid";
    private int mSquadId;

    public SquadFragment() {
        mSquadId = 0;
    }

    public SquadFragment(int stageId) {
        mSquadId = stageId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);

        if(savedInstanceState != null) {
            mSquadId = savedInstanceState.getInt(SQUAD_ID);
        }

        String packageName = getSherlockActivity().getPackageName();

        View layout = inflater.inflate(R.layout.squad_list_layout, null);

        TableLayout squadTable = (TableLayout)layout.findViewById(R.id.squadTableLayout);

        // for every player in the squad
        Squad squad = SquadsDefinition.getSquadsDefnInstance(
                getSherlockActivity()).getSquad(mSquadId);
        for(int i = 0; i < squad.getSize(); ++i) {

            TableRow playerRow = (TableRow)inflater.inflate(
                    R.layout.sqaud_table_row, null);

            try {

                Player player = squad.getPlayer(i);

                ImageView iconViewV = (ImageView)playerRow.findViewById(
                        R.id.playerIcon);
                String playerIcon = player.getPositionFilename();
                iconViewV.setImageResource((getResources().getIdentifier(playerIcon,
                        "drawable", packageName)));

                TextView playerNumberV = (TextView)playerRow.findViewById(
                        R.id.playerNumber);
                playerNumberV.setText(player.getNumber());

                TextView playerNameV = (TextView)playerRow.findViewById(
                        R.id.playerName);
                playerNameV.setText(player.getName());

                TextView playerAgeV = (TextView)playerRow.findViewById(
                        R.id.playerAge);
                playerAgeV.setText(player.getAge().toString());

                squadTable.addView(playerRow);

            } catch (PlayerPositionException ex) {
                HandleException(ex);
            }
        }

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        savedState.putInt(SQUAD_ID, mSquadId);
    }

    private void HandleException(PlayerPositionException ex) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getSherlockActivity());
        alertDialogBuilder.setTitle(R.string.errorHeader);
        alertDialogBuilder
            .setMessage(R.string.server_error_body)
            .setCancelable(true)
            .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog,int id) {
                    dialog.cancel();
                }
              });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}


