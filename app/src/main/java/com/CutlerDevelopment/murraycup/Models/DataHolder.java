package com.CutlerDevelopment.murraycup.Models;

import androidx.annotation.Nullable;

import com.CutlerDevelopment.murraycup.Interfaces.TeamDBListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class DataHolder {

    private static DataHolder instance = null;
    public TeamDBListener teamDbListener;


    private DataHolder() {
        AllTeams = new ArrayList<>();
        AllFixtures = new ArrayList<>();
        CreateDBListener();
    };
    public static DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }
    private Team ChosenTeam;
    public Team GetChosenTeam() {return ChosenTeam;}

    private ArrayList<Team> AllTeams;
    public ArrayList<Team> GetAllTeams() {return AllTeams;}

    private ArrayList<Fixture> AllFixtures;
    public ArrayList<Fixture> GetAllFixtures() {return AllFixtures;}

    private String SharedPrefsName = "TeamPrefs";
    public String GetSharedPrefsName() {return SharedPrefsName;}
    private String SharedPrefsTeamID = "ID";
    public String GetSharedPrefsTeamID() {return SharedPrefsTeamID;};

    public void AddTeam(Team t) {
        AllTeams.add(t);
    }
    public void RemoveTeam(Team t) {AllTeams.remove(t);}
    public Team GetTeamFromID(int ID) {
        for(Team t : AllTeams) {
            if (t.GetID() == ID) {return t;}
        }
        return null;
    }
    public Team GetTeamFromName(String name) {
        for(Team t : AllTeams) {
            if (t.GetName().equals(name)) {
                return t;
            }
        }
        return null;
    }
    public int GetNextTeamID() {return AllTeams.size() + 1;}

    public void ChooseTeam(Team t) {
        this.ChosenTeam = t;
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
                                    if (DataHolder.getInstance().GetTeamFromID(ID) == null) {

                                        Team t = new Team(ID, name, captain, colour, dbKey);
                                        AddTeam(t);
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
                                    Team t = DataHolder.getInstance().GetTeamFromID(ID2);
                                    t.ChangeName(newName);
                                    t.ChangeCaptain(newCaptain);
                                    t.ChangeColour(newColour);

                                    if (teamDbListener != null) {
                                        teamDbListener.teamModified(t);
                                    }
                                    break;
                                case REMOVED:
                                    int ID3 = dc.getDocument().getLong("ID").intValue();
                                    Team t2 = DataHolder.getInstance().GetTeamFromID(ID3);
                                    DataHolder.getInstance().RemoveTeam(t2);

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
