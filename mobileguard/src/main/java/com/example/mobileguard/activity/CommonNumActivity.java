package com.example.mobileguard.activity;

import java.util.ArrayList;

import com.example.mobileguard.R;
import com.example.mobileguard.bean.ComNumChildBean;
import com.example.mobileguard.bean.ComNumGroupBean;
import com.example.mobileguard.db.ComNumDBDao;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

public class CommonNumActivity extends Activity
        implements OnGroupClickListener, OnChildClickListener {

    private ExpandableListView elv;
    private ArrayList<ComNumGroupBean> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_num);
        elv = (ExpandableListView) findViewById(R.id.act_com_num_elv);
        datas = ComNumDBDao.getData(this);
        adapter = new CommonNumAdapter();
        elv.setAdapter(adapter);
        System.out.println(datas.size());
        elv.setOnGroupClickListener(this);
        elv.setOnChildClickListener(this);
    }

    class CommonNumAdapter extends BaseExpandableListAdapter {

        private TextView textView;

        @Override
        public int getGroupCount() {
            // TODO Auto-generated method stub
            return datas == null ? 0 : datas.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (datas != null) {
                if (datas.get(groupPosition) != null) {
                    return datas.get(groupPosition).childrens.size();
                }
            }
            return 0;
        }

        @Override
        public ComNumGroupBean getGroup(int groupPosition) {
            if (datas != null) {

                return datas.get(groupPosition);

            }
            return null;
        }

        @Override
        public ComNumChildBean getChild(int groupPosition, int childPosition) {
            if (datas != null) {
                if (getGroup(groupPosition) != null) {
                    return getGroup(groupPosition).childrens.get(childPosition);

                }
            }
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {
            GroupViewHolder groupViewHolder;
            if (convertView == null) {
                convertView =
                        View.inflate(CommonNumActivity.this, R.layout.com_num_item_group, null);
                groupViewHolder = new GroupViewHolder();
                groupViewHolder.tvTextView =
                        (TextView) convertView.findViewById(R.id.com_num_item_group);
                convertView.setTag(groupViewHolder);
            } else {
                groupViewHolder = (GroupViewHolder) convertView.getTag();
            }
            groupViewHolder.tvTextView.setText(getGroup(groupPosition).groupName);
            return convertView;
        }

        class GroupViewHolder {
            TextView tvTextView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {
            ChildViewHolder childViewHolder;
            if (convertView == null) {
                convertView =
                        View.inflate(CommonNumActivity.this, R.layout.com_num_item_child, null);
                childViewHolder = new ChildViewHolder();
                childViewHolder.name =
                        (TextView) convertView.findViewById(R.id.com_num_item_child_name);
                childViewHolder.number =
                        (TextView) convertView.findViewById(R.id.com_num_item_child_number);
                convertView.setTag(childViewHolder);
            } else {
                childViewHolder = (ChildViewHolder) convertView.getTag();
            }
            childViewHolder.name.setText(getChild(groupPosition, childPosition).childName);
            childViewHolder.number.setText(getChild(groupPosition, childPosition).num);
            return convertView;
        }

        class ChildViewHolder {
            TextView name;
            TextView number;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return true;
        }

    }

    private int groupClickItem = -1;
    private CommonNumAdapter adapter;

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if (groupClickItem == -1) {
            elv.expandGroup(groupPosition);
            groupClickItem = groupPosition;
        } else {
            if (groupPosition == groupClickItem) {
                elv.collapseGroup(groupPosition);
                groupClickItem = -1;
            } else {
                elv.collapseGroup(groupClickItem);
                elv.expandGroup(groupPosition);
                groupClickItem = groupPosition;
            }
        }
        return true;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
            int childPosition, long id) {
        ComNumChildBean bean = adapter.getChild(groupPosition, childPosition);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + bean.num));
        startActivity(intent);
        return true;
    }
}
