package com.samosys.paperai.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.ProjctBean;
import com.samosys.paperai.activity.activity.CreateCategoryActivity;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by samosys on 7/5/18.
 */

public class CategoryProjectAdapter extends RecyclerView.Adapter<CategoryProjectAdapter.Holder> {
    private Context context;
    private CustomFonts customFonts;
    private ArrayList<ProjctBean> listitem;

    public CategoryProjectAdapter(Context context, ArrayList<ProjctBean> listitem) {
        this.context = context;
        this.listitem = listitem;
        customFonts = new CustomFonts(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_project_item, parent, false);
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
            holder.checkboxcategort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        CreateCategoryActivity.proList.add(listitem.get(position).getParseObject());
                    } else {
                        CreateCategoryActivity.proList.remove(listitem.get(position).getParseObject());
                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        private TextView txt_project_value, txt_project;
        private ProgressBar img_progrss;
        private LinearLayout ll_projectName;
        private ImageView imgProject;
        private CheckBox checkboxcategort;

        public Holder(View itemView) {
            super(itemView);
            customFonts = new CustomFonts(context);
            txt_project = (TextView) itemView.findViewById(R.id.txt_project);
            txt_project_value = (TextView) itemView.findViewById(R.id.txt_project_value);
            img_progrss = (ProgressBar) itemView.findViewById(R.id.img_progrss);
            imgProject = (ImageView) itemView.findViewById(R.id.imgProject);
            ll_projectName = (LinearLayout) itemView.findViewById(R.id.ll_projectName);
            checkboxcategort = (CheckBox) itemView.findViewById(R.id.checkboxcategort);

            txt_project_value.setTypeface(customFonts.CabinRegular);
            txt_project.setTypeface(customFonts.CabinBold);

        }
    }


}

