package com.steo.europlanner;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.support.v4.app.Fragment;

public class Group {

    private final GroupState mGroupState;
    private GroupFragment mViewFragment;

    //TODO: Get from Strings.xml
    private static final String WARSAW = "National Stadium Warsaw (POL)";
    private static final String WROCLAW = "Municipal Stadium Wroclaw (POL)";
    private static final String KHARKIV = "Metalist Stadium Kharkiv (UKR)";
    private static final String LVIV = "Arena Lviv (UKR)";

    private static final String GDANSK = "Arena Gdansk (POL)";
    private static final String POZNAN = "Municipal Stadium Poznan (POL)";
    private static final String DONBASS = "Donbass Arena (UKR)";
    private static final String KYIV = "Olympic Stadium Kyiv (UKR)";

    public Group(GroupState state) {
        mGroupState = state;
    }

    public Fragment getView(Context ctx) {

        if(mViewFragment == null) {
            mViewFragment = new GroupFragment(mGroupState.getName(ctx),
                    mGroupState.getTeams(ctx), mGroupState.getFixtures());
        }

        return mViewFragment;
    }

    public enum GroupState {

        GROUP_A {

            @Override
            public String getName(Context context) {
                return context.getResources().getString(R.string.group_a_name);
            }

            @Override
            public Team[] getTeams(Context context) {

                Team[] teams = new Team[GROUP_SIZE];

                teams[0] = Team.TeamID.POLAND.getTeam(context);
                teams[1] = Team.TeamID.GREECE.getTeam(context);
                teams[2] = Team.TeamID.RUSSIA.getTeam(context);
                teams[3] = Team.TeamID.CZECH_REPUBLIC.getTeam(context);

                return teams;
            }

            @Override
            public ArrayList<Fixture> getFixtures() {

                ArrayList<Fixture> fixtures = new ArrayList<Fixture>();

                fixtures.add(new Fixture(Team.TeamID.POLAND, Team.TeamID.GREECE,
                        WARSAW, new Date()));
                fixtures.add(new Fixture(Team.TeamID.RUSSIA, Team.TeamID.CZECH_REPUBLIC,
                        WROCLAW, new Date()));
                fixtures.add(new Fixture(Team.TeamID.GREECE, Team.TeamID.CZECH_REPUBLIC,
                        WROCLAW, new Date()));
                fixtures.add(new Fixture(Team.TeamID.POLAND, Team.TeamID.RUSSIA,
                        WARSAW, new Date()));
                fixtures.add(new Fixture(Team.TeamID.GREECE, Team.TeamID.RUSSIA,
                        WARSAW, new Date()));
                fixtures.add(new Fixture(Team.TeamID.CZECH_REPUBLIC, Team.TeamID.POLAND,
                        WROCLAW, new Date()));

                return fixtures;
            }
        },
        GROUP_B {
            @Override
            public String getName(Context context) {
                return context.getResources().getString(R.string.group_b_name);
            }

            @Override
            public Team[] getTeams(Context context) {

                Team[] teams = new Team[GROUP_SIZE];

                teams[0] = Team.TeamID.NETHERLANDS.getTeam(context);
                teams[1] = Team.TeamID.DENMARK.getTeam(context);
                teams[2] = Team.TeamID.GERMANY.getTeam(context);
                teams[3] = Team.TeamID.PORTUGAL.getTeam(context);

                return teams;
            }

            @Override
            public ArrayList<Fixture> getFixtures() {
                ArrayList<Fixture> fixtures = new ArrayList<Fixture>();

                fixtures.add(new Fixture(Team.TeamID.NETHERLANDS, Team.TeamID.DENMARK,
                        KHARKIV, new Date()));
                fixtures.add(new Fixture(Team.TeamID.GERMANY, Team.TeamID.PORTUGAL,
                        LVIV, new Date()));
                fixtures.add(new Fixture(Team.TeamID.DENMARK, Team.TeamID.PORTUGAL,
                        LVIV, new Date()));
                fixtures.add(new Fixture(Team.TeamID.NETHERLANDS, Team.TeamID.GERMANY,
                        KHARKIV, new Date()));
                fixtures.add(new Fixture(Team.TeamID.PORTUGAL, Team.TeamID.NETHERLANDS,
                        KHARKIV, new Date()));
                fixtures.add(new Fixture(Team.TeamID.DENMARK, Team.TeamID.GERMANY,
                        LVIV, new Date()));

                return fixtures;
            }
        },
        GROUP_C {
            @Override
            public String getName(Context context) {
                return context.getResources().getString(R.string.group_c_name);
            }

            @Override
            public Team[] getTeams(Context context) {

                Team[] teams = new Team[GROUP_SIZE];

                teams[0] = Team.TeamID.SPAIN.getTeam(context);
                teams[1] = Team.TeamID.ITALY.getTeam(context);
                teams[2] = Team.TeamID.REPUBLIC_OF_IRELAND.getTeam(context);
                teams[3] = Team.TeamID.CROATIA.getTeam(context);

                return teams;
            }

            @Override
            public ArrayList<Fixture> getFixtures() {
                ArrayList<Fixture> fixtures = new ArrayList<Fixture>();

                fixtures.add(new Fixture(Team.TeamID.SPAIN, Team.TeamID.ITALY,
                        GDANSK, new Date()));
                fixtures.add(new Fixture(Team.TeamID.REPUBLIC_OF_IRELAND, Team.TeamID.CROATIA,
                        POZNAN, new Date()));
                fixtures.add(new Fixture(Team.TeamID.ITALY, Team.TeamID.CROATIA,
                        POZNAN, new Date()));
                fixtures.add(new Fixture(Team.TeamID.SPAIN, Team.TeamID.REPUBLIC_OF_IRELAND,
                        GDANSK, new Date()));
                fixtures.add(new Fixture(Team.TeamID.CROATIA, Team.TeamID.SPAIN,
                        GDANSK, new Date()));
                fixtures.add(new Fixture(Team.TeamID.ITALY, Team.TeamID.REPUBLIC_OF_IRELAND,
                        POZNAN, new Date()));


                return fixtures;
            }
        },
        GROUP_D {
            @Override
            public String getName(Context context) {
                return context.getResources().getString(R.string.group_d_name);
            }

            @Override
            public Team[] getTeams(Context context) {
                Team[] teams = new Team[GROUP_SIZE];

                teams[0] = Team.TeamID.UKRAINE.getTeam(context);
                teams[1] = Team.TeamID.SWEDEN.getTeam(context);
                teams[2] = Team.TeamID.FRANCE.getTeam(context);
                teams[3] = Team.TeamID.ENGLAND.getTeam(context);

                return teams;
            }

            @Override
            public ArrayList<Fixture> getFixtures() {
                ArrayList<Fixture> fixtures = new ArrayList<Fixture>();

                fixtures.add(new Fixture(Team.TeamID.FRANCE, Team.TeamID.ENGLAND,
                        DONBASS, new Date()));
                fixtures.add(new Fixture(Team.TeamID.UKRAINE, Team.TeamID.SWEDEN,
                        KYIV, new Date()));
                fixtures.add(new Fixture(Team.TeamID.UKRAINE, Team.TeamID.FRANCE,
                        DONBASS, new Date()));
                fixtures.add(new Fixture(Team.TeamID.SWEDEN, Team.TeamID.ENGLAND,
                        KYIV, new Date()));
                fixtures.add(new Fixture(Team.TeamID.SWEDEN, Team.TeamID.FRANCE,
                        KYIV, new Date()));
                fixtures.add(new Fixture(Team.TeamID.ENGLAND, Team.TeamID.UKRAINE,
                        DONBASS, new Date()));

                return fixtures;
            }
        };

        private static final int GROUP_SIZE = 4;

        public abstract Team[] getTeams(Context context);
        public abstract String getName(Context context);

        //TODO: Read from server maybe in case they change?
        public abstract ArrayList<Fixture> getFixtures();
    }
}

