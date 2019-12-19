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
import com.CutlerDevelopment.murraycup.Models.DatabaseConnectionHandler;
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
    DatabaseConnectionHandler dbPickTeamMenuActivity;
    DataHolder dataHolderPickTeamMenuActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_ateam_menu);
        dbPickTeamMenuActivity = new DatabaseConnectionHandler();
        dataHolderPickTeamMenuActivity = new DataHolder(dbPickTeamMenuActivity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        TeamMenuItemMap = new HashMap<>();
        myListView = findViewById(R.id.teamList);
        fillTeamMenuItemMap();
        assignListAdapter();

        teamDbListener = new TeamDBListener() {
            @Override
            public void teamCreated(Team t) {
                newTeamAdded(t);
            }

            @Override
            public void teamModified(Team t) {
                PickATeamMenu.this.teamModified(t);
            }

            @Override
            public void teamRemoved(Team t) {
                PickATeamMenu.this.teamRemoved(t);
            }
        };
        dataHolderPickTeamMenuActivity.teamDbListener = this.teamDbListener;
    }

    public void newTeamAdded(Team t) {
        MenuTeamItem item = new MenuTeamItem();
        item.setTeamName(t.getName());
        item.setImageName(this.getResources().getIdentifier(t.getColour(), "drawable", this.getPackageName()));
        TeamMenuItemMap.put(t, item);
        assignListAdapter();
    }

    public void teamModified(Team t) {

        MenuTeamItem item = TeamMenuItemMap.get(t);
        item.setTeamName(t.getName());
        item.setImageName(this.getResources().getIdentifier(t.getColour(), "drawable", this.getPackageName()));
        assignListAdapter();
    }

    public void teamRemoved(Team t) {

        TeamMenuItemMap.remove(t);
        assignListAdapter();
    }
    private void fillTeamMenuItemMap() {
        MenuTeamItem item_none = new MenuTeamItem();
        item_none.setTeamName("None");
        item_none.setImageName(R.drawable.none);
        TeamMenuItemMap.put(null, item_none);

        for(Team t : dataHolderPickTeamMenuActivity.getAllTeams()) {
            MenuTeamItem item = new MenuTeamItem();
            item.setTeamName(t.getName());
            item.setImageName(this.getResources().getIdentifier(t.getColour(), "drawable", this.getPackageName()));
            TeamMenuItemMap.put(t, item);
        }
    }


    public void assignListAdapter() {
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
                selectTeam(item);
            }
        });
    }

    public void selectTeam(MenuTeamItem i) {
        if (i != null) {
            Team t = dataHolderPickTeamMenuActivity.getTeamFromName(i.getTeamName());
            if (t != null) {
                dataHolderPickTeamMenuActivity.chooseTeam(t);
                SharedPreferences settings = getApplicationContext().getSharedPreferences(dataHolderPickTeamMenuActivity.getSharedPrefsName(), 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(dataHolderPickTeamMenuActivity.getSharedPrefsTeamID(), t.getID());
                editor.apply();
            }
            else {
                SharedPreferences settings = getApplicationContext().getSharedPreferences(dataHolderPickTeamMenuActivity.getSharedPrefsName(), 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(dataHolderPickTeamMenuActivity.getSharedPrefsTeamID(), -1);
                editor.apply();

            }
        }
        startActivity(new Intent(PickATeamMenu.this, MainMenu.class));
    }

}
