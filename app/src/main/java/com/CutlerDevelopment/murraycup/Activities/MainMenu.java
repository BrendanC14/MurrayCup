package com.CutlerDevelopment.murraycup.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.CutlerDevelopment.murraycup.Models.DataHolder;
import com.CutlerDevelopment.murraycup.Models.Team;
import com.CutlerDevelopment.murraycup.R;

public class MainMenu extends AppCompatActivity {

    TextView teamName;
    ImageView teamColour;
    Team chosenTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        chosenTeam = DataHolder.getInstance().GetChosenTeam();

        teamName = findViewById(R.id.teamName);
        teamColour = findViewById(R.id.teamColour);

        if (chosenTeam == null) {
            teamName.setText("None");
            teamColour.setImageResource(R.drawable.none);
        }
        else {
            teamName.setText(chosenTeam.GetName());
            teamColour.setImageResource((this.getResources().getIdentifier(chosenTeam.GetColour(), "drawable", this.getPackageName())));

        }

    }

    public void ChangeTeam(View view) {
        startActivity(new Intent(MainMenu.this, PickATeamMenu.class));
    }

    public void OpenAdminMenu(View view) {
        startActivity(new Intent(MainMenu.this, AdminMenu.class));
    }

}
