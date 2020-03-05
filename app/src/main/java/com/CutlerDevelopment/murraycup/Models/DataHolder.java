package com.CutlerDevelopment.murraycup.Models;

import androidx.annotation.Nullable;

import com.CutlerDevelopment.murraycup.Interfaces.TeamDBListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class DataHolder {

    private static DataHolder instance = null;
    public TeamDBListener teamDbListener;


    private DataHolder() {
        AllTeams = new ArrayList<>();
        AllFixtures = new ArrayList<>();
        AllGroups = new HashMap<>();
        CreateDBListener();
    };
    public static DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    private String SharedPrefsName = "TeamPrefs";
    public String GetSharedPrefsName() {return SharedPrefsName;}
    private String SharedPrefsTeamID = "ID";
    public String GetSharedPrefsTeamID() {return SharedPrefsTeamID;};

    private Team ChosenTeam;
    public Team GetChosenTeam() {return ChosenTeam;}
    private ArrayList<Team> AllTeams;
    public ArrayList<Team> GetAllTeams() {return AllTeams;}
    public void AddTeam(Team t) {
        if (GetTeamFromID(t.getID()) == null) {
            AllTeams.add(t);
            AddTeamToGroup(t.getGroup(), t);
        }
    }
    public void RemoveTeam(Team t) {AllTeams.remove(t);}
    public Team GetTeamFromID(int ID) {
        for(Team t : AllTeams) {
            if (t.getID() == ID) {return t;}
        }
        return null;
    }
    public Team GetTeamFromName(String name) {
        for(Team t : AllTeams) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }
    public int GetNextTeamID() {return AllTeams.size() + 1;}
    public void ChooseTeam(Team t) {
        this.ChosenTeam = t;
    }

    private HashMap<Integer, ArrayList<Team>> AllGroups;
    public HashMap<Integer, ArrayList<Team>> GetAllGroups() {return AllGroups; }
    public void AddTeamToGroup(int groupNumber, Team t) {
        if (!AllGroups.containsKey(groupNumber)) {
            AllGroups.put(groupNumber, new ArrayList<Team>());
        }
        AllGroups.get(groupNumber).add(t);
    }
    public void RemoveTeamFromGroup(int groupNumber, Team t) {
        if (AllGroups.containsKey(groupNumber)) {
            if (AllGroups.get(groupNumber).contains(t)) {
                AllGroups.get(groupNumber).remove(t);
            }
        }
    }
    public ArrayList<Team> GetAllTeamsInAGroup(int groupNumber) {
        if (AllGroups.containsKey(groupNumber)) {
            return AllGroups.get(groupNumber);
        }
        return new ArrayList<>();
    }


    private ArrayList<Fixture> AllFixtures;
    public ArrayList<Fixture> GetAllFixtures() {return AllFixtures;}
    public void AddFixture(Fixture fixture) {
        if (GetFixtureFromID(fixture.getFirestoreReference())== null){
            AllFixtures.add(fixture);
            GetTeamFromID(fixture.getHomeTeamID()).addFixture(fixture);
            GetTeamFromID(fixture.getAwayTeamID()).addFixture(fixture);
        }
    }
    public void UpdateScore(Fixture fixture, int homeScore, int awayScore) {
        if (GetFixtureFromID(fixture.getFirestoreReference()) != null) {
            Fixture f = GetFixtureFromID(fixture.getFirestoreReference());
            f.setHomeTeamScore(homeScore);
            f.setAwayTeamScore(awayScore);
            GetTeamFromID(f.getHomeTeamID()).updateFixture(f);
            GetTeamFromID(f.getAwayTeamID()).updateFixture(f);
        }
    }
    public void RemoveFixture(Fixture fixture) {
        AllFixtures.remove(fixture);
        GetTeamFromID(fixture.getHomeTeamID()).removeFixture(fixture);
        GetTeamFromID(fixture.getAwayTeamID()).removeFixture(fixture);
    }
    public Fixture GetFixtureFromID(String id) {
        for (Fixture f : AllFixtures) {
            if (f.getFirestoreReference() == id) { return f; }
        }
        return  null;
    }

    public void CreateDBListener() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("teams")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    String dbKey = dc.getDocument().getId();
                                    int ID = dc.getDocument().getLong("ID").intValue();
                                    String name = dc.getDocument().getString("Name");
                                    String captain = dc.getDocument().getString("Captain");
                                    String colour = dc.getDocument().getString("Colour");
                                    int group = dc.getDocument().getLong("Group").intValue();

                                    Team t = new Team(ID, name, captain, colour, group, dbKey);
                                    AddTeam(t);


                                    if (teamDbListener != null) {
                                        teamDbListener.teamCreated(t);
                                    }

                                    break;
                                case MODIFIED:

                                    int ID2 = dc.getDocument().getLong("ID").intValue();
                                    String newName = dc.getDocument().getString("Name");
                                    String newCaptain = dc.getDocument().getString("Captain");
                                    String newColour = dc.getDocument().getString("Colour");
                                    int newGroup = dc.getDocument().getLong("Group").intValue();
                                    Team t2 = GetTeamFromID(ID2);
                                    t2.setName(newName);
                                    t2.setCaptain(newCaptain);
                                    t2.setColour(newColour);

                                    if (t2.getGroup() != newGroup) {
                                        RemoveTeamFromGroup(t2.getGroup(), t2);
                                    }
                                    t2.setGroup((newGroup));
                                    if (teamDbListener != null) {
                                        teamDbListener.teamModified(t2);
                                    }
                                    break;
                                case REMOVED:
                                    int ID3 = dc.getDocument().getLong("ID").intValue();
                                    Team t3 = GetTeamFromID(ID3);
                                    RemoveTeam(t3);

                                    if (teamDbListener != null) {
                                        teamDbListener.teamRemoved(t3);
                                    }


                                    break;
                            }
                        }
                    }
                });
        db.collection("fixtures")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            String dbKey = dc.getDocument().getId();
                            int homeID = dc.getDocument().getLong("HomeTeam").intValue();
                            int awayID = dc.getDocument().getLong("AwayTeam").intValue();
                            int homeScore = dc.getDocument().getLong("HomeScore").intValue();
                            int awayScore = dc.getDocument().getLong("AwayScore").intValue();
                            int pitch = dc.getDocument().getLong("Pitch").intValue();
                            Date time = dc.getDocument().getTimestamp("Time").toDate();
                            switch (dc.getType()) {
                                case ADDED:

                                    Fixture fixToAdd = new Fixture(homeID, awayID, homeScore, awayScore, time, pitch, dbKey );

                                    AddFixture(fixToAdd);
                                    break;
                                case MODIFIED:

                                    Fixture fixModified = GetFixtureFromID(dbKey);
                                    if (fixModified.getHomeTeamScore() != homeScore || fixModified.getAwayTeamScore() != awayScore) {
                                        UpdateScore(fixModified, homeScore, awayScore);
                                    }

                                    break;
                                case REMOVED:

                                    Fixture fixToRemove = GetFixtureFromID(dbKey);
                                    RemoveFixture(fixToRemove);


                                    break;
                            }
                        }
                    }
                });
    }
}
