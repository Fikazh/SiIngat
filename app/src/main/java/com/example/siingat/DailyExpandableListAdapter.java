package com.example.siingat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class DailyExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Map<String, List<Daily>> dailyCollection;
    private List<String> daysList;

    public DailyExpandableListAdapter(Context context, List<String> daysList,
                                      Map<String, List<Daily>> dailyCollection){
        this.context = context;
        this.dailyCollection = dailyCollection;
        this.daysList = daysList;
    }

    @Override
    public int getGroupCount() {
        return dailyCollection.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return dailyCollection.get(daysList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return daysList.get(i);
    }

    @Override
    public Daily getChild(int i, int i1) {
        return dailyCollection.get(daysList.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String dayName = getGroup(i).toString();
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.daily_group_item, null);
        }
        TextView item = view.findViewById(R.id.dayTitle);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(dayName);
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        Daily daily = getChild(i, i1);
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.daily_child_item, null);
        }
        TextView item = view.findViewById(R.id.childDaily);
        ImageView delete = view.findViewById(R.id.delete);
        item.setText(daily.getName() + " " + CalendarUtils.formattedTime(daily.getTime()));
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to remove?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        List<Daily> child = dailyCollection.get(daysList.get(i));
                        child.remove(i1);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
