package com.samosys.paperai.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

import cn.zjy.actionsheet.ActionSheet;

/**
 * Created by samosys on 15/6/18.
 */

public class ArchiveProjectAdapter extends RecyclerView.Adapter<ArchiveProjectAdapter.Holder> {
    private Context context;
    private CustomFonts customFonts;
    private ArrayList<ProjctBean> listitem;

    public ArchiveProjectAdapter(Context context, ArrayList<ProjctBean> listitem, String frag) {
        this.context = context;
        this.listitem = listitem;

        customFonts = new CustomFonts(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.archive_project_item, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {


        holder.txtItem.setText(listitem.get(position).getName());


    }

    private void delete(final int position) {
        ActionSheet actionSheet = new ActionSheet.Builder()

                .setOtherBtn(new String[]{"View Project", "Delete Project"}, new int[]{Color.parseColor("#2dc8bc"), Color.parseColor("#2dc8bc")})


                .setCancelBtn("Cancel", Color.parseColor("#2dc8bc"))

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
                            //       query.whereEqualTo("objectId", listitem.get(position).getObjectId());
                            query.getInBackground(listitem.get(position).getObjectId(), new GetCallback<ParseObject>() {
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null) {
                                        object.deleteInBackground();
                                        Toast.makeText(context, "Delete Projct Successfully", Toast.LENGTH_SHORT).show();
                                        ((ProjectSettingActivity) context).getprojectlist();
//                                        ((HomeFeedActivity)context).getprojectlist();
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

    public void removeItem(int position) {
        listitem.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        private TextView txtItem;

        public Holder(View itemView) {
            super(itemView);
            customFonts = new CustomFonts(context);

            txtItem = (TextView) itemView.findViewById(R.id.txtItem);


            txtItem.setTypeface(customFonts.CabinRegular);

        }
    }


}
