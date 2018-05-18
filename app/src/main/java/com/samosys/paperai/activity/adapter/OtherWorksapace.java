package com.samosys.paperai.activity.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.WorkspaceBean;
import com.samosys.paperai.activity.fragment.PageFragment;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.First_Char_Capital;

import java.util.ArrayList;

/**
 * Created by samosys on 2/2/18.
 */

public class OtherWorksapace extends RecyclerView.Adapter<OtherWorksapace.Holder> {
    Context context;
    CustomFonts customFonts;
    ArrayList<WorkspaceBean> filterlist = new ArrayList<>();
    ArrayList<WorkspaceBean> listitem;

    Fragment fragment;

    public OtherWorksapace(Context context, ArrayList<WorkspaceBean> listitem, PageFragment pageFragment) {
        this.context = context;
        this.listitem = listitem;
        this.filterlist = listitem;
        customFonts = new CustomFonts(context);
        this.fragment = pageFragment;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.other_space_item, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

        holder.txtworkName.setText(First_Char_Capital.capitalizeString(filterlist.get(position).getWorkspace_name()));
        holder.txtURL.setText(filterlist.get(position).getWorkspace_url());
        holder.ll_follow_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject message = new ParseObject("workspace_follower");
                message.put("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
                message.put("workspace", ParseObject.createWithoutData("WorkSpace", filterlist.get(position).getObjectId()));
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            ((PageFragment) fragment).getOtherList();
                            ((PageFragment) fragment).getmyfolloelist();
                            ((PageFragment) fragment).getmyList();

                        } else {
                            e.printStackTrace();
                            Log.e("DATAAA", e.getMessage());
                        }
                    }
                });
            }
        });
        filterlist.get(position).getImage().getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e==null) {
                    Bitmap bmp = BitmapFactory
                            .decodeByteArray(
                                    data, 0,
                                    data.length);

                    // initialize
                    holder.imgwork.setImageBitmap(bmp);
                }
            }
        });
    }//

    @Override
    public int getItemCount() {
        return filterlist.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        TextView txtworkName, txtSelect, txtworkurl, cn, txtURL;
        LinearLayout rl_item, ll_follow_list;
        ImageView imgwork;

        public Holder(View itemView) {
            super(itemView);
            customFonts = new CustomFonts(context);
            txtworkName = (TextView) itemView.findViewById(R.id.txtworkName);
            txtSelect = (TextView) itemView.findViewById(R.id.txtSelect);
            imgwork = (ImageView) itemView.findViewById(R.id.imgwork);
            txtworkurl = (TextView) itemView.findViewById(R.id.txtworkurl);
            txtURL = (TextView) itemView.findViewById(R.id.txtURL);
            rl_item = (LinearLayout) itemView.findViewById(R.id.rl_item);
            ll_follow_list = (LinearLayout) itemView.findViewById(R.id.ll_follow_list);
            txtworkName.setTextColor(context.getResources().getColor(R.color.txt_light));
            txtworkName.setTypeface(customFonts.HelveticaNeueMedium);
            txtworkurl.setTypeface(customFonts.HelveticaNeueMedium);
            txtURL.setTypeface(customFonts.HelveticaNeueMedium);
        }
    }


}
