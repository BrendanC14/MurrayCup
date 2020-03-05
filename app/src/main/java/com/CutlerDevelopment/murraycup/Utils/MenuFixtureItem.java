package com.CutlerDevelopment.murraycup.Utils;

import java.util.Date;

public class MenuFixtureItem {

    public String Pitch;
    public String HomeTeam;
    public String HomeScore;
    public String AwayScore;
    public String AwayTeam;
    public String Time;

    public void setPitch(String pitch) {this.Pitch = pitch; }
    public String getPitch() { return Pitch; }

    public void setHomeTeam(String team) {this.HomeTeam = team; }
    public String getHomeTeam() { return HomeTeam; }

    public void setHomeScore(String score) {this.HomeScore = score; }
    public String getHomeScore() { return HomeScore; }

    public void setAwayScore(String score) {this.AwayScore = score; }
    public String getAwayScore() { return AwayScore; }

    public void setAwayTeam(String team) {this.AwayTeam = team; }
    public String getAwayTeam() { return AwayTeam; }

    public void setTime(String time) {this.Time = time; }
    public String getTime() { return Time; }

}
