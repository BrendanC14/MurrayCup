package com.CutlerDevelopment.murraycup.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.CutlerDevelopment.murraycup.R;

import java.util.ArrayList;

public class MenuGroupAdapter extends BaseAdapter {

    private ArrayList<MenuGroupItem> singleRow;
    private LayoutInflater thisInflater;

    public MenuGroupAdapter(Context context, ArrayList<MenuGroupItem> aRow) {
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
            convertView = thisInflater.inflate(R.layout.menu_group_view, parent, false);

        }

        TextView teamName = convertView.findViewById(R.id.TeamName);
        TextView wins = convertView.findViewById(R.id.Wins);
        TextView draws = convertView.findViewById(R.id.Draws);
        TextView losses = convertView.findViewById(R.id.Losses);
        TextView scored = convertView.findViewById(R.id.Scored);
        TextView conceded = convertView.findViewById(R.id.Conceded);
        TextView difference = convertView.findViewById(R.id.Difference);
        TextView points = convertView.findViewById(R.id.Points);

        MenuGroupItem currentItem = (MenuGroupItem) getItem(i);

        teamName.setText(currentItem.getTeamName());
        wins.setText(currentItem.getWins());
        draws.setText(currentItem.getDraws());
        losses.setText(currentItem.getLosses());
        scored.setText(currentItem.getScored());
        conceded.setText(currentItem.getConceded());
        difference.setText(currentItem.getDifference());
        points.setText(currentItem.getPoints());
        return convertView;
    }
}
