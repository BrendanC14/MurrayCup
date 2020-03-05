package com.CutlerDevelopment.murraycup.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.CutlerDevelopment.murraycup.Interfaces.TeamDBListener;
import com.CutlerDevelopment.murraycup.Models.DataHolder;
import com.CutlerDevelopment.murraycup.Models.Fixture;
import com.CutlerDevelopment.murraycup.Models.Team;
import com.CutlerDevelopment.murraycup.R;
import com.CutlerDevelopment.murraycup.Utils.MenuFixtureAdapter;
import com.CutlerDevelopment.murraycup.Utils.MenuFixtureItem;
import com.CutlerDevelopment.murraycup.Utils.MenuGroupAdapter;
import com.CutlerDevelopment.murraycup.Utils.MenuGroupItem;
import com.CutlerDevelopment.murraycup.Utils.TeamSortingComparator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainMenu extends AppCompatActivity {

    TextView teamName;
    ImageView teamColour;
    TeamDBListener teamDBListener;
    Team chosenTeam;
    ListView groupView;
    ListView fixtureView;
    Map<Team, MenuGroupItem> GroupMenuItemMap;
    Map<Fixture, MenuFixtureItem> FixtureMenuItemMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        chosenTeam = DataHolder.getInstance().GetChosenTeam();

        teamName = findViewById(R.id.WIns);
        teamColour = findViewById(R.id.teamColour);
        groupView = findViewById(R.id.groupList);
        fixtureView = findViewById(R.id.fixtureList);

        teamDBListener = new TeamDBListener() {
            @Override
            public void teamCreated(Team t) {
                NewTeamAdded(t);
            }

            @Override
            public void teamModified(Team t) {
                TeamModified(t);
            }

            @Override
            public void teamRemoved(Team t) {
                TeamRemoved(t);
            }
        };

        DataHolder.getInstance().teamDbListener = this.teamDBListener;

        if (chosenTeam == null) {
            teamName.setText("None");
            teamColour.setImageResource(R.drawable.none);
        }
        else {
            teamName.setText(chosenTeam.getName());
            teamColour.setImageResource((this.getResources().getIdentifier(chosenTeam.getColour(), "drawable", this.getPackageName())));
            GroupMenuItemMap = new HashMap<>();
            fillTeamMenuItemMap();
            AssignGroupListAdapter();

            FixtureMenuItemMap = new HashMap<>();
            fillFixtureMenuItemMap();
            AssignFixtureListAdapter();
        }



    }

    private void fillTeamMenuItemMap() {

        MenuGroupItem header = new MenuGroupItem();
        header.setTeamName("Team Name");
        header.setWins("W");
        header.setDraws("D");
        header.setLosses("L");
        header.setScored("GF");
        header.setConceded("GA");
        header.setDifference("GD");
        header.setPoints("Pts");
        GroupMenuItemMap.put(null, header);

        int groupNumber = chosenTeam.getGroup();
        ArrayList<Team> teamsInGroup = DataHolder.getInstance().GetAllTeamsInAGroup(groupNumber);
        Collections.sort(teamsInGroup, new TeamSortingComparator());


        for(Team t : teamsInGroup) {
            MenuGroupItem item = new MenuGroupItem();
            item.setTeamName(t.getName());
            item.setWins(String.valueOf(t.getWins()));
            item.setDraws(String.valueOf(t.getDraws()));
            item.setLosses(String.valueOf(t.getLosses()));
            item.setScored(String.valueOf(t.getScored()));
            item.setConceded(String.valueOf(t.getConceded()));
            item.setDifference(String.valueOf(t.getGoalDifference()));
            item.setPoints(String.valueOf(t.getPoints()));
            GroupMenuItemMap.put(t, item);

        }

    }

    private void fillFixtureMenuItemMap() {
        MenuFixtureItem header = new MenuFixtureItem();
        header.setPitch("Pitch");
        header.setHomeTeam("Team 1");
        header.setHomeScore("");
        header.setAwayScore("");
        header.setAwayTeam("Team 2");
        header.setTime("Time");
        FixtureMenuItemMap.put(null, header);

        for (Fixture f : chosenTeam.getAllFixtures()) {
            MenuFixtureItem item = new MenuFixtureItem();
            item.setPitch(String.valueOf(f.getPitchNumber()));
            item.setHomeTeam(DataHolder.getInstance().GetTeamFromID(f.getHomeTeamID()).getName());
            if (f.getHomeTeamScore() == -1) {
                item.setHomeScore("");
                item.setAwayScore("");
            }
            else {
                item.setHomeScore(String.valueOf(f.getHomeTeamScore()));
                item.setAwayScore(String.valueOf(f.getAwayTeamScore()));
            }
            item.setAwayTeam(DataHolder.getInstance().GetTeamFromID(f.getAwayTeamID()).getName());

            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
            String time = formatter.format(f.getTimeOfMatch());
            item.setTime(time);
            FixtureMenuItemMap.put(f, item);
        }
    }

    public void AssignGroupListAdapter() {

        final ArrayList<MenuGroupItem> myGroupItems = new ArrayList<>();
        for (Map.Entry<Team, MenuGroupItem> pair : GroupMenuItemMap.entrySet()) {
            myGroupItems.add(pair.getValue());
        }

        MenuGroupAdapter myAdapter = new MenuGroupAdapter(getApplicationContext(), myGroupItems);
        groupView.setAdapter(myAdapter);

    }

    public void AssignFixtureListAdapter() {
        final ArrayList<MenuFixtureItem> myFixtureItems = new ArrayList<>();
        for (Map.Entry<Fixture, MenuFixtureItem> pair : FixtureMenuItemMap.entrySet()) {
            myFixtureItems.add(pair.getValue());
        }
        MenuFixtureAdapter myAdapter = new MenuFixtureAdapter(getApplicationContext(), myFixtureItems);
        fixtureView.setAdapter(myAdapter);
    }

    public void NewTeamAdded(Team t) {
        MenuGroupItem item = new MenuGroupItem();
        item.setTeamName(t.getName());
        item.setWins(String.valueOf(0));
        item.setDraws(String.valueOf(0));
        item.setLosses(String.valueOf(0));
        item.setScored(String.valueOf(0));
        item.setConceded(String.valueOf(0));
        item.setDifference(String.valueOf(0));
        item.setPoints(String.valueOf(0));
        GroupMenuItemMap.put(t, item);

        AssignGroupListAdapter();
    }

    public void TeamModified(Team t) {


        if (GroupMenuItemMap.containsKey(t)) {
            if (t.getGroup() == chosenTeam.getGroup()) {
                MenuGroupItem item = GroupMenuItemMap.get(t);
                item.setTeamName(t.getName());
            }
            else {
                GroupMenuItemMap.remove(t);
            }
        }
        else {
            if (t.getGroup() == chosenTeam.getGroup()) {

                MenuGroupItem item = new MenuGroupItem();
                item.setTeamName(t.getName());
                item.setWins(String.valueOf(0));
                item.setDraws(String.valueOf(0));
                item.setLosses(String.valueOf(0));
                item.setScored(String.valueOf(0));
                item.setConceded(String.valueOf(0));
                item.setDifference(String.valueOf(0));
                item.setPoints(String.valueOf(0));
                GroupMenuItemMap.put(t, item);
            }
        }

        AssignGroupListAdapter();
    }

    public void TeamRemoved(Team t) {

        GroupMenuItemMap.remove(t);
        AssignGroupListAdapter();
    }

    public void ChangeTeam(View view) {

        DataHolder.getInstance().teamDbListener = null;
        startActivity(new Intent(MainMenu.this, PickATeamMenu.class));
        finish();
    }

    public void OpenAdminMenu(View view) {
        DataHolder.getInstance().teamDbListener = null;
        startActivity(new Intent(MainMenu.this, AdminMenu.class));
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
