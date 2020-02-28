package com.CutlerDevelopment.murraycup.Models;

import java.util.Date;

public class Fixture {

    private int homeTeamID;
    private int homeScore;
    private int awayTeamID;
    private int awayScore;
    private Date timeOfMatch;
    private int pitchNumber;

    public enum MatchResult {
        WIN,
        LOSE,
        DRAW,
        NOT_PLAYED
    }

    public Fixture(int homeID, int awayID, Date time, int pitch) {
        this.homeTeamID = homeID;
        this.awayTeamID = awayID;
        this.homeScore = -1;
        this.awayScore = -1;
        this.timeOfMatch = time;
        this.pitchNumber = pitch;
    }

    public int GetHomeTeamID() {return homeTeamID;}

    public int GetHomeTeamScore() {return homeScore; }
    public void SetHomeTeamScore(int score) {homeScore = score;}

    public int GetAwayTeamID() { return  awayTeamID; }

    public int GetAwayTeamScore() { return awayScore; }
    public void SetAwayTeamScore(int score) { awayScore = score; }

    public Date GetTimeOfMatch() { return timeOfMatch; }
    public void SetTimeOfMatch(Date newTime) { timeOfMatch = newTime; }

    public int GetPitchNumber() { return pitchNumber; }
    public void SetPitchNumber(int newPitch) { pitchNumber = newPitch; }

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
}
