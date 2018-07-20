package com.samosys.paperai.activity.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.UserBean;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.First_Char_Capital;

import java.util.ArrayList;

/**
 * Created by samosys on 19/2/18.
 */

public class Useradapter extends RecyclerView.Adapter<Useradapter.Viewholder> {
    private ArrayList<UserBean> userlist;
    private CustomFonts customFonts;
    private Context context;

    public Useradapter(FragmentActivity activity, ArrayList<UserBean> userlist) {
        this.context = activity;
        this.userlist = userlist;
        customFonts = new CustomFonts(context);

    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {
        holder.userName_folloe.setText(userlist.get(position).getFullname());
        holder.userName_work.setText(userlist.get(position).getPassion());

        userlist.get(position).getProfileimage().getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                Bitmap bmp = BitmapFactory
                        .decodeByteArray(
                                data, 0,
                                data.length);

                // initialize
                holder.user_image_folle.setImageBitmap(bmp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView userName_folloe, userName_work;
        private ImageView user_image_folle;

        public Viewholder(View itemView) {
            super(itemView);
            userName_folloe = (TextView) itemView.findViewById(R.id.userName_folloe);
            userName_work = (TextView) itemView.findViewById(R.id.userName_work);
            user_image_folle = (ImageView) itemView.findViewById(R.id.user_image_folle);
            userName_folloe.setTypeface(customFonts.HelveticaNeueBold);
            userName_work.setTypeface(customFonts.HelveticaNeue);
        }
    }
}
