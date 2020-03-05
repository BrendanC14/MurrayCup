package com.CutlerDevelopment.murraycup.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.CutlerDevelopment.murraycup.R;

import java.util.ArrayList;

public class MenuFixtureAdapter extends BaseAdapter {


    private ArrayList<MenuFixtureItem> singleRow;
    private LayoutInflater thisInflater;

    public MenuFixtureAdapter(Context context, ArrayList<MenuFixtureItem> aRow) {
        this.singleRow = aRow;
        thisInflater = ( LayoutInflater.from(context));
    }
    @Override
    public int getCount() {
        return singleRow.size();
    }

    @Override
    public Object getItem(int i) {
        return singleRow.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = thisInflater.inflate(R.layout.menu_fixture_view, parent, false);

        }

        TextView pitchNumber = convertView.findViewById(R.id.Pitch);
        TextView homeTeam = convertView.findViewById(R.id.HomeTeam);
        TextView homeScore = convertView.findViewById(R.id.HomeScore);
        TextView awayScore = convertView.findViewById(R.id.AwayScore);
        TextView awayTeam = convertView.findViewById(R.id.AwayTeam);
        TextView time = convertView.findViewById(R.id.Time);

        MenuFixtureItem currentItem = (MenuFixtureItem) getItem(i);

        pitchNumber.setText(currentItem.getPitch());
        homeTeam.setText(currentItem.getHomeTeam());
        homeScore.setText(currentItem.getHomeScore());
        awayScore.setText(currentItem.getAwayScore());
        awayTeam.setText(currentItem.getAwayTeam());
        time.setText(currentItem.getTime().toString());
        return convertView;
    }
}
