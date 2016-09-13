package com.epfl.dedis.cisc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.epfl.dedis.crypto.Utils;
import com.epfl.dedis.net.Config;
import com.epfl.dedis.net.Identity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceActivity extends AppCompatActivity implements Activity {

    public void taskJoin() {}
    public void taskFail(int error) {}

    // TODO: Find nicer way for expandable list?
    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context context;
        private List<String> expandableListTitle;
        private Map<String, List<String>> expandableListDetail;

        public ExpandableListAdapter(Context context, List<String> expandableListTitle,
                                     Map<String, List<String>> expandableListDetail) {
            this.context = context;
            this.expandableListTitle = expandableListTitle;
            this.expandableListDetail = expandableListDetail;
        }

        @Override
        public Object getChild(int listPosition, int expandedListPosition) {
            return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                    .get(expandedListPosition);
        }

        @Override
        public long getChildId(int listPosition, int expandedListPosition) {
            return expandedListPosition;
        }

        @Override
        public View getChildView(int listPosition, final int expandedListPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final String expandedListText = (String) getChild(listPosition, expandedListPosition);
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_item, parent, false);
            }
            TextView expandedListTextView = (TextView) convertView
                    .findViewById(R.id.expandedListItem);
            expandedListTextView.setText(expandedListText);
            return convertView;
        }

        @Override
        public int getChildrenCount(int listPosition) {
            return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                    .size();
        }

        @Override
        public Object getGroup(int listPosition) {
            return this.expandableListTitle.get(listPosition);
        }

        @Override
        public int getGroupCount() {
            return this.expandableListTitle.size();
        }

        @Override
        public long getGroupId(int listPosition) {
            return listPosition;
        }

        @Override
        public View getGroupView(int listPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String listTitle = (String) getGroup(listPosition);
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context.
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_group, parent, false);
            }
            TextView listTitleTextView = (TextView) convertView
                    .findViewById(R.id.listTitle);
            listTitleTextView.setTypeface(null, Typeface.BOLD);
            listTitleTextView.setText(listTitle);
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int listPosition, int expandedListPosition) {
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.device_device_list);

        SharedPreferences sharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        Config config = Utils.fromJson(sharedPreferences.getString(IDENTITY, ""), Identity.class).getConfig();

        Map<String, String> devices = config.getDevice();
        Map<String, String> data = config.getData();

        List<String> headers = new ArrayList<>(devices.keySet());
        Map<String, List<String>> children = new HashMap<>();

        for (Map.Entry<String, String> entry : devices.entrySet()) {
            List<String> singleton = new ArrayList<>();
            singleton.add(entry.getValue());
            children.put(entry.getKey(), singleton);
        }

        // TODO: Cothority doesn't create data entry with equal key
        for (Map.Entry<String, String> entry : data.entrySet()) {
            for (Map.Entry<String, String> e : devices.entrySet()) {
                if (entry.getKey().contains(e.getKey())) {
                    children.get(e.getKey()).add(entry.getValue());
                }
            }
        }
        ExpandableListAdapter ela = new ExpandableListAdapter(this, headers, children);
        expandableListView.setAdapter(ela);
    }
}