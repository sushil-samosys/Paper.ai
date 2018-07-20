package com.samosys.paperai.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.ChildBean;
import com.samosys.paperai.activity.Bean.ParentBean;
import com.samosys.paperai.activity.Bean.ProjctBean;
import com.samosys.paperai.activity.activity.HomeFeedActivity;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by samosys on 1/5/18.
 */

public class MenuProjectAdapter extends BaseExpandableListAdapter {
    int SELECTEDPOSITION = 0;
    private ArrayList<ProjctBean> projectList;
    private HashMap<String, List<String>> expandableListDetail;
    private CustomFonts customFonts;
    private ExpandableListView project_expandableList;
    private Context context;
    private ArrayList<ParentBean> parentBeans;

    public MenuProjectAdapter(HomeFeedActivity homeFeedActivity, ArrayList<ProjctBean> projectList,
                              HashMap<String, List<String>> expandableListDetail,
                              ArrayList<ParentBean> parentBeans, ExpandableListView project_expandableList) {
        this.context = homeFeedActivity;
        this.projectList = projectList;
        this.expandableListDetail = expandableListDetail;
        customFonts = new CustomFonts(context);
        this.parentBeans = parentBeans;
        this.project_expandableList = project_expandableList;

    }

    public void setSelectedItem(int selectedItem) {

        AppConstants.savePreferences(context, "proPosition", "" + selectedItem);

        SELECTEDPOSITION = selectedItem;
    }

    @Override
    public int getGroupCount() {
        return this.parentBeans.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ChildBean> chList = parentBeans.get(groupPosition)
                .getChildBeans();

        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (parentBeans.size() > 0) {
            return this.parentBeans.get(groupPosition);
        } else {
            return 0;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ChildBean> chList = parentBeans.get(groupPosition)
                .getChildBeans();
        return chList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ParentBean group = (ParentBean) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.header_group, null);
        }

        ImageView img_expand = (ImageView) convertView.findViewById(R.id.img_expand);

        if (groupPosition == 0) {

            project_expandableList.setGroupIndicator(null);

        }


        if (group.getCategoryName().equals("My Notes")) {
            img_expand.setImageResource(R.mipmap.home_lock);
        }

//

        if (parentBeans.get(groupPosition).getParentType().equals("2")) {
            img_expand.setVisibility(View.VISIBLE);
            img_expand.setImageResource(R.mipmap.home_lock);
        } else {
            img_expand.setVisibility(View.INVISIBLE);

        }


        if (groupPosition == 2) {

            project_expandableList.setGroupIndicator(null);

        }

        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setText(group.getCategoryName());


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ChildBean child = (ChildBean) getChild(groupPosition,
                childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.child_view_item, null);
        }
        ImageView img_proLock = (ImageView) convertView.findViewById(R.id.img_proLock);
        if (child.getChildType().equals("2")) {
            img_proLock.setVisibility(View.VISIBLE);
        } else {
            img_proLock.setVisibility(View.INVISIBLE);
        }


        TextView txtChildTitle = (TextView) convertView.findViewById(R.id.txtChildTitle);
        txtChildTitle.setText(child.getChildName());
        txtChildTitle.setTypeface(customFonts.CabinRegular);


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
