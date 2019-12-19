package com.CutlerDevelopment.murraycup.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.CutlerDevelopment.murraycup.Interfaces.TeamDBListener;
import com.CutlerDevelopment.murraycup.Models.DataHolder;
import com.CutlerDevelopment.murraycup.Models.Team;
import com.CutlerDevelopment.murraycup.R;
import com.CutlerDevelopment.murraycup.Utils.MenuItemAdapter;
import com.CutlerDevelopment.murraycup.Utils.MenuTeamItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Creates a list of View items for each team added to the db
public class PickATeamMenu extends AppCompatActivity {

    ListView myListView;
    TeamDBListener teamDbListener;
    Map<Team, MenuTeamItem> TeamMenuItemMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pick_ateam_menu);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        TeamMenuItemMap = new HashMap<>();
        myListView = findViewById(R.id.teamList);
        fillTeamMenuItemMap();
        AssignListAdapter();

        teamDbListener = new TeamDBListener() {
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

        DataHolder.getInstance().teamDbListener = this.teamDbListener;


    }

    public void NewTeamAdded(Team t) {
        MenuTeamItem item = new MenuTeamItem();
        item.setTeamName(t.GetName());
        item.setImageName(this.getResources().getIdentifier(t.GetColour(), "drawable", this.getPackageName()));
        TeamMenuItemMap.put(t, item);

        AssignListAdapter();
    }

    public void TeamModified(Team t) {


        MenuTeamItem item = TeamMenuItemMap.get(t);
        item.setTeamName(t.GetName());
        item.setImageName(this.getResources().getIdentifier(t.GetColour(), "drawable", this.getPackageName()));

        AssignListAdapter();
    }

    public void TeamRemoved(Team t) {

        TeamMenuItemMap.remove(t);
        AssignListAdapter();
    }
    private void fillTeamMenuItemMap() {
        MenuTeamItem item_none = new MenuTeamItem();
        item_none.setTeamName("None");
        item_none.setImageName(R.drawable.none);
        TeamMenuItemMap.put(null, item_none);


        for(Team t : DataHolder.getInstance().GetAllTeams()) {
            MenuTeamItem item = new MenuTeamItem();
            item.setTeamName(t.GetName());
            item.setImageName(this.getResources().getIdentifier(t.GetColour(), "drawable", this.getPackageName()));
            TeamMenuItemMap.put(t, item);
        }

    }


    public void AssignListAdapter() {

        final ArrayList<MenuTeamItem> myTeamItems = new ArrayList<>();
        for (Map.Entry<Team, MenuTeamItem> pair : TeamMenuItemMap.entrySet()) {
            myTeamItems.add(pair.getValue());
        }

        MenuItemAdapter myAdapter = new MenuItemAdapter(getApplicationContext(), myTeamItems);
        myListView.setAdapter(myAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MenuTeamItem item = myTeamItems.get(i);
                SelectTeam(item);
            }
        });
    }

    public void SelectTeam(MenuTeamItem i) {
        DataHolder dh = DataHolder.getInstance();
        if (i != null) {

            Team t = dh.GetTeamFromName(i.getTeamName());
            if (t != null) {
                dh.ChooseTeam(t);

                SharedPreferences settings = getApplicationContext().getSharedPreferences(dh.GetSharedPrefsName(), 0);
                SharedPreferences.Editor editor = settings.edit();

                editor.putInt(dh.GetSharedPrefsTeamID(), t.GetID());
                editor.apply();
            }
            else {
                SharedPreferences settings = getApplicationContext().getSharedPreferences(dh.GetSharedPrefsName(), 0);
                SharedPreferences.Editor editor = settings.edit();

                editor.putInt(dh.GetSharedPrefsTeamID(), -1);
                editor.apply();

            }
        }


        startActivity(new Intent(PickATeamMenu.this, MainMenu.class));
    }

}
