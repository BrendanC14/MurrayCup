package com.CutlerDevelopment.murraycup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//Creates a list of View items for each team added to the db
public class PickATeamMenu extends AppCompatActivity {

    ListView myListView;
    ArrayList<MenuTeamItem> myTeamItems;
    DBListener dbListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pick_ateam_menu);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        myTeamItems = new ArrayList<>();
        myListView = findViewById(R.id.teamList);
        fillTeamArrayList();
        AssignListAdapter();

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
        MenuTeamItem item_none = new MenuTeamItem();
        item_none.setTeamName("None");
        item_none.setImageName(R.drawable.none);
        myTeamItems.add(item_none);

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
