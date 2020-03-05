package com.CutlerDevelopment.murraycup.Models;

import android.renderscript.ScriptIntrinsicYuvToRGB;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Fixture {

    private int homeTeamID;
    private int homeScore;
    private int awayTeamID;
    private int awayScore;
    private Date timeOfMatch;
    private int pitchNumber;
    private String firestoreReference;

    public enum MatchResult {
        WIN,
        LOSE,
        DRAW,
        NOT_PLAYED
    }

    public Fixture(int homeID, int awayID, int homeScore, int awayScore, Date time, int pitch, String fsRef) {
        this.homeTeamID = homeID;
        this.awayTeamID = awayID;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.timeOfMatch = time;
        this.pitchNumber = pitch;
        this.firestoreReference = fsRef;
    }

    public Map<String, Object> getMatchReportForTeam(int teamID) {
        Map<String, Object> matchReport = new HashMap<>();
        int scored = 0;
        int conceded = 0;
        MatchResult result = MatchResult.DRAW;

        if (homeScore == -1) {
            matchReport.put("Result", MatchResult.NOT_PLAYED);
            return matchReport;
        }

        if (homeTeamID == teamID) {
            scored = homeScore;
            conceded = awayScore;
        }
        else {
            scored = awayScore;
            conceded = homeScore;
        }

        if (scored > conceded) {
            matchReport.put("Result", MatchResult.WIN);
        }
        else if (scored < conceded) {
            matchReport.put("Result", MatchResult.LOSE);
        }
        matchReport.put("Scored", scored);
        matchReport.put("Conceded", conceded);
        return matchReport;
    }

    public MatchResult GetTeamResult(int teamID) {
        if (homeScore == -1) { return MatchResult.NOT_PLAYED; }
        if (homeScore == awayScore) { return MatchResult.DRAW; }

        if (teamID == homeTeamID) {
            if (homeScore > awayScore) { return MatchResult.WIN; }
            else { return MatchResult.LOSE; }
        }
        else {
            if (awayScore > homeScore) { return MatchResult.WIN; }
            else { return MatchResult.LOSE; }
        }
    }

    public int getHomeTeamID() {return homeTeamID;}
    public int getHomeTeamScore() {return homeScore; }
    public int getAwayTeamID() { return  awayTeamID; }
    public int getAwayTeamScore() { return awayScore; }
    public Date getTimeOfMatch() { return timeOfMatch; }
    public int getPitchNumber() { return pitchNumber; }
    public String getFirestoreReference() {return this.firestoreReference; }

    public void setHomeTeamScore(int score) {homeScore = score;}
    public void setAwayTeamScore(int score) { awayScore = score; }
    public void setTimeOfMatch(Date newTime) { timeOfMatch = newTime; }
    public void setPitchNumber(int newPitch) { pitchNumber = newPitch; }


}
