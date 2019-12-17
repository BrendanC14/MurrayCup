package com.CutlerDevelopment.murraycup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuItemAdapter extends BaseAdapter {

    private ArrayList<MenuTeamItem> singleRow;
    private LayoutInflater thisInflater;

    public MenuItemAdapter(Context context, ArrayList<MenuTeamItem> aRow) {
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
            convertView = thisInflater.inflate(R.layout.menu_team_view, parent, false);

        }

        TextView teamName = convertView.findViewById(R.id.teamName);
        ImageView teamColour = convertView.findViewById(R.id.teamColour);

        MenuTeamItem currentItem = (MenuTeamItem) getItem(i);

        teamName.setText(currentItem.getTeamName());
        teamColour.setImageResource(currentItem.getImageName());
        return convertView;
    }
}
