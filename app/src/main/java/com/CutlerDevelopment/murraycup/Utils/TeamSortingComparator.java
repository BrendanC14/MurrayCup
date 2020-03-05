package com.CutlerDevelopment.murraycup.Utils;

import com.CutlerDevelopment.murraycup.Models.Team;

import java.util.Comparator;

public class TeamSortingComparator implements Comparator<Team> {
    @Override
    public int compare(Team team1, Team team2) {

        int comparePoints = compareTo(team1.getPoints(), team2.getPoints());
        int compareGD = compareTo(team1.getGoalDifference(), team2.getGoalDifference());
        int compareWins = compareTo(team1.getWins(), team2.getWins());

        if (comparePoints != 0) {
            return  comparePoints;
        }
        else {
            if (compareGD !=0) {
                return compareGD;
            }
            else {
                return compareWins;
            }
        }

    }

    public int compareTo(int team1, int team2) {
        if (team1 > team2)
            return 1;
        else if (team1 < team2)
            return -1;
        else
            return 0;
    }
}

