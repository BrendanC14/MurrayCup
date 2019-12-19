package com.CutlerDevelopment.murraycup.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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


public class TeamAdminMenu extends AppCompatActivity {

    TextView teamNameTextBox;
    Spinner colourSpinner;
    TextView captainNameTextBox;
    TextView feedbackTextBox;
    Button addButton;
    Button changeButton;
    Button removeButton;
    TeamDBListener teamDbListener;
    DatabaseConnectionHandler dbConnectionHandlerTeamAdmin;
    String teamSelected = "";
    Map<Team, MenuTeamItem> TeamMenuItemMap;
    ListView myListView;
    ArrayAdapter<String> spinnerAdapter;
    DataHolder dataHolderTeamAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_admin_menu);
        dbConnectionHandlerTeamAdmin = new DatabaseConnectionHandler();
        dataHolderTeamAdmin = new DataHolder(dbConnectionHandlerTeamAdmin);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        dbConnectionHandlerTeamAdmin = new DatabaseConnectionHandler();
        TeamMenuItemMap = new HashMap<>();
        myListView = findViewById(R.id.teamList);
        fillTeamMenuItemMap();
        assignListAdapter();

        teamNameTextBox = findViewById(R.id.nameText);
        colourSpinner = findViewById(R.id.colourDropdown);
        captainNameTextBox = findViewById(R.id.captainText);
        feedbackTextBox = findViewById(R.id.Feedback);
        addButton = findViewById(R.id.addTeam);
        changeButton = findViewById(R.id.changeButton);
        removeButton = findViewById(R.id.removeButton);

        teamDbListener = new TeamDBListener() {
            @Override
            public void teamCreated(Team team) { newTeamAdded(team); }

            @Override
            public void teamModified(Team team) {
                TeamAdminMenu.this.teamModified(team);
            }

            @Override
            public void teamRemoved(Team team) {
                TeamAdminMenu.this.teamRemoved(team);
            }
        };
        dataHolderTeamAdmin.teamDbListener = this.teamDbListener;
        String [] dropdownItems = new String[]{"red", "blue", "white"};
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, dropdownItems);
        colourSpinner.setAdapter(spinnerAdapter);
    }

    public void newTeamAdded(Team team) {
        MenuTeamItem item = new MenuTeamItem();
        item.setTeamName(team.getName());
        item.setImageName(this.getResources().getIdentifier(team.getColour(), "drawable", this.getPackageName()));
        TeamMenuItemMap.put(team, item);
        feedbackTextBox.setText(team.getName() + " has been added");
        assignListAdapter();
    }

    public void teamModified(Team team) {
        MenuTeamItem item = TeamMenuItemMap.get(team);
        item.setTeamName(team.getName());
        item.setImageName(this.getResources().getIdentifier(team.getColour(), "drawable", this.getPackageName()));
        feedbackTextBox.setText(team.getName() + " has been modified");
        assignListAdapter();
    }

    public void teamRemoved(Team team) {
        TeamMenuItemMap.remove(team);
        feedbackTextBox.setText(team.getName() + " has been removed");
        teamSelected = "";
        assignListAdapter();
    }

    private void fillTeamMenuItemMap() {
        for(Team team : dataHolderTeamAdmin.getAllTeams()) {
            MenuTeamItem item = new MenuTeamItem();
            item.setTeamName(team.getName());
            item.setImageName(this.getResources().getIdentifier(team.getColour(), "drawable", this.getPackageName()));
            TeamMenuItemMap.put(team, item);
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
                Team t = dataHolderTeamAdmin.getTeamFromName(item.getTeamName());
                selectTeam(item);
            }
        });
    }

    public void addTeam(View view) {
        feedbackTextBox.setText("");
        String name = teamNameTextBox.getText().toString();
        String colour = colourSpinner.getSelectedItem().toString();
        String captain = captainNameTextBox.getText().toString();

        if (name.equals("")) {
            feedbackTextBox.setText("You haven't set a team name");
            return;
        }

        if (captain.equals("")) {
            feedbackTextBox.setText("You haven't set a captain");
            return;
        }

        feedbackTextBox.setText("Just adding...");
        Map<String, Object> teamMap = new HashMap<>();
        int ID = dataHolderTeamAdmin.getNextTeamID();
        teamMap.put("ID", ID);
        teamMap.put("Name", name);
        teamMap.put("Colour", colour);
        teamMap.put("Captain", captain);
        dbConnectionHandlerTeamAdmin.addDocument("teams", teamMap);
    }

    public void changeTeam(View view) {
        feedbackTextBox.setText("Changing team...");
        Team team = dataHolderTeamAdmin.getTeamFromName(teamSelected);
        String fsRef = team.getFirestoreReference();
        String name = teamNameTextBox.getText().toString();
        String captain = captainNameTextBox.getText().toString();
        String colour = colourSpinner.getSelectedItem().toString();

        if (!team.getName().equals(name)) {
            dbConnectionHandlerTeamAdmin.updateDocumentStringField("teams", fsRef, "Name", name);
        }
        if (!team.getCaptain().equals(captain)) {
            dbConnectionHandlerTeamAdmin.updateDocumentStringField("teams", fsRef, "Captain", captain);
        }
        if (!team.getColour().equals(colour)) {
            dbConnectionHandlerTeamAdmin.updateDocumentStringField("teams", fsRef, "Colour", colour);
        }
    }

    public void removeTeam(View view) {
        String fsRef = dataHolderTeamAdmin.getTeamFromName(teamSelected).getFirestoreReference();
        dbConnectionHandlerTeamAdmin.deleteDocument("teams", fsRef);
    }

    public void selectTeam(MenuTeamItem i) {
        if (i.getTeamName().equals(teamSelected)) {
            teamSelected = "";
            changeButton.setEnabled(false);
            removeButton.setEnabled(false);
            addButton.setEnabled(true);
            teamNameTextBox.setText("");
            captainNameTextBox.setText("");
        } else {
            teamSelected = i.getTeamName();
            Team team = dataHolderTeamAdmin.getTeamFromName(teamSelected);
            changeButton.setEnabled(true);
            removeButton.setEnabled(true);
            addButton.setEnabled(false);

            teamNameTextBox.setText(i.getTeamName());
            captainNameTextBox.setText(team.getCaptain());
            int spinnerPosition = spinnerAdapter.getPosition(team.getColour());
            colourSpinner.setSelection(spinnerPosition);
        }
    }
}
