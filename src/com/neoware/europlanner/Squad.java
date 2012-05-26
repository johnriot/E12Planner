package com.neoware.europlanner;

import java.util.ArrayList;

public class Squad {

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

}
