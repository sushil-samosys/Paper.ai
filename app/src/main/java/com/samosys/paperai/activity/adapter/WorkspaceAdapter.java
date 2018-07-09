package com.samosys.paperai.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.WorkspaceBean;
import com.samosys.paperai.activity.activity.HomeFeedActivity;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.First_Char_Capital;
import com.samosys.paperai.activity.utils.ShapedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by samosys on 31/1/18.
 */

public class WorkspaceAdapter extends RecyclerView.Adapter<WorkspaceAdapter.Holder> {
    //     int radius = 5;
//     int margin = 5;
//     RoundedCornersTransformation.Corners corners;
    Context context;
    CustomFonts customFonts;
    ArrayList<WorkspaceBean> listitem;
    TextView workspace_name;
    private int mSelectedItem = 0;


    public WorkspaceAdapter(Context context, ArrayList<WorkspaceBean> listitem, TextView workspace_name) {
        this.context = context;
        this.listitem = listitem;
        customFonts = new CustomFonts(context);
        this.workspace_name = workspace_name;


    }

    public void setSelectedItem(int selectedItem) {
        mSelectedItem = selectedItem;
        AppConstants.savePreferences(context, "position", "" + selectedItem);
        AppConstants.workPOS = selectedItem;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_contact_view, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

        Picasso.with(context).load(listitem.get(position).getWs_image()).error(R.mipmap.sign_up_workspace)
                .into(holder.imgwork, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.img_progrss.setVisibility(View.GONE);

                    }

                    @Override
                    public void onError() {
                        holder.img_progrss.setVisibility(View.GONE);
                    }
                });

        Log.e("workspaceID", ">>>>>" + listitem.get(position).getObjectId());
        int pos = Integer.parseInt(AppConstants.loadPreferences(context, "position"));
        Log.e("workspaceID", ">>>>>" + pos);
        if (pos == 0) {

            if (position == mSelectedItem) {
               // if (AppConstants.loadPreferences(context, "workid").equals("00")) {

                    AppConstants.savePreferences(context, "workid", listitem.get(position).getObjectId());
                    AppConstants.savePreferences(context, "workname", listitem.get(position).getWorkspace_name());
                    workspace_name.setText(First_Char_Capital.capitalizeString(listitem.get(position).getWorkspace_name()));
                    holder.txtSelect.setVisibility(View.VISIBLE);
                    if (context instanceof HomeFeedActivity) {
//                    ((HomeFeedActivity)context).getworkspace("");
                        ((HomeFeedActivity) context).getprojectlist();
//                    ((HomeFeedActivity)context).getfeedpost();

//                    ((HomeFeedActivity)context).getuncatproject();
                        // ((HomeFeedActivity)context).getcatogory();


                    }
                //}
            } else {
                holder.txtSelect.setVisibility(View.INVISIBLE);
            }
        } else {
            if (pos == position) {


                workspace_name.setText(First_Char_Capital.capitalizeString(listitem.get(pos).getWorkspace_name()));
                AppConstants.savePreferences(context, "workid", listitem.get(pos).getObjectId());
                AppConstants.savePreferences(context, "workname", listitem.get(pos).getWorkspace_url());
                Log.e("workspaceID", "else>>>>>" + AppConstants.loadPreferences(context, "workid"));
                holder.txtSelect.setVisibility(View.VISIBLE);
                if (context instanceof HomeFeedActivity) {
//                    getuncatproject();
//                    getcatogory();
//                    ((HomeFeedActivity)context).getworkspace("1");
                    ((HomeFeedActivity) context).getprojectlist();
                    // ((HomeFeedActivity)context).getcatogory();
//                    ((HomeFeedActivity)context).getuncatproject();
                }
            } else {
                holder.txtSelect.setVisibility(View.INVISIBLE);
            }
        }


    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        TextView txtSelect;
        LinearLayout rl_item;
        ProgressBar img_progrss;
        ShapedImageView imgwork;

        public Holder(View itemView) {
            super(itemView);
            customFonts = new CustomFonts(context);
            imgwork = (ShapedImageView) itemView.findViewById(R.id.imgwork);
            rl_item = (LinearLayout) itemView.findViewById(R.id.rl_item);
            txtSelect = (TextView) itemView.findViewById(R.id.txtSelect);

            img_progrss = (ProgressBar) itemView.findViewById(R.id.img_progrss);
        }
    }


}
