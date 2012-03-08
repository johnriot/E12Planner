package com.steo.europlanner;

import android.content.Context;

public class Team {

    public TeamID teamId;
    public String teamName;
    public int teamIconResId;

    public Team(TeamID tid, String name, int teamIcon) {
        teamId = tid;
        teamName = name;
        teamIconResId = teamIcon;
    }

    public enum TeamID {

        //Group A Teams
        POLAND {
            @Override
            public Team getTeam(Context context) {
                return new Team(this, context.getResources().getString(R.string.polandName),
                        R.drawable.poland);
            }
        },
        GREECE {
            @Override
            public Team getTeam(Context context) {
                return new Team(this, context.getResources().getString(R.string.greeceName),
                        R.drawable.greece);
            }

        },
        RUSSIA {
            @Override
            public Team getTeam(Context context) {
                return new Team(this, context.getResources().getString(R.string.russiaName),
                        R.drawable.russia);
            }

        },
        CZECH_REPUBLIC {
            @Override
            public Team getTeam(Context context) {
                return new Team(this, context.getResources().getString(R.string.czechRepName),
                        R.drawable.czechrep);
            }
        },

        //Group B Teams
        NETHERLANDS {

            @Override
            public Team getTeam(Context context) {
                return new Team(this, context.getResources().getString(R.string.netherlandsName),
                        R.drawable.netherlands);
            }

        },
        DENMARK {
            @Override
            public Team getTeam(Context context) {
                return new Team(this, context.getResources().getString(R.string.denmarkName),
                        R.drawable.denmark);
            }

        },
        GERMANY {
            @Override
            public Team getTeam(Context context) {
                return new Team(this, context.getResources().getString(R.string.germanyName),
                        R.drawable.germany);
            }

        },
        PORTUGAL {
            @Override
            public Team getTeam(Context context) {
                return new Team(this, context.getResources().getString(R.string.portugalName),
                        R.drawable.portugal);
            }
        },

        //Group C Teams
        SPAIN {
            @Override
            public Team getTeam(Context context) {
                return new Team(this, context.getResources().getString(R.string.spainName),
                        R.drawable.spain);
            }
        },
        ITALY {
            @Override
            public Team getTeam(Context context) {
                return new Team(this, context.getResources().getString(R.string.italyName),
                        R.drawable.italy);
            }
        },
        REPUBLIC_OF_IRELAND {
            @Override
            public Team getTeam(Context context) {
                return new Team(this, context.getResources().getString(R.string.irelandName),
                        R.drawable.ireland);
            }
        },
        CROATIA {
            @Override
            public Team getTeam(Context context) {
                return new Team(this, context.getResources().getString(R.string.croatiaName),
                        R.drawable.croatia);
            }
        },

        //Group D Teams
        UKRAINE {
            @Override
            public Team getTeam(Context context) {
                return new Team(this, context.getResources().getString(R.string.ukraineName),
                        R.drawable.ukraine);
            }
        },
        SWEDEN {
            @Override
            public Team getTeam(Context context) {
                return new Team(this, context.getResources().getString(R.string.swedenName),
                        R.drawable.sweden);
            }

        },
        FRANCE {
            @Override
            public Team getTeam(Context context) {
                return new Team(this, context.getResources().getString(R.string.franceName),
                        R.drawable.france);
            }
        },
        ENGLAND {
            @Override
            public Team getTeam(Context context) {
                return new Team(this, context.getResources().getString(R.string.englandName),
                        R.drawable.england);
            }
        };

        public abstract Team getTeam(Context context);
    }
}
