package com.CutlerDevelopment.murraycup.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.CutlerDevelopment.murraycup.Models.DataHolder;
import com.CutlerDevelopment.murraycup.Models.DatabaseConnectionHandler;
import com.CutlerDevelopment.murraycup.Models.Fixture;
import com.CutlerDevelopment.murraycup.Models.Team;
import com.CutlerDevelopment.murraycup.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FixtureAdminMenu extends AppCompatActivity {

    DatabaseConnectionHandler dbcHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fixture_admin_menu);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        dbcHandler = new DatabaseConnectionHandler();

    }


    public void CreateFixtures(View view) {

        for (Fixture f : DataHolder.getInstance().GetAllFixtures()) {
            dbcHandler.DeleteDocument("fixtures", f.getFirestoreReference());
        }

        for (Map.Entry<Integer, ArrayList<Team>> entry : DataHolder.getInstance().GetAllGroups().entrySet()) {
            int teamCount = entry.getValue().size();

            for (int homeTeam = 0; homeTeam < teamCount; homeTeam++) {
                for (int awayTeam = homeTeam + 1; awayTeam < teamCount; awayTeam++) {
                    int homeID = entry.getValue().get(homeTeam).getID();
                    int awayID = entry.getValue().get(awayTeam).getID();
                    Date time = new Date(2020,7,21,9,15,00);
                    int pitch = entry.getValue().get(homeTeam).getGroup();

                    Map<String, Object> fixtureMap = new HashMap<>();
                    fixtureMap.put("HomeTeam", homeID);
                    fixtureMap.put("HomeScore", -1);
                    fixtureMap.put("AwayTeam", awayID);
                    fixtureMap.put("AwayScore", -1);
                    fixtureMap.put("Time", time);
                    fixtureMap.put("Pitch", pitch);

                    dbcHandler.AddDocument("fixtures", fixtureMap);
                }
            }
        }

    }

}
