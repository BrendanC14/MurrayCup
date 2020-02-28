package com.CutlerDevelopment.murraycup.Activities;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.core.app.NavUtils;

import com.CutlerDevelopment.murraycup.Models.DataHolder;
import com.CutlerDevelopment.murraycup.Models.DatabaseConnectionHandler;
import com.CutlerDevelopment.murraycup.Models.Team;
import com.CutlerDevelopment.murraycup.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GroupAdminMenu extends AppCompatActivity {

    DatabaseConnectionHandler dbcHandler;
    TextView feedback;
    TextView numGroupsTextBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_admin_menu);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        dbcHandler = new DatabaseConnectionHandler();
        feedback = findViewById(R.id.feedback);
        numGroupsTextBox = findViewById(R.id.numGroups);
    }

    public void AutoSortGroups (View view) {


        int numGroups = Integer.parseInt(numGroupsTextBox.getText().toString());
        List<Team> allTeams = DataHolder.getInstance().GetAllTeams();

        HashMap<String, ArrayList<Team>> teamColourMap = new HashMap<String, ArrayList<Team>>();

        for (Team t : allTeams) {
            String colour = t.GetColour();

            if (!teamColourMap.containsKey(colour)) {
                teamColourMap.put(colour, new ArrayList<Team>());
            }
            teamColourMap.get(colour).add(t);
        }


        int group = 1;
        for (Map.Entry<String, ArrayList<Team>> entry : teamColourMap.entrySet()) {
            for (Team t : entry.getValue()) {
                DataHolder.getInstance().AddTeamToGroup(group, t);
                t.SetGroup(group);
                dbcHandler.UpdateDocumentIntField("teams", t.GetFirestoreReference(), "Group", group);
                group++;
                if (group > numGroups) { group = 1; }
            }
        }
    }
}
