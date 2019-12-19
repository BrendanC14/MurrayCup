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
    DatabaseConnectionHandler dbcHandler;

    String teamSelected = "";
    Map<Team, MenuTeamItem> TeamMenuItemMap;

    ListView myListView;
    ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_team_admin_menu);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        dbcHandler = new DatabaseConnectionHandler();
        TeamMenuItemMap = new HashMap<>();
        myListView = findViewById(R.id.teamList);
        fillTeamMenuItemMap();
        AssignListAdapter();

        teamNameTextBox = findViewById(R.id.nameText);
        colourSpinner = findViewById(R.id.colourDropdown);
        captainNameTextBox = findViewById(R.id.captainText);
        feedbackTextBox = findViewById(R.id.Feedback);
        addButton = findViewById(R.id.addTeam);
        changeButton = findViewById(R.id.changeButton);
        removeButton = findViewById(R.id.removeButton);

        teamDbListener = new TeamDBListener() {
            @Override
            public void teamCreated(Team t) { NewTeamAdded(t); }

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

        String [] dropdownItems = new String[]{"red", "blue", "white"};
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, dropdownItems);
        colourSpinner.setAdapter(spinnerAdapter);


    }

    public void NewTeamAdded(Team t) {

        MenuTeamItem item = new MenuTeamItem();
        item.setTeamName(t.GetName());
        item.setImageName(this.getResources().getIdentifier(t.GetColour(), "drawable", this.getPackageName()));
        TeamMenuItemMap.put(t, item);

        feedbackTextBox.setText(t.GetName() + " has been added");

        AssignListAdapter();
    }

    public void TeamModified(Team t) {

        MenuTeamItem item = TeamMenuItemMap.get(t);
        item.setTeamName(t.GetName());
        item.setImageName(this.getResources().getIdentifier(t.GetColour(), "drawable", this.getPackageName()));
        feedbackTextBox.setText(t.GetName() + " has been modified");
        AssignListAdapter();
    }

    public void TeamRemoved(Team t) {

        TeamMenuItemMap.remove(t);
        feedbackTextBox.setText(t.GetName() + " has been removed");
        teamSelected = "";
        AssignListAdapter();
    }

    private void fillTeamMenuItemMap() {

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
                Team t = DataHolder.getInstance().GetTeamFromName(item.getTeamName());
                SelectTeam(item);
            }
        });
    }

    public void AddTeam(View view) {


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
        int ID = DataHolder.getInstance().GetNextTeamID();
        teamMap.put("ID", ID);
        teamMap.put("Name", name);
        teamMap.put("Colour", colour);
        teamMap.put("Captain", captain);

        dbcHandler.AddDocument("teams", teamMap);

    }

    public void ChangeTeam(View view) {

        feedbackTextBox.setText("Changing team...");
        Team t = DataHolder.getInstance().GetTeamFromName(teamSelected);
        String fsRef = t.GetFirestoreReference();
        String name = teamNameTextBox.getText().toString();
        String captain = captainNameTextBox.getText().toString();
        String colour = colourSpinner.getSelectedItem().toString();

        if (!t.GetName().equals(name)) {
            dbcHandler.UpdateDocumentStringField("teams", fsRef, "Name", name);
        }
        if (!t.GetCaptain().equals(captain)) {
            dbcHandler.UpdateDocumentStringField("teams", fsRef, "Captain", captain);
        }
        if (!t.GetColour().equals(colour)) {
            dbcHandler.UpdateDocumentStringField("teams", fsRef, "Colour", colour);
        }

    }

    public void RemoveTeam(View view) {

        String fsRef = DataHolder.getInstance().GetTeamFromName(teamSelected).GetFirestoreReference();
        dbcHandler.DeleteDocument("teams", fsRef);

    }

    public void SelectTeam(MenuTeamItem i) {


        if (i.getTeamName().equals(teamSelected)) {
            teamSelected = "";
            changeButton.setEnabled(false);
            removeButton.setEnabled(false);
            addButton.setEnabled(true);
            teamNameTextBox.setText("");
            captainNameTextBox.setText("");
        } else {
            teamSelected = i.getTeamName();
            Team t = DataHolder.getInstance().GetTeamFromName(teamSelected);
            changeButton.setEnabled(true);
            removeButton.setEnabled(true);
            addButton.setEnabled(false);

            teamNameTextBox.setText(i.getTeamName());
            captainNameTextBox.setText(t.GetCaptain());
            int spinnerPosition = spinnerAdapter.getPosition(t.GetColour());
            colourSpinner.setSelection(spinnerPosition);

        }


    }
}
