package com.CutlerDevelopment.murraycup.Models;

import androidx.annotation.Nullable;

import com.CutlerDevelopment.murraycup.Interfaces.TeamDBListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class DataHolder {
    private static DataHolder instance = null;
    public TeamDBListener teamDbListener;
    private Team ChosenTeam;
    private ArrayList<Team> AllTeams;
    private ArrayList<Fixture> AllFixtures;
    private String SharedPrefsName = "TeamPrefs";
    private String SharedPrefsTeamID = "ID";
    private DatabaseConnectionHandler db;

    public DataHolder(DatabaseConnectionHandler databaseConnectionHandler) {
        db = new DatabaseConnectionHandler();
        AllTeams = new ArrayList<>();
        AllFixtures = new ArrayList<>();
        createDBListener();
    }//end of constructor

    public Team getChosenTeam() {
        return ChosenTeam;
    }


    public ArrayList<Team> getAllTeams() {
        return AllTeams;
    }


    public ArrayList<Fixture> getAllFixtures() {
        return AllFixtures;
    }


    public String getSharedPrefsName() {
        return SharedPrefsName;
    }

    public String getSharedPrefsTeamID() {
        return SharedPrefsTeamID;
    }

    public void addTeam(Team t) {
        AllTeams.add(t);
    }

    public void removeTeam(Team t) {
        AllTeams.remove(t);
    }

    public Team getTeamFromID(int ID) {
        for (Team t : AllTeams) {
            if (t.getID() == ID) {
                return t;
            }
        }
        return null;
    }

    public Team getTeamFromName(String name) {
        for (Team t : AllTeams) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }

    public int getNextTeamID() {
        return AllTeams.size() + 1;
    }

    public void chooseTeam(Team t) {
        this.ChosenTeam = t;
    }

    public void createDBListener() {
        db.dbConnection().collection("teams")
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
                                    if (getTeamFromID(ID) == null) {

                                        Team t = new Team(ID, name, captain, colour, dbKey);
                                        addTeam(t);
                                        if (teamDbListener != null) {
                                            teamDbListener.teamCreated(t);
                                        }
                                    }
                                    break;
                                case MODIFIED:

                                    int ID2 = dc.getDocument().getLong("ID").intValue();
                                    String newName = dc.getDocument().getString("Name");
                                    String newCaptain = dc.getDocument().getString("Captain");
                                    String newColour = dc.getDocument().getString("Colour");
                                    Team t = getTeamFromID(ID2);
                                    t.changeName(newName);
                                    t.changeCaptain(newCaptain);
                                    t.changeColour(newColour);

                                    if (teamDbListener != null) {
                                        teamDbListener.teamModified(t);
                                    }
                                    break;
                                case REMOVED:
                                    int ID3 = dc.getDocument().getLong("ID").intValue();
                                    Team t2 = getTeamFromID(ID3);
                                    removeTeam(t2);

                                    if (teamDbListener != null) {
                                        teamDbListener.teamRemoved(t2);
                                    }


                                    break;
                            }
                        }
                    }
                });
    }
}
