package com.samosys.paperai.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.Myfollowlist;
import com.samosys.paperai.activity.Bean.WorkspaceBean;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.First_Char_Capital;

import java.util.ArrayList;

/**
 * Created by samosys on 16/2/18.
 */

public class MyFollowsAdapter extends RecyclerView.Adapter<MyFollowsAdapter.Holder> implements Filterable {
    private  Context context;
    private  CustomFonts customFonts;
    private  ArrayList<Myfollowlist> filterlist = new ArrayList<>();
    private  ArrayList<Myfollowlist> listitem;

    ContactsFilter mFilter;
    int selectedPosition = 0;

    public MyFollowsAdapter(Context context, ArrayList<Myfollowlist> listitem ) {
        this.context = context;

        this.listitem = listitem;
        this.filterlist = listitem;
        customFonts = new CustomFonts(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_contact_view12, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

        holder.ll_follow.setVisibility(View.VISIBLE);

        if (filterlist.size() == 1) {

            AppConstants.savePreferences(context, "workid", filterlist.get(0).getObjectId());
            AppConstants.savePreferences(context, "workname", filterlist.get(0).getWorkspace_url());
        }
        int pos = Integer.parseInt(AppConstants.loadPreferences(context, "position"));
        if (AppConstants.loadPreferences(context, "position").equals("00")) {
            if (selectedPosition == position)
                holder.txtSelect.setVisibility(View.VISIBLE);
            else
                holder.txtSelect.setVisibility(View.INVISIBLE);
        } else if (pos == position) {
            holder.txtSelect.setVisibility(View.VISIBLE);
        } else {
            holder.txtSelect.setVisibility(View.INVISIBLE);
        }

        holder.txtworkurl.setText(filterlist.get(position).getWorkspace_url());
        holder.txtworkName.setText(filterlist.get(position).getWorkspace_name());
        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedPosition = position;
                AppConstants.savePreferences(context, "projpos", "" + "00");
                AppConstants.savePreferences(context, "position", "" + selectedPosition);
                AppConstants.savePreferences(context, "workid", filterlist.get(position).getObjectId());
                AppConstants.savePreferences(context, "workname", filterlist.get(position).getWorkspace_url());
                notifyDataSetChanged();
            }
        });
        if (position == filterlist.size() - 1) {
            holder.detail_view.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return filterlist.size();
    }


    @Override
    public Filter getFilter() {
        if (mFilter == null)
            mFilter = new ContactsFilter();

        return mFilter;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txtworkName, txtSelect, txtworkurl, txt_follow;
        LinearLayout rl_item, ll_follow;
        View detail_view;

        public Holder(View itemView) {
            super(itemView);
            customFonts = new CustomFonts(context);
            txtworkName = (TextView) itemView.findViewById(R.id.txtworkName);
            txtSelect = (TextView) itemView.findViewById(R.id.txtSelect);
            txtworkurl = (TextView) itemView.findViewById(R.id.txtworkurl);
            detail_view = (View) itemView.findViewById(R.id.detail_view);
            txt_follow = (TextView) itemView.findViewById(R.id.txt_follow);
            rl_item = (LinearLayout) itemView.findViewById(R.id.rl_item);
            ll_follow = (LinearLayout) itemView.findViewById(R.id.ll_follow);

            //  txtworkurl.setTextColor(context.getResources().getColor(R.color.txt_light));
            txtworkurl.setTypeface(customFonts.HelveticaNeueMedium);
            txtworkName.setTypeface(customFonts.HelveticaNeueMedium);
            txt_follow.setTypeface(customFonts.HelveticaNeueMedium);

        }
    }

    private class ContactsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.values = listitem;
                results.count = listitem.size();
            } else {
                ArrayList<Myfollowlist> filteredContacts = new ArrayList<Myfollowlist>();
                for (Myfollowlist c : listitem) {
                    Log.e("FIRST!NAME", c.getWorkspace_url().toUpperCase());
                    Log.e("FIRST!NAME12", constraint.toString().toUpperCase());
                    if (c.getWorkspace_name().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        filteredContacts.add(c);
                    }
                }
                results.values = filteredContacts;
                results.count = filteredContacts.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterlist = (ArrayList<Myfollowlist>) results.values;
            notifyDataSetChanged();
        }
    }

}
