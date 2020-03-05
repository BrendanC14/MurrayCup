package com.CutlerDevelopment.murraycup.Models;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Team {

    private int id;
    private String firestoreReference;
    private String name;
    private String captain;
    private String colour;
    private int group;
    private ArrayList<Fixture> allFixtures;

    private int wins;
    private int draws;
    private int losses;
    private int scored;
    private int conceded;

    public Team(int theID, String theName, String theCaptain, String theColour, int theGroup, String fbRef) {
        this.id = theID;
        this.name = theName;
        this.captain = theCaptain;
        this.colour = theColour;
        this.group = theGroup;
        this.firestoreReference = fbRef;
        this.allFixtures = new ArrayList<>();
    }

    public void addFixture(Fixture fixture) {
        if (!allFixtures.contains(fixture)) {
            allFixtures.add(fixture);
            if (fixture.getHomeTeamScore() != -1) {
                updateFixture(fixture);
            }
        }
    }
    public void updateFixture(Fixture fixture) {

        Map<String, Object> matchReport = fixture.getMatchReportForTeam(id);

        if (matchReport.get("Result") == Fixture.MatchResult.WIN) { wins++; }
        else if (matchReport.get("Result") == Fixture.MatchResult.LOSE) { losses++; }
        else { draws++; }
        scored = matchReport.get("Scored");
        if (fixture.getHomeTeamID() == id) {
            scored = fixture.getHomeTeamScore();
            conceded = fixture.getAwayTeamScore();
        }
        else {
            scored = fixture.getAwayTeamScore();
            conceded = fixture.getHomeTeamScore();
        }

        if (fixture.GetTeamResult(id) == Fixture.MatchResult.WIN) {
            wins++;
        }
        else if (fixture.GetTeamResult(id) == Fixture.MatchResult.LOSE) {
            losses++;
        }
        else {
            draws++;
        }
    }
    public void removeFixture(Fixture fixture) {
        if (allFixtures.contains(fixture)) {
            allFixtures.remove(fixture);
        }
    }



    public int getID() {return this.id;}
    public String getFirestoreReference() {return this.firestoreReference;}
    public String getName() {
        return this.name;
    }
    public String getCaptain() {
        return this.captain;
    }
    public String getColour() {
        return this.colour;
    }
    public int getGroup() {
        return this.group;
    }
    public ArrayList<Fixture> getAllFixtures() { return this.allFixtures; }
    public int getWins() { return this.wins; }
    public int getLosses() { return this.losses; }
    public int getDraws() { return this.draws; }
    public int getScored() { return this.scored; }
    public int getConceded() { return this.conceded; }
    public int getGoalDifference() {return scored - conceded; }
    public int getPoints() { return (wins * 3) + draws; }


    public void setID(int newID) {this.id = newID;}
    public void setName(String newName) { this.name = newName; }
    public void setCaptain(String newCaptain) { this.captain = newCaptain; }
    public void setColour(String newColour) { this.colour = newColour; }
    public void setGroup(int newGroup) { this.group = newGroup; }

}
