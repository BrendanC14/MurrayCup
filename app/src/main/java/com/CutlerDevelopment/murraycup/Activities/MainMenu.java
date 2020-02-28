package com.CutlerDevelopment.murraycup.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.CutlerDevelopment.murraycup.Interfaces.TeamDBListener;
import com.CutlerDevelopment.murraycup.Models.DataHolder;
import com.CutlerDevelopment.murraycup.Models.Fixture;
import com.CutlerDevelopment.murraycup.Models.Team;
import com.CutlerDevelopment.murraycup.R;
import com.CutlerDevelopment.murraycup.Utils.MenuGroupAdapter;
import com.CutlerDevelopment.murraycup.Utils.MenuGroupItem;
import com.CutlerDevelopment.murraycup.Utils.MenuItemAdapter;
import com.CutlerDevelopment.murraycup.Utils.MenuTeamItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainMenu extends AppCompatActivity {

    TextView teamName;
    TextView FixtList;
    ImageView teamColour;
    TeamDBListener teamDBListener;
    Team chosenTeam;
    ListView groupView;
    Map<Team, MenuGroupItem> GroupMenuItemMap;

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
        FixtList = findViewById(R.id.tempFixtureList);
        teamColour = findViewById(R.id.teamColour);
        groupView = findViewById(R.id.groupList);


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
            teamName.setText(chosenTeam.GetName());
            teamColour.setImageResource((this.getResources().getIdentifier(chosenTeam.GetColour(), "drawable", this.getPackageName())));
            GroupMenuItemMap = new HashMap<>();
            fillTeamMenuItemMap();
            AssignListAdapter();

            String Fixtures = "";
            for (Fixture f : chosenTeam.GetAllFixtures()) {
                String homeTeamName = DataHolder.getInstance().GetTeamFromID(f.GetHomeTeamID()).GetName();
                String awayTeamName = DataHolder.getInstance().GetTeamFromID(f.GetAwayTeamID()).GetName();

                Fixtures += homeTeamName + " V " + awayTeamName + "\n";
            }
            FixtList.setText(Fixtures);
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

        int groupNumber = chosenTeam.GetGroup();

        for(Team t : DataHolder.getInstance().GetAllTeamsInAGroup(groupNumber)) {
            MenuGroupItem item = new MenuGroupItem();
            item.setTeamName(t.GetName());
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

    public void NewTeamAdded(Team t) {
        MenuGroupItem item = new MenuGroupItem();
        item.setTeamName(t.GetName());
        item.setWins(String.valueOf(0));
        item.setDraws(String.valueOf(0));
        item.setLosses(String.valueOf(0));
        item.setScored(String.valueOf(0));
        item.setConceded(String.valueOf(0));
        item.setDifference(String.valueOf(0));
        item.setPoints(String.valueOf(0));
        GroupMenuItemMap.put(t, item);

        AssignListAdapter();
    }

    public void TeamModified(Team t) {


        if (GroupMenuItemMap.containsKey(t)) {
            if (t.GetGroup() == chosenTeam.GetGroup()) {
                MenuGroupItem item = GroupMenuItemMap.get(t);
                item.setTeamName(t.GetName());
            }
            else {
                GroupMenuItemMap.remove(t);
            }
        }
        else {
            if (t.GetGroup() == chosenTeam.GetGroup()) {

                MenuGroupItem item = new MenuGroupItem();
                item.setTeamName(t.GetName());
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

        AssignListAdapter();
    }

    public void TeamRemoved(Team t) {

        GroupMenuItemMap.remove(t);
        AssignListAdapter();
    }

    public void AssignListAdapter() {

        final ArrayList<MenuGroupItem> myGroupItems = new ArrayList<>();
        for (Map.Entry<Team, MenuGroupItem> pair : GroupMenuItemMap.entrySet()) {
            myGroupItems.add(pair.getValue());
        }

        MenuGroupAdapter myAdapter = new MenuGroupAdapter(getApplicationContext(), myGroupItems);
        groupView.setAdapter(myAdapter);

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
