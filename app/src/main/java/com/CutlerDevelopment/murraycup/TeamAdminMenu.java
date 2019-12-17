package com.CutlerDevelopment.murraycup;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TeamAdminMenu extends AppCompatActivity {

    TextView teamName;
    Spinner colourName;
    TextView captainName;
    TextView feedback;
    Button addButton;
    Button changeButton;
    Button removeButton;

    DBListener dbListener;

    String teamSelected = "";
    boolean addingTeam = false;

    HashMap<Team, View> TeamViewMap;

    ListView myListView;
    ArrayList<MenuTeamItem> myTeamItems;
    ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_team_admin_menu);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        TeamViewMap = new HashMap<>();
        myTeamItems = new ArrayList<>();
        myListView = findViewById(R.id.teamList);
        fillTeamArrayList();
        AssignListAdapter();

        teamName = findViewById(R.id.nameText);
        colourName = findViewById(R.id.colourDropdown);
        captainName = findViewById(R.id.captainText);
        feedback = findViewById(R.id.Feedback);
        addButton = findViewById(R.id.addTeam);
        changeButton = findViewById(R.id.changeButton);
        removeButton = findViewById(R.id.removeButton);

        dbListener = new DBListener() {
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
        DataHolder.getInstance().dbListener = this.dbListener;

        String [] dropdownItems = new String[]{"red", "blue", "white"};
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, dropdownItems);
        colourName.setAdapter(spinnerAdapter);


    }

    public void NewTeamAdded(Team t) {
        AddOneItemToListDisplay(t.GetName(), t.GetColour());
    }

    public void TeamModified(Team t) {


        myTeamItems = new ArrayList<>();
        fillTeamArrayList();
        AssignListAdapter();
    }

    public void TeamRemoved(Team t) {

        myTeamItems = new ArrayList<>();
        fillTeamArrayList();
        AssignListAdapter();
    }

    private void fillTeamArrayList() {

        for(Team t : DataHolder.getInstance().GetAllTeams()) {
            MenuTeamItem item = new MenuTeamItem();
            item.setTeamName(t.GetName());
            item.setImageName(this.getResources().getIdentifier(t.GetColour(), "drawable", this.getPackageName()));
            myTeamItems.add(item);
        }


    }

    public void AddOneItemToListDisplay(String name, String colour) {
        MenuTeamItem item = new MenuTeamItem();
        item.setTeamName(name);
        item.setImageName(this.getResources().getIdentifier(colour, "drawable", this.getPackageName()));
        myTeamItems.add(item);

        AssignListAdapter();
    }

    public void AssignListAdapter() {

        MenuItemAdapter myAdapter = new MenuItemAdapter(getApplicationContext(), myTeamItems);
        myListView.setAdapter(myAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MenuTeamItem item = myTeamItems.get(i);
                Team t = DataHolder.getInstance().GetTeamFromName(item.getTeamName());
                if (!TeamViewMap.containsKey(t)) {
                    TeamViewMap.put(t, view);
                }
                SelectTeam(item);
            }
        });
    }

    public void AddTeam(View view) {


        feedback.setText("");
        if (addingTeam) {
            feedback.setText("Still adding the last team, please wait");
            return;
        }
        String name = teamName.getText().toString();
        String colour = colourName.getSelectedItem().toString();
        String captain = captainName.getText().toString();

        if (name.equals("")) {
            feedback.setText("You haven't set a team name");
            return;
        }

        if (captain.equals("")) {
            feedback.setText("You haven't set a captain");
            return;
        }

        addingTeam = true;
        feedback.setText("Just adding...");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> team = new HashMap<>();
        int ID = DataHolder.getInstance().GetAllTeams().size() + 1;
        team.put("ID", ID);
        team.put("Name", name);
        team.put("Colour", colour);
        team.put("Captain", captain);

        db.collection("teams").document()
                .set(team)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        feedback.setText("Team added");
                        teamName.setText("");
                        captainName.setText("");
                        addingTeam = false;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        feedback.setText("There was a problem with the last team, please try again.");

                    }
                });

    }

    public void ChangeTeam(View view) {

        addingTeam = true;
        feedback.setText("Changing team...");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String name = teamName.getText().toString();
        String captain = captainName.getText().toString();
        String colour = colourName.getSelectedItem().toString();
        String fsRef = DataHolder.getInstance().GetTeamFromName(teamSelected).GetFirestoreReference();
        DocumentReference team = db.collection("teams").document(fsRef);

        team.update("Name", name, "Captain", captain, "Colour", colour)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        teamSelected = "";
                        changeButton.setEnabled(false);
                        addButton.setEnabled(true);
                        teamName.setText("");
                        captainName.setText("");
                        feedback.setText( "Team changed");
                        addingTeam = false;
                    }
                }
                );

    }

    public void RemoveTeam(View view) {

        addingTeam = true;
        feedback.setText("Removing team...");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String fsRef = DataHolder.getInstance().GetTeamFromName(teamSelected).GetFirestoreReference();
        DocumentReference team = db.collection("teams").document(fsRef);

        team.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                teamSelected = "";
                removeButton.setEnabled(false);
                addButton.setEnabled(true);
                teamName.setText("");
                captainName.setText("");
                feedback.setText("Team removed");
                addingTeam = false;

            }
        });

    }

    public void SelectTeam(MenuTeamItem i) {


        if (i != null) {
            if (i.getTeamName().equals(teamSelected)) {
                teamSelected = "";
                changeButton.setEnabled(false);
                removeButton.setEnabled(false);
                addButton.setEnabled(true);
                teamName.setText("");
                captainName.setText("");
            }
            else {
                teamSelected = i.getTeamName();
                Team t = DataHolder.getInstance().GetTeamFromName(teamSelected);
                changeButton.setEnabled(true);
                removeButton.setEnabled(true);
                addButton.setEnabled(false);

                teamName.setText(i.getTeamName());
                captainName.setText(t.GetCaptain());
                int spinnerPosition = spinnerAdapter.getPosition(t.GetColour());
                colourName.setSelection(spinnerPosition);

            }
        }

    }
}
