package com.neoware.brazilplanner;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;

public class Squad {

    private static final String WRITE_UP_RES_PREFIX = "writeup";
    private static final String STAR_PLAYER_RES_PREFIX = "starman";
    private static final String PREDICTION_RES_PREFIX = "prediction";
    private static final String STAR_PLAYER_NAME_RES_PREFIX = "starmanname";

    private final int mId;
    private final ArrayList<Player> mPlayers = new ArrayList<Player>();

    public Squad(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public int getSize() {
        return mPlayers.size();
    }

    public void addPlayer(Player player) {
        mPlayers.add(player);
    }

    public Player getPlayer(int id) {
        return mPlayers.get(id);
    }

    public String getWriteUp(Context context) {

        String writeUpIdStr = WRITE_UP_RES_PREFIX + mId;

        int id = context.getResources().getIdentifier(writeUpIdStr,
                "string", context.getPackageName());

        try {
            return context.getResources().getString(id);
        }
        catch(Resources.NotFoundException ex) {
            return context.getResources().getString(R.string.writeupNotFound);
        }
    }

    public String getStarPlayerWriteUp(Context context) {

        String starPlayerStr = STAR_PLAYER_RES_PREFIX + mId;

        int id = context.getResources().getIdentifier(starPlayerStr ,
                "string", context.getPackageName());

        try {
            return context.getResources().getString(id);
        }
        catch(Resources.NotFoundException ex) {
            return context.getResources().getString(R.string.starPlayerWriteupNotFound);
        }
    }

    public String getStarPlayerUrl(Context context) {

        String [] urls = context.getResources().getStringArray(R.array.starPlayersUrls);

        if(mId < urls.length) {
            return urls[mId];
        }

        return null;
    }

    public String getPredictionString(Context context) {

        String predictionStrId = PREDICTION_RES_PREFIX + mId;

        int id = context.getResources().getIdentifier(predictionStrId ,
                "string", context.getPackageName());

        try {
            return context.getResources().getString(id);
        }
        catch(Resources.NotFoundException ex) {
            return context.getResources().getString(R.string.predictionNotFound);
        }
    }

    public String getStarPlayerName(Context context) {

        String predictionStrId = STAR_PLAYER_NAME_RES_PREFIX + mId;

        int id = context.getResources().getIdentifier(predictionStrId ,
                "string", context.getPackageName());

        try {
            return context.getResources().getString(id);
        }
        catch(Resources.NotFoundException ex) {
            return context.getResources().getString(R.string.nameNotFound);
        }
    }

}
