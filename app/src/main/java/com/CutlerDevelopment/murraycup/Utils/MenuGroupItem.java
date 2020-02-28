package com.CutlerDevelopment.murraycup.Utils;

public class MenuGroupItem {
    private String teamName;
    private String wins;
    private String draws;
    private String losses;
    private String scored;
    private String conceded;
    private String difference;
    private String pts;

    public void setTeamName(String theTeamName) {
        this.teamName = theTeamName;
    }
    public String getTeamName() {
        return this.teamName;
    }

    public void setWins(String numWins) {
        this.wins = numWins;
    }
    public String getWins() {
        return this.wins;
    }

    public void setDraws(String numDraws) {
        this.draws = numDraws;
    }
    public String getDraws() {
        return this.draws;
    }

    public void setLosses(String numLosses) {this.losses = numLosses;}
    public String getLosses() {return this.losses;}

    public void setScored(String numScored) {this.scored = numScored; }
    public String getScored() {return this.scored;}

    public void setConceded(String numConceded) {this.conceded = numConceded; }
    public String getConceded() {return this.conceded; }

    public void setDifference(String numDifference) {this.difference = numDifference; }
    public String getDifference() {return  this.difference;}

    public void setPoints(String newPoints) {this.pts = newPoints;}
    public String getPoints() {return this.pts;}

}
