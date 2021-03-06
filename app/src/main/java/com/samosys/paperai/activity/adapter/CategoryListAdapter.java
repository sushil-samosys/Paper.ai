package com.samosys.paperai.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.CategoryList;
import com.samosys.paperai.activity.activity.CategoryListingActivity;
import com.samosys.paperai.activity.utils.CustomFonts;

import java.util.ArrayList;

/**
 * Created by samosys on 7/5/18.
 */

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.Holder> {
    private Context context;
    private CustomFonts customFonts;
    private ArrayList<CategoryList> listitem;


    public CategoryListAdapter(CategoryListingActivity context, ArrayList<CategoryList> categoryList) {
        this.context = context;
        this.listitem = categoryList;
        customFonts = new CustomFonts(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {


        holder.txt_cat_value.setText(listitem.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        private TextView txt_cat_value;

        public Holder(View itemView) {
            super(itemView);
            customFonts = new CustomFonts(context);
            txt_cat_value = (TextView) itemView.findViewById(R.id.txt_cat_value);


            txt_cat_value.setTypeface(customFonts.CabinRegular);


        }
    }


}

