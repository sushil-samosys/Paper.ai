package com.samosys.paperai.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.ProjctBean;
import com.samosys.paperai.activity.activity.HomeFeedActivity;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.First_Char_Capital;

import java.util.ArrayList;

/**
 * Created by samosys on 2/2/18.
 */

public class MenuPr0jectAdapter extends RecyclerView.Adapter<MenuPr0jectAdapter.Holder>  {
    Context context;
    CustomFonts customFonts;
    ArrayList<ProjctBean> listitem;
    ArrayList<ProjctBean> filterlist = new ArrayList<>();
    String frag;
    int selectedPosition = 0;


    public MenuPr0jectAdapter(HomeFeedActivity homeFeedActivity, ArrayList<ProjctBean> projectList, String frag) {
        this.context = homeFeedActivity;
        this.listitem = projectList;


        customFonts = new CustomFonts(context);
    }

//    public MenuPr0jectAdapter(Context context, ArrayList<ProjctBean> listitem, String frag) {
//        this.context = context;
//        this.listitem = listitem;
//        this.filterlist = listitem;
//        this.frag = frag;
//        customFonts = new CustomFonts(context);
//    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_project_item, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

        int pos = Integer.parseInt(AppConstants.loadPreferences(context, "projpos"));
        if (AppConstants.loadPreferences(context, "projpos").equals("00")) {
            if (selectedPosition == position)
                holder.imgSelect.setVisibility(View.VISIBLE);
            else
                holder.imgSelect.setVisibility(View.INVISIBLE);
        } else if (pos == position) {
            holder.imgSelect.setVisibility(View.VISIBLE);
        } else {
            holder.imgSelect.setVisibility(View.INVISIBLE);
        }
        holder.txtworkName.setText(First_Char_Capital.capitalizeString(filterlist.get(position).getName()));
        if (filterlist.get(position).getType().equals("1")) {
            holder.imgwork.setImageResource(R.mipmap.home_hedge);
        } else {
            holder.imgwork.setImageResource(R.mipmap.home_lock);
        }
//        holder.txtworkurl.setText(filterlist.get(position).getWorkspace_url());
        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                holder.imgSelect.setVisibility(View.INVISIBLE);
                AppConstants.savePreferences(context, "projpos", "" + selectedPosition);
                Intent intent = new Intent(context, HomeFeedActivity.class);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return filterlist.size();
    }



    public class Holder extends RecyclerView.ViewHolder {
        TextView txtworkName;
        LinearLayout rl_item;
        ImageView imgwork, imgSelect;

        public Holder(View itemView) {
            super(itemView);
            customFonts = new CustomFonts(context);
            txtworkName = (TextView) itemView.findViewById(R.id.project_item);
            imgwork = (ImageView) itemView.findViewById(R.id.img_privacy);
            imgSelect = (ImageView) itemView.findViewById(R.id.imgSelect);

            rl_item = (LinearLayout) itemView.findViewById(R.id.rl_item_pro);


            txtworkName.setTypeface(customFonts.HelveticaNeueMedium);

            if (frag.equals("frag")) {
                txtworkName.setTextColor(context.getResources().getColor(R.color.light));
            } else {
                txtworkName.setTextColor(context.getResources().getColor(R.color.txt_dark));
            }
        }
    }




}
