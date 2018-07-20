package com.samosys.paperai.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.ProjctBean;
import com.samosys.paperai.activity.activity.ProjectDetailActivity;
import com.samosys.paperai.activity.activity.ProjectSettingActivity;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.zjy.actionsheet.ActionSheet;

/**
 * Created by samosys on 2/2/18.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.Holder> {
    private Context context;
    private CustomFonts customFonts;
    private ArrayList<ProjctBean> listitem;


    public ProjectAdapter(Context context, ArrayList<ProjctBean> listitem, String frag) {
        this.context = context;
        this.listitem = listitem;

        customFonts = new CustomFonts(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_item, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {


        holder.txt_project_value.setText(listitem.get(position).getName());

        if (listitem.get(position).getImage() != null) {

            Picasso.with(context).load(listitem.get(position).getImage()).error(R.mipmap.sign_up_workspace)
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

        }
        holder.ll_projectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                delete(position);
            }
        });


    }

    private void delete(final int position) {
        ActionSheet actionSheet = new ActionSheet.Builder()
                .setOtherBtn(new String[]{"View Project", "Delete Project"}, new int[]{Color.parseColor("#4C4C4C"), Color.parseColor("#4C4C4C")})
                .setCancelBtn("Cancel", Color.parseColor("#4C4C4C"))
                .setCancelableOnTouchOutside(true)
                .setActionSheetListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isByBtn) {
                        // Toast.makeText(HomeFeedActivity.this, "onDismiss: " + isByBtn, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onButtonClicked(ActionSheet actionSheet, int index) {
                        if (index == 0) {
                            Intent intent = new Intent(context, ProjectDetailActivity.class);
                            intent.putExtra("id", listitem.get(position).getObjectId());
                            context.startActivity(intent);
                        }
                        if (index == 1) {


                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
                            query.getInBackground(listitem.get(position).getObjectId(), new GetCallback<ParseObject>() {
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null) {
                                        object.deleteInBackground();
                                        Toast.makeText(context, "Delete Projct Successfully", Toast.LENGTH_SHORT).show();
                                        ((ProjectSettingActivity) context).getprojectlist();

                                    } else {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }


                        //  Toast.makeText(HomeFeedActivity.this, "onButtonClicked: " + index, Toast.LENGTH_SHORT).show();
                    }
                }).build();

        actionSheet.show(((Activity) context).getFragmentManager());
    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        TextView txt_project_value, txt_project;
        ProgressBar img_progrss;
        LinearLayout ll_projectName;
        ImageView imgProject;

        public Holder(View itemView) {
            super(itemView);
            customFonts = new CustomFonts(context);
            txt_project = (TextView) itemView.findViewById(R.id.txt_project);
            txt_project_value = (TextView) itemView.findViewById(R.id.txt_project_value);
            img_progrss = (ProgressBar) itemView.findViewById(R.id.img_progrss);
            imgProject = (ImageView) itemView.findViewById(R.id.imgProject);
            ll_projectName = (LinearLayout) itemView.findViewById(R.id.ll_projectName);

            txt_project_value.setTypeface(customFonts.CabinRegular);
            txt_project.setTypeface(customFonts.CabinBold);

        }
    }


}
