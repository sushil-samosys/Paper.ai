package com.samosys.paperai.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.WorkspaceBean;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by samosys on 2/2/18.
 */

public class SearchWorkspace extends RecyclerView.Adapter<SearchWorkspace.Holder> implements Filterable {
    Context context;
    CustomFonts customFonts;
    ArrayList<WorkspaceBean> listitem;
    ArrayList<WorkspaceBean> filterlist = new ArrayList<>();
    private ContactsFilter mFilter;


    public SearchWorkspace(Context context, ArrayList<WorkspaceBean> listitem) {
        this.context = context;
        this.listitem = listitem;
        this.filterlist = listitem;
        customFonts = new CustomFonts(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_search_item, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final SearchWorkspace.Holder holder, final int position) {


//
        holder.txt_workspace.setText(filterlist.get(position).getWorkspace_name());
        holder.txt_workspace_value.setText(filterlist.get(position).getMission());
        Log.e("IAMGEW", filterlist.get(position).getWs_image());
        Picasso.with(context).load(filterlist.get(position).getWs_image()).error(R.mipmap.sign_up_workspace)
                .into(holder.imgProject, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.img_progrss.setVisibility(View.GONE);

                    }

                    @Override
                    public void onError() {
                        holder.img_progrss.setVisibility(View.GONE);
                    }
                });

//        holder.txt_follow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ParseObject message = new ParseObject("workspace_follower");
//                message.put("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
//                message.put("workspace", ParseObject.createWithoutData("WorkSpace", filterlist.get(position).getObjectId()));
//                message.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        if (e == null) {
//                            Intent intent = new Intent(context, HomeFeedActivity.class);
//                            context.startActivity(intent);
//                        } else {
//                            e.printStackTrace();
//                            Log.e("DATAAA", e.getMessage());
//                        }
//                    }
//                });
//            }
//        });

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
        TextView txt_workspace, txt_workspace_value, txt_follow;
        LinearLayout rl_item;
        ImageView imgProject;
        ProgressBar img_progrss;

        public Holder(View itemView) {
            super(itemView);


            //  txtworkurl = (TextView) itemView.findViewById(R.id.txtworkurl);
            txt_workspace = (TextView) itemView.findViewById(R.id.txt_workspace);
            txt_workspace_value = (TextView) itemView.findViewById(R.id.txt_workspace_value);
            imgProject = (ImageView) itemView.findViewById(R.id.imgProject);
//            rl_item = (LinearLayout) itemView.findViewById(R.id.rl_item);
            img_progrss = (ProgressBar) itemView.findViewById(R.id.img_progrss);

            txt_workspace_value.setTypeface(customFonts.calibri);
//            txt_follow.setTypeface(customFonts.HelveticaNeue);
            //   txtworkurl.setTypeface(customFonts.HelveticaNeue);
            txt_workspace.setTypeface(customFonts.CabinBold);
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
                ArrayList<WorkspaceBean> filteredContacts = new ArrayList<WorkspaceBean>();
                for (WorkspaceBean c : listitem) {
                    Log.e("FIRST!NAME", c.getWorkspace_url().toUpperCase());
                    Log.e("FIRST!NAME12", constraint.toString().toUpperCase());
                    if (c.getWorkspace_url().toUpperCase().contains(constraint.toString().toUpperCase())) {
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
            filterlist = (ArrayList<WorkspaceBean>) results.values;
            notifyDataSetChanged();
        }
    }
}
