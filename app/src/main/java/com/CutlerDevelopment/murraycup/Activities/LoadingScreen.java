

package com.CutlerDevelopment.murraycup.Activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.CutlerDevelopment.murraycup.Models.DataHolder;
import com.CutlerDevelopment.murraycup.Models.Fixture;
import com.CutlerDevelopment.murraycup.Models.Team;
import com.CutlerDevelopment.murraycup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import io.opencensus.tags.Tag;


public class LoadingScreen extends AppCompatActivity {



    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        GetTeams();

    //Nothing more is done when this page is created, except to show it saying "Loading".
        //Once the listener knows the teams documents have been returned we'll then progress.


    }

    //Calls the db and gets all documents in the teams collection, then adds them to the DataHolder Instance which will hold the teams.
    public void GetTeams() {
        db.collection("teams").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {

                                DataHolder.getInstance().AddTeam(new Team(
                                        doc.getLong("ID").intValue(),
                                        doc.getString("Name"),
                                        doc.getString("Captain"),
                                        doc.getString("Colour"),
                                        doc.getLong("Group").intValue(),
                                        doc.getId()));
                            }
                            GetFixtures();
                        }
                    }
                });
    }

    public void GetFixtures() {
        db.collection("fixtures").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {

                                DataHolder.getInstance().AddFixture(new Fixture(
                                        doc.getLong("HomeTeam").intValue(),
                                        doc.getLong("AwayTeam").intValue(),
                                        doc.getLong("HomeScore").intValue(),
                                        doc.getLong("AwayScore").intValue(),
                                        doc.getTimestamp("Time").toDate(),
                                        doc.getLong("Pitch").intValue(),
                                        doc.getId()));
                            }
                            CheckTeamChosen();
                        }
                    }
                });
    }

    //Once the teams are loaded, check whether this app has already chosen a team. If not, load a "Pick a team menu", else load the normal menu.
    void CheckTeamChosen() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(DataHolder.getInstance().GetSharedPrefsName(), 0);
        DataHolder dh = DataHolder.getInstance();
        int teamID = settings.getInt(dh.GetSharedPrefsTeamID(), 0);

        if (teamID == 0) {
            startActivity(new Intent(LoadingScreen.this, PickATeamMenu.class));
            finish();
        }
        else {
            dh.ChooseTeam(dh.GetTeamFromID(teamID));
            startActivity(new Intent(LoadingScreen.this, MainMenu.class));
            finish();

        }
    }




}
